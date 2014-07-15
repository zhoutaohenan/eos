package com.surge.engine.protocol.sms;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.AbstractClient;
import com.surge.engine.monitor.ChannelStatMgr;

/**
 * (����)��һ�����ӵ�ʵ��
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
	/** ͨ������ͳ�ƹ��� **/
	private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;
	/** ����������ʱ��ʱ�� **/
	private long windowSizeFullTime;

	/** ���������Ƿ��� **/
	private boolean isWindowSizeFull = false;

	/** �������ڴ�С **/
	private int windowSize = 16;

	/** ���������������sesssion **/
	protected IoSession session;

	private int fluxCount = 0; // �������Ƽ�����

	private int mtFlux = 0;

	/** �ϴη����ŵ�ʱ�� , ���������� **/
	private long lastTime = System.currentTimeMillis();

	/** ������������ʱʱ�� **/
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
	 * ��鵥λʱ���ڶ��ŷ��������Ƿ񳬹��涨
	 * 
	 * @return boolean ����������:true δ����:false
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
	 * ��黬�������Ƿ���
	 * 
	 * @return boolean ����������ʱ:true ����:false
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
