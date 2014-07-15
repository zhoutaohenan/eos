package com.surge.engine.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.surge.engine.dbadapter.mgr.ReportQueueMgr;
import com.surge.engine.monitor.pojo.ChannelInfo;
import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsMessage;
import com.surge.engine.sms.service.SmsChannelMgr;
import com.surge.engine.sms.service.SmsQueueMgr;

/**
 * 监控通道状态管理类
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-23
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class ChannelStatMgr
{
	/**
	 * 通道统计队列
	 */
	private ConcurrentHashMap<String, ChannelInfo> channelInfoMap = new ConcurrentHashMap<String, ChannelInfo>();

	private ReportQueueMgr reportInstance = ReportQueueMgr.instance;

	// 状态报告队列
	private ConcurrentLinkedQueue<Report> reportsQueue = reportInstance.getReportsQueue();

	// 响应队列　
	private ConcurrentLinkedQueue<Sms> respsQueue = reportInstance.getRespsQueue();

	// MO队列
	private ConcurrentLinkedQueue<SmsMessage> smsMessagesQueue = reportInstance
			.getSmsMessagesQueue();

	/**
	 * 单例
	 */
	public static ChannelStatMgr instance = new ChannelStatMgr();

	private ChannelStatMgr()
	{
	}

	public ConcurrentHashMap<String, ChannelInfo> getChannelInfoMap()
	{
		return channelInfoMap;
	}

	public void setChannelInfoMap(ConcurrentHashMap<String, ChannelInfo> channelInfoMap)
	{
		this.channelInfoMap = channelInfoMap;
	}

	/**
	 * 添加通道统计信息到队列 TODO
	 * 
	 * @param channelName
	 *            通道名
	 * @param info
	 *            通道统计信息 void
	 * @throws
	 */
	public void addChannelInfo(String channelName)
	{
		ChannelInfo info = new ChannelInfo();
		info.setChannelName(channelName);
		channelInfoMap.put(channelName, info);
	}
	/**
	 * 从队列中移除通道信息 TODO
	 * 
	 * @param channelName
	 *            通道名 void
	 * @throws
	 */
	public void removeChannelInfo(String channelName)
	{
		channelInfoMap.remove(channelName);
	}
	/**
	 * 设置通道单位时间内,某一时间点的流量 TODO
	 * 
	 * @param channelName
	 *            通道名 void
	 * @throws
	 */
	public void addChannelFlux(String channelName, int flux, long statTime)
	{
		ChannelInfo info = this.channelInfoMap.get(channelName);

		if (info != null)
		{
			info.setFlux(flux);
			info.setStatTime(statTime);
			info.setSendQueue(SmsQueueMgr.getInstance().getMtQeueMap().get(channelName).size());
		}
	}
	/**
	 * 成功状态报告+1 TODO
	 * 
	 * @param channelName
	 *            void
	 * @throws
	 */
	public void addSuccReports(String channelName)
	{
		ChannelInfo info = this.channelInfoMap.get(channelName);
		if (info != null)
		{
			info.setSuccessReports();
		}
	}
	/**
	 * 失败状态报告+1 TODO
	 * 
	 * @param channelName
	 *            void
	 * @throws
	 */
	public void addFailReports(String channelName)
	{
		ChannelInfo info = this.channelInfoMap.get(channelName);
		if (info != null)
		{
			info.setFailReports();
		}
	}
	/**
	 * 短信发送总量+1 TODO
	 * 
	 * @param channelName
	 *            void
	 * @throws
	 */
	public void addSmsTotal(String channelName)
	{
		ChannelInfo info = this.channelInfoMap.get(channelName);
		if (info != null)
		{
			info.setTotoalSms();
		}
	}
	/**
	 * 清空通道统计的部分信息，进行重新统计 TODO
	 * 
	 * @param channelName
	 *            不为空时，清空指定通道信息;为空时,清空所有统道信息 void
	 * @throws
	 */
	public void clearZero(String channelName)
	{
		if (channelName != null && channelName.length() > 0)
		{
			ChannelInfo info = this.channelInfoMap.get(channelName);
			if (info != null)
			{
				info.clearZero();
			}
		} else
		{
			for (String name : channelInfoMap.keySet())
			{
				channelInfoMap.get(name).clearZero();
			}
		}
	}
	/**
	 * 统计通道各队列的状态 TODO
	 * 
	 * @param channelName
	 * @return List
	 * @throws
	 */
	public List statChannelInfo(String channelName)
	{
		ArrayList<String> infoList = new ArrayList<String>();

		Map<String, Integer> reportMap = this.statReportQueue();
		Map<String, Integer> responseMap = this.statResponseQueue();
		Map<String, Integer> moMap = this.statMOQueue();
		SmsChannelMgr channelMgr = SmsChannelMgr.getInstance();
		SmsQueueMgr queueMgr = SmsQueueMgr.getInstance();
		if (channelName != null && channelName.length() > 0)
		{
			ChannelInfo info = channelInfoMap.get(channelName);
			int nowQueueSize = queueMgr.getMtQueue(channelName).size();
			int channelFlux = channelMgr.getChannel(channelName).getFlux();
			if (info != null)
			{
				int sendQueueSize = info.getSendQueue();

				// info.setSendQueue(sendQueueSize);
				info.setReportQueue(reportMap.get(channelName));
				info.setResponseQueue(responseMap.get(channelName));
				info.setMoQueue(moMap.get(channelName));
				// 判断最后一次统计速度的时间距现在是否超过10S，若超过,则速度清空
				if (System.currentTimeMillis() - info.getStatTime() > 10000)
				{
					// info.setSendQueue(0);
					info.setFlux(0);
				} else
				{
					if (nowQueueSize > 0)
					{
						if (sendQueueSize > 0 && sendQueueSize < channelFlux)
						{
							int statFlux = info.getFlux();
							statFlux = statFlux * sendQueueSize / channelFlux;
							statFlux = statFlux > channelFlux ? channelFlux : statFlux;
							info.setFlux(statFlux);
						} else if (sendQueueSize == 0)
						{
							info.setFlux(0);
						}
					}
				}
				if (nowQueueSize == 0)
				{
					info.setSendQueue(0);
				}
				infoList.add(info.toString());
			}

		} else
		{
			Set<String> namesSet = channelInfoMap.keySet();
			for (String name : namesSet)
			{
				ChannelInfo info = channelInfoMap.get(name);
				int channelFlux = channelMgr.getChannel(name).getFlux();
				int nowQueueSize = queueMgr.getMtQueue(name).size();
				if (info != null)
				{
					int sendQueueSize = info.getSendQueue();

					if (reportMap.get(name) != null)
					{
						info.setReportQueue(reportMap.get(name));
					} else
					{
						info.setReportQueue(0);
					}
					if (responseMap.get(name) != null)
					{
						info.setResponseQueue(responseMap.get(name));
					} else
					{
						info.setResponseQueue(0);
					}
					if (moMap.get(name) != null)
					{
						info.setMoQueue(moMap.get(name));
					} else
					{
						info.setMoQueue(0);
					}
					// 判断最后一次统计速度的时间距现在是否超过10S，若超过,则速度清空
					if (System.currentTimeMillis() - info.getStatTime() > 10000)
					{
						// info.setSendQueue(0);
						info.setFlux(0);
					} else
					{
						if (nowQueueSize > 0)
						{
							if (sendQueueSize > 0 && sendQueueSize < channelFlux)
							{
								int statFlux = info.getFlux();
								statFlux = statFlux * sendQueueSize / channelFlux;
								statFlux = statFlux > channelFlux ? channelFlux : statFlux;
								info.setFlux(statFlux);
							} else if (sendQueueSize == 0)
							{
								info.setFlux(0);
							}
						}
					}
					if (nowQueueSize == 0)
					{
						info.setSendQueue(0);
					}

					infoList.add(info.toString());
				}
			}
		}

		return infoList;
	}
	/**
	 * 统计各通道的状态报告数 TODO
	 * 
	 * @return Map<String,Integer>
	 * @throws
	 */
	private Map<String, Integer> statReportQueue()
	{
		Map<String, Integer> reportMap = new HashMap<String, Integer>();
		for (Report report : reportsQueue)
		{
			Integer count = reportMap.get(report.getChannelId());
			if (count == null)
			{
				count = new Integer(0);
				reportMap.put(report.getChannelId(), count);
			} else
			{
				count++;
			}
		}
		return reportMap;
	}
	/**
	 * 统计各通道的响应数 TODO
	 * 
	 * @return Map<String,Integer>
	 * @throws
	 */
	private Map<String, Integer> statResponseQueue()
	{
		Map<String, Integer> responseMap = new HashMap<String, Integer>();
		for (Sms sms : respsQueue)
		{
			Integer count = responseMap.get(sms.getChannel().getChanneId());
			if (count == null)
			{
				count = new Integer(0);
				responseMap.put(sms.getChannel().getChanneId(), count);
			} else
			{
				count++;
			}
		}
		return responseMap;
	}
	/**
	 * 统计各通道的MO数 TODO
	 * 
	 * @return Map<String,Integer>
	 * @throws
	 */
	private Map<String, Integer> statMOQueue()
	{
		Map<String, Integer> moMap = new HashMap<String, Integer>();
		for (SmsMessage sms : smsMessagesQueue)
		{
			Integer count = moMap.get(sms.getChannelId());
			if (count == null)
			{
				count = new Integer(0);
				moMap.put(sms.getChannelId(), count);
			} else
			{
				count++;
			}
		}
		return moMap;
	}

}
