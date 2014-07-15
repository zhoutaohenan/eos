package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.CharacterCodingException;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.surge.engine.protocol.sms.common.SmsProtocolConstant;

public class SgipReportResp extends SgipSendBase
{
	private static final Logger logger = Logger.getLogger(SgipReportResp.class);

	private static final long serialVersionUID = -4804331290370158882L;

	private int result;

	private String reserve;

	public SgipReportResp(long nodeID, int datetime, int seq, byte result)
	{
		this.result = result;
		this.reserve = "";
		IoBuffer buffer = IoBuffer.allocate(9);
		// Tools1 tool = new Tools1(100);
		buffer.put(result);
		// tool.appendByte(result);
		try
		{
			buffer.putString(this.reserve, 8, encoder);
			// tool.appendString(this.reserve, 8);
			this.body = this.getBytesFromBuffer(buffer);
			this.head = new SgipMsgHead(this.body.length, SmsProtocolConstant.SGIP_REPORT_RESP,
					nodeID, datetime, seq);
		} catch (CharacterCodingException e)
		{
			logger.error("×é½¨SgipReportResp°üÊ§°Ü", e);
		}

	}

	public String getReserve()
	{
		return this.reserve;
	}

	public int getResult()
	{
		return this.result;
	}
}