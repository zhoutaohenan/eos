package com.surge.communication.framework.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.surge.communication.framework.conf.ProtocolConfig;

/**
 * @description
 * @project: nioframe
 * @Date:2010-7-29
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class AbstractServer
{

	/** socket������ */
	protected SocketAcceptor acceptor = null;

	/** ��Ϣ���ջص��ӿ� */
	protected IoHandler ioHandler;

	/** server������ */
	private ProtocolConfig serverConfig;

	private int readSize=1024;

	public AbstractServer(IoHandler ioHandler, ProtocolConfig serverConfig)
	{
		this.ioHandler = ioHandler;
		this.serverConfig = serverConfig;
	}

	public void startListen() throws IOException
	{

		// ���Ӵ���������
		int processCount = 0;//serverConfig.getProcessorCount();
		if (processCount >= 0)
		{
			acceptor = new NioSocketAcceptor(processCount);
		} else
		{
			acceptor = new NioSocketAcceptor();
		}

		// ÿ�δ�io���ȡ���ֽ���
		//int readBufferSize = serverConfig.getReadBufferSize();
		int readBufferSize=readSize;
		if (readBufferSize > 0)
		{
			acceptor.getSessionConfig().setReadBufferSize(readBufferSize);
		}

		acceptor.setReuseAddress(true);
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		

		// Add SSL filter if SSL is enabled.

		// Bind
		acceptor.setHandler(ioHandler);

		acceptor.bind(new InetSocketAddress(serverConfig.getListenPort()));

		System.out.println("Listening on port " + serverConfig.getListenPort());
	}
}
