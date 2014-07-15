package com.surge.engine.sms.service;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface SmsManager
{
	/**
	 * 
	 * sms 服务启动
	 * void
	 * @throws
	 */
	void start();

	/**
	 * 
	 * sms 服务关闭
	 * void
	 * @throws
	 */
	void stop();
}
