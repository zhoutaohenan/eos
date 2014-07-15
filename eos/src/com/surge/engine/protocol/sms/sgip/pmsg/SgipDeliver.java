package com.surge.engine.protocol.sms.sgip.pmsg;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.surge.engine.util.DataTools;

public class SgipDeliver extends SgipSendBase
{
	private static final long serialVersionUID = -3690740747730343822L;

	private static final Logger logger = Logger.getLogger(SgipDeliver.class);

	private String userNumber;

	private String spNumber;

	private int tP_pid;

	private int tP_udhi;

	private int messageCoding;

	private int messageLength;

	private String messageContent;

	private byte[] messageByte;

	private String reserve;

	private int totalCout = 0;

	private int currCout = 0;

	private long currTime;

	private String messageId;

	private String[] longContent;

	public SgipDeliver(IoBuffer buffer)
	{
		// Tools2 tool = new Tools2(bodybytes, 0);
		try
		{
			this.head = new SgipMsgHead(buffer);
			this.userNumber = buffer.getString(21, decoder);
			// this.UserNumber = tool.getString(21);

			if (this.userNumber.startsWith("+86"))
			{
				this.userNumber = this.userNumber.substring(3);
			} else if (this.userNumber.startsWith("86"))
				this.userNumber = this.userNumber.substring(2);

			this.userNumber = this.userNumber.trim();

			this.spNumber = buffer.getString(21, decoder);
			// this.SpNumber = tool.getString(21);
			this.spNumber = this.spNumber.trim();
			this.tP_pid = buffer.get();
			// this.TP_pid = tool.getByte();
			this.tP_udhi = buffer.get();
			// this.TP_udhi = tool.getByte();
			this.messageCoding = buffer.get();
			// this.MessageCoding = tool.getByte();
			this.messageLength = buffer.getInt();
			// this.MessageLength = tool.getInt();
			if (this.messageLength < 0)
				this.messageLength += 256;
			this.messageByte = new byte[this.messageLength];
			this.getContent(buffer, this.messageByte);
			// buffer.get(this.messageByte, buffer.position(),
			// this.messageLength);
			// this.MessageByte = tool.getBytes(this.MessageLength);
			int smsLength = 69 + this.messageLength;
			if (this.head.getTotalLength() > smsLength)
			{
				this.reserve = buffer.getString(8, decoder);
			}
			// this.Reserve = tool.getString(8);

			if (this.messageByte == null)
			{
				this.messageContent = "";
			} else
			{
				byte[] buff = (byte[]) null;
				if ((this.messageByte.length > 6) && (this.messageByte[0] == 5)
						&& (this.messageByte[1] == 0) && (this.messageByte[2] == 3))
				{
					this.totalCout = this.messageByte[4];
					this.currCout = this.messageByte[5];
					buff = new byte[this.messageLength - 6];
					System.arraycopy(this.messageByte, 6, buff, 0, this.messageLength - 6);
					this.messageByte = buff;
				}

				if ((this.messageCoding == 8) || (this.messageCoding == 25))
				{
					this.messageContent = new String(this.messageByte, "UTF-16BE");
				}

				else if (this.messageCoding == 15)
				{
					this.messageContent = new String(this.messageByte, "gbk");

				} else if (this.messageCoding == 4)
				{
					this.messageContent = DataTools.binary2Hex(this.messageByte);

				} else
				{
					this.messageContent = new String(this.messageByte);
				}
				messageId = String.valueOf(head.getDatetime()) + String.valueOf(head.getseqID());
			}
//			logger.info("ÄÚÈÝ:" + messageContent);
		} catch (Exception e)
		{
			logger.error("½âÎöSgipDeliver°üÊ§°Ü", e);
		}

	}

	public byte[] getMessageByte()
	{
		return this.messageByte;
	}

	public int getMessageCoding()
	{
		return this.messageCoding;
	}

	public String getMessageContent()
	{
		return this.messageContent;
	}

	public int getMessageLength()
	{
		return this.messageLength;
	}

	public String getSpNumber()
	{
		return this.spNumber;
	}

	public int getTP_pid()
	{
		return this.tP_pid;
	}

	public int getTP_udhi()
	{
		return this.tP_udhi;
	}

	public String getUserNumber()
	{
		return this.userNumber;
	}

	public String getReserve()
	{
		return this.reserve;
	}

	public int getCurrCout()
	{
		return this.currCout;
	}

	public long getCurrTime()
	{
		return this.currTime;
	}

	public int getTotalCout()
	{
		return this.totalCout;
	}

	public void setCurrCout(int currCout)
	{
		this.currCout = currCout;
	}

	public void setCurrTime(long currTime)
	{
		this.currTime = currTime;
	}

	public void addCurrCout()
	{
		this.currCout += 1;
	}

	public void setMessageContent(String messageContent)
	{
		this.messageContent = messageContent;
	}

	public void setTotalCout(int totalCout)
	{
		this.totalCout = totalCout;
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
		StringBuilder sb = new StringBuilder(200);
		sb.append(" SgipDeliver :");
		sb.append(" msg_id:");
		sb.append(messageId);
		sb.append(" mobile:");
		sb.append(userNumber);
		sb.append(" content:");
		sb.append(messageContent);
		return sb.toString();
	}

	private void getContent(IoBuffer buffer, byte[] conents)
	{
		for (int i = 0; i < conents.length; i++)
		{
			conents[i] = buffer.get();
		}
	}

	public String[] getLongContent()
	{
		return longContent;
	}

	public void setLongContent(String[] longContent)
	{
		this.longContent = longContent;
	}
}