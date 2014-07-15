package com.surge.engine.protocol.sms.gw.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.gw.pmsg.CLogoutReq;
import com.surge.engine.sms.conf.GwConfig;

public class GwClientIoHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger.getLogger(GwClientIoHandler.class);

	private Processor processor;

	private SmsProtocolHandler protocolHandler;

	private GwConfig protocolConfig;

	/** 协议账号连接池 */
	private final ConnectionPool connectionPool;

	public GwClientIoHandler(Processor processor, SmsProtocolHandler protocolHandler,
			GwConfig protocolConfig, ConnectionPool connectionPool)
	{
		this.processor = processor;
		this.protocolHandler = protocolHandler;
		this.protocolConfig = protocolConfig;
		this.connectionPool = connectionPool;

	}
	@Override
	public void messageReceived(IoSession session, Object message)
	{
		Client client = (Client) session.getAttribute("client");
		// 收到一条消息包，设置最后一次连接时间
		client.setLastReceveTime(System.currentTimeMillis());
		try
		{
			processor.doProcess(client, (PMessage) message);
		} catch (Exception e)
		{
			logger.error("处理消息报出错", e);
		}

	}
	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		Client client = (Client) session.getAttribute("client");
		if (client != null)
		{
			client.setUnAvailable();
		}
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		logger.error("EXCEPTION, please implement " + getClass().getName()
				+ ".exceptionCaught() for proper handling:", cause);

		Client client = (Client) session.getAttribute("client");
		if (client != null)
		{
			// 若不是网关那边主动关闭出现的异常，则发登出消息包，以使网关尽快关闭此条通道
			if (!(cause instanceof IOException))
			{
				CLogoutReq logout = new CLogoutReq(protocolConfig.getUserName());
				client.sendPMessage(logout);
			}
			connectionPool.removeClient(client);
			protocolHandler.doConnectStatus(client.getProtocolId(), false);
			client.setUnAvailable();
		}
	}

}
