package com.surge.communication.framework.client.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.AbstractClient;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsAbstractClient;

/**
 * @description
 * @project: nioframe
 * @Date:2010-7-29
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class DefaultClientIoHandler
		extends IoHandlerAdapter
{

	/** 协议包处理器 */
	private final Processor processor;

	public DefaultClientIoHandler(Processor processor)
	{
		this.processor = processor;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		AbstractClient client = (AbstractClient) session.getAttribute("client");
		client.setUnAvailable();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		try
		{
			SmsAbstractClient client = (SmsAbstractClient) session.getAttribute("client");
			client.setLastReceveTime(System.currentTimeMillis());
			processor.doProcess(client, (PMessage) message);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		Client client = (Client) session.getAttribute("client");
		client.setUnAvailable();
	}
}