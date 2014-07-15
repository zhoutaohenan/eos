package com.surge.engine.protocol.sms.sgip.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.communication.framework.net.Client;
import com.surge.engine.monitor.ChannelStatMgr;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.pojo.Mo;
import com.surge.engine.protocol.sms.pojo.Receipt;
import com.surge.engine.protocol.sms.sgip.client.SgipClient;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipBind;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipBindResp;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipDeliver;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipDeliverResp;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipMsgHead;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipReport;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipReportResp;
import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.sms.service.SmsQueueMgr;
import com.surge.engine.util.MobileErrorUitl;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipServerProcess implements Processor
{
	private static final Logger logger = Logger.getLogger(SgipServerProcess.class);

	private static MobileErrorUitl mobileErrorUitl = MobileErrorUitl.mobileErrorUitl;

	private ProtocolConfig config;

	private SmsProtocolHandler protocolHandler;

	private SmsQueueMgr queueMgr = SmsQueueMgr.getInstance();

	/** 通道队列统计管理 **/
	private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;

	public SgipServerProcess(SmsProtocolHandler protocolHandler, ProtocolConfig config)
	{
		this.protocolHandler = protocolHandler;
		this.config = config;
	}

	@Override
	public void doProcess(Client client, PMessage pMessage)
	{

		SgipClient sgipClient = (SgipClient) client;
		if (pMessage instanceof SgipBind)
		{
			SgipBind bind = (SgipBind) pMessage;
			logger.info("SGIP " + client.getProtocolId() + " bind:[" + " LoginName: "
					+ bind.getLoginName() + "]");
			SgipBindResp bindResp = new SgipBindResp(bind.getHead().getNodeID(), bind.getHead()
					.getDatetime(), bind.getHead().getseqID(), bind.getResult());
			sgipClient.sendPMessage(bindResp);
		} else if (pMessage instanceof SgipDeliver)
		{
			SgipDeliver deliver = (SgipDeliver) pMessage;
			deliver.setCurrTime(System.currentTimeMillis());
			logger.debug("channel:" + client.getProtocolId() + deliver.toString());
			SgipDeliverResp deliveResp = new SgipDeliverResp(deliver.getHead().getNodeID(), deliver
					.getHead().getDatetime(), deliver.getHead().getseqID(), (byte) 0);
			sgipClient.sendPMessage(deliveResp);
			if (config.isSupportLongMoSms() && deliver.getTotalCout() > 0)
			{
				SgipDeliver deliver_temp = this.doLongMo(deliver);
				if (deliver_temp != null)
				{
					Mo mo = new Mo(deliver_temp.getMessageId(), deliver_temp.getUserNumber(),
							deliver_temp.getSpNumber(), deliver_temp.getMessageContent());

					protocolHandler.doMo(sgipClient.getProtocolId(), mo);
				}
			} else
			{
				Mo mo = new Mo(deliver.getMessageId(), deliver.getUserNumber(), deliver
						.getSpNumber(), deliver.getMessageContent());

				protocolHandler.doMo(sgipClient.getProtocolId(), mo);
			}

		} else if (pMessage instanceof SgipReport)
		{
			SgipReport report = (SgipReport) pMessage;
			logger.debug("channel:" + client.getProtocolId() + report.toString());
			SgipMsgHead head = report.getHead();

			SgipReportResp reportResp = new SgipReportResp(head.getNodeID(), head.getDatetime(),
					head.getseqID(), (byte) 0);
			sgipClient.sendPMessage(reportResp);
			String desc = SmsTools.getSgipDetailInfo(report.getErrorCode());
			int result = 0;
			if (mobileErrorUitl.getSgipList().contains(report.getErrorCode()))
			{
				result = SmsErrCode.USER_MOBILE_ERROR.getValue();
			} else if (report.getErrorCode() != 0)
			{
				result = 1;
			}
			Receipt receipt = new Receipt(report.getMessageId(), report.getUserNumber(), result,
					String.valueOf(report.getState()), desc);
			if (report.getErrorCode() == 0)
			{
				channelStatMgr.addSuccReports(client.getProtocolId());
			} else
			{
				channelStatMgr.addFailReports(client.getProtocolId());
			}

			if (protocolHandler != null)
			{
				protocolHandler.doReceipt(sgipClient.getProtocolId(), receipt);
			}
		}
	}
	/**
	 * 处理长短信 TODO
	 * 
	 * @param deliver
	 *            void
	 * @throws
	 */
	private SgipDeliver doLongMo(SgipDeliver deliver)
	{
		Map<String, PMessage> moMap = queueMgr.getLongMoSMSMap().get(config.getProtocolId());
		if (moMap == null)
		{
			moMap = new ConcurrentHashMap<String, PMessage>();
			queueMgr.getLongMoSMSMap().put(config.getProtocolId(), moMap);
		}
		SgipDeliver passMo = (SgipDeliver) moMap.get(deliver.getUserNumber());
		if (passMo != null)
		{
			passMo.getLongContent()[deliver.getCurrCout() - 1] = deliver.getMessageContent();
			StringBuilder build = new StringBuilder();
			// 遍历长短信内容数组
			for (String st : passMo.getLongContent())
			{
				build.append(st);
				// 如果一个内容没有回来，就先缓存 起来
				if (st == null)
				{
					passMo.setCurrTime(System.currentTimeMillis());
					return null;
				}
			}
			// 组合内容
			passMo.setMessageContent(build.toString());
			moMap.remove(deliver.getUserNumber());
			return passMo;
		} else
		{
			// 编辑内容
			String content[] = new String[deliver.getTotalCout()];
			content[deliver.getCurrCout() - 1] = deliver.getMessageContent();
			deliver.setLongContent(content);
			moMap.put(deliver.getUserNumber(), deliver);
			queueMgr.getLongMoSMSMap().put(config.getProtocolId(), moMap);
		}
		return null;
	}
}
