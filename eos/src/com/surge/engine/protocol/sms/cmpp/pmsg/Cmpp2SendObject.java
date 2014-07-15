// Copyright (c) 2001 gaohu

package com.surge.engine.protocol.sms.cmpp.pmsg;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.util.AppendUtils;

public class Cmpp2SendObject extends PMessage
{

	protected Cmpp2MsgHead head;

	protected byte[] body;

	public byte[] getOutBytes()
	{

		int len = 0;
		if (body != null)
			len = body.length;

		AppendUtils t = new AppendUtils(Cmpp2MsgHead.size + len);
		t.appendBytes(head.getOutBytes());
		t.appendBytes(body);
		byte[] buf = t.getOutBytes();
		return buf;
	}

	public int getCmdId()
	{

		return head.getCmdId();
	}

	public String getSeqId()
	{

		return String.valueOf(head.getSeqId());
	}

	@Override
	public int getCommonId()
	{

		// TODO Auto-generated method stub
		return head.getCmdId();
	}

	@Override
	public IoBuffer getIoBuffer()
	{

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getOut()
	{

		int len = 0;
		if (body != null)
			len = body.length;

		AppendUtils t = new AppendUtils(Cmpp2MsgHead.size + len);
		t.appendBytes(head.getOutBytes());
		t.appendBytes(body);
		byte[] buf = t.getOutBytes();
		return buf;
	}
}
