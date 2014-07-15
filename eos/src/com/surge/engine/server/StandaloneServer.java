package com.surge.engine.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.missian.server.codec.MissianCodecFactory;
import com.missian.server.handler.MissianHandler;
import com.surge.engine.sms.conf.SmsConfig;

/**
 * ͨ��״̬ͳ�������ӿڷ����
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-24
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class StandaloneServer
{
	private static Logger logger = Logger.getLogger(StandaloneServer.class);

	private NioSocketAcceptor acceptor;

	public static StandaloneServer instance = new StandaloneServer();
	
	private static int listenPort=SmsConfig.getInstance().getListenPort();

	private StandaloneServer()
	{

	}

	/**
	 * ����ͨ��״̬ͳ�������ӿ� TODO void
	 * 
	 * @throws
	 */
	public void start()
	{
		acceptor = new NioSocketAcceptor();
		acceptor.setDefaultLocalAddress(new InetSocketAddress(listenPort));
		acceptor.setHandler(new MissianHandler(new ExampleBeanLocator()));
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 20);
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new MissianCodecFactory()));
		acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(1));// use
		try
		{
			acceptor.bind();
			logger.info("*********����ͨ��״̬ͳ�Ʒ���˿� :" +listenPort + " �ɹ�*****");
		} catch (IOException e)
		{
			logger.error("", e);
		}
	}
	/**
	 * �ر�ͨ��״̬ͳ�������ӿ� TODO void
	 * 
	 * @throws
	 */
	public void stop()
	{
		if (acceptor != null)
		{
			acceptor.dispose();
		}
	}

}
