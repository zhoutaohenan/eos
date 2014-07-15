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
 * Sgip协议连接 创建,管理
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

	/** 最后一次创建连接时间 */
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
					// 清空滑动窗口
					logger.error("清理连接 channel:" + client.getProtocolId() + "滑动窗口中垃圾数据 ");
					client.removeSeqIdAll();
				}
			}
		} catch (Exception e)
		{
			logger.error("", e);
		}

	}

	/**
	 * 创建SGIP连接 根据配置文件和已经创建的活跃连接数计算得到
	 */
	@Override
	public synchronized void connect()
	{
		// 连接未建立完成前，禁止频繁创建连接
		if (lastConnTime != 0 && (System.currentTimeMillis() - lastConnTime) < 1000 * 30)
		{
			return;
		}
		lastConnTime = System.currentTimeMillis();
		int count = this.protocolConfig.getConnCount()
				- this.connectionPool.getActiveClientCount(this.protocolConfig.getProtocolId());
		for (int i = 0; i < count; i++)
		{
			logger.info("正在创建SGIP连接 IP:" + config.getIp() + " port:" + config.getPort()
					+ " userName:" + config.getUserName());

			IoSession session = this.createIoSession();
			
			if (session == null || session.isClosing()|| !session.isConnected())
			{
				continue;
			}
			logger.info("与sgip网关建立Tcp连接成功 ,发送登录消息 protocol:" + config.getIsmgid());
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
	 * 检查并清除失效连接
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

				// 如果连接失效，清除连接
				if ((!client.isAvailable()))
				{
					logger.info("SGIP服务与网关建立的连接连接失效,移除此条连接 protocolId:" + client.getProtocolId());
					connectionPool.removeClient(client);
				}

				if (client.isAvailable())
				{
					long cur_time = System.currentTimeMillis();

					if (cur_time - client.getLastReceveTime() > config.getTimeout() * 1000)
					{
						logger.info("SGIP服务与网关建立的连接闲置超过 " + config.getTimeout()
								+ "秒,连接关闭 protocolId: " + client.getProtocolId());
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
			// 检查连接
			checkConnect();
			// 清理超时数据
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
		logger.info("关闭" + config.getProtocolId() + "所有连接");

	}
}
