package com.surge.engine.protocol.sms.cmpp.net;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsAbstractClient;
import com.surge.engine.protocol.sms.SmsProtocolHandler;

public class CmppClientIoHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger.getLogger(CmppClientIoHandler.class);

	private Processor processor;

	private SmsProtocolHandler protocolHandler;

	public CmppClientIoHandler(Processor processor, SmsProtocolHandler protocolHandler)
	{
		this.processor = processor;
		this.protocolHandler = protocolHandler;

	}
	@Override
	public void messageReceived(IoSession session, Object message)
	{
		SmsAbstractClient client = (SmsAbstractClient) session.getAttribute("client");
		// 设置最后一次收到网关消息的时间
		client.setLastReceveTime(System.currentTimeMillis());
		processor.doProcess(client, (PMessage) message);
	}
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		logger.error("EXCEPTION, please implement " + getClass().getName()
				+ ".exceptionCaught() for proper handling:", cause);
		Client client = (Client) session.getAttribute("client");

		if (client != null)
		{
			client.setUnAvailable();
			protocolHandler.doConnectStatus(client.getProtocolId(), false);
		}

	}
	public void sessionClosed(IoSession session) throws Exception
	{
		logger.info("CMPP2网关关闭了此条连接");
		Client client = (Client) session.getAttribute("client");
		if (client != null)
		{
			client.setUnAvailable();
		}
	}
}
