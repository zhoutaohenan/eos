package com.surge.engine.protocol.sms.cmpp.net;

import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.SmsAbstractClient;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2Submit;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.sms.conf.Cmpp2Config;

/**
 * 代表一条cmpp协议连接
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class CmppClient extends SmsAbstractClient
{
	public CmppClient(IoSession session, Cmpp2Config smsconfig)
	{
		super(session, smsconfig.getProtocolId(), smsconfig.getWindow_size(), smsconfig.getMtFlux());
		this.session = session;
	}

	@Override
	public int sendPMessage(PMessage pMessage)
	{
		// 只对MT信息进行流量控制及滑动窗口是否满校验
		if (pMessage instanceof Cmpp2Submit)
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
			addSeqId(pMessage.getSeqId());
			session.write(pMessage);
		}
		else
		{
			session.write(pMessage);
		}
		return 0;
	}

}
