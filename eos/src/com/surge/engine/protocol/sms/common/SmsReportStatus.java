package com.surge.engine.protocol.sms.common;

public class SmsReportStatus
{

	public static String getSmsStatus(int status)
	{
		String statusResult = null;
		switch (status)
		{
		case 0:
			statusResult = "���ͳɹ�";
			break;
		case 1:
			statusResult = "�ȴ�����";
			break;
		case 2:
			statusResult = "���ͳɰ�";
			break;
		default:
			break;
		}
		return statusResult;
	}
}
