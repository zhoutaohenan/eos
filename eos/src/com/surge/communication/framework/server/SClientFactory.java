package com.surge.communication.framework.server;

import com.surge.engine.protocol.sms.SmsAbstractClient;

/**
 * ���Ӵ���������ͨ������ʵ�ִ�������Э������ӣ�
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