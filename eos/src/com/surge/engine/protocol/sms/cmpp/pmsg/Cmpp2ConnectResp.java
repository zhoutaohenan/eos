// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;

import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;

//public class CmppConnectResp {


public class Cmpp2ConnectResp extends Cmpp2SendObject
{
	private byte status = -1;
	private String auth_ismg;
	private byte version;

	public Cmpp2ConnectResp(byte result, int sequence_id)
	{
		AppendUtils tool = new AppendUtils(100);

		tool.appendByte(result);
		tool.appendString("AuthenticatorISMG", 16);
		tool.appendByte((byte) 2);

		body = tool.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_CONNECT_RESP, sequence_id);
	}

	public Cmpp2ConnectResp(byte[] bys)
	{
		SubtractTools tool = new SubtractTools(bys, 0);

		status = tool.getByte();
		auth_ismg = tool.getString(16);
		version = tool.getByte();

	}

	public int getStatus()
	{
		return status;
	}

	public String getAuthIsmg()
	{
		return auth_ismg;
	}

	public byte getVersion()
	{
		return version;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append("status:");
		sb.append(status);
		sb.append(" auth_ismg:");
		sb.append(auth_ismg);
		sb.append(" version:");
		sb.append(version);
		return sb.toString();
	}
}
