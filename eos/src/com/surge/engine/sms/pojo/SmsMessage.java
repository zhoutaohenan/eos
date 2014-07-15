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
public class SmsMessage implements Serializable
{
	private static final long serialVersionUID = -4418898807883913432L;

	private String msg_id;

	/** 发起本条MO信息地址**/
	private String src_id;

	private String dest;

	private String content;

	private String channelId;

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

	public String getChannelId()
	{
		return channelId;
	}

	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}
}
