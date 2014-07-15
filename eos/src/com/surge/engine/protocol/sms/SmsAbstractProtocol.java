package com.surge.engine.protocol.sms;

import com.surge.communication.framework.conf.ProtocolConfig;

public abstract class SmsAbstractProtocol implements ProtocolConfig
{
	/** ���ŵ�λʱ�䷢������ **/
	private int mtFlux;
	
	/**�������ڴ�С**/
	private int windowSize;
	/**�����������������˹涨ʱ����ջ�������**/
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
