package com.surge.engine.protocol.sms.pojo;

/**
 * MOÏûÏ¢µÄpojo
 * 
 * @description
 * @project: esk
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class Mo
{
	private String msg_id;

	private String src_id;

	private String dest;

	private String content;

	public Mo()
	{
	}
	public Mo(String msg_id, String src_id, String dest, String content)
	{
		this.msg_id = msg_id;
		this.src_id = src_id;
		this.dest = dest;
		this.content = content;

	}
	public String toString()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append("msg_id:");
		sb.append(msg_id);
		sb.append(" src_id:");
		sb.append(src_id);
		sb.append(" result:");
		sb.append(dest);
		sb.append(" dest:");
		sb.append(dest);
		sb.append(" content:");
		sb.append(content);
		return sb.toString();
	}
	public String getMsg_id()
	{
		return msg_id;
	}
	public void setMsg_id(String msgId)
	{
		msg_id = msgId;
	}
	public String getSrc_id()
	{
		return src_id;
	}
	public void setSrc_id(String srcId)
	{
		src_id = srcId;
	}
	public String getDest()
	{
		return dest;
	}
	public void setDest(String dest)
	{
		this.dest = dest;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
}
