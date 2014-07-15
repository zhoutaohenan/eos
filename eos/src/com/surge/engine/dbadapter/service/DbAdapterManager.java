package com.surge.engine.dbadapter.service;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-9
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface DbAdapterManager
{
	/**
	 * 
	 * DbAdapter ��������
	 * 
	 * @throws
	 */
	void start();

	/**
	 * 
	 * DbAdapter ����ر�
	 * 
	 * @throws
	 */
	void stop();

	/**
	 * �رշ����߳̽ӿڡ�
	 * TODO
	 * void
	 * @throws
	 */
	void stopSendThread();

	/**
	 * ���������߳̽ӿ�
	 * TODO
	 * void
	 * @throws
	 */
	void startSendThread();
}
