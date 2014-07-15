package com.surge.engine;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.surge.engine.dbadapter.dao.DBOperateDao;
import com.surge.engine.dbadapter.runnable.DistractThread;
import com.surge.engine.dbadapter.runnable.MtDistractThread;
import com.surge.engine.dbadapter.runnable.SmsMoProcessThread;
import com.surge.engine.dbadapter.runnable.SmsProcessThread;
import com.surge.engine.dbadapter.runnable.SmsSendThread;
import com.surge.engine.sms.service.SmsAgentServiceImpl;
import com.surge.engine.util.Tools;

public class ThreadMonitor extends Thread
{
	private Logger logger = Logger.getLogger(ThreadMonitor.class);

	private ThreadQueueMgr quereMgr = ThreadQueueMgr.instance;

	public void run()
	{
		this.setName("ThreadMonitor");
		while (true)
		{
			try
			{
				Tools.csleep(1000 * 60 * 3);
				doMonitor();
			} catch (InterruptedException e)
			{
				logger.info("线程中断");
				this.interrupt();
			}
		}
	}
	private void doMonitor()
	{
		int threadNum = quereMgr.getThreadLinkedQueue().size();
		Iterator<BaseThread> threadIte = quereMgr.getThreadLinkedQueue().iterator();
		while (threadNum > 0)
		{
			BaseThread temp = threadIte.next();
			threadNum--;
			if ((System.currentTimeMillis() - temp.getLastActiveTime() > 5 * 60 * 1000)
					|| !temp.isAlive())
			{
				logger.info(temp.getName() + " 线程不活动时间超时,重启该线程");
				threadIte.remove();
				temp.interrupt();
				try
				{
					temp.join(0);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
//				if (temp instanceof SmsSendThread)
//				{
//					temp = new SmsSendThread(((SmsSendThread) temp).getSmsAgent());
//
//				} else 
				if (temp instanceof MtDistractThread)
				{
					temp = new MtDistractThread(((MtDistractThread) temp).getSmsAgent());
				} else if (temp instanceof DistractThread)
				{
					temp = new DistractThread();
				} else if (temp instanceof SmsProcessThread)
				{
					temp = new SmsProcessThread(((SmsProcessThread) temp).basicOperate);
				} else if (temp instanceof SmsMoProcessThread)
				{
					temp = new SmsMoProcessThread(((SmsMoProcessThread) temp).basicOperate);
				}
				// 重启线程
				temp.start();
				// 重新放到线程监控队列中
				quereMgr.addThread(temp);
			}
		}

	}
}
