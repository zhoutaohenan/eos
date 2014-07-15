package com.surge.engine.sms.receive;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.pojo.Mo;
import com.surge.engine.protocol.sms.pojo.Receipt;
import com.surge.engine.protocol.sms.pojo.Response;
import com.surge.engine.protocol.sms.pojo.SmsFee;
import com.surge.engine.sms.common.ConnectStatus;
import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsMessage;
import com.surge.engine.sms.service.SmsChannelMgr;
import com.surge.engine.sms.service.SmsHandler;
import com.surge.engine.util.EskLog;
import com.surge.engine.util.TimerPool;

/**
 * ʵ��Э����Ϣ�ص��ӿ�
 * 
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsReceiveHandler implements SmsProtocolHandler
{

	private static final Logger logger = Logger.getLogger(SmsReceiveHandler.class);

	/** �ѷ��Ͷ��� */
	private Map<String, Map<String, Sms>> sentSubmits;

	/** ���ŷ�����Ϣ�ص� */
	private SmsHandler smsHandler;

	public SmsReceiveHandler(Map<String, Map<String, Sms>> sentSubmits, SmsHandler smsHandler)
	{

		this.sentSubmits = sentSubmits;
		this.smsHandler = smsHandler;
		TimerPool.schedule(new Routine(), 10, 30);
	}

	@Override
	public void doConnectStatus(String channelId, boolean status)
	{

		logger.debug("channel:" + channelId + "������״̬Ϊ:" + status);
		if (status)
		{
			SmsChannelMgr.getInstance().getChannel(channelId).setConnectStatus(
					ConnectStatus.Connect);
		} else
		{
			SmsChannelMgr.getInstance().getChannel(channelId).setConnectStatus(
					ConnectStatus.Disconnect);
		}
	}

	@Override
	public void doFeeNotify(String channelId, SmsFee smsFee)
	{

		float sFeeLeft = Float.valueOf(smsFee.getsFeeLeft());
		float free = Float.valueOf(smsFee.getsFee());
		if (free != 0)
		{
			int count = (int) (sFeeLeft / free);
			SmsChannel smsChannel = SmsChannelMgr.getInstance().getChannel(channelId);
			if (smsChannel != null)
			{
				logger.info("ƽ̨�˺�������ý����channelId:" + channelId + " sFeeLeft:" + sFeeLeft
						+ " free:" + free + " count:" + count);
				smsChannel.getRemainCount().set(count);
			}
		}
	}

	@Override
	public void doMo(String channelId, Mo mo)
	{

		SmsMessage smsMessage = new SmsMessage();
		smsMessage.setContent(mo.getContent());
		smsMessage.setDest(mo.getDest());
		smsMessage.setMsg_id(mo.getMsg_id());
		smsMessage.setSrc_id(mo.getSrc_id());
		smsMessage.setChannelId(channelId);
		smsHandler.notirySmsMessage(smsMessage);
		logger.debug("channel:" + channelId + " mo:" + mo.toString());
	}

	@Override
	public void doReceipt(String channelId, Receipt receipt)
	{
		Report report = new Report();
		report.setChannelId(channelId);
		report.setDesc(receipt.getDesc());
		report.setMessageId(receipt.getMessageId());
		report.setResult(receipt.getResult());
		report.setStats(receipt.getState());
		report.setReciveTime(System.currentTimeMillis());
		report.setMobile(receipt.getMobile());
		smsHandler.notifyReport(report);
		if (EskLog.isDebugEnabled())
		{
			logger.debug("channel:" + channelId + " report:" + receipt.toString());
		}

	}
	/**
	 * Э����յ���Ӧʱ����ص��˷���
	 * 
	 * ���𽫶��Ŵ� "�ѷ��Ͷ���ת��" �� "�յ���Ӧ����" ,��д�����ݿ��()
	 */
	@Override
	public void doResponse(String protocolId, Response response)
	{
		Map<String, Sms> ismg = sentSubmits.get(protocolId);
		if (ismg == null)
		{
			logger.warn("�Ҳ�����Ӧ��protocolId:" + protocolId);
			return;
		}
		Sms sms = null;
		for (int i = 0; i < 3; i++)
		{
			sms = ismg.remove(response.getSeqId());
			if (sms != null)
			{
				break;
			} else
			{
				try
				{
					com.surge.engine.util.Tools.csleep(1000);
				} catch (InterruptedException e)
				{
					Thread.interrupted();
					return;
				}
			}
		}
		if (sms == null)
		{
			logger.warn("channel:" + protocolId + "�޶�Ӧsms,����!" + " submitresp:"
					+ response.toString());
			return;
		}
		if (EskLog.isDebugEnabled())
		{
			logger.debug("channel:" + protocolId + " submitresp:" + response.toString());
		}
		// subResps.get(protocolId).put(response.getMessageId(), sms);
		sms.setMessageId(response.getMessageId());
		sms.setRespTime(System.currentTimeMillis());
		sms.setSendResult(response.getResult());
		sms.setDesc(response.getDesc());
		if (response.getResult() != 0)
		{
			sms.setNeedReSend(true);
		}
		smsHandler.notifyResp(sms);
	}

	class Routine implements Runnable
	{

		public void run()
		{
			try
			{
				doExpire();
			} catch (Exception e)
			{
				logger.error("", e);
			}
		}
	}

	public void doExpire()
	{
		// �����ѷ��Ͷ���
		for (Map.Entry<String, Map<String, Sms>> entry : sentSubmits.entrySet())
		{
			Map<String, Sms> sent = entry.getValue();
			Set<String> tmpKeys = new HashSet<String>();
			for (Map.Entry<String, Sms> entry2 : sent.entrySet())
			{
				String key2 = entry2.getKey();
				Sms sms = entry2.getValue();

				// ��Ӧ10���ӳ�ʱ����
				if ((System.currentTimeMillis() - sms.getSubmit2IsmgTime()) > 10 * 60 * 1000)
				{
					tmpKeys.add(key2);
					sms.setSendResult(SmsErrCode.NO_RESPONSE_ERROR.getValue());
					sms.setNeedReSend(false);
					sms.setDesc("10����δ�յ�������Ӧ��״̬δ֪");
					logger.debug("10����δ�յ�������Ӧ,������Ӧ��mtID:" + sms.getMtId() + " smsID:"
							+ sms.getSmsid() + " content:"
							+ sms.getContents()[sms.getSmsIndex() - 1] + " smsIndex:"
							+ sms.getSmsIndex());
					this.smsHandler.notifyResp(sms);
				}
			}
			for (String tmpKey : tmpKeys)
			{
				sent.remove(tmpKey);
			}
		}

	}
}
