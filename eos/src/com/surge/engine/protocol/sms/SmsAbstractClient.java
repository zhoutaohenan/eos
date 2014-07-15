package com.surge.engine.protocol.sms;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.AbstractClient;
import com.surge.engine.monitor.ChannelStatMgr;

/**
 * (短信)对一条连接的实现
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public abstract class SmsAbstractClient extends AbstractClient
{
	/** 通道队列统计管理 **/
	private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;
	/** 滑动窗口满时的时间 **/
	private long windowSizeFullTime;

	/** 滑动窗口是否满 **/
	private boolean isWindowSizeFull = false;

	/** 滑动窗口大小 **/
	private int windowSize = 16;

	/** 本条连接所代表的sesssion **/
	protected IoSession session;

	private int fluxCount = 0; // 流量控制计数器

	private int mtFlux = 0;

	/** 上次发短信的时间 , 用流量控制 **/
	private long lastTime = System.currentTimeMillis();

	/** 滑动窗口满超时时间 **/
	private long expireWindowTime = 60 * 3 * 1000;

	private Map<String, Long> sequenceMap = new ConcurrentHashMap<String, Long>();

	public SmsAbstractClient(IoSession session, String protocolId, int windowSize, int mtFlux)
	{
		super(session, protocolId);
		this.windowSize = windowSize;
		this.mtFlux = mtFlux;
	}

	@Override
	public void setIoSession(IoSession ioSession)
	{
		this.session = ioSession;
	}

	public abstract int sendPMessage(PMessage pMessage);

	/**
	 * 
	 * 检查单位时间内短信发送流量是否超过规定
	 * 
	 * @return boolean 超过控制量:true 未超过:false
	 */
	protected boolean checkMTFlux()
	{
		if (System.currentTimeMillis() - lastTime >= 1000)
		{
			channelStatMgr.addChannelFlux(protocolId, fluxCount,lastTime);
			lastTime = System.currentTimeMillis();
			fluxCount = 0;
		}
		if (fluxCount < this.mtFlux)
		{
			fluxCount++;
			return false;
		}
		return true;
	}

	/**
	 * 检查滑动窗口是否满
	 * 
	 * @return boolean 滑动窗口满时:true 否则:false
	 * @throws
	 */
	protected boolean checkWindowsSize()
	{
		if (sequenceMap.size() > windowSize)
		{
			if (!isWindowSizeFull)
			{
				windowSizeFullTime = System.currentTimeMillis();
				isWindowSizeFull = true;
			}
			return true;
		}
		return false;
	}

	public void addSeqId(String seq_id)
	{
		sequenceMap.put(seq_id, System.currentTimeMillis());
	}

	public void removeSeqId(String seq_id)
	{
		sequenceMap.remove(seq_id);
	}

	public void removeSeqIdAll()
	{
		sequenceMap.clear();
	}

	public long getWindowSizeFullTime()
	{
		return windowSizeFullTime;
	}

	public void setWindowSizeFullTime(long windowSizeFullTime)
	{
		this.windowSizeFullTime = windowSizeFullTime;
	}
	public long getExpireWindowTime()
	{
		return expireWindowTime;
	}

	public void setExpireWindowTime(long expireWindowTime)
	{
		this.expireWindowTime = expireWindowTime;
	}
}
