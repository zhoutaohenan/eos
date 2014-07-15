package com.surge.communication.framework;

import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;

/**
 * 协议包处理器
 * 
 * @author Administrator
 * 
 */
public interface Processor
{

	/***
	 * 处理具体连接回来的消息包
	 * 
	 * @param client
	 * @param pMessage
	 */
	public void doProcess(Client client, PMessage pMessage);
}
