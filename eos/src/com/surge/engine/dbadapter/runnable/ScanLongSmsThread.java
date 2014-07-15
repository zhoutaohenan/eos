package com.surge.engine.dbadapter.runnable;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2Deliver;
import com.surge.engine.protocol.sms.pojo.Mo;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipDeliver;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpDeliver;
import com.surge.engine.sms.receive.SmsReceiveHandler;
import com.surge.engine.sms.service.SmsQueueMgr;
import com.surge.engine.util.Tools;

public class ScanLongSmsThread extends Thread
{

	private static final Logger logger = Logger.getLogger(ScanLongSmsThread.class);

	private boolean isClose = false;

	/** 队列管理 */
	private SmsQueueMgr smsQueueMgr = SmsQueueMgr.getInstance();

	/** 长短信保留时间 **/
	private int longMoTime;

	private SmsReceiveHandler smsReceiveHandler;

	public ScanLongSmsThread(int longMoTime, SmsReceiveHandler smsReceiveHandler)
	{
		this.longMoTime = longMoTime;
		this.smsReceiveHandler = smsReceiveHandler;
	}

	@Override
	public void run()
	{
		while (!this.isInterrupted() && !isClose)
		{
			try
			{
				Tools.csleep(10 * 1000);

				Map<String, Map<String, PMessage>> protocolMap = smsQueueMgr.getLongMoSMSMap();
				for (String key : protocolMap.keySet())
				{
					Map<String, PMessage> longMap = protocolMap.get(key);
					Iterator<String> smIter=longMap.keySet().iterator();
					while (smIter.hasNext())
					{
						String dest=smIter.next();
						if (key.startsWith("cmpp2"))
						{
							Cmpp2Deliver deliver = (Cmpp2Deliver) longMap.get(dest);
							if (System.currentTimeMillis() - deliver.getCurrTime() > longMoTime * 60 * 1000)
							{
								for (String content : deliver.getLongContent())
								{
									if (content != null && content.trim().length() > 0)
									{
										deliver.setContent(content);
										Mo mo = new Mo(String.valueOf(deliver.getMsgId()), deliver
												.getSrcTerminalId(), deliver.getDestId(), deliver
												.getContent());
										smsReceiveHandler.doMo(key, mo);
										logger.info("保存" + key + "通道超时长MO短信" + mo.toString());
									}

								}
								longMap.remove(dest);
							}
						} else if (key.startsWith("sgip"))
						{
							SgipDeliver deliver = (SgipDeliver) longMap.get(dest);
							if (System.currentTimeMillis() - deliver.getCurrTime() > longMoTime * 60 * 1000)
							{
								for (String content : deliver.getLongContent())
								{
									if (content != null && content.trim().length() > 0)
									{
										deliver.setMessageContent(content);
										Mo mo = new Mo(deliver.getMessageId(), deliver
												.getUserNumber(), deliver.getSpNumber(), deliver
												.getMessageContent());
										smsReceiveHandler.doMo(key, mo);
										logger.info("保存" + key + "通道超时长MO短信" + mo.toString());
									}

								}
								longMap.remove(dest);
							}
						} else if (key.startsWith("smgp"))
						{
							SmgpDeliver deliver = (SmgpDeliver) longMap.get(dest);
							if (System.currentTimeMillis() - deliver.getCurrTime() > longMoTime * 60 * 60 * 1000)
							{
								for (String content : deliver.getLongContent())
								{
									if (content != null && content.trim().length() > 0)
									{
										deliver.setContent(content);
										Mo mo = new Mo(String.valueOf(deliver.getMsgId()), deliver
												.getSrcTerminalId(), deliver.getDestId(), deliver
												.getContent());
										smsReceiveHandler.doMo(key, mo);
										logger.info("保存" + key + "通道超时长MO短信" + mo.toString());
									}

								}
								longMap.remove(dest);
							}
						}
					}
				}
			} catch (InterruptedException e)
			{
				logger.error("", e);
				this.interrupt();
				isClose = true;
			}
		}
	}

	public void stopThread()
	{
		this.isClose = true;
	}
	


}
