package com.surge.engine.protocol.sms.sgip.client;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsAbstractClient;

public class SgipClientIoHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger.getLogger(SgipClientIoHandler.class);

	private Processor processor;

	public SgipClientIoHandler(Processor processor)
	{
		this.processor = processor;

	}
	@Override
	public void messageReceived(IoSession session, Object message)
	{
		SmsAbstractClient client = (SmsAbstractClient) session.getAttribute("client");
		// 设置最后一次收到网关消息的时间
		client.setLastReceveTime(System.currentTimeMillis());
		try
		{
			processor.doProcess(client, (PMessage) message);
		} catch (Exception e)
		{
			logger.error("处理sgip消息出错", e);
		}

	}
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{

		logger.error("EXCEPTION, please implement " + getClass().getName()
				+ ".exceptionCaught() for proper handling:", cause);
//		Client client = (Client) session.getAttribute("client");
//		if (client != null)
//		{
//			client.setUnAvailable();
//		}
		session.close(true);

	}
	public void sessionClosed(IoSession session) throws Exception
	{
		Client client = (Client) session.getAttribute("client");
		if (client != null)
		{
			logger.info("*********开始关闭protocolId:" + client.getProtocolId()+"连接***");
			client.setUnAvailable();
			logger.info("*********关闭protocolId:" + client.getProtocolId()+"连接成功***");
		}
	}
}
