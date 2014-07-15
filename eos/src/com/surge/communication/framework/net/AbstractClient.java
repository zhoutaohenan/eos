package com.surge.communication.framework.net;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * 一条连接的抽象实现
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

	/** 一条连接对应一个IoSession,对外可见的只有Client */
	private IoSession session;

	/** TCP连接是否有效标志 */
	protected boolean available = true;

	/** 是否已经验证通过标志 */
	private boolean logined = false;

	/** 协议账号ID */
	protected String protocolId;

	/** 最后一次收到网关消息包的时间，单位(毫秒) **/

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
				logger.info("开始关闭" + this.protocolId + "连接IOService关闭");
				service.dispose();
				logger.info("关闭" + this.protocolId + "连接IOService关闭成功");
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
