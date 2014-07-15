package com.surge.engine.sms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.comm.Persistence;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.util.ShutdownHook;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsQueueMgr implements Persistence
{
	private static final Logger logger = Logger.getLogger(SmsQueueMgr.class);

	/** 各个通道对应的MT队列 key：通道名称 value:队列 */
	private final Map<String, LinkedBlockingQueue<Sms>> mtQeueMap = new ConcurrentHashMap<String, LinkedBlockingQueue<Sms>>();

	/** mt临时队列，用于启动临时加载数据 key：通道名称 value:队列 **/
	private Map<String, LinkedBlockingQueue<Sms>> mtQeueMapTmp = new ConcurrentHashMap<String, LinkedBlockingQueue<Sms>>();

	/** 已发送队列 */
	private Map<String, Map<String, Sms>> sentSubmitMap = new ConcurrentHashMap<String, Map<String, Sms>>();

	private Map<String,Map<String,PMessage>> longMoSMSMap=new ConcurrentHashMap<String,Map<String,PMessage>>();
	
	private static SmsQueueMgr instance;

	private static final String filePath = "ser/smsQueue.ser";

	private SmsQueueMgr()
	{
		ShutdownHook.add(this);
		this.loadData();
	}
	/**
	 * 
	 * 为一个通道分配一个mt队列
	 * 
	 * @param ismg_id
	 * @param capacity
	 * @return LinkedBlockingQueue<Sms>
	 * @throws
	 */
	public LinkedBlockingQueue<Sms> allocateMtQueue(String channel, int capacity)
	{
		LinkedBlockingQueue<Sms> smsMtQueue = mtQeueMap.get(channel);
		if (smsMtQueue == null)
		{
			LinkedBlockingQueue<Sms> smsMtQueueTmp = mtQeueMapTmp.get(channel);
			if (smsMtQueueTmp != null)
			{
				capacity = Math.max(capacity, smsMtQueueTmp.size());
			}
			smsMtQueue = new LinkedBlockingQueue<Sms>(capacity + 1);
			if (smsMtQueueTmp != null)
			{
				smsMtQueue.addAll(smsMtQueueTmp);
				mtQeueMapTmp.remove(channel);
			}
			mtQeueMap.put(channel, smsMtQueue);
		}
		return smsMtQueue;
	}
	/**
	 * 
	 * 为一个通道分配一个已发送队列
	 * 
	 * @param ismg_id
	 * @return ConcurrentLinkedQueue<Sms>
	 * @throws
	 */
	public Map<String, Sms> allocateSentQueue(String channel)
	{
		Map<String, Sms> sentSubmitQueue = sentSubmitMap.get(channel);
		if (sentSubmitQueue == null)
		{
			sentSubmitQueue = new ConcurrentHashMap<String, Sms>();
			sentSubmitMap.put(channel, sentSubmitQueue);
		}
		return sentSubmitQueue;
	}
	private void loadData()
	{
		ObjectInputStream ois = null;
		Map<String, Map<String, Sms>> sentSubmitMap_tmp = null;
		Map<String,Map<String,PMessage>> longSmsMap_temp=null;
		try
		{
			ois = new ObjectInputStream(new FileInputStream(filePath));
			mtQeueMapTmp = (Map<String, LinkedBlockingQueue<Sms>>) ois.readObject();
			sentSubmitMap_tmp = (Map<String, Map<String, Sms>>) ois.readObject();
			longSmsMap_temp=(Map<String,Map<String,PMessage>>)ois.readObject();
			logger.info("装载sms缓冲队列成功");
		} catch (FileNotFoundException e)
		{
		} catch (Exception e)
		{
			logger.error("装载sms缓冲队列失败", e);
		} finally
		{
			try
			{
				if (ois != null)
				{
					ois.close();
				}
			} catch (IOException ioe)
			{
				// ignore
			}
			new File(filePath).delete();
		}
		if (sentSubmitMap_tmp != null)
		{
			sentSubmitMap.putAll(sentSubmitMap_tmp);
		}
		if(longSmsMap_temp!=null)
		{
			longMoSMSMap.putAll(longSmsMap_temp);
		}
	}
	@Override
	public void saveData()
	{

		ObjectOutputStream oos = null;
		Logger log = Logger.getLogger(SmsQueueMgr.class.getName());
		try
		{
			oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(mtQeueMap);
			oos.writeObject(sentSubmitMap);
			oos.writeObject(longMoSMSMap);
			log.info("保存sms缓冲队列成功");
		} catch (Exception e)
		{
			log.error("保存sms缓冲队列失败", e);
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
	public Map<String, LinkedBlockingQueue<Sms>> getMtQeueMap()
	{
		return mtQeueMap;
	}

	public LinkedBlockingQueue<Sms> getMtQueue(String channelId)
	{
		return mtQeueMap.get(channelId);
	}
	public void putSmsMt(String channelId, Sms sms)
	{
		this.mtQeueMap.get(channelId).offer(sms);
	}

	public Map<String, Map<String, Sms>> getSentSubmitMap()
	{
		return sentSubmitMap;
	}
	public Map<String, LinkedBlockingQueue<Sms>> getMtQeueMapTmp()
	{
		return mtQeueMapTmp;
	}

	/**
	 * 新增接口
	 */
	public void stopQueue()
	{
		ShutdownHook.remove(this);
		this.saveData();
		mtQeueMap.clear();
		mtQeueMapTmp.clear();
		sentSubmitMap.clear();
		if (instance != null)
		{
			instance = null;
		}
	}
	public static synchronized SmsQueueMgr getInstance()
	{
		if (instance == null)
		{
			instance = new SmsQueueMgr();
		}
		return instance;
	}
	public Map<String, Map<String, PMessage>> getLongMoSMSMap()
	{
		return longMoSMSMap;
	}

}
