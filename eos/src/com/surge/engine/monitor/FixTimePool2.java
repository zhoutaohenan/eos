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
	 * 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期； 也就是将在 initialDelay 后开始执行，然后在
	 * initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推。
	 * initialDelay、period单位为秒。
	 * 
	 * @param command
	 *            要执行的任务。
	 * @param delay
	 *            首次执行的延迟时间。
	 * @param period
	 *            连续执行之间的周期。
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
	 * 销毁定时器池对象
	 */
	public synchronized static void destroy()
	{
		if (pool != null)
		{
			pool.shutdown();
			pool = null;
			Logger.getLogger(FixTimePool2.class).debug("销毁TimerPool成功。");
		}
	}

	/**
	 * 初始化定时器池对象
	 * 
	 */
	public synchronized static void init()
	{
		if (pool == null || pool.isShutdown())
		{
			pool = new ScheduledThreadPoolExecutor(3);
			pool.setMaximumPoolSize(3);
			Logger.getLogger(FixTimePool2.class).debug("初始化TimerPool成功。");
		}
	}

	/**
	 * 检查TimerPool是否已经被初始化,如果没有被初始化则抛出NullPointerException异常
	 */
	private static void assertValid()
	{
		if (pool == null || pool.isShutdown())
		{
			throw new NullPointerException("TimerPool还没有被初始化, 请首先调用TimerPool.init()进行初始化");
		}
	}

	/**
	 * 修改定时器核心线程数，包括空闲线程数量
	 * 
	 * 
	 * @param size
	 *            定时器线程数量
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
					return "错误啦.........";
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
		// 如果通知操作未完成，则中止，因为超时
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
