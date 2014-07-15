/**
 * Title:涓浗绉诲姩CmppSubmitResp鎺ュ彛
 * Description:
 * Copyright:    Copyright (c) 2003-2004
 * Company: seasee
 * @author huzi
 * @version 1.0, 07/06/05
 * @since JDK1.5
 */

package com.surge.engine.protocol.sms.cmpp.pmsg;

import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;

public class Cmpp2SubmitResp extends Cmpp2SendObject
{

	private static final long serialVersionUID = 9006406289043174055L;

	/**
	 * 信息标识
	 */
	private long msg_id;

	/**
	 * 错误代码 只有0表示成功
	 */
	private byte result;

	/**
	 * 详细返回信息，非网关返回
	 */
	private String desc = null;

	private int seq_id;

	public Cmpp2SubmitResp()
	{

	}

	public Cmpp2SubmitResp(int sequence_id, long msg_id, byte result)
	{

		AppendUtils tool = new AppendUtils(100);

		tool.appendLong(msg_id);
		tool.appendByte(result);

		body = tool.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_SUBMIT_RESP, sequence_id);
	}

	public Cmpp2SubmitResp(byte[] bys, int seq_id)
	{

		SubtractTools tool = new SubtractTools(bys, 0);

		msg_id = tool.getLong();
		result = tool.getByte();
		this.seq_id = seq_id;
		desc = SmsTools.getCmppSubmitRespResult(result);
	}

	// public String getDetailResult()
	// {
	// return detail_result;
	// }

	public long getMsgId()
	{

		return msg_id;
	}

	public void setResult(byte result)
	{

		this.result = result;
	}

	public byte getResult()
	{

		return result;
	}
	public String getSeqId()
	{

		return String.valueOf(seq_id);
	}

	public String toString()
	{

		StringBuilder sb = new StringBuilder(100);

		sb.append(" CmppSubmitResp :");
		sb.append(" seqId:").append(seq_id);
		sb.append(" msg_id:").append(msg_id);
		sb.append(" result:").append(result);
		sb.append(" detail:").append(desc);
		return sb.toString();
	}

	public String getDetail_result()
	{

		return desc;
	}

	public void setDetail_result(String detailResult)
	{

		desc = detailResult;
	}
}
