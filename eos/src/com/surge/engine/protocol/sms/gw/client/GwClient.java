package com.surge.engine.protocol.sms.gw.client;

import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.SmsAbstractClient;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.protocol.sms.gw.pmsg.CSendReq;
import com.surge.engine.sms.conf.GwConfig;

public class GwClient extends SmsAbstractClient
{

	public GwClient(IoSession session, GwConfig config)
	{
		super(session, config.getProtocolId(), config.getWindow_size(), config.getMtFlux());
		this.session = session;
	}

	@Override
	public void setIoSession(IoSession ioSession)
	{
		this.session = ioSession;
	}

	@Override
	public int sendPMessage(PMessage pMessage)
	{
		// 只对MT信息进行流量控制及滑动窗口是否满校验
		if (pMessage instanceof CSendReq)
		{
			synchronized (this)
			{
				if (checkMTFlux())
				{
					return SendResult.MTFULX_ERROR.getValue();
				}
				if (checkWindowsSize())
				{
					return SendResult.WINDOWSIZE_ERROR.getValue();
				}
			}
			session.write(pMessage);
			this.removeSeqId(pMessage.getSeqId());
		}
		else
		{
			session.write(pMessage);
		}
		return 0;
	}

}
