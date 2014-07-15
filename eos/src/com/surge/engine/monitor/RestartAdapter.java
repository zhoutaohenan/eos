package com.surge.engine.monitor;

import com.surge.engine.dbadapter.runnable.InitSmsChannel;
import com.surge.engine.dbadapter.service.DbAdapterManager;
import com.surge.engine.dbadapter.util.FixTimePool;
import com.surge.engine.monitor.thread.FixScanQueueRunnable;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.service.SmsManagerImpl;
import com.surge.engine.util.TimerPool;

/**
 * 修改通讯配置，自动重启管理器
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-22
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class RestartAdapter
{
	private DbAdapterManager dbAdapter;

	private SmsManagerImpl senderManager;

	private FixScanQueueRunnable scanQueue;

	/** 短信配置 */
	private SmsConfig smsConfig = SmsConfig.getInstance();

	public RestartAdapter(DbAdapterManager dbAdapter, SmsManagerImpl senderManager)
	{
		this.dbAdapter = dbAdapter;
		this.senderManager = senderManager;
		scanQueue = new FixScanQueueRunnable();
		FixTimePool.schedule(scanQueue, 0, 60 * 12 * 60 * 1000);
	}

	/**
	 * 修改文件时，关闭接口　 TODO void
	 * 
	 * @throws
	 */
	public void stop()
	{
		TimerPool.destroy();
		// 关闭转移、发送线程
		dbAdapter.stopSendThread();
		// 关闭通道线程及通道协议层
		senderManager.stop();
		// 关闭通讯配置文件　
		smsConfig.stop();
		scanQueue = null;
	}
	/**
	 * 修改文件时，启动接口　 TODO void
	 * 
	 * @throws
	 */
	public void start()
	{
		// 初始化系统定时线程池
		TimerPool.init();
		// 重新加载通讯配置文件　
		smsConfig = SmsConfig.getInstance();
		// 启动通道线程及通道协议层
		senderManager.start();
		try
		{
			Thread.currentThread().sleep(10*1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		// 启动发送线各及MT转移线程
		dbAdapter.startSendThread();
		// 启动定时侦测配置文件是否修改定时器
		scanQueue = new FixScanQueueRunnable();
		TimerPool.schedule(scanQueue, 0, 60 * 1000);
		// 同步通道信息到数据库
		InitSmsChannel.initChannel();
	}

}
