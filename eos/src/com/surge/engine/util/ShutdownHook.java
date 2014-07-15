package com.surge.engine.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.surge.engine.comm.Persistence;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class ShutdownHook implements Runnable
{

	private static final Logger logger = Logger.getLogger(ShutdownHook.class);

	private static List<Persistence> persistences = new ArrayList<Persistence>();

	@Override
	public void run()
	{
		Thread.currentThread().setName("ShutdownHook_Thread");
		logger.info("系统退出，开始保存数据...");
		for (Persistence p : persistences)
		{
			p.saveData();
		}
		logger.info("系统退出，保存数据完毕。");
	}
	public static void add(Persistence p)
	{
		persistences.add(p);
	}
	public static void remove(Persistence p)
	{
		persistences.remove(p);
	}

}
