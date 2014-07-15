package com.surge.engine.dbadapter.runnable;

import org.apache.log4j.Logger;

import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.util.DistractUtil;

public class Routine implements Runnable
{
	private static Logger logger = Logger.getLogger(Routine.class);

	private DbConfig instance = DbConfig.instance;

	public void run()
	{
		try
		{
			logger.info("开始转移状态报告超时没回的短信");
			DistractUtil.distractUtil.distract(instance.getQueryExpireSql());
		} catch (Exception e)
		{
			logger.error("", e);
		}
	}

}
