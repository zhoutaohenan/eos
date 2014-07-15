package com.surge.engine.util;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-13
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class ThreadFactory
{
	public static Thread newThread(String name, Runnable runnable)
	{
		return new Thread(runnable, name);
	}
}
