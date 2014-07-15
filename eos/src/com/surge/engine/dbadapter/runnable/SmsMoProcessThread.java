package com.surge.engine.dbadapter.runnable;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.surge.engine.BaseThread;
import com.surge.engine.dbadapter.dao.DBOperateDao;
import com.surge.engine.dbadapter.mgr.ReportQueueMgr;
import com.surge.engine.sms.pojo.SmsMessage;
import com.surge.engine.util.Tools;

public class SmsMoProcessThread extends BaseThread
{

	private static Logger logger = Logger.getLogger(SmsMoProcessThread.class);

	/** MO队列 **/
	private ConcurrentLinkedQueue<SmsMessage> smsMessagesQueue = ReportQueueMgr.instance
			.getSmsMessagesQueue();

	private volatile boolean cancelled;

	public DBOperateDao basicOperate;

	private long activeTime = System.currentTimeMillis();

	public SmsMoProcessThread(DBOperateDao basicOperate)
	{
		this.basicOperate = basicOperate;
	}

	@Override
	public void run()
	{
		this.setName("SmsMoProcessThread");
		while (!cancelled)
		{
			try
			{
				Tools.csleep(5000);
				activeTime=System.currentTimeMillis();
				ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>();
				// 每500条一批
				for (; smsMessagesQueue.size() > 0;)
				{
					messageList.add(smsMessagesQueue.poll());
					if (messageList.size() >= 500 || smsMessagesQueue.size() == 0)
					{
						basicOperate.saveMo(messageList);
						break;
					}
				}
			} catch (InterruptedException ie)
			{
				logger.info("MoWroker线程中断");
				this.interrupt();
				break;
			} catch (Exception ex)
			{
				logger.error("", ex);
			}
		}
	}

	public void cancel()
	{
		cancelled = true;
		this.interrupt();
	}

	@Override
	public long getLastActiveTime()
	{
		// TODO Auto-generated method stub
		return activeTime;
	}

}
