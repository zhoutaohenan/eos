/*
 * Title: mas20
 * @author Administrator
 * Created on 2008-5-19 
 */

package com.surge.engine.protocol.sms.smgp.pmsg;

import com.surge.engine.protocol.sms.cmpp.pmsg.Common;
import com.surge.engine.util.AppendUtils;

public class SmgpSubmit extends SmgpSendObject
{

	private static final long serialVersionUID = 4390449817086597652L;

	private long msg_id;

	private byte pk_total;

	private byte pk_number;

	private byte registered_delivery;

	private String service_id;

	private byte fee_user_type;

	private String fee_terminal_id;

	private byte tp_pid;

	private byte tp_udhi;

	private byte msg_fmt;

	private String sp_id;

	private String fee_type;

	private String fee_code;

	private String src_id;

	private String dest;

	private int content_length;

	private String smsContent;

	private byte[] contentbytes;

	private int rptIndex;

	private int count;

	private long reSendCurrTime;

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public long getReSendCurrTime()
	{
		return reSendCurrTime;
	}

	public void setReSendCurrTime(long reSendCurrTime)
	{
		this.reSendCurrTime = reSendCurrTime;
	}

	public SmgpSubmit(int seq_id, byte pk_total, byte pk_number, byte registered_delivery,
			String service_id, byte fee_user_type, String fee_terminal_id, byte tp_pid,
			byte tp_udhi, byte msg_fmt, String sp_id, String fee_type, String fee_code,
			String src_id, String dest, byte[] msg_content, int offset, int len, int msg_type)
	{

		this.rptIndex = pk_number;

		// AppendTools tool = new AppendTools(200 + 21 + msg_content.length);
		// //* list.size() + msg_content.length);

		AppendUtils tool = new AppendUtils(200 + 21 + msg_content.length);
		tool.appendByte((byte) msg_type);
		tool.appendByte(registered_delivery);
		tool.appendByte((byte) 1); // msg_level
		tool.appendString(service_id, 10);
		tool.appendString(fee_type, 2); // fee_type
		tool.appendString("0", 6); // fee_code
		tool.appendString("000", 6); // fixedfee
		tool.appendByte(msg_fmt);
		tool.appendString("", 17); // valid_time
		tool.appendString("", 17); // at_time
		tool.appendString(src_id, 21);
		tool.appendString(fee_terminal_id.trim(), 21);
		tool.appendByte((byte) 1);
		if (dest.startsWith("106"))
			dest = dest.substring(3);
		tool.appendString(dest.trim(), 21);
		tool.appendByte((byte) len);
		tool.appendBytes(msg_content, len);
		tool.appendString("", 8);
		// tool.appendString("",379-tool.size());
		if(tp_udhi==1)
		{   
			byte udhi[]= new byte[5];
			TypeConvert.int2byte2(TlvId.TP_udhi, udhi, 0);
			TypeConvert.int2byte2(1, udhi, 2);
			TypeConvert.int2byte3(1, udhi, 4);
			tool.appendBytes(udhi);
			
			byte pkto[] =new byte[5];
			TypeConvert.int2byte2(TlvId.PkTotal, pkto, 0);
			TypeConvert.int2byte2(1, pkto, 2);
			TypeConvert.int2byte3(pk_total, pkto, 4);
			tool.appendBytes(pkto);
			
			byte pknu[] =new byte[5];
			TypeConvert.int2byte2(TlvId.PkNumber, pknu, 0);
			TypeConvert.int2byte2(1, pknu, 2);
			TypeConvert.int2byte3(pk_number, pknu, 4);
			tool.appendBytes(pknu);
		}

		body = tool.getOutBytes();
		head = new SmgpMsgHead(body.length, Common.SMGP_SUBMIT, seq_id);// body.length
	}

	public String getSrcId()
	{
		return src_id;
	}

	public String getDest()
	{
		return dest;
	}

	public byte getRegisteredDelivery()
	{
		return registered_delivery;
	}

	public String getServiceId()
	{
		return service_id;
	}

	public int getFeeUserType()
	{
		return fee_user_type;
	}

	public String getFeeTerminalId()
	{
		return fee_terminal_id;
	}

	public int getFeeType()
	{
		int ret = 2;
		ret = Integer.parseInt(fee_type);
		return ret;
	}

	public int getFeeCode()
	{
		int ret = Integer.parseInt(fee_code);
		return ret;
	}

	public byte getTpPid()
	{
		return tp_pid;
	}

	public byte getTpUdhi()
	{
		return tp_udhi;
	}

	public byte getMsgFmt()
	{
		return msg_fmt;
	}

	// public long getMsgId() {
	// return msg_id;
	// }

	public byte getPkTotal()
	{
		return pk_total;
	}

	public byte getPkNumber()
	{
		return pk_number;
	}

	public String getSpId()
	{
		return sp_id;
	}

	public int getContentLength()
	{
		return content_length;
	}

	public byte[] getContentBytes()
	{
		return contentbytes;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(1024);
		sb.append("msg_id:");
		sb.append(msg_id);
		sb.append(" pk_total:");
		sb.append(pk_total);
		sb.append(" pk_number:");
		sb.append(pk_number);
		sb.append(" registered_delivery:");
		sb.append(registered_delivery);
		sb.append(" service_id:");
		sb.append(service_id);
		sb.append(" fee_user_type:");
		sb.append(fee_user_type);
		sb.append(" fee_terminal_id:");
		sb.append(fee_terminal_id);
		sb.append(" tp_pid:");
		sb.append(tp_pid);
		sb.append(" tp_udhi:");
		sb.append(tp_udhi);
		sb.append(" msg_fmt:");
		sb.append(msg_fmt);
		sb.append(" sp_id:");
		sb.append(sp_id);
		sb.append(" fee_type:");
		sb.append(fee_type);
		sb.append(" fee_code:");
		sb.append(fee_code);
		sb.append(" src_id:");
		sb.append(src_id);
		sb.append(" dest:");
		sb.append(dest);
		sb.append(" msg_len:");
		sb.append(content_length);
		sb.append(" msg_content:");
		sb.append(smsContent);

		return sb.toString();
	}
}