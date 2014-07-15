package com.surge.engine.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.exception.BaseException;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-9
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class HibernateUtil
{

	private static final Logger logger = Logger.getLogger(HibernateUtil.class);

	// private static final String CONFIG_FILE = "config/hibernate.cfg.xml";

	// private static final String CONFIG_FILE =
	// "config/hibernate-sybase.cfg.xml";
	//
	private static final String CONFIG_FILE = DbConfig.instance.hibernate_Config_File;

	private static Configuration configuration;

	private static SessionFactory sessionFactory;

	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

	private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	private static final ThreadLocal<Interceptor> threadInterceptor = new ThreadLocal<Interceptor>();

	private static final ConcurrentLinkedQueue<Connection> connectQueue = new ConcurrentLinkedQueue<Connection>();

	static
	{
		try
		{
			configuration = new Configuration();
			File file = new File(CONFIG_FILE);
			logger.info("初始化SessionFactory开始.....");
			sessionFactory = configuration.configure(file).buildSessionFactory();
		} catch (Throwable ex)
		{
			logger.error("Building SessionFactory failed.", ex);
			throw new ExceptionInInitializerError(ex);
		}
		logger.info("初始化SessionFactory成功.....");
	}

	/**
	 * 得到一个新的Session，此Session不能用HibernateUtil里面的方法操作Session，
	 * 必须使用Session自身的方法，如：session.beginTransaction(), session.close()等。
	 * 
	 * @return Session
	 * @throws BaseException
	 */
	public static Session getNewSession() throws BaseException
	{
		try
		{
			return getSessionFactory().openSession();
		} catch (HibernateException e)
		{
			throw new BaseException(e);
		}
	}

	/**
	 * Retrieves the current Session local to the thread.
	 * <p/>
	 * If no Session is open, opens a new Session for the running thread.
	 * 
	 * @return Session
	 */
	public static Session getSession() throws BaseException
	{
		Session s = (Session) threadSession.get();
		try
		{
			if (s == null)
			{
				logger.debug("Opening new Session for this thread.");
				if (getInterceptor() != null)
				{
					logger.debug("Using interceptor: " + getInterceptor().getClass());
					s = getSessionFactory().openSession(getInterceptor());
				} else
				{
					s = getSessionFactory().openSession();
				}
				threadSession.set(s);
			}
		} catch (HibernateException ex)
		{
			throw new BaseException(ex);
		}
		return s;
	}

	/** Closes the Session local to the thread. */
	public static void closeSession()
	{
		try
		{
			Session s = (Session) threadSession.get();
			threadSession.set(null);
			if (s != null && s.isOpen())
			{
				logger.debug("Closing Session of this thread.");
				s.close();
			}
		} catch (HibernateException ex)
		{
			logger.error(ex);
		}
	}

	/**
	 * First commit or rollback the transtraction by commit then Closes the
	 * Session local to the thread.
	 * 
	 * @param commit
	 *            true:提交事务，false:回滚事务
	 */
	public static void closeSession(boolean commit) throws BaseException
	{
		if (commit)
			commitTransaction();
		else
			rollbackTransaction();

		closeSession();

	}

	/** Start a new database transaction. */
	public static void beginTransaction() throws BaseException
	{
		Transaction tx = (Transaction) threadTransaction.get();
		try
		{
			if (tx == null)
			{
				logger.debug("Starting new database transaction in this thread.");
				tx = getSession().beginTransaction();
				threadTransaction.set(tx);
			}
		} catch (HibernateException ex)
		{
			throw new BaseException(ex);
		}
	}

	/** Commit the database transaction. */
	public static void commitTransaction() throws BaseException
	{
		Transaction tx = (Transaction) threadTransaction.get();
		try
		{
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
			{
				logger.debug("Committing database transaction of this thread.");
				tx.commit();
			}
			threadTransaction.set(null);
		} catch (HibernateException ex)
		{
			logger.error("Commit database error: " + ex);
			rollbackTransaction();
			throw new BaseException(ex);
		}
	}

	/** Rollback the database transaction. */
	public static void rollbackTransaction()
	{
		Transaction tx = (Transaction) threadTransaction.get();
		try
		{
			threadTransaction.set(null);
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
			{
				logger.debug("Tyring to rollback database transaction of this thread.");
				tx.rollback();
			}
		} catch (HibernateException ex)
		{
			logger.error("Rollback database error: " + ex);
		} finally
		{
			closeSession();
		}
	}

	/** Rebuild the SessionFactory with the static Configuration. */
	public synchronized static void rebuildSessionFactory() throws BaseException
	{
		try
		{
			sessionFactory = getConfiguration().buildSessionFactory();
		} catch (Exception ex)
		{
			throw new BaseException(ex);
		}
	}

	/**
	 * Rebuild the SessionFactory with the given Hibernate Configuration.
	 * 
	 * @param cfg
	 */
	public synchronized static void rebuildSessionFactory(Configuration cfg) throws BaseException
	{
		try
		{
			sessionFactory = cfg.buildSessionFactory();
			configuration = cfg;
		} catch (Exception ex)
		{
			throw new BaseException(ex);
		}
	}

	/**
	 * Reconnects a Hibernate Session to the current Thread.
	 * 
	 * @param session
	 *            The Hibernate Session to be reconnected.
	 */
	public static void reconnect(Session session) throws BaseException
	{
		try
		{
			session.reconnect();
			threadSession.set(session);
		} catch (HibernateException ex)
		{
			throw new BaseException(ex);
		}
	}

	/**
	 * Disconnect and return Session from current Thread.
	 * 
	 * @return Session the disconnected Session
	 */
	public static Session disconnectSession() throws BaseException
	{

		Session session = getSession();
		try
		{
			threadSession.set(null);
			if (session.isConnected() && session.isOpen())
				session.disconnect();
		} catch (HibernateException ex)
		{
			throw new BaseException(ex);
		}
		return session;
	}

	/**
	 * Returns the SessionFactory used for this static class.
	 * 
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory()
	{
		/*
		 * Instead of a static variable, use JNDI: SessionFactory sessions =
		 * null; try { Context ctx = new InitialContext(); String jndiName =
		 * "java:hibernate/HibernateFactory"; sessions =
		 * (SessionFactory)ctx.lookup(jndiName); } catch (NamingException ex) {
		 * throw new InfrastructureException(ex); } return sessions;
		 */
		return sessionFactory;
	}

	/**
	 * Returns the original Hibernate configuration.
	 * 
	 * @return Configuration
	 */
	public static Configuration getConfiguration()
	{
		return configuration;
	}

	/**
	 * Register a Hibernate interceptor with the current thread.
	 * <p/>
	 * Every Session opened is opened with this interceptor after registration.
	 * Has no effect if the current Session of the thread is already open,
	 * effective on next close()/getSession().
	 */
	public static void registerInterceptor(Interceptor interceptor)
	{
		threadInterceptor.set(interceptor);
	}

	private static Interceptor getInterceptor()
	{
		return (Interceptor) threadInterceptor.get();
	}

	/**
	 * 关闭 SessionFactory Destroy this SessionFactory and release all resources
	 * (caches, connection pools, etc).
	 */
	public static void closeSessionFactory()
	{
		if (sessionFactory != null)
		{
			sessionFactory.close();
			logger.info("SessionFactory closed.");
		}
	}

	public static Connection getConnection()
	{
		Connection conn = null;
		if (connectQueue.size() == 0)
		{
			createConnection();
		}
		conn = connectQueue.remove();
		if (conn == null)
		{
			logger.info("conn is null");
		}
		return conn;
	}
	public static void closeConnection(Connection conn)
	{
		connectQueue.add(conn);
	}

	private static void createConnection()
	{
		Connection conn = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String url = "jdbc:sybase:Tds:10.0.11.9:5000/esk1"; // orcl为数据库的SID
			String user = "sa";
			String password = "";
			conn = DriverManager.getConnection(url, user, password);

		} catch (Exception e)
		{

		}
		if (conn != null)
		{
			connectQueue.add(conn);
		}
	}
}