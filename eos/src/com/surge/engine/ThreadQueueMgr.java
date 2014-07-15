package com.surge.engine;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 管理线程队列
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
	 * 将线程放到监控队列 TODO
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
	 * 从线程监控队列中移去线程 TODO
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
