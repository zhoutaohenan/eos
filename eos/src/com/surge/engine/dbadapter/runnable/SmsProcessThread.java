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

	/** ��Ӧ���� **/
	private ConcurrentLinkedQueue<Sms> respsQueue = instance.getRespsQueue();

	/** ״̬������� **/
	private ConcurrentLinkedQueue<Report> reportsQueue = instance.getReportsQueue();

	private Map<String, ConcurrentHashMap<String, Long>> smsMessgeIDQueue = instance
			.getSmsMessgeIDQueue();

	private long activeTime = System.currentTimeMillis();

	/**
	 * ״̬���泬ʱʱ�䣬��������ʱ����û��MESSAGEID�뱾״̬�����Ӧ������´�״̬���浽temp��(��λ:����)
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
				// ��־1ִ�� ������Ӧ
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
				if (SysConst.getFalg() == 2)// ����״̬����
				{
					this.processReport();
					// ״̬�������С�����ô�Сʱ��ת��4���ж���ת��
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
				if (SysConst.getFalg() == 3)// �����ط�����
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
				logger.info("RespWroker�߳��ж�");
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
	 * ������Ӧ TODO
	 * 
	 * @param count
	 *            ÿ��������Ӧ���� void
	 * @throws
	 */
	private void processResponse(int count)
	{
		ArrayList<Sms> listResp = new ArrayList<Sms>();
		for (; respsQueue.size() > 0;)
		{
			listResp.add(respsQueue.poll());
			// ÿ300��һ��
			if (listResp.size() >= count || respsQueue.size() == 0)
			{
				try
				{
					basicOperate.saveResponse(listResp);
				} catch (Exception e)
				{
					// �쳣����ʱ��LIST�Żض���
					if (listResp != null)
					{
						respsQueue.addAll(listResp);
					}
					break;
				}
				logger.debug(">>>>>>>>response queue size:" + respsQueue.size());
				for (Sms sms : listResp)
				{
					// ��Ϣ��Ӧ����
					ConcurrentHashMap<String, Long> queue = smsMessgeIDQueue.get(sms.getChannel()
							.getChanneId());
					if (queue == null)
					{
						// ͨ����Ӧ��MESSAGEID����Ϊ��ʱ,��ʼ��������������й�����
						queue = new ConcurrentHashMap<String, Long>();
						smsMessgeIDQueue.put(sms.getChannel().getChanneId(), queue);
					}
					if (sms.getIsNeedReport() == 0)
					{
						// ���Ų�Ҫ״̬����,����Ҫ����MessageID,������������
						continue;
					}
					// ��messageIdΪ�գ����Ƿ���ʧ��,����Ҫ����MessageID,������������
					if (sms.getMessageId() == null || sms.getMessageId().length() <= 0)
					{
						continue;
					}
					// ������Ӧ��Ϊ0,��Ϊʧ�ܶ��ţ�������״̬���棬�̲���Ҫ����MessageID,������������
					if (sms.getSendResult() != 0)
					{
						continue;
					}
					// ͨ����Ӧ��messageID����С��80000ʱ���Żᱣ��MessageID,������
					if (queue.size() <= 80000)
					{
						// ��Ӧ���������
						queue.put(sms.getMessageId().trim(), System.currentTimeMillis());
					}
				}
				break;
			}
		}
		listResp = null;

	}

	/**
	 * ����״̬���� TODO void
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
					// ��ͨ����Ӧ��MessageID���л�û��ʼ�������ڴ˳�ʼ������ֹֻ�յ�״̬���棬
					// ��״̬����һֱ�벻�˿����,���磺��ǰһ��ʱ�䷢�ͺ�æ��״̬����־û����ļ�
					// ��ĳһʱ�̣��ر�ϵͳ������ϵͳ�󣬻��ȡ�ļ��е�״̬���棬 ����Ӧ������⣬
					// ϵͳ�������ţ���������Ӧ��messageid����һֱ
					// Ϊ�գ������������ó�ʱ���ƣ���״̬�������

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
						// û���ҵ���Ӧ�ѳ־û���messagid��û�г�ʱ,�ٲ���״̬�������β
						reportsQueue.offer(report);
						// ״̬������ʱ���г���50��������״̬������д�����Ӧ��������ʱ���в�Ϊ��,
						// �򱣴�һ��״̬����
						if (listReport.size() > 0)
						{
							basicOperate.updateReport(listReport);
							logger.debug(">>>>>>>>reports queue size:" + reportsQueue.size());
						}
						break;
					}

				}
				// ��ʱ���г���50��״̬�������Ϊ��,����һ��״̬����
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
