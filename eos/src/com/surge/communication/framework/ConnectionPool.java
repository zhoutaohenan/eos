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
 * ���ӳ�
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class ConnectionPool
{

	/** ���� (���Ե��� Ҳ���Զ���) */
	public static ConnectionPool instance = new ConnectionPool();

	/** �洢���Э���˺ŵ����� */
	private final ConcurrentMap<String, ConcurrentLinkedQueue<Client>> pooledClients;

	public ConnectionPool()
	{
		pooledClients = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Client>>();
	}

	/**
	 * 
	 * ���һ��client
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
	 * ��ȡ���õ�����
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
	 * ��ȡ���õ�������
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
	 * ��ȡ�����˺ŵ���������
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
	 * ɾ��һ����������
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
	 * ɾ��һ��Э���˺ŵľ�������
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
	 * ������ӳ����������� void
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
