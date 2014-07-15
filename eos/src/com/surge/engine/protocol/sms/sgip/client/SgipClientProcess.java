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
					sb.append("\r\nSGIP_ISMG_" + client.getProtocolId() + ":创建连接成功！");
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
						retDesc = "非法登录，如登录名、口令出错、登录名与口令不符等。";
						break;
					case 2:
						retDesc = "重复登录，如在同一TCP/IP连接中连续两次以上请求登录。";
						break;
					case 3:
						retDesc = "连接过多，指单个节点要求同时建立的连接数过多。";
						break;
					case 4:
						retDesc = "登录类型错，指bind命令中的logintype字段出错。";
						break;
					default:
						retDesc = "其他错误";
						break;
				}
				logger.error("SGIP:" + client.getProtocolId() + "创建连接失败，错误描述为:" + retDesc + "("
						+ ret + ")。");
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
			String desc = "提交成功";
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
