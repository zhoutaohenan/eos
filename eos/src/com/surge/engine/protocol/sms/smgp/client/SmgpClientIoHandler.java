package com.surge.engine.protocol.sms.smgp.client;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsAbstractClient;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpSubmit;

public class SmgpClientIoHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger.getLogger(SmgpClientIoHandler.class);

	private Processor processor;

	private SmsProtocolHandler protocolHandler;

	public SmgpClientIoHandler(Processor processor, SmsProtocolHandler protocolHandler)
	{
		this.processor = processor;
		this.protocolHandler = protocolHandler;

	}
	@Override
	public void messageReceived(IoSession session, Object message)
	{
		SmsAbstractClient client = (SmsAbstractClient) session.getAttribute("client");
		// �������һ���յ�������Ϣ��ʱ��
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
		logger.info("SMGP���عر��˴�������");
		Client client = (Client) session.getAttribute("client");
		if (client != null)
		{
			client.setUnAvailable();
		}
	}
}