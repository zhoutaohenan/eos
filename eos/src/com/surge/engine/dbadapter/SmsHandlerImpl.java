package com.surge.engine.dbadapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.surge.engine.ThreadQueueMgr;
import com.surge.engine.dbadapter.dao.DBOperateDao;
import com.surge.engine.dbadapter.mgr.ReportQueueMgr;
import com.surge.engine.dbadapter.runnable.SmsMoProcessThread;
import com.surge.engine.dbadapter.runnable.SmsProcessThread;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsMessage;
import com.surge.engine.sms.service.SmsAgentServiceImpl;
import com.surge.engine.sms.service.SmsHandler;
import com.surge.engine.util.TimerPool;

/**
 * DB��״̬���棬MO����ӦHandler
 * 
 * @description
 * @project: eskprj
 * @Date:2010-8-13
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsHandlerImpl implements SmsHandler
{
	private static Logger logger = Logger.getLogger(SmsHandlerImpl.class);

	private static ReportQueueMgr instance = ReportQueueMgr.instance;

	/** MessageID ���� **/
	private Map<String, ConcurrentHashMap<String, Long>> smsMessgeIDQueue = ReportQueueMgr.instance
			.getSmsMessgeIDQueue();

	/** ״̬������� **/
	private ConcurrentLinkedQueue<Report> reportsQueue = instance.getReportsQueue();

	/** MESSAGEID ��ʱ��ʱ���Ҳ���״̬���棬������MESSAGEID (��λ:����) **/
	private static final long MESSAGEID_EXPIRE_TIME = 20 * 60 * 1000;

	private ConcurrentLinkedQueue<Report> reportsQueueTemp = new ConcurrentLinkedQueue<Report>();

	/** �����������Ŀ���ļ� **/
	private static final String FILEPATH = "ser/tempReport.ser";

	private SmsProcessThread processThread;

	private SmsMoProcessThread moProcessThread;

	private ThreadQueueMgr queueMgr = ThreadQueueMgr.instance;

	private final static char DIVISION = 0;

	private DBOperateDao basicOperate = new DBOperateDao(new SmsAgentServiceImpl());

	public SmsHandlerImpl()
	{
		TimerPool.schedule(new Routine(), 10, 30);
		TimerPool.schedule(new ReportProcess(), 10, 10);
		processThread = new SmsProcessThread(basicOperate);
		processThread.start();
		queueMgr.addThread(processThread);
		moProcessThread = new SmsMoProcessThread(basicOperate);
		moProcessThread.start();
		queueMgr.addThread(moProcessThread);
	}

	@Override
	public void notifyReport(Report report)
	{
		if (reportsQueue.size() < SmsConfig.getInstance().getReportsSize())
		{
			reportsQueue.offer(report);
		} else
		{
			this.reportsQueueTemp.add(report);
		}
	}

	@Override
	public void notifyResp(Sms sms)
	{
		// logger.debug(sms.getChannel().getChanneId()
		// + " receive resp,messageId= " + sms.getMessageId() + " result="
		// + sms.getSendResult());
		instance.getRespsQueue().add(sms);
	}

	@Override
	public void notirySmsMessage(SmsMessage smsMessage)
	{
		instance.getSmsMessagesQueue().add(smsMessage);
	}

	class ReportProcess implements Runnable
	{

		public void run()
		{
			saveReport();
			loadReport();
		}

	}

	private void loadReport()
	{
		FileReader fr = null;
		BufferedReader br = null;
		File file = new File(FILEPATH);
		boolean isRead = false;
		try
		{
			if (reportsQueue.size() == 0 && instance.getRespsQueue().size() == 0)
			{
				if (file.exists())
				{
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					String entryStr = null;
					while ((entryStr = br.readLine()) != null)
					{
						isRead = true;
						String entryArray[] = entryStr.split(String.valueOf(DIVISION));
						Report report = new Report();
						if (entryArray.length == 6)
						{
							report.setChannelId(entryArray[0]);
							report.setMessageId(entryArray[1]);
							report.setResult(Integer.parseInt(entryArray[2]));
							report.setStats(entryArray[3]);
							report.setDesc(entryArray[4]);
							report.setReciveTime(Long.parseLong(entryArray[5]));
							reportsQueue.add(report);
						} else
						{
							logger.debug("���ļ��ж�ȡ��״̬����������:" + entryStr + "����");
						}
					}
				}
			}
		} catch (Exception e)
		{
			logger.debug("��ȡ״̬������ʱ�ļ� ����", e);

		} finally
		{
			try
			{
				if (br != null)
				{
					br.close();
				}
				if (fr != null)
				{
					fr.close();
				}
			} catch (Exception e)
			{

			} finally
			{
				if (isRead)
				{
					file.delete();
				}
			}
		}
	}

	/**
	 * ���ݲ��ܴ����״̬���汣�浽�ļ� ���ļ� �ж�ȡ״̬
	 */
	public void saveReport()
	{
		FileWriter fw = null;
		BufferedWriter bw = null;
		File file = new File(FILEPATH);
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			if (reportsQueueTemp.size() > 0)
			{
				fw = new FileWriter(file, true);
				bw = new BufferedWriter(fw);
				int reportSize = reportsQueueTemp.size();
				for (int i = 0; i < reportSize; i++)
				{
					Report re = reportsQueueTemp.poll();
					if (re != null)
					{
						bw.write(re.toString());
					}
				}

			}
		} catch (IOException e)
		{
			logger.debug("����״̬������ʱ�ļ� ����", e);
		} finally
		{
			try
			{
				if (bw != null)
				{
					bw.close();
				}
				if (fw != null)
				{
					fw.close();
				}
			} catch (Exception e)
			{

			}

		}
	}

	class Routine implements Runnable
	{

		public void run()
		{
			try
			{
				doExpire();
			} catch (Exception e)
			{
				logger.error("", e);
			}
		}
	}

	/**
	 * ����ʱ��û���ҵ�״̬�����messagid TODO void
	 * 
	 * @throws
	 */
	public void doExpire()
	{
		Map<String, ConcurrentHashMap<String, Long>> tmpMap = smsMessgeIDQueue;
		ConcurrentHashMap<String, Long> temphashMap = null;
		for (String mkey : tmpMap.keySet())
		{
			temphashMap = tmpMap.get(mkey);
			for (String key : temphashMap.keySet())
			{
				if (temphashMap.get(key) == null)
				{
					temphashMap.put(key, System.currentTimeMillis());
					continue;
				} else
				{
					if (System.currentTimeMillis() - temphashMap.get(key) > MESSAGEID_EXPIRE_TIME)
					{
						temphashMap.remove(key);
					}
				}
			}

		}
	}

}
