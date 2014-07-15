package com.surge.engine.dbadapter.runnable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.BaseThread;
import com.surge.engine.SysConst;
import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.util.DistractUtil;
import com.surge.engine.util.HibernateUtil;

/**
 * 转移数据线程(从SMS_SENT_TEMP表转移到SMS_SENT表)
 * 
 * @description
 * @project: esk
 * @Date:2010-9-9
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class DistractThread extends BaseThread
{
	private static Logger logger = Logger.getLogger(DistractThread.class);

	private DbConfig instance = DbConfig.instance;

	private volatile boolean cancelled;

	private long activeTime = System.currentTimeMillis();

	public DistractThread()
	{

	}
	// 退出标志
	public void cancel()
	{
		cancelled = true;

		this.interrupt();
	}

	public void run()
	{
		this.setName("DistractThread");
		logger.info("DistractThread线程启动成功");
		// 退出标志被设置或者线程中断时
		while (!cancelled && !this.isInterrupted())
		{
			try
			{
				Thread.sleep(5);
				// 活动时间
				activeTime = System.currentTimeMillis();
				// 控制标志为4
				if (SysConst.getFalg() == 4)
				{
					if (instance.isOracle_run())
					{
						distractOracleAnother();

					} else if (instance.isMySql_run())
					{
						distractMySqlAnother();
					} else
					{
						DistractUtil.distractUtil.distractSQLServer(instance.queryTempSql);
					}
					// 处理完成标志位设置为3
					SysConst.setValue(3);
				}

			} catch (InterruptedException e)
			{
				logger.info("DistractThread线程中断");
				this.interrupt();
				SysConst.setValue(3);
			}
		}
	}

	private synchronized void distractMySqlAnother()
	{
		long beginL = System.currentTimeMillis();
		String querySql = "select max(SENT_TEMP_ID) as n from SMS_SENT_TEMP where ((ISNEEDREPORT=0 and ISNEED_RESEND=0) or (ISNEEDREPORT=1 and ISNEED_RESEND=0  and STATUS is not null) or (RESEND_CHANNEL is  null  and  ISNEED_RESEND=1 and RESEND_STATUS!=2) or (RESEND_CHANNEL is not null  and  ISNEED_RESEND!=0)  or (ISNEED_RESEND!=0 and (unix_timestamp(now())- unix_timestamp(SEND_TIME) >  60*10 )))  order by SENT_TEMP_ID limit 1000";
		Session session = null;
		Connection conn = null;
		Statement queryStat = null;
		ResultSet rs = null;
		Statement insertStat = null;
		Statement deleteStat = null;

		Transaction tranction = null;
		try
		{
			session = HibernateUtil.getNewSession();
			tranction = session.beginTransaction();
			conn = session.connection();
			queryStat = conn.createStatement();
			rs = queryStat.executeQuery(querySql);
			rs.next();
			long id = rs.getLong("n");
			if (id > 0)
			{
				String insertSql = "Insert into SMS_SENT "
						+ "(MT_ID, SMSID, DESTADDR, CREATE_TIME, ISNEEDREPORT, SEND_FLAG, SEND_TIME,CHANNEL, RESEND_CHANNEL, MESSAGE, SPLIT_MESSAGE, PK_TOTAL, PK_NUMBER, ISNEED_RESEND,RESEND_STATUS, MESSAGE_ID,SEND_RET,STATUS,DESCRIPTION,REPORT_TIME,EXTCODE,ORGID,SEND_USERID) "
						+ "select MT_ID, SMSID, DESTADDR, CREATE_TIME, ISNEEDREPORT, SEND_FLAG, SEND_TIME,CHANNEL, RESEND_CHANNEL, MESSAGE, SPLIT_MESSAGE, PK_TOTAL, PK_NUMBER, ISNEED_RESEND,RESEND_STATUS, MESSAGE_ID,SEND_RET,STATUS,DESCRIPTION,REPORT_TIME,EXTCODE,ORGID,SEND_USERID from SMS_SENT_TEMP "
						+ "where ((ISNEEDREPORT=0 and ISNEED_RESEND=0) or (ISNEEDREPORT=1 and ISNEED_RESEND=0  and (STATUS is not null or STATUS='')) or ((RESEND_CHANNEL is  null or RESEND_CHANNEL='')  and  ISNEED_RESEND=1 and RESEND_STATUS!=2) or (RESEND_CHANNEL !=''  and  ISNEED_RESEND!=0)  or (ISNEED_RESEND!=0 and (unix_timestamp(now())- unix_timestamp(SEND_TIME) > 60*10))) and SENT_TEMP_ID<= "
						+ id + " limit 1000";
				String deleteSql = "delete from SMS_SENT_TEMP where ((ISNEEDREPORT=0 and ISNEED_RESEND=0) or (ISNEEDREPORT=1 and ISNEED_RESEND=0  and (STATUS is not null or STATUS='')) or ((RESEND_CHANNEL is  null or RESEND_CHANNEL='') and  ISNEED_RESEND=1 and RESEND_STATUS!=2) or (RESEND_CHANNEL !=''  and  ISNEED_RESEND!=0)  or (ISNEED_RESEND!=0 and (unix_timestamp(now())- unix_timestamp(SEND_TIME) > 60*10))) and SENT_TEMP_ID <= "
						+ id + " limit 1000";
				insertStat = conn.createStatement();
				insertStat.execute(insertSql);
				deleteStat = conn.createStatement();
				deleteStat.execute(deleteSql);
				tranction.commit();
				logger.debug("转移到SMS_SENT表一批短信,用时:" + (System.currentTimeMillis() - beginL));
			}

		} catch (Exception e)
		{
			tranction.rollback();
			e.printStackTrace();
		} finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (queryStat != null)
				{
					queryStat.close();
				}
				if (insertStat != null)
				{
					insertStat.close();
				}
				if (deleteStat != null)
				{
					deleteStat.close();
				}
				if (conn != null)
				{
					conn.close();
				}
				if (session != null)
				{
					session.close();
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

	private synchronized void distractOracleAnother()
	{
		long beginL = System.currentTimeMillis();
		// 得到状态报告为0重发标志为0状态报告为1重发标志为0标志位不为空通道为空重发为1重发标志不为2重发通道为空重发标志为0或重发标识为0系统时间减发送时间小于10分钟
		String querySql = "select max(SENT_TEMP_ID)as n from SMS_SENT_TEMP where ((ISNEEDREPORT=0 and ISNEED_RESEND=0) or (ISNEEDREPORT=1 and ISNEED_RESEND=0  and STATUS is not null) or (RESEND_CHANNEL is  null  and  ISNEED_RESEND=1 and RESEND_STATUS!=2) or (RESEND_CHANNEL is not null  and  ISNEED_RESEND!=0)  or (ISNEED_RESEND!=0 and ((sysdate-SEND_TIME) > (1/1440 *10)))) and rownum <= 1000 order by SENT_TEMP_ID ";
		Session session = null;
		Connection conn = null;
		Statement queryStat = null;
		ResultSet rs = null;
		Statement insertStat = null;
		Statement deleteStat = null;

		Transaction tranction = null;
		try
		{
			session = HibernateUtil.getNewSession();
			tranction = session.beginTransaction();
			conn = session.connection();
			queryStat = conn.createStatement();
			rs = queryStat.executeQuery(querySql);
			rs.next();
			// 得到最大的ID值
			long id = rs.getLong("n");
			if (id > 0)
			{
				// 查询批量插入
				String insertSql = "Insert into SMS_SENT "
						+ "(SENT_ID,MT_ID, SMSID, DESTADDR, CREATE_TIME, ISNEEDREPORT, SEND_FLAG, SEND_TIME,CHANNEL, RESEND_CHANNEL, MESSAGE, SPLIT_MESSAGE, PK_TOTAL, PK_NUMBER, ISNEED_RESEND,RESEND_STATUS, MESSAGE_ID,SEND_RET,STATUS,DESCRIPTION,REPORT_TIME,EXTCODE,ORGID,SEND_USERID) "
						+ "select SENT_TEMP_ID, MT_ID, SMSID, DESTADDR, CREATE_TIME, ISNEEDREPORT, SEND_FLAG, SEND_TIME,CHANNEL, RESEND_CHANNEL, MESSAGE, SPLIT_MESSAGE, PK_TOTAL, PK_NUMBER, ISNEED_RESEND,RESEND_STATUS, MESSAGE_ID,SEND_RET,STATUS,DESCRIPTION,REPORT_TIME,EXTCODE,ORGID,SEND_USERID from SMS_SENT_TEMP "
						+ "where ((ISNEEDREPORT=0 and ISNEED_RESEND=0) or (ISNEEDREPORT=1 and ISNEED_RESEND=0  and (STATUS is not null or STATUS='')) or ((RESEND_CHANNEL is  null or RESEND_CHANNEL='') and  ISNEED_RESEND=1 and RESEND_STATUS!=2) or (RESEND_CHANNEL is not null  and  ISNEED_RESEND!=0)  or (ISNEED_RESEND!=0 and ((sysdate-SEND_TIME) > (1/1440 *10)))) and rownum <= 1000  and SENT_TEMP_ID<= "
						+ id;
				// 查询批量删除
				String deleteSql = "delete from SMS_SENT_TEMP where ((ISNEEDREPORT=0 and ISNEED_RESEND=0) or (ISNEEDREPORT=1 and ISNEED_RESEND=0  and (STATUS is not null or STATUS='')) or ((RESEND_CHANNEL is  null or RESEND_CHANNEL='') and  ISNEED_RESEND=1 and RESEND_STATUS!=2) or (RESEND_CHANNEL is not null  and  ISNEED_RESEND!=0)  or (ISNEED_RESEND!=0 and ((sysdate-SEND_TIME) > (1/1440 *10)))) and rownum <= 1000 and SENT_TEMP_ID<= "
						+ id;
				insertStat = conn.createStatement();
				insertStat.execute(insertSql);
				deleteStat = conn.createStatement();
				deleteStat.execute(deleteSql);
				tranction.commit();
				logger.debug("转移到SMS_SENT表一批短信,用时:" + (System.currentTimeMillis() - beginL));
			}

		} catch (Exception e)
		{
			tranction.rollback();
			e.printStackTrace();
		} finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (queryStat != null)
				{
					queryStat.close();
				}
				if (insertStat != null)
				{
					insertStat.close();
				}
				if (deleteStat != null)
				{
					deleteStat.close();
				}
				if (conn != null)
				{
					conn.close();
				}
				if (session != null)
				{
					session.close();
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}
	@Override
	public long getLastActiveTime()
	{
		return activeTime;
	}

}
