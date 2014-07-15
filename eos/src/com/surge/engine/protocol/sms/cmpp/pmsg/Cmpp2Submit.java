// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.surge.engine.util.AppendUtils;

public class Cmpp2Submit extends Cmpp2SendObject
{
	private static final long serialVersionUID = 7774782422779271110L;

	private String service_id;

	private String sp_id;

	private String src_id;

	private String dest;

	private int count;// 已重发次数

	private long reSendCurrTime;// 发送到网关的时间

	private int seqId;

	public Cmpp2Submit(int seq_id, int rptIndex, byte pk_total, byte pk_number,
			byte registered_delivery, String service_id, byte fee_user_type,
			String fee_terminal_id, byte tp_pid, byte tp_udhi, byte msg_fmt, String sp_id,
			String fee_type, String fee_code, String src_id, String dest, byte[] msg_content,
			int offset, int len)
	{
		this.service_id = service_id;
		this.sp_id = sp_id;
		this.src_id = src_id;
		this.dest = dest;
		this.seqId = seq_id;
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(dest, ",");
		String tk = null;
		while (st.hasMoreTokens())
		{
			//多个号码时拆分号码
			tk = st.nextToken();
			if (tk.length() > 21)
				continue;
			list.add(tk);
			if (list.size() > 100)
				break;
		}
		//计费类型为无效时由目的号码计费
		if (fee_user_type == 3 && (fee_terminal_id == null || fee_terminal_id.length() == 0))
		{
			fee_terminal_id = dest;
		}
		/*
		 * 这里200并不是很精确，大于原来的实际数，但是完全没有错误，因为后面发送的时候是根据pos发送的
		 */
		AppendUtils tool = new AppendUtils(200 + 21 * list.size() + msg_content.length);
		//信息标识，由SP侧短信网关本身产生，本处填空。
		tool.appendLong(0);
		//相同Msg_Id的信息总条数，从1开始
		tool.appendByte(pk_total);
		//相同Msg_Id的信息序号，从1开始
		tool.appendByte(pk_number);
		//是否要求返回状态确认报告：
		//0：不需要
		//1：需要
		//2：产生SMC话单
		//（该类型短信仅供网关计费使用，不发送给目的终端)
		tool.appendByte(registered_delivery);
		//信息级别
		tool.appendByte((byte) 0);// msg_level
		//业务类型，是数字、字母和符号的组合
		tool.appendString(service_id, 10);
		//计费用户类型字段
		//0：对目的终端MSISDN计费；
		//1：对源终端MSISDN计费；
		//2：对SP计费;
		//3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
		tool.appendByte(fee_user_type);
		//被计费用户的号码（如本字节填空，则表示本字段无效，对谁计费参见Fee_UserType字段，本字段与Fee_UserType字段互斥）
		tool.appendString(fee_terminal_id, 21);
		tool.appendByte(tp_pid);
		tool.appendByte(tp_udhi);
		//信息格式
		//0：ASCII串
		//3：短信写卡操作
		//4：二进制信息
		//8：UCS2编码
		//15：含GB汉字
		tool.appendByte(msg_fmt);
		tool.appendString(sp_id, 6);// msg_src 同sp_id
		tool.appendString(fee_type, 2);// fee_type
		tool.appendString(fee_code, 6);// fee_code
		tool.appendString("", 17);// valid_time
		tool.appendString("", 17);// at_time
		//接收短信的MSISDN号码
		tool.appendString(src_id, 21);
		//接收信息的用户数量(小于100个用户)
		tool.appendByte((byte) list.size());
		for (String mobile : list)
		{
			tool.appendString(mobile.trim(), 21);
		}
		tool.appendByte((byte) len);
		tool.appendBytes(msg_content, offset, len);
		tool.appendString("", 8);

		body = tool.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_SUBMIT, seq_id);
	}

	public Cmpp2Submit()
	{
	}

	public String getSrcId()
	{
		return src_id;
	}

	public String getDest()
	{
		return dest;
	}

	public String getServiceId()
	{
		return service_id;
	}

	public String getSpId()
	{
		return sp_id;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(1024);
		sb.append(" service_id:");
		sb.append(service_id);
		sb.append(" sp_id:");
		sb.append(sp_id);
		sb.append(" fee_type:");
		sb.append(" src_id:");
		sb.append(src_id);
		sb.append(" dest:");
		sb.append(dest);

		return sb.toString();
	}

	public long getReSendCurrTime()
	{
		return reSendCurrTime;
	}

	public void setReSendCurrTime(long reSendCurrTime)
	{
		this.reSendCurrTime = reSendCurrTime;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getService_id()
	{
		return service_id;
	}

	public void setService_id(String service_id)
	{
		this.service_id = service_id;
	}

	public String getSp_id()
	{
		return sp_id;
	}

	public void setSp_id(String sp_id)
	{
		this.sp_id = sp_id;
	}

	public String getSrc_id()
	{
		return src_id;
	}

	public void setSrc_id(String src_id)
	{
		this.src_id = src_id;
	}

	public void setDest(String dest)
	{
		this.dest = dest;
	}

	public void setSeqId(int seqId)
	{
		this.seqId = seqId;
	}
}
