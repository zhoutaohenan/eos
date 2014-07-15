package com.surge.engine.sms.service;

import com.surge.engine.sms.pojo.Report;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsMessage;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface SmsHandler
{
	/**
	 * 
	 * 通知状态报告
	 * 
	 * @param reports
	 *            void
	 * @throws
	 */
	void notifyReport(Report report);

	/**
	 * 
	 * 通知MO
	 * 
	 * @param smsMessage
	 *            void
	 * @throws
	 */
	void notirySmsMessage(SmsMessage smsMessage);

	/**
	 * 
	 * 通知状态报告
	 * 
	 * @param reports
	 *            void
	 * @throws
	 */
	void notifyResp(Sms sms);

}
