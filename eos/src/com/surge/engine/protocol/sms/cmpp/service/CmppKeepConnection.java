package com.surge.engine.protocol.sms.cmpp.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.client.service.ClientKeepConnection;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.cmpp.net.CmppClient;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2Connect;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2KeepActive;
import com.surge.engine.sms.conf.Cmpp2Config;
import com.surge.engine.util.TimerPool;

public class CmppKeepConnection extends ClientKeepConnection
{

	private static final Logger logger = Logger.getLogger(CmppKeepConnection.class);

	private Cmpp2Config config;

	public CmppKeepConnection(ProtocolConfig protocolConfig, IoHandler ioHandler,
			ConnectionPool connectionPool, ProtocolCodecFilter protocolCodecFilter)
	{

		super(protocolConfig, ioHandler, connectionPool, protocolCodecFilter);
		this.config = (Cmpp2Config) protocolConfig;
		TimerPool.schedule(new ConnectWroker(), 1, 30);
		TimerPool.schedule(new Worker(), 10, 10);
	}

	class Worker implements Runnable
	{

		@Override
		public void run()
		{
			keepActive();
			clearClientData();
		}

	}

	class ConnectWroker implements Runnable
	{
		@Override
		public void run()
		{
			connect();
		}

	}

	@Override
	public void clearClientData()
	{
		try
		{
			List<Client> clientList = this.connectionPool.getAllClient(this.protocolConfig
					.getProtocolId());
			for (Client c : clientList)
			{
				CmppClient client = (CmppClient) c;
				if (client.getWindowSizeFullTime() - System.currentTimeMillis() > client
						.getExpireWindowTime())
				{
					logger.info("清理 protocolId:" + client.getProtocolId() + "连接滑动窗口的垃圾数据!");
					// 清空滑动窗口
					client.removeSeqIdAll();
				}
			}
		} catch (Exception e)
		{
			logger.error("", e);
		}

	}

	@Override
	public void connect()
	{
		try
		{
			int count = this.protocolConfig.getConnCount()
					- this.connectionPool.getActiveClientCount(this.protocolConfig.getProtocolId());
			for (int i = 0; i < count; i++)
			{
				logger.info("正在创建CMPP连接 IP:" + config.getIp() + " port:" + config.getPort()
						+ " userName:" + config.getUserName());

				IoSession session = this.createIoSession();
			
				if (session == null || session.isClosing()|| !session.isConnected())
				{
					continue;
				}
				logger.info("与cmpp网关建立Tcp连接成功 ,发送登录消息 protocol:" + config.getIsmgid());
				Client cmppClient = new CmppClient(session, config);
				this.connectionPool.addClient(cmppClient, this.protocolConfig.getProtocolId());
				session.setAttribute("client", cmppClient);

				Cmpp2Connect con = new Cmpp2Connect(config.getUserName(), config.getPwd(),
						(byte) 0x20);
				cmppClient.sendPMessage(con);
			}
		} catch (Exception e)
		{
			logger.error("", e);
		}

	}
	@Override
	public void keepActive()
	{
		try
		{
			List<Client> activeClients = this.connectionPool.getActiveClient(this.config
					.getProtocolId());
			for (Client activeclient : activeClients)
			{
				CmppClient client = (CmppClient) activeclient;
				if (client.isAvailable())
				{
					long cur_time = System.currentTimeMillis();

					if (cur_time - client.getLastReceveTime() > 360000)
					{
						// 如果已经6分钟没有心跳，则连接失效
						client.setUnAvailable();
						logger.error("超过6分钟与网关没有交互，连接失效");
					} else if (client.isLogined())
					{
						if (cur_time - client.getLastReceveTime() > 30000)
						{
							Cmpp2KeepActive keepalive = new Cmpp2KeepActive();
							client.sendPMessage(keepalive);
						}
					}
				}
			}
		} catch (Exception e)
		{
			logger.error("", e);
		}

	}

	/**
	 * 关闭所有连接
	 * TODO
	 * void
	 * @throws
	 */
	public void stop()
	{
		List<Client> activeClients = this.connectionPool.getActiveClient(this.config
				.getProtocolId());
		for (Client c : activeClients)
		{
			CmppClient client = (CmppClient) c;
			if (client.isAvailable())
			{
				client.setUnAvailable();
			}
		}
	}

}
