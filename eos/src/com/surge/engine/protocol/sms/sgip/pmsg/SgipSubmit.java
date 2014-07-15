package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.CharacterCodingException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.surge.engine.protocol.sms.common.SmsProtocolConstant;

public class SgipSubmit extends SgipSendBase
{
	private static final long serialVersionUID = -760388540003983145L;

	private static final Logger logger = Logger.getLogger(SgipSubmit.class);

	/** SP的接入号码 **/
	private String spNumber;

	/** 付费号码 手机号码前加“86”国别标志； **/
	private String chargeNumber;

	/** 接收短消息的手机数量，取值范围1至100 **/
	private int userCount;

	/** 接收该短消息的手机号 **/
	private String[] userNumber;

	private String dest;

	private String corpId;

	private String serviceType;

	private int feeType;

	private String feeValue;

	private String givenValue;

	private int agentFlag;

	private int morelatetoMTFlag;

	private int priority;

	private String expireTime;

	private String scheduleTime;

	private int reportFlag;

	private int tp_pid;

	private int tp_udhi;

	private int messageCoding;

	private int messageType;

	private int messageLength;

	private byte[] binContent;

	private int contentLength;

	private String reserve;

	private long imId;

	private int fee_user_type;

	private String fee_terminal_id;

	private String submit_time;

	private String corp_id;

	private int content_type;

	private int seq;

	private boolean isRpt;

	private Date sendTime;

	private int datetime;

	private long nodeId;

	private String sendDate;

	private int rptIndex;

	private int send_user_id;

	private String busi_code;

	public String getBusi_code()
	{
		return this.busi_code;
	}

	public void setBusi_code(String busi_code)
	{
		this.busi_code = busi_code;
	}

	public int getRptIndex()
	{
		return this.rptIndex;
	}

	public void setRptIndex(int rptIndex)
	{
		this.rptIndex = rptIndex;
	}

	public int getSend_user_id()
	{
		return this.send_user_id;
	}

	public void setSend_user_id(int send_user_id)
	{
		this.send_user_id = send_user_id;
	}

	public SgipSubmit(long nodeID, String spNumber, String chargeNumber, String userNumber,
			String corpId, String serviceType, byte feeType, String feeValue, String givenValue,
			byte agentFlag, byte morelatetoMTFlag, byte priority, String expireTime,
			String scheduleTime, byte reportFlag, byte tp_pid, byte tp_udhi, byte messageCoding,
			byte messageType, byte[] msgContent)
	{
		this.isRpt = (reportFlag != 2);
		this.spNumber = spNumber;
		this.chargeNumber = chargeNumber;
		this.userNumber = userNumber.split(",");
		this.dest = userNumber;
		this.userCount = this.userNumber.length;
		if (this.userCount > 100)
		{
			logger.error("Too many users!");
		}
		this.corpId = corpId;
		this.serviceType = serviceType;
		this.feeType = feeType;
		this.feeValue = feeValue;
		this.givenValue = givenValue;
		this.agentFlag = agentFlag;
		this.morelatetoMTFlag = morelatetoMTFlag;
		this.priority = priority;
		this.expireTime = expireTime;
		this.scheduleTime = scheduleTime;
		this.reportFlag = reportFlag;
		this.tp_pid = tp_pid;
		this.tp_udhi = tp_udhi;
		this.messageCoding = messageCoding;
		this.messageType = messageType;
		this.messageLength=msgContent.length;
		this.reserve = "";

		int bodyLength = 300 + this.userCount * 21 + this.messageLength;
		IoBuffer buffer = IoBuffer.allocate(bodyLength);
		try
		{
			buffer.putString(this.spNumber, 21, encoder);
			// tool.appendString(this.spNumber, 21);
			buffer.putString(this.chargeNumber, 21, encoder);
			// tool.appendString(this.chargeNumber, 21);
			buffer.put((byte) this.userCount);
			// tool.appendByte((byte) this.userCount);

			for (int i = 0; i < this.userNumber.length; ++i)
			{
				String mobile = this.userNumber[i];
				if (!(mobile.startsWith("86")))
				{
					mobile = "86" + mobile;
					this.userNumber[i] = mobile;
				}
				buffer.putString(mobile, 21, encoder);
				// tool.appendString(mobile, 21);
			}
			buffer.putString(this.corpId, 5, encoder);
			// tool.appendString(this.corpId, 5);
			buffer.putString(this.serviceType, 10, encoder);
			buffer.put(feeType);
			buffer.putString(this.feeValue, 6, encoder);
			buffer.putString(this.givenValue, 6, encoder);
			buffer.put(agentFlag);
			buffer.put(morelatetoMTFlag);
			buffer.put((byte)9);
			buffer.putString(this.expireTime, 16, encoder);
			buffer.putString(this.scheduleTime, 16, encoder);
			buffer.put(reportFlag);
			buffer.put(tp_pid);
			buffer.put(tp_udhi);
			buffer.put(messageCoding);
			buffer.put(messageType);
			buffer.putInt(msgContent.length);
			buffer.put(msgContent);
			buffer.putString(this.reserve, 8, encoder);
			this.body = this.getBytesFromBuffer(buffer);
			this.seq = Seq.getGlobalSeq3();
			this.datetime = Seq.getGlobalSeq2();
			this.nodeId = nodeID;
			this.head = new SgipMsgHead(this.body.length, SmsProtocolConstant.SGIP_SUBMIT, nodeID,
					this.datetime, this.seq);
		} catch (CharacterCodingException e)
		{
			logger.error("解析SgipSubmit包错误", e);
		}

	}

