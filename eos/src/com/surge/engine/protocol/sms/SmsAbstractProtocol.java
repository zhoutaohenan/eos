package com.surge.engine.protocol.sms;

import com.surge.communication.framework.conf.ProtocolConfig;

public abstract class SmsAbstractProtocol implements ProtocolConfig
{
	/** 短信单位时间发送流量 **/
	private int mtFlux;
	
	/**滑动窗口大小**/
	private int windowSize;
	/**滑动窗口满，超过此规定时间清空滑动窗口**/
	private long expireWindowTime;

	public long getExpireWindowTime()
	{
		return expireWindowTime;
	}

	public void setExpireWindowTime(long expireWindowTime)
	{
		this.expireWindowTime = expireWindowTime;
	}

	public int getWindowSize()
	{
		return windowSize;
	}

	public void setWindowSize(int windowSize)
	{
		this.windowSize = windowSize;
	}

	public int getMtFlux()
	{
		return mtFlux;
	}

}
