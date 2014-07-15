package com.surge.communication.framework.server;

import com.surge.engine.protocol.sms.SmsAbstractClient;

/**
 * 连接创建工厂（通过具体实现创建具体协议的连接）
 * 
 * @project: nioframe
 * @Date:2010-7-29
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface SClientFactory
{
	public SmsAbstractClient createClient();
}