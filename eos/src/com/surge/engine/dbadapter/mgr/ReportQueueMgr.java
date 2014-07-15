package com.surge.engine.dbadapter.mgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.surge.engine.comm.Persistence;
import com.surge.engine.dbadapter.SmsHandlerImpl;
import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsMessage;
import com.surge.engine.util.ShutdownHook;

public class ReportQueueMgr implements Persistence
{
	private static Logger logger = Logger.getLogger(ReportQueueMgr.class);
	/** ��Ӧ���� **/
	private ConcurrentLinkedQueue<Sms> respsQueue = new ConcurrentLinkedQueue<Sms>();

	/** ״̬������� **/
	private ConcurrentLinkedQueue<Report> reportsQueue = new ConcurrentLinkedQueue<Report>();

	/** MO���� **/
	private ConcurrentLinkedQueue<SmsMessage> smsMessagesQueue = new ConcurrentLinkedQueue<SmsMessage>();

	/**
	 * MessageID����
	 */
	private  Map<String, ConcurrentHashMap<String, Long>> smsMessgeIDQueue = new ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>();

	/** ���� **/
	public static ReportQueueMgr instance = new ReportQueueMgr();

	/** �����������Ŀ���ļ� **/
	private static final String FILEPATH = "ser/ReportQueueMgr.ser";

	private ReportQueueMgr()
	{
		ShutdownHook.add(this);
		this.loadData();
	}

	@Override
	public void saveData()
	{
		ObjectOutputStream oos = null;
		Logger log = Logger.getLogger(SmsHandlerImpl.class.getName());
		try
		{
			oos = new ObjectOutputStream(new FileOutputStream(FILEPATH));
			oos.writeObject(respsQueue);
			oos.writeObject(reportsQueue);
			oos.writeObject(smsMessagesQueue);
			oos.writeObject(smsMessgeIDQueue);
			log.info("������Ӧ��״̬���桢MO������гɹ�");
		} catch (Exception e)
		{
			log.error("������Ӧ��״̬���桢MO�������ʧ��", e);
		} finally
		{
			try
			{
				if (oos != null)
				{
					oos.close();
				}
			} catch (IOException ioe)
			{
				// ignore
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void loadData()
	{
		ObjectInputStream ois = null;

		try
		{
			ois = new ObjectInputStream(new FileInputStream(FILEPATH));
			respsQueue = (ConcurrentLinkedQueue<Sms>) ois.readObject();
			reportsQueue = (ConcurrentLinkedQueue<Report>) ois.readObject();
			smsMessagesQueue = (ConcurrentLinkedQueue<SmsMessage>) ois.readObject();
			smsMessgeIDQueue = (ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>) ois
					.readObject();
			logger.info("����ReportQueueMgr.ser�ɹ�");

		} catch (FileNotFoundException e)
		{
		} catch (Exception e)
		{
			logger.error("����ReportQueueMgr.serʧ��", e);
		} finally
		{
			if (ois != null)
			{
				try
				{
					ois.close();
				} catch (IOException e)
				{
				}

			}
			new File(FILEPATH).delete();
		}

	}
	public ConcurrentLinkedQueue<Sms> getRespsQueue()
	{
		return respsQueue;
	}

	public void setRespsQueue(ConcurrentLinkedQueue<Sms> respsQueue)
	{
		this.respsQueue = respsQueue;
	}

	public ConcurrentLinkedQueue<Report> getReportsQueue()
	{
		return reportsQueue;
	}

	public void setReportsQueue(ConcurrentLinkedQueue<Report> reportsQueue)
	{
		this.reportsQueue = reportsQueue;
	}

	public ConcurrentLinkedQueue<SmsMessage> getSmsMessagesQueue()
	{
		return smsMessagesQueue;
	}

	public void setSmsMessagesQueue(ConcurrentLinkedQueue<SmsMessage> smsMessagesQueue)
	{
		this.smsMessagesQueue = smsMessagesQueue;
	}

	public Map<String, ConcurrentHashMap<String, Long>> getSmsMessgeIDQueue()
	{
		return smsMessgeIDQueue;
	}

	public void setSmsMessgeIDQueue(Map<String, ConcurrentHashMap<String, Long>> smsMessgeIDQueue)
	{
		this.smsMessgeIDQueue = smsMessgeIDQueue;
	}

}
