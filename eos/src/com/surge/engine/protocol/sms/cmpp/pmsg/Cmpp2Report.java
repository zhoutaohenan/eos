
package com.surge.engine.protocol.sms.cmpp.pmsg;

import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.Tools;

/**
 * Title:中国移动Report接口(状态包),从CMPP_MO.getDeliver().getReport()获得 Description:
 * Copyright: Copyright (c) 2003-2004 Company: seasee
 * 
 * @author huzi
 * @version 1.0, 07/06/05
 * @since JDK1.5
 */

public class Cmpp2Report extends Cmpp2SendObject
{

	private static final long serialVersionUID = -5902480258671927048L;

	/**
	 * 信息标识
	 */
	private long msg_id;

	/**
	 * 发送短信的应答结果，含义与SMPP协议要求中stat字段定义相同，
	 * 
	 * 详见表一。SP根据该字段确定CMPP_SUBMIT消息的处理状态
	 */
	private String stat;

	/**
	 * YYMMDDHHMM(YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59)
	 */
	private String submit_time;

	/**
	 * YYMMDDHHMM
	 */
	private String done_time;

	/**
	 * 源终端MSISDN号码(状态报告时填为CMPP_SUBMIT消息的目的终端号码)
	 */
	private String src_id;

	/**
	 * 目的终端MSISDN号码(SP发送CMPP_SUBMIT消息的目标终端)
	 */
	private String dest_id;

	/**
	 * 取自SMSC发送状态报告的消息体中的消息标识
	 */
	private int smsc_sequence;

	/**
	 * 根据stat生成的结果，非网关返回
	 */
	private int result;

	/**
	 * 详细返回信息，非网关返回
	 */
	private String desc = null;

	public Cmpp2Report()
	{

	}

	public Cmpp2Report(String src_id, byte[] bys)
	{

		this.src_id = src_id;

		SubtractTools tool = new SubtractTools(bys, 0);

		msg_id = tool.getLong();
		stat = tool.getTrimString(7);
		submit_time = tool.getTrimString(10);
		done_time = tool.getTrimString(10);
		dest_id = tool.getTrimString(21);
		smsc_sequence = tool.getInt();

		dest_id = Tools.trimMobile(dest_id);
		result = SmsTools.getCmppReportResult(stat);
		desc = SmsTools.getCmppReportDetailResult(stat);
	}

	public Cmpp2Report(long msg_id, String src_id, String dest_id, int result,
			String detail)
	{

		AppendUtils t1 = new AppendUtils(100);

		t1.appendLong(msg_id);
		t1.appendString(src_id, 21);
		t1.appendString("", 10);
		t1.appendByte((byte) 0);
		t1.appendByte((byte) 0);
		t1.appendByte((byte) 15);
		t1.appendString(dest_id, 21);
		t1.appendByte((byte) 1);

		AppendUtils tmp = new AppendUtils(100);
		tmp.appendLong(msg_id);
		tmp.appendString(detail, 7);
		tmp.appendString(TimeUtil.getDate() + "" + TimeUtil.getTime(), 10);
		tmp.appendString(TimeUtil.getDate() + "" + TimeUtil.getTime(), 10);
		tmp.appendString(dest_id, 21);
		tmp.appendInt(2004);
		byte[] bys = tmp.getOutBytes();
		t1.appendByte((byte) bys.length);
		t1.appendBytes(bys);

		t1.appendLong(0);

		body = t1.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_DELIVER, Tools
				.getSeqId());
	}

	public String getDestId()
	{

		return dest_id;
	}

	public String getDoneTime()
	{

		return done_time;
	}

	public long getMsgId()
	{

		return msg_id;
	}

	public int getResult()
	{

		return result;
	}

	public String getSrcId()
	{

		return src_id;
	}

	public String getStat()
	{

		return stat;
	}

	public String getSubmitTime()
	{

		return submit_time;
	}

	public String toString()
	{

		StringBuilder sb = new StringBuilder(100);

		sb.append(" CmppReport :");
		sb.append(" msg_id:").append(msg_id);
		sb.append(" dest:").append(dest_id);
		sb.append(" result:").append(result);
		sb.append(" stat:").append(stat);
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
