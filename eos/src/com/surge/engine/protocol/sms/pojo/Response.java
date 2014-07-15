package com.surge.engine.protocol.sms.pojo;

/**
 * 发送短信响应pojo
 * 
 * @description
 * @project: esk
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class Response
{
	/** 序列号 */
	private String seqId;

	/** MT ID 唯一标识一条短信 **/
	private String messageId;

	/** 响应结果 **/
	private int result;

	/** 描述 */
	private String desc;

	/** 通道ID 只针对GW网关 **/
	private String agentID;

	public Response()
	{

	}

	public Response(String seqId, int result, String messageId, String desc)
	{
		this.seqId = seqId;
		this.messageId = messageId;
		this.result = result;
		this.desc = desc;
	}
	public String getReqID()
	{
		return messageId;
	}

	public void setReqID(String reqID)
	{
		this.messageId = reqID;
	}

	public String getAgentID()
	{
		return agentID;
	}

	public void setAgentID(String agentID)
	{
		this.agentID = agentID;
	}
	public int getResult()
	{
		return result;
	}
	public void setResult(int result)
	{
		this.result = result;
	}
	public String getMessageId()
	{
		return messageId;
	}
	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}
	public String getSeqId()
	{
		return seqId;
	}
	public void setSeqId(String seqId)
	{
		this.seqId = seqId;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public String toString()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append(" seqId:");
		sb.append(seqId);
		sb.append(" msg_id:");
		sb.append(messageId);
		sb.append(" result:");
		sb.append(result);

		return sb.toString();
	}
}
