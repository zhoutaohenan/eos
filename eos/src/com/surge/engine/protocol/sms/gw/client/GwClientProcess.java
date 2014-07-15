package com.surge.engine.protocol.sms.gw.client;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.monitor.ChannelStatMgr;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.gw.pmsg.CFeeRet;
import com.surge.engine.protocol.sms.gw.pmsg.CLinkReq;
import com.surge.engine.protocol.sms.gw.pmsg.CLinkRet;
import com.surge.engine.protocol.sms.gw.pmsg.CLoginRet;
import com.surge.engine.protocol.sms.gw.pmsg.CMoReq;
import com.surge.engine.protocol.sms.gw.pmsg.CMoRet;
import com.surge.engine.protocol.sms.gw.pmsg.CReportReq;
import com.surge.engine.protocol.sms.gw.pmsg.CReportRet;
import com.surge.engine.protocol.sms.gw.pmsg.CSendRet;
import com.surge.engine.protocol.sms.pojo.Mo;
import com.surge.engine.protocol.sms.pojo.Receipt;
import com.surge.engine.protocol.sms.pojo.Response;
import com.surge.engine.protocol.sms.pojo.SmsFee;
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
public class GwClientProcess implements Processor
{
	private static final Logger logger = Logger.getLogger(GwClientProcess.class);

	private SmsProtocolHandler protocolHandler;

	/** ͨ������ͳ�ƹ��� **/
	private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;

	public GwClientProcess(SmsProtocolHandler protocolHandler)
	{
		this.protocolHandler = protocolHandler;
	}

	@Override
	public void doProcess(Client client, PMessage pMessage)
	{
		GwClient gwClient = (GwClient) client;
		if (pMessage instanceof CLoginRet)
		{
			CLoginRet loginRet = (CLoginRet) pMessage;
			int status = loginRet.bStatus;
			if (status == 0 || status == 1)
			{
				gwClient.setLogined(true);
				if (logger.isInfoEnabled())
				{
					StringBuilder sb = new StringBuilder();
					sb.append("\r\n*****************************************");
					sb.append("\r\nGW_ISMG_" + client.getProtocolId() + "�������ӳɹ���");
					sb.append("\r\n*****************************************");
					logger.info(sb.toString());
				}
				if (protocolHandler != null)
				{
					protocolHandler.doConnectStatus(gwClient.getProtocolId(), true);
				}
			} else
			{

				String dec = "";

				switch (status)
				{
				case 0x02:
					dec = "ϵͳ�ڲ����������Ч�Ŀͻ�״��";
					break;
				case 0x12:
					dec = "�˺Ż�������� ���߸�״���Ѿ���ֹʹ��";
					break;
				case 0x14:
					dec = "�ͻ�Ȩ�޲���";
					break;
				case 0x15:
					dec = "���Ǵ�ָ����IP����¼";
					break;
				case 0x16:
					dec = "�ʺ��Ѿ���¼";
					break;
				case 0x88:
					dec = "���ݰ�������ʽ���� ";
					break;
				case 0xFF:
					dec = "���������(��ָ���ʽ�ȴ���)";
					break;
				}
				logger.error("GW:" + client.getProtocolId() + "��������ʧ�ܣ���������Ϊ:" + dec + "(" + status
						+ ")��");
				client.setUnAvailable();
				if (protocolHandler != null)
				{
					protocolHandler.doConnectStatus(gwClient.getProtocolId(), false);
				}
				gwClient.setUnAvailable();
			}
		} else if (pMessage instanceof CSendRet)
		{
			CSendRet submitResp = (CSendRet) pMessage;
			logger.debug("GW:" + client.getProtocolId() + " submitResp:" + submitResp.toString());
			int result = submitResp.bStatus;
			// ����һ�£������ϲ�ͳһΪ:0Ϊ�ɹ�
			if (result == 1)
			{
				result = 0;
			}
			else
			{
				result=SmsTools.getGWResponseResult(result);
			}
			

			Response response = new Response(submitResp.sSerialNo, result, submitResp.sReqID,
					submitResp.sMsg);
			// ��������
			gwClient.removeSeqId(submitResp.getSeqId());
			protocolHandler.doResponse(gwClient.getProtocolId(), response);
		} else if (pMessage instanceof CFeeRet)
		{
			CFeeRet feeRet = (CFeeRet) pMessage;
			logger.debug("GW:" + client.getProtocolId() + " feeRet:" + feeRet.toString());
			SmsFee fee = new SmsFee();
			fee.setStatus(feeRet.bStatus);
			fee.setiNumRec(feeRet.iNumRec);
			fee.setsFee(feeRet.sFee);
			fee.setsFeeLeft(feeRet.sFeeLeft);
			protocolHandler.doFeeNotify(gwClient.getProtocolId(), fee);
		} else if (pMessage instanceof CReportReq)
		{
			CReportReq report = (CReportReq) pMessage;
			logger.debug("GW:" + client.getProtocolId() + " report:" + report.toString());
			CReportRet reportRep = new CReportRet(report.sUserID, (byte) 1);
			String status = null;
			int result = 0;
			if (report.bStatus == 3)
			{
				report.bStatus = 0;
				status = "DELIVRD";
				channelStatMgr.addSuccReports(client.getProtocolId());
			} else
			{
				status = "UNDELIV";
				result = 1;
				channelStatMgr.addFailReports(client.getProtocolId());
			}
			Receipt receipt = new Receipt(report.sReqID, report.sPhone, result, status, report.sMsg);
			gwClient.sendPMessage(reportRep);
			protocolHandler.doReceipt(gwClient.getProtocolId(), receipt);
		} else if (pMessage instanceof CMoReq)
		{
			CMoReq moReq = (CMoReq) pMessage;
			logger.debug("GW:" + client.getProtocolId() + " MO:" + moReq.toString());
			CMoRet moRep = new CMoRet(moReq.sUserID, (byte) 1);
			gwClient.sendPMessage(moRep);
			String content = "";
			if (moReq.bMessage != null)
			{
				try
				{
					content = new String(moReq.bMessage, "gbk");
				} catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Mo mo = new Mo(moReq.sMoid, moReq.sFrom, moReq.sTarget, content);
			protocolHandler.doMo(gwClient.getProtocolId(), mo);

		} else if (pMessage instanceof CLinkReq)
		{
			CLinkReq linkReq = (CLinkReq) pMessage;
			logger.debug("GW:" + client.getProtocolId() + "CLinkReq");
			CLinkRet linkRet = new CLinkRet(linkReq.sUserID);
			gwClient.sendPMessage(linkRet);
			if (!client.isLogined())
			{
				client.setUnAvailable();
			}

		} else if (pMessage instanceof CLinkRet)
		{
			if (!client.isLogined())
			{
				client.setUnAvailable();
			}
			// logger.debug("GW:" + client.getProtocolId() + "CLinkRet");
		}

	}
}
