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
	 * DbAdapter 服务启动
	 * 
	 * @throws
	 */
	void start();

	/**
	 * 
	 * DbAdapter 服务关闭
	 * 
	 * @throws
	 */
	void stop();

	/**
	 * 关闭发送线程接口　
	 * TODO
	 * void
	 * @throws
	 */
	void stopSendThread();

	/**
	 * 启动发送线程接口
	 * TODO
	 * void
	 * @throws
	 */
	void startSendThread();
}
