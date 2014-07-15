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
		// �������һ���յ�������Ϣ��ʱ��
		client.setLastReceveTime(System.currentTimeMillis());
		try
		{
			processor.doProcess(client, (PMessage) message);
		} catch (Exception e)
		{
			logger.error("����sgip��Ϣ����", e);
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
			logger.info("*********��ʼ�ر�protocolId:" + client.getProtocolId()+"����***");
			client.setUnAvailable();
			logger.info("*********�ر�protocolId:" + client.getProtocolId()+"���ӳɹ�***");
		}
	}
}
