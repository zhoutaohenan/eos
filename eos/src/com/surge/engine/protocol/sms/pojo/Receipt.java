package com.surge.engine.protocol.sms.pojo;

/**
 * 状态报告pojo
 * 
 * @description
 * @project: esk
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class Receipt
{
	private String messageId;

	/** 接收短消息的手机号 **/
	private String mobile;

	/** 错误描述 */
	private String desc;

	/** 根据各个网关状态结果适配而来，0：成功 1：失败 */
	private int result;

	/** 状态码，每个协议不同，仅供参考 **/
	private String state;

	/** 通道ID GW网特有 */
	private String agentID;

	/** 状态描述 GW网特有 **/
	private String sDescribe;

	public Receipt()
	{
	}
	public String toString()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append("msg_id:");
		sb.append(messageId);
		sb.append(" mobile:");
		sb.append(mobile);
		sb.append(" result:");
		sb.append(result);
		sb.append(" state:");
		sb.append(state);
		sb.append(" desc:");
		sb.append(desc);
		return sb.toString();
	}
	public Receipt(String messageId, String mobile, int result, String state, String desc)
	{
		this.messageId = messageId;
		this.mobile = mobile;
		this.state = state;
		this.result = result;
		this.desc = desc;
	}

	public String getAgentID()
	{
		return agentID;
	}

	public void setAgentID(String agentID)
	{
		this.agentID = agentID;
	}

	public String getsDescribe()
	{
		return sDescribe;
	}

	public void setsDescribe(String sDescribe)
	{
		this.sDescribe = sDescribe;
	}
	public String getMessageId()
	{
		return messageId;
	}
	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public int getResult()
	{
		return result;
	}
	public void setResult(int result)
	{
		this.result = result;
	}

}
