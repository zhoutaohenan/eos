package com.surge.engine.protocol.sms.cmpp.pmsg;

import java.io.UnsupportedEncodingException;

import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;
import com.surge.engine.util.Tools;

/**
 * Title:中国移动Deliver接口 Description: Copyright: Copyright (c) 2003-2004 Company:
 * seasee
 * 
 * @author huzi
 * @version 1.0, 07/06/05
 * @since JDK1.5
 */

public class Cmpp2Deliver extends Cmpp2SendObject
{

	private static final long serialVersionUID = -3452122137731734770L;

	private int ismg_id;

	/** 信息标识 */
	private long msg_id;

	/**
	 * 目的号码 SP的服务代码，一般4--6位，或者是前缀为服务代码的长号码； 该号码是手机用户短消息的被叫号码
	 */
	private String dest_id;

	/** 业务类型，是数字、字母和符号的组合 */
	private String service_id;

	/** GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9 */
	private byte tp_pid;

	/** GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23，仅使用1位，右对齐 */
	private byte tp_udhi;

	/** 信息格式 0：ASCII串 3：短信写卡操作 4：二进制信息 8：UCS2编码 15：含GB汉字 */
	private byte msg_fmt;

	/** 源终端MSISDN号码(状态报告时填为CMPP_SUBMIT消息的目的终端号码) */
	private String src_id;

	/** 是否为状态报告 0：非状态报告 1：状态报告 */
	private byte registered_delivery;

	/** 消息内容(二进制) */
	private byte[] msg_content = null;

	/** 保留项 */
	private String reserved;

	// 总条数

	private int totalCout = 0;

	// 当前条数
	private int currCout = 0;

	// 当前时间
	private long currTime;

	/** 消息内容(字符串,由msg_content得到) */
	private String content = "";

	private int seq_id;
	
	private String [] longContent;

	public Cmpp2Deliver()
	{

	}

	public Cmpp2Deliver(String src_id, String dest_id, String content)
	{

		AppendUtils t1 = new AppendUtils(100);

		t1.appendLong(Tools.getMsgId());
		t1.appendString(dest_id, 21);
		t1.appendString("", 10);
		t1.appendByte((byte) 0);
		t1.appendByte((byte) 0);
		t1.appendByte((byte) 15);
		t1.appendString(src_id, 21);
		t1.appendByte((byte) 0);
		try
		{
			byte[] bys = content.getBytes("GBK");
			t1.appendByte((byte) bys.length);
			t1.appendBytes(bys);
		}
		catch (UnsupportedEncodingException ex)
		{
		}
		t1.appendLong(0);

		body = t1.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_DELIVER, Tools.getSeqId());
	}

	public Cmpp2Deliver(byte[] bys, int seq_id)
	{

		SubtractTools tool = new SubtractTools(bys, 0);
		this.seq_id = seq_id;
		msg_id = tool.getLong();
		dest_id = tool.getTrimString(21);
		service_id = tool.getTrimString(10);
		tp_pid = tool.getByte();
		tp_udhi = tool.getByte();
		msg_fmt = tool.getByte();

		src_id = tool.getTrimString(21);
		if (src_id.startsWith("+86"))
		{
			src_id = src_id.substring(3);
		}
		else if (src_id.startsWith("86"))
		{
			src_id = src_id.substring(2);
		}

		registered_delivery = tool.getByte();
		int msg_len = tool.getByte();
		if (msg_len < 0)
			msg_len += 256;
		msg_content = tool.getBytes(msg_len);
		reserved = tool.getString(8);

		src_id = Tools.trimMobile(src_id);

		// 数据前4个字节为5，0，3; 长度为6个字节 西门子不支持
		if (msg_content != null && msg_content.length >= 3)
		{
			if (msg_content[0] == 5 && msg_content[1] == 0 && msg_content[2] == 3)
			{
				totalCout = msg_content[4]; // 总条数
				currCout = msg_content[5]; // 当前数
			}
		}
		// 是MO短信
		if (registered_delivery == 0)
		{
			content = SmsTools.getContent(msg_content, msg_fmt);
		}
	}

	/**
	 * 确认是否是状态报告
	 * 
	 * 
	 * @return true:状态报告 false:非状态报告
	 */
	public boolean isReport()
	{

		return (registered_delivery == 1);
	}

	/**
	 * 返回状态报告
	 * 
	 * @return 如果是状态报告则返回CmppReport，否则返回null
	 */
	public Cmpp2Report getReport()
	{

		if (registered_delivery != 1 || msg_content == null)
			return null;
		else
		{
			Cmpp2Report StatRpt = new Cmpp2Report(dest_id, msg_content);
			return StatRpt;
		}
	}

	public String getContent()
	{

		return content;
	}

	public String getDestId()
	{

		return dest_id;
	}

	public long getMsgId()
	{

		return msg_id;
	}

	public String getSrcTerminalId()
	{

		return src_id;
	}

	public byte getMsgFmt()
	{

		return msg_fmt;
	}

	public String getServiceId()
	{

		return service_id;
	}

	public int getCurrCout()
	{

		return currCout;
	}

	public int addCurrCout()
	{

		return ++currCout;
	}

	public int getTotalCout()
	{

		return totalCout;
	}

	public void setContent(String content)
	{

		this.content = content;
	}

	public long getCurrTime()
	{

		return currTime;
	}

	public void setCurrTime(long currTime)
	{

		this.currTime = currTime;
	}

	public void setTotalCout(int totalCout)
	{

		this.totalCout = totalCout;
	}

	public int getIsmgId()
	{

		return ismg_id;
	}

	public void setIsmgId(int ismg_id)
	{

		this.ismg_id = ismg_id;
	}
	public String getSeqId()
	{

		return String.valueOf(this.seq_id);
	}
	public String toString()
	{

		StringBuilder sb = new StringBuilder(200);
		sb.append(" CmppDeliver :");
		sb.append(" msg_id:").append(msg_id);
		sb.append(" dest:").append(dest_id);
		sb.append(" src:").append(src_id);
		sb.append(" content:").append(content);

		return sb.toString();
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
