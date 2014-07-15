package com.surge.engine.protocol.sms.sgip.client;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.pojo.Response;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipBindResp;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipSubmitResp;
import com.surge.engine.protocol.sms.util.SmsTools;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipClientProcess implements Processor
{
	private static final Logger logger = Logger.getLogger(SgipClientProcess.class);

	private SmsProtocolHandler protocolHandler;

	public SgipClientProcess(SmsProtocolHandler protocolHandler)
	{
		this.protocolHandler = protocolHandler;
	}

	@Override
	public void doProcess(Client client, PMessage pMessage)
	{
		SgipClient sgipClient = (SgipClient) client;
		if (pMessage instanceof SgipBindResp)
		{
			SgipBindResp bindResp = (SgipBindResp) pMessage;
			int ret = bindResp.getResult();
			if (ret == 0)
			{
				sgipClient.setLogined(true);
				if (logger.isInfoEnabled())
				{
					StringBuilder sb = new StringBuilder();
					sb.append("\r\n*****************************************");
					sb.append("\r\nSGIP_ISMG_" + client.getProtocolId() + ":�������ӳɹ���");
					sb.append("\r\n*****************************************");
					logger.info(sb.toString());
				}
				if (protocolHandler != null)
				{
					protocolHandler.doConnectStatus(sgipClient.getProtocolId(), true);
				}
			}
			else
			{
				String retDesc = "";
				switch (ret)
				{
					case 1:
						retDesc = "�Ƿ���¼�����¼�������������¼���������ȡ�";
						break;
					case 2:
						retDesc = "�ظ���¼������ͬһTCP/IP�����������������������¼��";
						break;
					case 3:
						retDesc = "���ӹ��ָ࣬�����ڵ�Ҫ��ͬʱ���������������ࡣ";
						break;
					case 4:
						retDesc = "��¼���ʹ�ָbind�����е�logintype�ֶγ���";
						break;
					default:
						retDesc = "��������";
						break;
				}
				logger.error("SGIP:" + client.getProtocolId() + "��������ʧ�ܣ���������Ϊ:" + retDesc + "("
						+ ret + ")��");
				client.setUnAvailable();
				if (protocolHandler != null)
				{
					protocolHandler.doConnectStatus(sgipClient.getProtocolId(), false);
				}
			}
		}
		else if (pMessage instanceof SgipSubmitResp)
		{
			SgipSubmitResp submitResp = (SgipSubmitResp) pMessage;

			logger.debug("channel:"+ client.getProtocolId()+submitResp.toString());

			int result = submitResp.getResult();
			String desc = "�ύ�ɹ�";
			if (result != 0)
			{
				desc = SmsTools.getSgipDetailInfo(result);
				result=1;
			}
			Response response = new Response(submitResp.getSeqId(), result,
					submitResp.getMessageId(), desc);
			sgipClient.removeSeqId(submitResp.getSeqId());
			if (protocolHandler != null)
			{
				protocolHandler.doResponse(sgipClient.getProtocolId(), response);
			}
		}

	}
}
