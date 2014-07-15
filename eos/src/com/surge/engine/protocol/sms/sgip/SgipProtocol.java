package com.surge.engine.protocol.sms.sgip;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.surge.communication.framework.AbstractProtocol;
import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.codec.SmsCodecFactory;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.protocol.sms.sgip.client.SgipClientIoHandler;
import com.surge.engine.protocol.sms.sgip.client.SgipClientProcess;
import com.surge.engine.protocol.sms.sgip.codec.SgipDecoder;
import com.surge.engine.protocol.sms.sgip.codec.SgipEncoder;
import com.surge.engine.protocol.sms.sgip.server.SgipServerIoHandler;
import com.surge.engine.protocol.sms.sgip.server.SgipServerProcess;
import com.surge.engine.protocol.sms.sgip.service.SgipClientKeepConnection;
import com.surge.engine.sms.conf.SgipConfig;

/**
 * sgip服务接口具体实现 ，负责启动服务端
 * 
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-5
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipProtocol extends AbstractProtocol
{

	private static final Logger logger = Logger.getLogger(SgipProtocol.class);

	private SgipConfig config;

	private SgipClientKeepConnection clientConnetion;

	private ConnectionPool pool = ConnectionPool.instance;

	private SmsProtocolHandler protocolHandler;

	private IoAcceptor serverAcceptor;

	public SgipProtocol(String protocolId, SgipConfig sgip12Config,
			SmsProtocolHandler protocolHandler)
	{

		super(protocolId);
		this.config = sgip12Config;
		this.protocolHandler = protocolHandler;
	}

	/**
	 * sgip协议层发送消息接口实现
	 * 
	 * return
	 */
	@Override
	public int sendPMessage(PMessage pMessage)
	{

		if (this.pool.getActiveClientCount(this.config.getProtocolId()) == 0)
		{
			clientConnetion.connect();
		}

		List<Client> clientList = this.pool.getActiveClient(this.config.getProtocolId());
		if (clientList.size() == 0)
		{
			return SendResult.DISCONNECECT_ERROR.getValue();
		}
		Client client = clientList.get(0);
		return client.sendPMessage(pMessage);

	}

	/**
	 * 启动sgip服务，创建服务端侦听连接
	 * 
	 */
	@Override
	public synchronized boolean start()
	{
		serverAcceptor = new NioSocketAcceptor();
		try
		{
			serverAcceptor.setHandler(new SgipServerIoHandler(new SgipServerProcess(
					protocolHandler, config), pool, config));
			serverAcceptor.getFilterChain().addLast(
					"SgipDecode",
					new ProtocolCodecFilter(new SmsCodecFactory(new SgipDecoder(),
							new SgipEncoder())));
			serverAcceptor.getFilterChain().addLast("Executor",
					new ExecutorFilter(IoEventType.MESSAGE_RECEIVED, IoEventType.MESSAGE_SENT));
			serverAcceptor.bind(new InetSocketAddress(config.getListenPort()));
		} catch (IOException e)
		{
			logger.error("启动SGIP服务端口失败，侦听端口：" + config.getListenPort(), e);
			return false;
		}
		logger.info("**************************************");
		logger.info("启动SGIP协议服务端成功,侦听端口：" + config.getListenPort());
		logger.info("**************************************");
		ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(new SmsCodecFactory(
				new SgipDecoder(), new SgipEncoder()));
		clientConnetion = new SgipClientKeepConnection(config, new SgipClientIoHandler(
				new SgipClientProcess(protocolHandler)), pool, codecFilter);
		return true;
	}

	@Override
	public void stop()
	{
		serverAcceptor.dispose();
		logger.info("*************关闭SGIP协议服务端**********");
		clientConnetion.stop();
	}

}
