package com.surge.engine.protocol.sms.gw.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.client.service.ClientKeepConnection;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.gw.client.GwClient;
import com.surge.engine.protocol.sms.gw.pmsg.CLinkReq;
import com.surge.engine.protocol.sms.gw.pmsg.CLoginReq;
import com.surge.engine.protocol.sms.gw.pmsg.CLogoutReq;
import com.surge.engine.sms.conf.GwConfig;
import com.surge.engine.util.TimerPool;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class GwClientKeepConnection extends ClientKeepConnection
{

	private Logger logger = Logger.getLogger(GwClientKeepConnection.class);

	private static final String SVERSION = "SE9.0.0";

	private static final int ITYPE = 2;

	private GwConfig config;

	public GwClientKeepConnection(GwConfig protocolConfig, IoHandler ioHandler,
			ConnectionPool connectionPool, ProtocolCodecFilter protocolCodecFilter)
	{
		super(protocolConfig, ioHandler, connectionPool, protocolCodecFilter);
		this.config = protocolConfig;
		TimerPool.schedule(new Work(), 1, 10);
		TimerPool.schedule(new CreateConnect(), 1, 30);
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
				GwClient client = (GwClient) c;
				if (client.getWindowSizeFullTime() - System.currentTimeMillis() > client
						.getExpireWindowTime())
				{
					logger.info("���� protocolId:" + client.getProtocolId() + "���ӻ������ڵ���������!");
					// ��ջ�������
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
			int count = this.config.getConnCount()
					- this.connectionPool.getActiveClientCount(this.protocolConfig.getProtocolId());
			for (int i = 0; i < count; i++)
			{
				IoSession session = this.createIoSession();
				if (session == null || !session.isConnected() || !session.isConnected())
				{
					continue;
				}
				logger.info("��gw���ؽ���Tcp���ӳɹ� ,���͵�¼��Ϣ protocol:" + config.getProtocolId());
				Client gwClient = new GwClient(session, config);
				this.connectionPool.addClient(gwClient, this.config.getProtocolId());
				session.setAttribute("client", gwClient);
				CLoginReq loginReq = new CLoginReq(config.getUserName(), config.getPwd());
				loginReq.iType = ITYPE;
				loginReq.sVersion = SVERSION;
				gwClient.sendPMessage(loginReq);
			}
		} catch (Exception e)
		{
			logger.error("", e);
		}
	}
	@Override
	public void keepActive()
	{

		List<Client> activeClients = this.connectionPool.getActiveClient(this.config
				.getProtocolId());
		for (Client activeclient : activeClients)
		{
			GwClient client = (GwClient) activeclient;
			if (client.isAvailable())
			{
				long cur_time = System.currentTimeMillis();

				if (cur_time - client.getLastReceveTime() > 360000)
				{
					// ����Ѿ�6����û��������������ʧЧ
					client.setUnAvailable();
					logger.error("����6����������û�н���������ʧЧ");
				} else if (client.isLogined())
				{
					if (cur_time - client.getLastReceveTime() > 10 * 1000)
					{
						CLinkReq linkReq = new CLinkReq(config.getUserName());
						client.sendPMessage(linkReq);
					}
				}
			}
		}
	}

	class Work implements Runnable
	{

		@Override
		public void run()
		{
			// �������
			checkConnect();
			// ����ʱ����
			clearClientData();
			// ��·���
			keepActive();

		}

	}
	class CreateConnect implements Runnable
	{
		@Override
		public void run()
		{
			// ��������
			connect();

		}
	}

	public void stop()
	{
		List<Client> activeClients = this.connectionPool.getActiveClient(this.config
				.getProtocolId());
		for (Client activeclient : activeClients)
		{
			GwClient client = (GwClient) activeclient;
			if (client.isAvailable())
			{
				CLogoutReq logout = new CLogoutReq(config.getUserName());
				client.sendPMessage(logout);
				client.setUnAvailable();
			}
		}
		logger.info("�ر�" + config.getProtocolId() + "��������");
	}

}
