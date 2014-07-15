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
	/** Э���˺����ӳ� */
	protected final  ConnectionPool connectionPool = ConnectionPool.instance;

	/** ������ʱʱ�� */
	protected long connIdleTimeout = 5 * 60 * 1000;

	/**
	 * 
	 * ��鲢���ʧЧ����
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

				// �������ʧЧ�����ӿ��г�ʱ�����
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

		//��������ӵ��˺�
		for (String protocolId : tmpKey)
		{
			connectionPool.clearClient(protocolId);
		}
	}

	/**
	 * 
	 * �����ƶ����������� client
	 * 
	 * @param count
	 *            void
	 * @throws
	 */
	public abstract void connect(int count);

	/**
	 * 
	 * ��������
	 * 
	 * @throws
	 */
	public abstract void keepActive();

	/**
	 * 
	 * �������ӵ���������
	 * 
	 * @throws
	 */
	public abstract void clearClientData();

	/**
	 * 
	 * ����
	 * 
	 * @throws
	 */
	public abstract void start();

	/**
	 * 
	 * �ر� void
	 * 
	 * @throws
	 */
	public abstract void stop();

}
