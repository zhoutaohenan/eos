package com.surge.communication.framework;

import com.surge.communication.framework.common.PMessage;

/**
 * 一个协议账号
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface Protocol {

	/**
	 * 
	 * 启动Protocol void
	 * 
	 * @throws
	 */
	boolean start();

	/**
	 * 
	 * 停止Protocol void
	 * 
	 * @throws
	 */
	void stop();

	/***
	 * 
	 * 发送一PMessage条消息
	 * 
	 * @param pMessage
	 * @return int
	 * @throws
	 */
	int sendPMessage(PMessage pMessage);


	/**
	 * 
	 * 获取协议账号ID
	 * 
	 * @return String
	 * @throws
	 */
	 String getProtocolId();

}
