package com.surge.engine.sms.pojo;

import com.surge.engine.sms.common.MsgFmt;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SplitMsg
{
	private String[] msgs = null;

	private MsgFmt msgFmt = null;

	/**��ֺ��Ƿ����ֻ����Ժϳ�һ���ĳ�����*/
	private boolean isLongSms;

	/**�Ƿ���Ӣ�Ķ���*/
	private boolean isEnglish;

	public String[] getMsgs()
	{
		return msgs;
	}

	public void setMsgs(String[] msgs)
	{
		this.msgs = msgs;
	}

	public MsgFmt getMsgFmt()
	{
		return msgFmt;
	}

	public void setMsgFmt(MsgFmt msgFmt)
	{
		this.msgFmt = msgFmt;
	}

	public boolean isLongSms()
	{
		return isLongSms;
	}

	public void setLongSms(boolean isLongSms)
	{
		this.isLongSms = isLongSms;
	}

	public boolean isEnglish()
	{
		return isEnglish;
	}

	public void setEnglish(boolean isEnglish)
	{
		this.isEnglish = isEnglish;
	}
}
