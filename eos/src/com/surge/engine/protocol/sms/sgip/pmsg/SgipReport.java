package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.CharacterCodingException;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

public class SgipReport extends SgipSendBase
{
	private static final long serialVersionUID = -448698893940806210L;

	private static final Logger logger = Logger.getLogger(SgipReport.class);

	private int submitSeqNodeId;

	private int submitSeqDateTime;

	private int submitSeqSn;

	private long submitSequenceId;

	private int reportType;

	private String userNumber;

	private int state;

	private int errorCode;

	private String reserve;

	private String messageId;

	public SgipReport(IoBuffer buffer)
	{
		// this.submitSequenceId = SgipTools.Bytes8ToLong(new byte[] {
		// bodybytes[4], bodybytes[5], bodybytes[6], bodybytes[7],
		// bodybytes[8], bodybytes[9], bodybytes[10], bodybytes[11] });
		// Tools2 tool = new Tools2(bodybytes, 0);
		this.head = new SgipMsgHead(buffer);
		this.submitSeqNodeId = buffer.getInt();
		// this.submitSeqNodeId = tool.getInt();
		this.submitSeqDateTime = buffer.getInt();
		// this.submitSeqDateTime = tool.getInt();
		this.submitSeqSn = buffer.getInt();
		// this.submitSeqSn = tool.getInt();
		this.reportType = buffer.get();
		// this.reportType = tool.getByte();
		try
		{
			this.userNumber = buffer.getString(21, decoder);
			// this.userNumber = tool.getString(21);
			if (this.userNumber.startsWith("+86"))
				this.userNumber = this.userNumber.substring(3);
			else if (this.userNumber.startsWith("86"))
				this.userNumber = this.userNumber.substring(2);
			this.userNumber = this.userNumber.trim();

			this.state = buffer.get();
			// this.state = tool.getByte();
			this.errorCode = buffer.get();
			// this.errorCode = tool.getByte();
			if (this.head.getTotalLength() > 56)
			{
				this.reserve = buffer.getString(8, decoder);
			}
			if (this.head.getTotalLength() == 85)
			{
				String spnumber = buffer.getString(21, decoder);
			}

			messageId = String.valueOf(submitSeqDateTime) + String.valueOf(submitSeqSn);
		} catch (CharacterCodingException e)
		{
			logger.error("SgipReport°ü½âÎö³ö´í!", e);
		}

	}

	public int getErrorCode()
	{
		return this.errorCode;
	}

	public int getReportType()
	{
		return this.reportType;
	}

	public String getReserve()
	{
		return this.reserve;
	}

	public int getState()
	{
		return this.state;
	}

	public String getUserNumber()
	{
		return this.userNumber;
	}

	public int getSubmitSeqDateTime()
	{
		return this.submitSeqDateTime;
	}

	public int getSubmitSeqNodeId()
	{
		return this.submitSeqNodeId;
	}

	public int getSubmitSeqSn()
	{
		return this.submitSeqSn;
	}

	public long getSubmitSequenceId()
	{
		return this.submitSequenceId;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append(" SgipReport:");
		sb.append(" msg_id:");
		sb.append(messageId);
		sb.append(" mobile:");
		sb.append(userNumber);
		sb.append(" state:");
		sb.append(state);
		sb.append(" errorCode:");
		sb.append(errorCode);
		return sb.toString();
	}

	public String getMessageId()
	{
		return messageId;
	}

	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}
}