package com.surge.engine.protocol.sms.sgip.service;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.client.service.ClientKeepConnection;
import com.surge.communication.framework.net.AbstractClient;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.sgip.client.SgipClient;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipBind;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.engine.util.TimerPool;

/**
 * SgipЭ������ ����,����
 * 
 * @description
 * @project: esk
 * @Date:2010-8-6
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipClientKeepConnection extends ClientKeepConnection
{
	private Logger logger = Logger.getLogger(SgipClientKeepConnection.class);

	private SgipConfig config;

	/** ���һ�δ�������ʱ�� */
	private long lastConnTime = 0;

	public SgipClientKeepConnection(SgipConfig protocolConfig, IoHandler ioHandler,
			ConnectionPool pool, ProtocolCodecFilter codecFilter)
	{
		super(protocolConfig, ioHandler, pool, codecFilter);
		this.config = protocolConfig;
		TimerPool.schedule(new Work(), 20, 30);
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
				SgipClient client = (SgipClient) c;
				if (client.getWindowSizeFullTime() - System.currentTimeMillis() > client
						.getExpireWindowTime())
				{
					// ��ջ�������
					logger.error("�������� channel:" + client.getProtocolId() + "������������������ ");
					client.removeSeqIdAll();
				}
			}
		} catch (Exception e)
		{
			logger.error("", e);
		}

	}

	/**
	 * ����SGIP���� ���������ļ����Ѿ������Ļ�Ծ����������õ�
	 */
	@Override
	public synchronized void connect()
	{
		// ����δ�������ǰ����ֹƵ����������
		if (lastConnTime != 0 && (System.currentTimeMillis() - lastConnTime) < 1000 * 30)
		{
			return;
		}
		lastConnTime = System.currentTimeMillis();
		int count = this.protocolConfig.getConnCount()
				- this.connectionPool.getActiveClientCount(this.protocolConfig.getProtocolId());
		for (int i = 0; i < count; i++)
		{
			logger.info("���ڴ���SGIP���� IP:" + config.getIp() + " port:" + config.getPort()
					+ " userName:" + config.getUserName());

			IoSession session = this.createIoSession();
			
			if (session == null || session.isClosing()|| !session.isConnected())
			{
				continue;
			}
			logger.info("��sgip���ؽ���Tcp���ӳɹ� ,���͵�¼��Ϣ protocol:" + config.getIsmgid());
			Client sgipClient = new SgipClient(session, config);
			this.connectionPool.addClient(sgipClient, this.protocolConfig.getProtocolId());
			session.setAttribute("client", sgipClient);
			long nodeId = config.getNodeID();
			SgipBind bind = new SgipBind(nodeId, (byte) 1, config.getUserName(), config.getPwd());
			sgipClient.sendPMessage(bind);
		}
	}
	/**
	 * 
	 * ��鲢���ʧЧ����
	 * 
	 * @throws
	 */
	@Override
	public void checkConnect()
	{
		try
		{

			List<Client> clients = connectionPool.getAllClient(this.protocolConfig.getProtocolId());
			Iterator<Client> it = clients.iterator();
			for (; it.hasNext();)
			{
				AbstractClient client = (AbstractClient) it.next();

				// �������ʧЧ���������
				if ((!client.isAvailable()))
				{
					logger.info("SGIP���������ؽ�������������ʧЧ,�Ƴ��������� protocolId:" + client.getProtocolId());
					connectionPool.removeClient(client);
				}

				if (client.isAvailable())
				{
					long cur_time = System.currentTimeMillis();

					if (cur_time - client.getLastReceveTime() > config.getTimeout() * 1000)
					{
						logger.info("SGIP���������ؽ������������ó��� " + config.getTimeout()
								+ "��,���ӹر� protocolId: " + client.getProtocolId());
						client.getSession().close(true);
						connectionPool.removeClient(client);
					}
				}
			}
		} catch (Exception e)
		{
			logger.error("", e);
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
		}

	}

	@Override
	public void keepActive()
	{

	}
	public void stop()
	{
		List<Client> clients = connectionPool.getAllClient(this.protocolConfig.getProtocolId());
		for (Client activeclient : clients)
		{
			SgipClient client = (SgipClient) activeclient;
			if (client.isAvailable())
			{
				client.setUnAvailable();
			}
		}
		logger.info("�ر�" + config.getProtocolId() + "��������");

	}
}
