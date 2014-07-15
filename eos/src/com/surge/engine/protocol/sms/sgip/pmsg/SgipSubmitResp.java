package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.CharacterCodingException;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

public class SgipSubmitResp extends SgipSendBase
{
	private static final Logger logger = Logger.getLogger(SgipSubmitResp.class);

	private static final long serialVersionUID = 7944867401889135110L;

	private String messageId;

	private byte result;

	private String reserve;

	public SgipSubmitResp(IoBuffer buffer)
	{
		this.head = new SgipMsgHead(buffer);
		this.result = buffer.get();
		messageId = String.valueOf(head.getDatetime()) + String.valueOf(head.getseqID());
		if (this.head.getTotalLength() == 29)
		{
			try
			{
				this.reserve = buffer.getString(8, decoder);
			} catch (CharacterCodingException e)
			{
				logger.error("Ω‚ŒˆSgipSubmitResp ß∞‹", e);
			}
		}

	}
	public byte getResult()
	{
		return this.result;
	}

	public String getReserve()
	{
		return this.reserve;
	}
	public String getMessageId()
	{
		return messageId;
	}
	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}
	public String toString()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append(" SgipSubmitResp :");
		sb.append(" seqId:");
		sb.append(head.getseqID());
		sb.append(" msg_id:");
		sb.append(messageId);
		sb.append(" result:");
		sb.append(result);
		return sb.toString();
	}
}