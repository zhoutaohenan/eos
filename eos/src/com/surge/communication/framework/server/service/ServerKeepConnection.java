package com.surge.communication.framework.server.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.KeepConnection;
import com.surge.communication.framework.net.Client;

/**
 * @description
 * @project: nioframe
 * @Date:2010-7-29
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public abstract class ServerKeepConnection implements KeepConnection
{
	/** 协议账号连接池 */
	protected final  ConnectionPool connectionPool = ConnectionPool.instance;

	/** 交互超时时间 */
	protected long connIdleTimeout = 5 * 60 * 1000;

	/**
	 * 
	 * 检查并清除失效连接
	 * 
	 * @throws
	 */
	public void checkConnect()
	{
		ConcurrentMap<String, ConcurrentLinkedQueue<Client>> clientsMap = connectionPool
				.getPooledClients();

		HashSet<String> tmpKey = new HashSet<String>();
		for (Entry<String, ConcurrentLinkedQueue<Client>> entry : clientsMap
				.entrySet())
		{
			ConcurrentLinkedQueue<Client> clientList = entry.getValue();
			Iterator<Client> it = clientList.iterator();
			for (; it.hasNext();)
			{
				Client client = it.next();

				// 如果连接失效或连接空闲超时，清除
				if (!client.isAvailable()
						|| (System.currentTimeMillis()
								- client.getLastReceveTime() < connIdleTimeout))
				{
					it.remove();
				}
			}

			if (clientList.size() <= 0)
			{
				tmpKey.add(entry.getKey());
			}
		}

		//清除空链接的账号
		for (String protocolId : tmpKey)
		{
			connectionPool.clearClient(protocolId);
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
	public abstract void connect(int count);

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

	/**
	 * 
	 * 启动
	 * 
	 * @throws
	 */
	public abstract void start();

	/**
	 * 
	 * 关闭 void
	 * 
	 * @throws
	 */
	public abstract void stop();

}
