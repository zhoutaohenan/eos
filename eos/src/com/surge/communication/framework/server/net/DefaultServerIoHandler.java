package com.surge.communication.framework.server.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.communication.framework.server.SClientFactory;
import com.surge.engine.protocol.sms.SmsAbstractClient;

/**
 * �����IoHandlerʵ��
 * 
 * @project: nioframe
 * @Date:2010-7-29
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class DefaultServerIoHandler
		extends IoHandlerAdapter
{

	/** Э��������� */
	private final Processor processor;

	/** client�������� */
	private final SClientFactory clientFactory;

	private final ConnectionPool connectionPool;

	public DefaultServerIoHandler(Processor processor,
			SClientFactory clientFactory, ConnectionPool connectionPool)
	{
		this.processor = processor;
		this.clientFactory = clientFactory;
		this.connectionPool = connectionPool;
	}

	public void sessionOpened(IoSession session) throws Exception
	{
		// �յ�TCP�������󣬴���һ��client ����session��
		Client client = clientFactory.createClient();
		session.setAttribute("client", client);
		client.setIoSession(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		Client client = (Client) session.getAttribute("client");
		if (client != null)
		{
			client.setUnAvailable();
		}
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		try
		{
			SmsAbstractClient client = (SmsAbstractClient) session
					.getAttribute("client");
			if (client == null)
			{
				client = clientFactory.createClient();
				session.setAttribute("client", client);
				client.setIoSession(session);
			}
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
		if (client != null)
		{
			client.setUnAvailable();
			// tcp���ӶϿ������������ӳ���ɾ��������
			connectionPool.removeClient(client);
		}
	}
}