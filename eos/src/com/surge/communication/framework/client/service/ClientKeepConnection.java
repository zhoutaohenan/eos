package com.surge.communication.framework.client.service;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.KeepConnection;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.communication.framework.net.Client;

/**
 * �������ӣ��������ӽӿ�
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public abstract class ClientKeepConnection implements KeepConnection
{

	private static final Logger logger = Logger.getLogger(ClientKeepConnection.class);

	/** ����tcp���ӳ�ʱʱ�� */
	private long connTcpTimeout = 60 * 1000L;

	/** Э���˺����� */
	protected final ProtocolConfig protocolConfig;

	/** ��Ϣ�ص� */
	private final IoHandler ioHandler;

	/** Э���˺����ӳ� */
	protected final ConnectionPool connectionPool;

	/** �����������˽�����ʱʱ�� */
	protected long connIdleTimeout = 5 * 60 * 1000;

	/** ���������� **/
	private ProtocolCodecFilter protocolCodecFilter;

	private int readSize = 1024;

	public ClientKeepConnection(ProtocolConfig protocolConfig, IoHandler ioHandler,
			ConnectionPool connectionPool, ProtocolCodecFilter protocolCodecFilter)
	{
		this.protocolConfig = protocolConfig;
		this.connectionPool = connectionPool;
		this.protocolCodecFilter = protocolCodecFilter;
		this.ioHandler = ioHandler;
	}

	/***
	 * 
	 * ����tcp����
	 * 
	 * @return boolean
	 * @throws
	 */
	protected IoSession createIoSession()
	{
		NioSocketConnector connector = null;
		IoSession session = null;
		try
		{
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(connTcpTimeout);
			int readBufferSize = readSize;
			if (readBufferSize > 0)
			{
				connector.getSessionConfig().setReadBufferSize(readBufferSize);
			}

			connector.setHandler(ioHandler);
			connector.getFilterChain().addLast("Code", protocolCodecFilter);
			ConnectFuture cf = connector.connect(new InetSocketAddress(protocolConfig.getIp(),
					protocolConfig.getPort()));
			// �ȴ��Ƿ����ӳɹ����൱����ת�첽ִ��Ϊͬ��ִ�С�
			cf.awaitUninterruptibly();
			session = cf.getSession();
		} catch (Exception e)
		{
			if (connector != null)
			{
				connector.dispose();
			}
			logger.error("��������" + this.protocolConfig.getProtocolId() + "ʧ��", e);
		}
		if (session == null || session.isClosing() || !session.isConnected())
		{
			logger.info("******����������ʧ�ܣ��޷�����TCP����****** protocol:"
					+ this.protocolConfig.getProtocolId());
			// ��connector�Ѿ��رգ�connector.dispose();��ֱ�ӷ��صģ����ڴ˴�����!connector.isDisposed();
			if (connector != null)
			{
				connector.dispose();
				logger.info("**********�ɹ����մ���" + this.protocolConfig.getProtocolId()
						+ "����ʱռ�õ���Դ**********");
			}
			if (session != null)
			{
				session.close(true);
			}
		}
		return session;
	}
	/**
	 * 
	 * ��鲢���ʧЧ����
	 * 
	 * @throws
	 */
	public void checkConnect()
	{
		List<Client> clients = connectionPool.getAllClient(this.protocolConfig.getProtocolId());
		Iterator<Client> it = clients.iterator();
		for (; it.hasNext();)
		{
			Client client = it.next();

			// �������ʧЧ�����ӿ��г�ʱ�����
			if (!client.isAvailable()
					|| (System.currentTimeMillis() - client.getLastReceveTime() > connIdleTimeout))
			{
				logger.info("�������ӳ�ʱ,protocolId��" + client.getProtocolId() + ",�Ƴ���������!");
				connectionPool.removeClient(client);
				client.setUnAvailable();
				client = null;
			}
		}
	}
	/**
	 * 
	 * �����ƶ����������� client
	 * 
	 * @param count
	 *            void
	 * @throws
	 */
	public abstract void connect();

	/**
	 * 
	 * ��������
	 * 
	 * @throws
	 */
	public abstract void keepActive();

	/**
	 * 
	 * �������ӵ���������
	 * 
	 * @throws
	 */
	public abstract void clearClientData();

}
