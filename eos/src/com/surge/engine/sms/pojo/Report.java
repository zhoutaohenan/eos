package com.surge.engine.sms.pojo;

import java.io.Serializable;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class Report implements Serializable
{
	private static final long serialVersionUID = 6038815132424222802L;

	private final static char DIVISION = 0;

	/** ����ͨ�� */
	private String channelId;

	/** ��ϢID */
	private String messageId;

	/** ״̬������ */
	private int result;

	/** ״̬�� */
	private String stats;

	/** ���� */
	private String desc;

	private long reciveTime;
	
	private String mobile;

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}

	public String getMessageId()
	{
		return messageId;
	}

	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}

	public int getResult()
	{
		return result;
	}

	public void setResult(int result)
	{
		this.result = result;
	}

	public String getStats()
	{
		return stats;
	}

	public void setStats(String stats)
	{
		this.stats = stats;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public long getReciveTime()
	{
		return reciveTime;
	}

	public void setReciveTime(long reciveTime)
	{
		this.reciveTime = reciveTime;
	}
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(channelId).append(DIVISION).append(messageId).append(DIVISION).append(result).append(DIVISION)
				.append(stats).append(DIVISION).append(desc).append(DIVISION).append(reciveTime).append("\n");
		return sb.toString();
	}

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
