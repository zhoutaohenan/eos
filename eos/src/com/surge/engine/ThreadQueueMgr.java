package com.surge.engine;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * �����̶߳���
 * 
 * @description
 * @project: esk
 * @Date:2010-10-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class ThreadQueueMgr
{
	public static ThreadQueueMgr instance = new ThreadQueueMgr();

	private ConcurrentLinkedQueue<BaseThread> threadLinkedQueue = new ConcurrentLinkedQueue<BaseThread>();

	private ThreadQueueMgr()
	{

	}

	/**
	 * ���̷߳ŵ���ض��� TODO
	 * 
	 * @param thread
	 *            void
	 * @throws
	 */
	public void addThread(BaseThread thread)
	{
		threadLinkedQueue.add(thread);
	}
	/**
	 * ���̼߳�ض�������ȥ�߳� TODO
	 * 
	 * @param thread
	 *            void
	 * @throws
	 */
	public void removeThread(BaseThread thread)
	{
		threadLinkedQueue.remove(thread);
	}

	public ConcurrentLinkedQueue<BaseThread> getThreadLinkedQueue()
	{
		return threadLinkedQueue;
	}

}
