package com.surge.communication.framework.client.service;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.KeepConnection;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.communication.framework.net.Client;

/**
 * 创建连接，保持连接接口
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public abstract class ClientKeepConnection implements KeepConnection
{

	private static final Logger logger = Logger.getLogger(ClientKeepConnection.class);

	/** 建立tcp连接超时时间 */
	private long connTcpTimeout = 60 * 1000L;

	/** 协议账号配置 */
	protected final ProtocolConfig protocolConfig;

	/** 消息回调 */
	private final IoHandler ioHandler;

	/** 协议账号连接池 */
	protected final ConnectionPool connectionPool;

	/** 长连接与服务端交互超时时间 */
	protected long connIdleTimeout = 5 * 60 * 1000;

	/** 编解码过滤器 **/
	private ProtocolCodecFilter protocolCodecFilter;

	private int readSize = 1024;

	public ClientKeepConnection(ProtocolConfig protocolConfig, IoHandler ioHandler,
			ConnectionPool connectionPool, ProtocolCodecFilter protocolCodecFilter)
	{
		this.protocolConfig = protocolConfig;
		this.connectionPool = connectionPool;
		this.protocolCodecFilter = protocolCodecFilter;
		this.ioHandler = ioHandler;
	}

	/***
	 * 
	 * 创建tcp连接
	 * 
	 * @return boolean
	 * @throws
	 */
	protected IoSession createIoSession()
	{
		NioSocketConnector connector = null;
		IoSession session = null;
		try
		{
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(connTcpTimeout);
			int readBufferSize = readSize;
			if (readBufferSize > 0)
			{
				connector.getSessionConfig().setReadBufferSize(readBufferSize);
			}

			connector.setHandler(ioHandler);
			connector.getFilterChain().addLast("Code", protocolCodecFilter);
			ConnectFuture cf = connector.connect(new InetSocketAddress(protocolConfig.getIp(),
					protocolConfig.getPort()));
			// 等待是否连接成功，相当于是转异步执行为同步执行。
			cf.awaitUninterruptibly();
			session = cf.getSession();
		} catch (Exception e)
		{
			if (connector != null)
			{
				connector.dispose();
			}
			logger.error("创建连接" + this.protocolConfig.getProtocolId() + "失败", e);
		}
		if (session == null || session.isClosing() || !session.isConnected())
		{
			logger.info("******与网关连接失败，无法建立TCP连接****** protocol:"
					+ this.protocolConfig.getProtocolId());
			// 若connector已经关闭，connector.dispose();会直接返回的，所在此处不加!connector.isDisposed();
			if (connector != null)
			{
				connector.dispose();
				logger.info("**********成功回收创建" + this.protocolConfig.getProtocolId()
						+ "连接时占用的资源**********");
			}
			if (session != null)
			{
				session.close(true);
			}
		}
		return session;
	}
	/**
	 * 
	 * 检查并清除失效连接
	 * 
	 * @throws
	 */
	public void checkConnect()
	{
		List<Client> clients = connectionPool.getAllClient(this.protocolConfig.getProtocolId());
		Iterator<Client> it = clients.iterator();
		for (; it.hasNext();)
		{
			Client client = it.next();

			// 如果连接失效或连接空闲超时，清除
			if (!client.isAvailable()
					|| (System.currentTimeMillis() - client.getLastReceveTime() > connIdleTimeout))
			{
				logger.info("此条连接超时,protocolId：" + client.getProtocolId() + ",移除此条连接!");
				connectionPool.removeClient(client);
				client.setUnAvailable();
				client = null;
			}
		}
	}
	/**
	 * 
	 * 创建制定数量的连接 client
	 * 
	 * @param count
	 *            void
	 * @throws
	 */
	public abstract void connect();

	/**
	 * 
	 * 保持心跳
	 * 
	 * @throws
	 */
	public abstract void keepActive();

	/**
	 * 
	 * 清理连接的垃圾数据
	 * 
	 * @throws
	 */
	public abstract void clearClientData();

}
