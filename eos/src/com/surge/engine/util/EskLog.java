package com.surge.engine.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-14
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class EskLog
{
	public static final int ERROR = 4;

	public static final int INFO = 2;

	public static final int DEBUG = 1;

	private static AtomicInteger curreLevel = new AtomicInteger(INFO);

	public static boolean isDebugEnabled()
	{
		return curreLevel.get() <= DEBUG;
	}

	public static boolean isInfoEnabled()
	{
		return curreLevel.get() <= INFO;
	}

	public static void setLogLevel(int curreLevel)
	{
		EskLog.curreLevel.set(curreLevel);
	}

	public static int getLogLevel()
	{
		return EskLog.curreLevel.get();
	}
}
