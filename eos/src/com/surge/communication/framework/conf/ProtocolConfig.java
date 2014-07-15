package com.surge.communication.framework.conf;

/**
 * 协议配置信息(具体协议配置信息需要实现并扩展)
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface ProtocolConfig
{

	/**
	 * 
	 * 获取协议账号ID
	 * 
	 * @return String
	 * @throws
	 */
	String getProtocolId();


	/**
	 * 
	 * 获取IP
	 * 
	 * @return String
	 * @throws
	 */
	String getIp();


	/**
	 * 
	 * 获取端口
	 * 
	 * @return int
	 * @throws
	 */
	int getPort();


	/**
	 * 
	 * 获取连接数
	 * 
	 * @return int
	 * @throws
	 */
	int getConnCount();


	
	/**
	 * 
	 * 获取端口
	 * 
	 * @return int
	 * @throws
	 */
	int getListenPort();
	
	/**
	 * 是否支持长短信
	 * TODO
	 *
	 * @return boolean
	 * @throws
	 */
	
	boolean isSupportLongMoSms();
	/**
	 * 设置是否支持长短信
	 * TODO
	 *
	 * @param isSms void
	 * @throws
	 */
	void setLongMoSms(boolean isSms);


}