	public String getSPNumber()
	{
		return this.spNumber;
	}

	public long getImId()
	{
		return this.imId;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(100);
		sb.append("SgipSubmit imId:").append(this.imId).append(" nodeId:").append(this.nodeId)
				.append(" datetime:").append(this.datetime).append(" seq:").append(this.seq)
				.append(" SPNumber:").append(this.spNumber).append(" ChargeNumber:").append(
						this.chargeNumber).append(" UserNumber:").append(this.userNumber[0])
				.append(" CorpId:").append(this.corpId).append(" ServiceType:").append(
						this.serviceType).append(" morelatetoMTFlag:")
				.append(this.morelatetoMTFlag).append(" reportFlag:").append(this.reportFlag)
				.append(" feeType:").append(this.feeType).append(" GivenValue:").append(
						this.givenValue);
		return sb.toString();
	}

	public String getFee_terminal_id()
	{
		return this.fee_terminal_id;
	}

	public int getFee_user_type()
	{
		return this.fee_user_type;
	}

	public String getSubmit_time()
	{
		return this.submit_time;
	}

	public void setSubmit_time(String submit_time)
	{
		this.submit_time = submit_time;
	}

	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getCorp_id()
	{
		return this.corp_id;
	}

	public int getContent_type()
	{
		return this.content_type;
	}

	public int getAgentFlag()
	{
		return this.agentFlag;
	}

	public byte[] getBinContent()
	{
		return this.binContent;
	}

	public String getChargeNumber()
	{
		return this.chargeNumber;
	}

	public int getContentLength()
	{
		return this.contentLength;
	}

	public String getCorpId()
	{
		return this.corpId;
	}

	public String getExpireTime()
	{
		return this.expireTime;
	}

	public int getFeeType()
	{
		return this.feeType;
	}

	public String getFeeValue()
	{
		return this.feeValue;
	}

	public String getGivenValue()
	{
		return this.givenValue;
	}

	public int getMessageCoding()
	{
		return this.messageCoding;
	}

	public int getMessageType()
	{
		return this.messageType;
	}

	public int getMessageLength()
	{
		return this.messageLength;
	}

	public int getMorelatetoMTFlag()
	{
		return this.morelatetoMTFlag;
	}

	public int getPriority()
	{
		return this.priority;
	}

	public int getReportFlag()
	{
		return this.reportFlag;
	}

	public String getReserve()
	{
		return this.reserve;
	}

	public String getScheduleTime()
	{
		return this.scheduleTime;
	}

	public String getServiceType()
	{
		return this.serviceType;
	}

	public String getSpNumber()
	{
		return this.spNumber;
	}

	public int getTp_pid()
	{
		return this.tp_pid;
	}

	public int getTp_udhi()
	{
		return this.tp_udhi;
	}

	public int getUserCount()
	{
		return this.userCount;
	}

	public String[] getUserNumber()
	{
		return this.userNumber;
	}

	public Date getSendTime()
	{
		return this.sendTime;
	}

	public String getDest()
	{
		return this.dest;
	}

	public boolean isIsRpt()
	{
		return this.isRpt;
	}

	public String getSendDate()
	{
		return this.sendDate;
	}

	public void setSendDate(String sendDate)
	{
		this.sendDate = sendDate;
	}
}