package test.sms;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsMessage;
import com.surge.engine.sms.service.SmsHandler;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-20
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsHandlerImplTest implements SmsHandler
{

	private Logger logger = Logger.getLogger(SmsHandlerImplTest.class);

	private AtomicInteger respCount = new AtomicInteger(0);

	private AtomicInteger reportCount = new AtomicInteger(0);

	private AtomicInteger moCount = new AtomicInteger(0);

	@Override
	public void notifyReport(Report report)
	{
		logger.info("收到状态报告总数：" + respCount.incrementAndGet());
	}

	@Override
	public void notifyResp(Sms sms)
	{
		logger.info("收到响应总数：" + reportCount.incrementAndGet());
	}

	@Override
	public void notirySmsMessage(SmsMessage smsMessage)
	{
		logger.info("收到MO总数：" + respCount.incrementAndGet());
	}


}
