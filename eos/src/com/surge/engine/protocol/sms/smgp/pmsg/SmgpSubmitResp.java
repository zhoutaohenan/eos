package com.surge.engine.protocol.sms.smgp.pmsg;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.util.SubtractTools;
import com.surge.engine.util.Tools;

public class SmgpSubmitResp extends SmgpSendObject
{
	private static final long serialVersionUID = 3720895485074645704L;

	private String msg_id;

	private int result;

	private String detail_result = null;

	public SmgpSubmitResp(byte[] bys,int seqID)
	{
		this.head=new SmgpMsgHead();
		head.setSeqID(seqID);
		SubtractTools tool = new SubtractTools(bys, 0);
		msg_id=Tools.bcd2Str(tool.getBytes(10));
		result=tool.getInt();
		detail_result = SmsTools.getSmgpSubmitRespResult(result);
	}

	public SmgpSubmitResp(IoBuffer bys)
	{
		try
		{
			this.head = new SmgpMsgHead(bys);
			msg_id = Tools.bcd2Str(bys.getString(10, decoder).getBytes());
			result = bys.getInt();
			detail_result = SmsTools.getSmgpSubmitRespResult(result);
		} catch (Exception e)
		{

		}
	}
	public String getDetailResult()
	{
		return detail_result;
	}

	public String getMsgId()
	{
		return msg_id;
	}

	public int getResult()
	{
		return result;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append(" SubmitResp[");
		sb.append(" msg_id:");
		sb.append(msg_id);
		sb.append(" result:");
		sb.append(result);
		sb.append(" detail:");
		sb.append(detail_result);
		sb.append("]");
		return sb.toString();
	}
	public String getDetail_result()
	{
		return detail_result;
	}
	public void setDetail_result(String detailResult)
	{
		detail_result = detailResult;
	}

}