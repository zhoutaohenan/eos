package com.surge.engine.monitor;

import com.surge.engine.dbadapter.runnable.InitSmsChannel;
import com.surge.engine.dbadapter.service.DbAdapterManager;
import com.surge.engine.dbadapter.util.FixTimePool;
import com.surge.engine.monitor.thread.FixScanQueueRunnable;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.service.SmsManagerImpl;
import com.surge.engine.util.TimerPool;

/**
 * �޸�ͨѶ���ã��Զ�����������
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

	/** �������� */
	private SmsConfig smsConfig = SmsConfig.getInstance();

	public RestartAdapter(DbAdapterManager dbAdapter, SmsManagerImpl senderManager)
	{
		this.dbAdapter = dbAdapter;
		this.senderManager = senderManager;
		scanQueue = new FixScanQueueRunnable();
		FixTimePool.schedule(scanQueue, 0, 60 * 12 * 60 * 1000);
	}

	/**
	 * �޸��ļ�ʱ���رսӿڡ� TODO void
	 * 
	 * @throws
	 */
	public void stop()
	{
		TimerPool.destroy();
		// �ر�ת�ơ������߳�
		dbAdapter.stopSendThread();
		// �ر�ͨ���̼߳�ͨ��Э���
		senderManager.stop();
		// �ر�ͨѶ�����ļ���
		smsConfig.stop();
		scanQueue = null;
	}
	/**
	 * �޸��ļ�ʱ�������ӿڡ� TODO void
	 * 
	 * @throws
	 */
	public void start()
	{
		// ��ʼ��ϵͳ��ʱ�̳߳�
		TimerPool.init();
		// ���¼���ͨѶ�����ļ���
		smsConfig = SmsConfig.getInstance();
		// ����ͨ���̼߳�ͨ��Э���
		senderManager.start();
		try
		{
			Thread.currentThread().sleep(10*1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		// ���������߸���MTת���߳�
		dbAdapter.startSendThread();
		// ������ʱ��������ļ��Ƿ��޸Ķ�ʱ��
		scanQueue = new FixScanQueueRunnable();
		TimerPool.schedule(scanQueue, 0, 60 * 1000);
		// ͬ��ͨ����Ϣ�����ݿ�
		InitSmsChannel.initChannel();
	}

}
