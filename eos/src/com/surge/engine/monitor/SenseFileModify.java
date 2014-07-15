package com.surge.engine.monitor;

import java.io.File;

import org.apache.log4j.Logger;

import com.surge.engine.dbadapter.util.FixTimePool;

/**
 * 检查通讯配置文件是否修改线程
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-19
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SenseFileModify implements Runnable
{
	private static Logger logger = Logger.getLogger(SenseFileModify.class);

	private static final String CONFIG_FILE = "config/smsconfig.xml";

	private long lastModifyTime;

	private RestartAdapter statAdapter;

	public SenseFileModify(RestartAdapter statAdapter)
	{
		this.statAdapter = statAdapter;
		lastModifyTime = new File(CONFIG_FILE).lastModified();
	}

	@Override
	public void run()
	{
		File configFile = new File(CONFIG_FILE);

		if (configFile.lastModified() != lastModifyTime)
		{
			logger.info("程序侦测到通讯配置文件修改，程序自动重启!");
			lastModifyTime = configFile.lastModified();
			// 侦测出通讯配置信息修改过关闭
			logger.info("*********** 开始关闭*************");
			statAdapter.stop();
			logger.info("*********** 关闭结束*************");
			logger.info("*********** 开始重启*************");
			statAdapter.start();
			logger.info("*********** 重启成功*************");
		}
	}

	public long getLastModifyTime()
	{
		return lastModifyTime;
	}

	public void setLastModifyTime(long lastModifyTime)
	{
		this.lastModifyTime = lastModifyTime;
	}

}
