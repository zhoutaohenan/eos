package com.surge.communication.framework.net;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * һ�����ӵĳ���ʵ��
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public abstract class AbstractClient implements Client
{

	private static final Logger logger = Logger.getLogger(AbstractClient.class);

	/** һ�����Ӷ�Ӧһ��IoSession,����ɼ���ֻ��Client */
	private IoSession session;

	/** TCP�����Ƿ���Ч��־ */
	protected boolean available = true;

	/** �Ƿ��Ѿ���֤ͨ����־ */
	private boolean logined = false;

	/** Э���˺�ID */
	protected String protocolId;

	/** ���һ���յ�������Ϣ����ʱ�䣬��λ(����) **/

	private long lastReceveTime = System.currentTimeMillis();

	public AbstractClient(IoSession session, String protocolId)
	{
		this.session = session;
		this.protocolId = protocolId;
	}

	public boolean isAvailable()
	{
		return available;
	}

	public  void setUnAvailable()
	{
		this.available = false;
		this.logined = false;
		session.close(true);
		IoService service = session.getService();
		synchronized (service)
		{
			if (!service.isDisposing())
			{
				logger.info("��ʼ�ر�" + this.protocolId + "����IOService�ر�");
				service.dispose();
				logger.info("�ر�" + this.protocolId + "����IOService�رճɹ�");
			} 
		}
		

	}
	public boolean isLogined()
	{
		return logined;
	}

	public void setLogined(boolean logined)
	{
		this.logined = logined;
	}

	public IoSession getSession()
	{
		return session;
	}

	public void setSession(IoSession session)
	{
		this.session = session;
	}

	public long getLastReceveTime()
	{
		return lastReceveTime;
	}

	public void setLastReceveTime(long lastReceveTime)
	{
		this.lastReceveTime = lastReceveTime;
	}

	public String getProtocolId()
	{
		return protocolId;
	}

	public void setProtocolId(String protocolId)
	{
		this.protocolId = protocolId;
	}
}
