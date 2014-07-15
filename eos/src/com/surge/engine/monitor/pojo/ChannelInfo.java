package com.surge.engine.monitor.pojo;

import com.surge.engine.sms.service.SmsChannelMgr;

/**
 * ͨ��ͳ����Ϣ
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-23
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class ChannelInfo
{

	/**
	 * ͨ������
	 */
	private String channelName;

	/**
	 * ͨ��״̬��
	 */
	private String state = "����";

	/**
	 * ���Ͷ��д�С
	 */
	private int sendQueue = 0;

	/**
	 * ״̬������д�С
	 */
	private int reportQueue = 0;

	/**
	 * ״̬������д�С
	 */
	private int responseQueue = 0;

	/**
	 * MO���д�С
	 */
	private int moQueue = 0;

	/**
	 * ʵ�ʷ����ٶ�
	 */
	private int flux = 0;

	/**
	 * ����������
	 */
	private int totoalSms = 0;

	/**
	 * ״̬����ɹ�����
	 */
	private int successReports = 0;

	/**
	 * ״̬����ʧ������
	 */
	private int failReports = 0;

	private long statTime;

	public String getChannelName()
	{
		return channelName;
	}

	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public int getSendQueue()
	{
		return sendQueue;
	}

	public void setSendQueue(int sendQueue)
	{
		this.sendQueue = sendQueue;
	}

	public int getReportQueue()
	{
		return reportQueue;
	}

	public void setReportQueue(int reportQueue)
	{
		this.reportQueue = reportQueue;
	}

	public int getResponseQueue()
	{
		return responseQueue;
	}

	public void setResponseQueue(int responseQueue)
	{
		this.responseQueue = responseQueue;
	}

	public int getMoQueue()
	{
		return moQueue;
	}

	public void setMoQueue(int moQueue)
	{
		this.moQueue = moQueue;
	}

	public int getFlux()
	{
		return flux;
	}

	public void setFlux(int flux)
	{
		this.flux = flux;
	}

	public int getTotoalSms()
	{
		return totoalSms;
	}

	public void setTotoalSms()
	{
		this.totoalSms++;
	}

	public int getSuccessReports()
	{
		return successReports;
	}

	public void setSuccessReports()
	{
		this.successReports++;
	}

	public int getFailReports()
	{
		return failReports;
	}

	public void setFailReports()
	{
		this.failReports++;
	}
	public int getTotalReports()
	{
		return this.failReports + this.successReports;
	}

	public void clearZero()
	{
		this.failReports = 0;
		this.successReports = 0;
		this.totoalSms = 0;
		this.flux = 0;
	}
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		this.state = SmsChannelMgr.getInstance().getChannel(this.channelName).getConnectStatus()
				.getValue() == 1 ? "����" : "�Ͽ�";
		buffer.append(this.channelName).append(",").append(this.state).append(",").append(
				this.sendQueue).append(",").append(this.responseQueue).append(",").append(
				this.reportQueue).append(",").append(this.moQueue).append(",").append(
				this.totoalSms).append(",").append(this.successReports).append(",").append(
				this.failReports).append(",").append(this.getTotalReports()).append(",").append(
				flux);
		return buffer.toString();
	}

	public long getStatTime()
	{
		return statTime;
	}

	public void setStatTime(long statTime)
	{
		this.statTime = statTime;
	}
}
