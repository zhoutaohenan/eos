package com.surge.engine.dbadapter.runnable;

import org.apache.log4j.Logger;

import com.surge.engine.BaseThread;
import com.surge.engine.SysConst;
import com.surge.engine.dbadapter.dao.DBOperateDao;
import com.surge.engine.sms.service.SmsAgentService;
import com.surge.engine.util.EskLog;

/**
 *  短信失败重发线程
 * @description 
 * @project: esk2.0
 * @Date:2011-2-23
 * @version  1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsResendThread extends BaseThread
{
	private static final Logger logger = Logger.getLogger(SmsResendThread.class);

	private volatile boolean isCancle = false;

	private final SmsAgentService smsAgentService;

	private DBOperateDao dboperate;
	
	private long activeTime = System.currentTimeMillis();

	public SmsResendThread(SmsAgentService smsAgentService)
	{
		this.smsAgentService = smsAgentService;
		dboperate = new DBOperateDao(this.smsAgentService);
	}

	@Override
	public void run()
	{
		this.setName("SmsResendThread");
		logger.info("SmsResendThread线程启动成功");
		while (!isCancle && !this.isInterrupted())
		{
			try
			{
				Thread.sleep(5);
				activeTime=System.currentTimeMillis();
				if (SysConst.getFalg() == 3)
				{
					if (EskLog.isDebugEnabled())
					{
						logger.debug("SmsResendThread begin ....");
					}
					dboperate.queryFailSms();
					SysConst.setValue(4);
				}
			} catch (InterruptedException e)
			{
				this.interrupt();
				logger.info("SmsResendThread线程中断");
				SysConst.setValue(4);
			}
		}
	}
	/**
	 * 关闭线程 TODO void
	 * 
	 * @throws
	 */
	public void cancel()
	{
		isCancle = true;
		this.interrupt();

	}

	public SmsAgentService getSmsAgentService()
	{
		return smsAgentService;
	}

	@Override
	public long getLastActiveTime()
	{
		// TODO Auto-generated method stub
		return activeTime;
	}
}
