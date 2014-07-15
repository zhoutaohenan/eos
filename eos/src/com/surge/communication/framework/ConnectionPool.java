package com.surge.communication.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.surge.communication.framework.net.Client;

/**
 * 
 * 连接池
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class ConnectionPool
{

	/** 单例 (可以单例 也可以多列) */
	public static ConnectionPool instance = new ConnectionPool();

	/** 存储多个协议账号的连接 */
	private final ConcurrentMap<String, ConcurrentLinkedQueue<Client>> pooledClients;

	public ConnectionPool()
	{
		pooledClients = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Client>>();
	}

	/**
	 * 
	 * 添加一个client
	 * 
	 * @param client
	 * @param protocolId
	 *            void
	 * @throws
	 */
	public void addClient(Client client, String protocolId)
	{
		ConcurrentLinkedQueue<Client> queue = pooledClients.get(protocolId);
		if (queue == null)
		{
			queue = new ConcurrentLinkedQueue<Client>();
		}
		queue.add(client);
		pooledClients.put(protocolId, queue);
	}

	/**
	 * 
	 * 获取可用的连接
	 * 
	 * @param protocolId
	 * @return Client
	 * @throws
	 */
	public List<Client> getActiveClient(String protocolId)
	{
		List<Client> retList = new ArrayList<Client>();
		ConcurrentLinkedQueue<Client> queue = pooledClients.get(protocolId);
		if (queue != null)
		{
			Iterator<Client> it = queue.iterator();
			for (; it.hasNext();)
			{
				Client client = it.next();
				if (client.isAvailable() && client.isLogined())
				{
					retList.add(client);
				}
			}
		}
		return retList;
	}

	/**
	 * 
	 * 获取可用的连接数
	 * 
	 * @param protocolId
	 * @return Client
	 * @throws
	 */
	public int getActiveClientCount(String protocolId)
	{
		int retCount = 0;
		ConcurrentLinkedQueue<Client> queue = pooledClients.get(protocolId);
		if (queue != null)
		{
			Iterator<Client> it = queue.iterator();
			for (; it.hasNext();)
			{
				Client client = it.next();
				if (client.isAvailable())
				{
					retCount++;
				}
			}
		}
		return retCount;
	}

	/**
	 * 
	 * 获取具体账号的所有连接
	 * 
	 * @param protocolId
	 * @return List<Client>
	 * @throws
	 */
	public List<Client> getAllClient(String protocolId)
	{
		List<Client> retList = new ArrayList<Client>();
		ConcurrentLinkedQueue<Client> queue = pooledClients.get(protocolId);
		if (queue != null)
		{
			Iterator<Client> it = queue.iterator();
			for (; it.hasNext();)
			{
				Client client = it.next();
				retList.add(client);
			}
		}
		return retList;
	}

	/**
	 * 
	 * 删除一个具体连接
	 * 
	 * @param client
	 * @param protocolId
	 *            void
	 * @throws
	 */
	public void removeClient(Client client)
	{
		ConcurrentLinkedQueue<Client> queue = pooledClients.get(client.getProtocolId());
		if (queue != null)
		{
			queue.remove(client);
		}
	}

	/**
	 * 
	 * 删除一个协议账号的具体连接
	 * 
	 * @param protocolId
	 *            void
	 * @throws
	 */
	public void clearClient(String protocolId)
	{
		ConcurrentLinkedQueue<Client> queue = pooledClients.get(protocolId);
		if (queue != null)
		{
			queue.clear();
		}
	}

	/**
	 * 
	 * 清楚连接池中所有连接 void
	 * 
	 * @throws
	 */
	public void clearAllClient()
	{
		this.pooledClients.clear();
	}

	public ConcurrentMap<String, ConcurrentLinkedQueue<Client>> getPooledClients()
	{
		return pooledClients;
	}
}
