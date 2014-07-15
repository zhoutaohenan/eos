package com.surge.engine.monitor;

import java.io.File;

import org.apache.log4j.Logger;

import com.surge.engine.dbadapter.util.FixTimePool;

/**
 * ���ͨѶ�����ļ��Ƿ��޸��߳�
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
			logger.info("������⵽ͨѶ�����ļ��޸ģ������Զ�����!");
			lastModifyTime = configFile.lastModified();
			// ����ͨѶ������Ϣ�޸Ĺ��ر�
			logger.info("*********** ��ʼ�ر�*************");
			statAdapter.stop();
			logger.info("*********** �رս���*************");
			logger.info("*********** ��ʼ����*************");
			statAdapter.start();
			logger.info("*********** �����ɹ�*************");
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
