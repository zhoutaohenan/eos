package com.surge.engine.protocol.sms.common;

public class SmsReportStatus
{

	public static String getSmsStatus(int status)
	{
		String statusResult = null;
		switch (status)
		{
		case 0:
			statusResult = "发送成功";
			break;
		case 1:
			statusResult = "等待发送";
			break;
		case 2:
			statusResult = "发送成败";
			break;
		default:
			break;
		}
		return statusResult;
	}
}
