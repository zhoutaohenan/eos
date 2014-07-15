package com.surge.engine.protocol.sms.sgip.client;

import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.SmsAbstractClient;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipSubmit;
import com.surge.engine.sms.conf.SgipConfig;

/**
 * ����һ��SgipЭ������
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipClient extends SmsAbstractClient
{
	public SgipClient(IoSession session, SgipConfig smsconfig)
	{
		super(session, smsconfig.getProtocolId(), smsconfig.getWindow_size(), smsconfig.getMtFlux());
		this.session = session;
	}

	@Override
	public int sendPMessage(PMessage pMessage)
	{
		// ֻ��MT��Ϣ�����������Ƽ����������Ƿ���У��
		if (pMessage instanceof SgipSubmit)
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
		} else
		{
			session.write(pMessage);
		}
		return 0;

	}

}
