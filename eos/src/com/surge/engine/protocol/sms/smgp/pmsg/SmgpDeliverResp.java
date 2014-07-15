
package com.surge.engine.protocol.sms.smgp.pmsg;

import com.surge.engine.protocol.sms.cmpp.pmsg.Common;
import com.surge.engine.util.AppendUtils;

public class SmgpDeliverResp extends SmgpSendObject
{
	private static final long serialVersionUID = 1058482571879706393L;

	private long msg_id;

	private byte result;

	// public SmgpDeliverResp(byte[] bys) {
	// SubtractTools t2 = new SubtractTools(bys, 0);
	// msg_id = t2.getLong();
	// result = t2.getByte();
	// }

	public SmgpDeliverResp(int seq_id, String msg_id, int result)
	{
		// AppendTools tool = new AppendTools(20);
		//
		// tool.appendString(msg_id, 10);
		// tool.appendInt(result);
		//
		// body = tool.getOutBytes();
		//
		// head = new SmgpMsgHead(body.length, Common.SMGP_DELIVER_RESP,
		// seq_id);
		AppendUtils tool = new AppendUtils(20);

		tool.appendString(msg_id, 10);
		tool.appendInt(result);

		body = tool.getOutBytes();
		head = new SmgpMsgHead(body.length, Common.SMGP_DELIVER_RESP, seq_id);
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