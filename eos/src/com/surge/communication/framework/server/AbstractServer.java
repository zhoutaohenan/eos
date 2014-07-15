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

	/** socket连接器 */
	protected SocketAcceptor acceptor = null;

	/** 消息接收回调接口 */
	protected IoHandler ioHandler;

	/** server端配置 */
	private ProtocolConfig serverConfig;

	private int readSize=1024;

	public AbstractServer(IoHandler ioHandler, ProtocolConfig serverConfig)
	{
		this.ioHandler = ioHandler;
		this.serverConfig = serverConfig;
	}

	public void startListen() throws IOException
	{

		// 连接处理器个数
		int processCount = 0;//serverConfig.getProcessorCount();
		if (processCount >= 0)
		{
			acceptor = new NioSocketAcceptor(processCount);
		} else
		{
			acceptor = new NioSocketAcceptor();
		}

		// 每次从io层读取的字节数
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
