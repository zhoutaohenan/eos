package com.surge.communication.framework.net;

import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.common.PMessage;

/**
 * 
 * 一条连接
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface Client
{

	/**
	 * 发送一条消息
	 * 
	 * @param pMessage
	 * @return
	 */
	int sendPMessage(PMessage pMessage);

	/**
	 * 
	 * TCP连接是否有效
	 * 
	 * @return boolean
	 * @throws
	 */
	boolean isAvailable();

	/**
	 * 
	 * 设置tcp连接是否有效
	 * 
	 * @param available
	 *            void
	 * @throws
	 */
	void setUnAvailable();

	/**
	 * 
	 * 是否登录服务端成功
	 * 
	 * @return boolean
	 * @throws
	 */
	boolean isLogined();

	/**
	 * 
	 * 设置是否登录成功
	 * 
	 * @param logined
	 *            void
	 * @throws
	 */
	void setLogined(boolean logined);

	/**
	 * 
	 * 获取最后一次交互时间
	 * 
	 * @return long
	 * @throws
	 */
	long getLastReceveTime();

	/**
	 * 
	 * 设置最后一次交互时间
	 * 
	 * @param lastReceveTime
	 *            void
	 * @throws
	 */
	void setLastReceveTime(long lastReceveTime);

	/**
	 * 
	 * 获取此链接属于那个一协议账号
	 * 
	 * @return String
	 * @throws
	 */
	String getProtocolId();

	/**
	 * 
	 * 设置IoSession void
	 * 
	 * @throws
	 */
	void setIoSession(IoSession ioSession);

}
