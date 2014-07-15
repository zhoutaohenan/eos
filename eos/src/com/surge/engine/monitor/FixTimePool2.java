package com.surge.engine.monitor;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-13
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class FixTimePool2
{
	private static ScheduledThreadPoolExecutor pool = null;

	/**
	 * ������ִ��һ���ڸ�����ʼ�ӳٺ��״����õĶ��ڲ����������������и��������ڣ� Ҳ���ǽ��� initialDelay ��ʼִ�У�Ȼ����
	 * initialDelay+period ��ִ�У������� initialDelay + 2 * period ��ִ�У��������ơ�
	 * initialDelay��period��λΪ�롣
	 * 
	 * @param command
	 *            Ҫִ�е�����
	 * @param delay
	 *            �״�ִ�е��ӳ�ʱ�䡣
	 * @param period
	 *            ����ִ��֮������ڡ�
	 */
	public static void schedule(Runnable command, long delay, long period)
	{
		assertValid();
		pool.scheduleAtFixedRate(command, delay, period, TimeUnit.SECONDS);
	}

	public static void schedule(Runnable command, long delay)
	{
		assertValid();
		pool.schedule(command, delay, TimeUnit.SECONDS);
	}

	/**
	 * ���ٶ�ʱ���ض���
	 */
	public synchronized static void destroy()
	{
		if (pool != null)
		{
			pool.shutdown();
			pool = null;
			Logger.getLogger(FixTimePool2.class).debug("����TimerPool�ɹ���");
		}
	}

	/**
	 * ��ʼ����ʱ���ض���
	 * 
	 */
	public synchronized static void init()
	{
		if (pool == null || pool.isShutdown())
		{
			pool = new ScheduledThreadPoolExecutor(3);
			pool.setMaximumPoolSize(3);
			Logger.getLogger(FixTimePool2.class).debug("��ʼ��TimerPool�ɹ���");
		}
	}

	/**
	 * ���TimerPool�Ƿ��Ѿ�����ʼ��,���û�б���ʼ�����׳�NullPointerException�쳣
	 */
	private static void assertValid()
	{
		if (pool == null || pool.isShutdown())
		{
			throw new NullPointerException("TimerPool��û�б���ʼ��, �����ȵ���TimerPool.init()���г�ʼ��");
		}
	}

	/**
	 * �޸Ķ�ʱ�������߳��������������߳�����
	 * 
	 * 
	 * @param size
	 *            ��ʱ���߳�����
	 */
	public synchronized static void setCorePoolSize(int size)
	{
		assertValid();
		pool.setCorePoolSize(size);
		pool.setMaximumPoolSize(size);
	}

	public static void main(String[] args)
	{
		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(2);
		Future future = pool.schedule(new Callable<String>()
		{
			public String call() throws Exception
			{
				try
				{
					Thread.sleep(20 * 1000);
					System.out.println("run()...");
				} catch (InterruptedException e)
				{
					e.printStackTrace();
					Thread.currentThread().interrupt();
					System.out.println("12345");
					return "������.........";
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				return get();
			}

			private String get()
			{
				return "hello:";
				// throw new NullPointerException();
			}

		}, 1, TimeUnit.SECONDS);
		pool.schedule(new Checker(future), 5, TimeUnit.SECONDS);
	}
}

class Checker implements Runnable
{
	private Future<String> future;

	Checker(Future<String> future)
	{
		this.future = future;
	}

	public void run()
	{
		// ���֪ͨ����δ��ɣ�����ֹ����Ϊ��ʱ
		if (!future.isDone())
		{
			try
			{
				future.cancel(true);
			} catch (RuntimeException e1)
			{
				e1.printStackTrace();
			}

			try
			{
				System.out.println(future.isDone());
				String result = future.get();
				System.out.println(result);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (ExecutionException e)
			{
				e.printStackTrace();
			} catch (CancellationException e)
			{
				e.printStackTrace();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("cancel");
		}
	}
}
