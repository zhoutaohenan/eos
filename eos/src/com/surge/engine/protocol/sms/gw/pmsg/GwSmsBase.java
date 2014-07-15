package com.surge.engine.protocol.sms.gw.pmsg;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.communication.framework.common.PMessage;

public class GwSmsBase extends PMessage
{

	private static final long serialVersionUID = 4182930456474976483L;

	@Override
	public int getCommonId()
	{
		return 0;
	}

	@Override
	public IoBuffer getIoBuffer()
	{
		return null;
	}

	@Override
	public byte[] getOut()
	{
		return null;
	}

	@Override
	public String getSeqId()
	{
		return null;
	}

}
