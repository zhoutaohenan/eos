package com.surge.engine.sms.pojo;

import java.io.Serializable;

import com.surge.engine.sms.common.ErrCode;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsResponse implements Serializable
{
	private static final long serialVersionUID = -6097686655250261938L;

	/** �����ʶID */
	// protected final String requestID;
	/** ������룬null��ʾ������������Ϊnull��ʾ������Ϣ */
	protected ErrCode errCode;

	public ErrCode getErrCode()
	{
		return errCode;
	}

	public void setErrCode(ErrCode errCode)
	{
		this.errCode = errCode;
	}
}
