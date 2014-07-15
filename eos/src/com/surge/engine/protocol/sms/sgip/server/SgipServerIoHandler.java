package com.surge.engine.protocol.sms.sgip.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.Processor;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.sgip.client.SgipClient;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.communication.framework.common.PMessage;

/**
 * sgipЭ�������IOHandler,��Ϣ���ɴ˷ַ���Processor
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipServerIoHandler extends IoHandlerAdapter
{
	private static final Logger logger = Logger.getLogger(SgipServerIoHandler.class);

	private Processor processor;

	private ConnectionPool pool;

	private SgipConfig config;

	public SgipServerIoHandler(Processor processor, ConnectionPool pool, SgipConfig config)
	{
		this.processor = processor;
		this.pool = pool;
		this.config = config;
	}

	public void sessionOpened(IoSession session) throws Exception
	{

		SgipClient client = (SgipClient) session.getAttribute("server");
		if (client == null)
		{
			client = new SgipClient(session, config);
			session.setAttribute("server", client);
		}

	}

	public void messageReceived(IoSession session, Object message) throws Exception
	{
		Client client = (Client) session.getAttribute("server");
		if (client == null)
		{
			client = new SgipClient(session, config);
			session.setAttribute("server", client);
		}
		// �յ���Ϣ������������ʱ��
		client.setLastReceveTime(System.currentTimeMillis());
		processor.doProcess(client, (PMessage) message);

	}
	public void sessionClosed(IoSession session) throws Exception
	{
		logger.info("�ͻ��˹ر���һ��TCP����");
		session.removeAttribute("server");
		session.close(true);
		// Client client = (Client) session.getAttribute("server");
		// if(client!=null)
		// {
		// client.setUnAvailable();
		// }
	}
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		logger.info("exceptionCaught", cause);
		session.removeAttribute("server");
		session.close(true);

	}

}
