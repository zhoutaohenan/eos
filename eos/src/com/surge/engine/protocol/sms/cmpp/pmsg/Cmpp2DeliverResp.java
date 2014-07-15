// Copyright (c) 2001 gaohu

package com.surge.engine.protocol.sms.cmpp.pmsg;

import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;

public class Cmpp2DeliverResp extends Cmpp2SendObject
{

	private long msg_id;

	private byte result;

	public Cmpp2DeliverResp(byte[] bys)
	{

		SubtractTools t2 = new SubtractTools(bys, 0);
		msg_id = t2.getLong();
		result = t2.getByte();
	}

	public Cmpp2DeliverResp(int seq_id, long msg_id, byte result)
	{

		AppendUtils tool = new AppendUtils(20);

		tool.appendLong(msg_id);
		tool.appendByte(result);

		body = tool.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_DELIVER_RESP, seq_id);
	}

	public String toString()
	{

		StringBuilder sb = new StringBuilder(100);
		sb.append("msg_id:");
		sb.append(msg_id);
		sb.append(" result:");
		sb.append(result);
		return sb.toString();
	}
}
