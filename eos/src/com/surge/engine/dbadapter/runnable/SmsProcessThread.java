package com.surge.engine.dbadapter.runnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.surge.engine.BaseThread;
import com.surge.engine.SysConst;
import com.surge.engine.dbadapter.dao.DBOperateDao;
import com.surge.engine.dbadapter.mgr.ReportQueueMgr;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.util.Tools;

public class SmsProcessThread extends BaseThread
{
	private static Logger logger = Logger.getLogger(SmsProcessThread.class);

	private static ReportQueueMgr instance = ReportQueueMgr.instance;

	/** 响应队列 **/
	private ConcurrentLinkedQueue<Sms> respsQueue = instance.getRespsQueue();

	/** 状态报告队列 **/
	private ConcurrentLinkedQueue<Report> reportsQueue = instance.getReportsQueue();

	private Map<String, ConcurrentHashMap<String, Long>> smsMessgeIDQueue = instance
			.getSmsMessgeIDQueue();

	private long activeTime = System.currentTimeMillis();

	/**
	 * 状态报告超时时间，若超过此时间仍没有MESSAGEID与本状态报告对应，则更新此状态报告到temp表(单位:毫秒)
	 */
	private static final long REPORT_EXPIRES_TIME = 10 * 60 * 1000;

	public DBOperateDao basicOperate;

	public SmsProcessThread(DBOperateDao basicOperate)
	{
		this.basicOperate = basicOperate;
	}

	private volatile boolean cancelled;

	public void run()
	{
		this.setName("SmsProcessThread");
		while (!cancelled)
		{

			try
			{
				Tools.csleep(5);
				activeTime = System.currentTimeMillis();
				// 标志1执行 处理响应
				if (SysConst.getFalg() == 1)
				{
					if(respsQueue.size() >= SmsConfig.getInstance().getRespsSize() * 0.6)
					{
					   this.processResponse(1000);
					}
					else
					{
						 this.processResponse(100);
					}
					SysConst.setValue(2);
					Tools.csleep(5);
				}
				if (SysConst.getFalg() == 2)// 处理状态报告
				{
					this.processReport();
					// 状态报告队列小于配置大小时跳转到4进行短信转移
					if (reportsQueue.size() < SmsConfig.getInstance().getReportsSize())
					{
						SysConst.setValue(4);
						Tools.csleep(5);
					} else
					{
						if (respsQueue.size() >= SmsConfig.getInstance().getRespsSize() * 0.5)
						{
							SysConst.setValue(1);
							Tools.csleep(5);
						} else
						{
							SysConst.setValue(4);
							Tools.csleep(5);
						}

					}
				}
				if (SysConst.getFalg() == 3)// 处理重发短信
				{
					if (respsQueue.size() <= SmsConfig.getInstance().getRespsSize() * 0.5)
					{
						basicOperate.queryFailSms();
					}
					SysConst.setValue(1);
					Tools.csleep(5);
				}
			} catch (InterruptedException ie)
			{
				logger.info("RespWroker线程中断");
				this.interrupt();
				SysConst.setValue(1);
				break;
			} catch (Exception ex)
			{
				SysConst.setValue(1);
				logger.error("", ex);
			}
		}
	}

	public void cancel()
	{
		cancelled = true;

		this.interrupt();
	}

	/**
	 * 处理响应 TODO
	 * 
	 * @param count
	 *            每次入库的响应数量 void
	 * @throws
	 */
	private void processResponse(int count)
	{
		ArrayList<Sms> listResp = new ArrayList<Sms>();
		for (; respsQueue.size() > 0;)
		{
			listResp.add(respsQueue.poll());
			// 每300条一批
			if (listResp.size() >= count || respsQueue.size() == 0)
			{
				try
				{
					basicOperate.saveResponse(listResp);
				} catch (Exception e)
				{
					// 异常错误时将LIST放回队列
					if (listResp != null)
					{
						respsQueue.addAll(listResp);
					}
					break;
				}
				logger.debug(">>>>>>>>response queue size:" + respsQueue.size());
				for (Sms sms : listResp)
				{
					// 消息响应队列
					ConcurrentHashMap<String, Long> queue = smsMessgeIDQueue.get(sms.getChannel()
							.getChanneId());
					if (queue == null)
					{
						// 通道对应的MESSAGEID队列为空时,初始化并将其放至队列管理中
						queue = new ConcurrentHashMap<String, Long>();
						smsMessgeIDQueue.put(sms.getChannel().getChanneId(), queue);
					}
					if (sms.getIsNeedReport() == 0)
					{
						// 短信不要状态报告,不需要保存MessageID,跳过此条短信
						continue;
					}
					// 若messageId为空，已是发送失败,不需要保存MessageID,跳过此条短信
					if (sms.getMessageId() == null || sms.getMessageId().length() <= 0)
					{
						continue;
					}
					// 短信响应不为0,即为失败短信，不会有状态报告，固不需要保存MessageID,跳过此条短信
					if (sms.getSendResult() != 0)
					{
						continue;
					}
					// 通道对应的messageID队列小于80000时，才会保存MessageID,否则丢弃
					if (queue.size() <= 80000)
					{
						// 响应放入队列中
						queue.put(sms.getMessageId().trim(), System.currentTimeMillis());
					}
				}
				break;
			}
		}
		listResp = null;

	}

	/**
	 * 处理状态报告 TODO void
	 * 
	 * @throws
	 */
	private void processReport()
	{
		ArrayList<Report> listReport = new ArrayList<Report>();
		for (; reportsQueue.size() > 0;)
		{
			try
			{
				Report report = reportsQueue.poll();
				ConcurrentHashMap<String, Long> map = smsMessgeIDQueue.get(report.getChannelId());
				if (map == null)
				{
					// 若通道对应的MessageID队列还没初始化，则在此初始化，防止只收到状态报告，
					// 而状态报告一直入不了库情况,例如：因前一段时间发送很忙，状态报告持久化到文件
					// 在某一时刻，关闭系统，重启系统后，会读取文件中的状态报告， 因响应早已入库，
					// 系统不发短信，不会有响应，messageid队列一直
					// 为空，这样可以利用超时机制，将状态报告入库

					map = new ConcurrentHashMap<String, Long>();
					smsMessgeIDQueue.put(report.getChannelId(), map);
				}
				if (map.remove(report.getMessageId().trim()) != null)
				{
					listReport.add(report);
				}

				else
				{
					if (System.currentTimeMillis() - report.getReciveTime() > REPORT_EXPIRES_TIME)
					{
						listReport.add(report);
					} else
					{
						// 没有找到对应已持久化的messagid且没有超时,再插入状态报告队列尾
						reportsQueue.offer(report);
						// 状态报告临时队列超过50条，或则状态报告队列大于响应队列且临时队列不为空,
						// 则保存一批状态报告
						if (listReport.size() > 0)
						{
							basicOperate.updateReport(listReport);
							logger.debug(">>>>>>>>reports queue size:" + reportsQueue.size());
						}
						break;
					}

				}
				// 临时队列超过50或状态报告队列为空,保存一批状态报告
				if (listReport.size() >= 50 || reportsQueue.size() == 0)
				{
					basicOperate.updateReport(listReport);
					logger.debug(">>>>>>>>reports queue size:" + reportsQueue.size());
					break;
				}
			} catch (Exception e)
			{
				if (listReport != null)
				{
					reportsQueue.addAll(listReport);
				}
			}
		}
		listReport = null;
	}
	public long getLastActiveTime()
	{
		return activeTime;
	}

}
