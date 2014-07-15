package com.surge.engine.dbadapter.service;

import com.surge.engine.ThreadQueueMgr;
import com.surge.engine.dbadapter.runnable.DistractThread;
import com.surge.engine.dbadapter.runnable.MtDistractThread;
import com.surge.engine.dbadapter.runnable.Routine;
import com.surge.engine.dbadapter.util.FixTimePool;
import com.surge.engine.sms.service.SmsAgentService;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class DbAdapterManagerImpl implements DbAdapterManager
{

	private MtDistractThread mtDistractThread;

	private SmsAgentService smsAgent;

	private DistractThread distractThread;

	private ThreadQueueMgr queueMgr = ThreadQueueMgr.instance;

	public DbAdapterManagerImpl(SmsAgentService smsAgent)
	{
		this.smsAgent = smsAgent;
		FixTimePool.schedule(new Routine(), 10, 60 * 60 * 12);
	}

	@Override
	public void start()
	{
		mtDistractThread = new MtDistractThread(smsAgent);
		distractThread = new DistractThread();

		mtDistractThread.start();
		queueMgr.addThread(mtDistractThread);
		distractThread.start();
		queueMgr.addThread(distractThread);
	}

	@Override
	public void stop()
	{
		if (mtDistractThread != null)
		{
			mtDistractThread.interrupt();
		}
		if (distractThread != null)
		{
			distractThread.cancel();
		}
	}
	public void stopSendThread()
	{
		queueMgr.removeThread(mtDistractThread);
		mtDistractThread.interrupt();
	}
	public void startSendThread()
	{
		mtDistractThread = new MtDistractThread(smsAgent);
		mtDistractThread.start();
		queueMgr.addThread(mtDistractThread);
	}

}
