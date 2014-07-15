

package com.surge.engine.protocol.sms.smgp.pmsg;
import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.engine.util.SubtractTools;

public class SmgpConnectResp extends SmgpSendObject
{
	private static final long serialVersionUID = 1094668661541742995L;
	private int status = -1;
	private String auth_ismg;
	private byte version;

	public SmgpConnectResp(byte result, int sequence_id)
	{
//		AppendTools tool = new AppendTools(100);
//
//		tool.appendByte(result);
//		tool.appendString("AuthenticatorISMG", 16);
//		tool.appendByte((byte) 2);
//
//		body = tool.getOutBytes();
//
//		head = new SmgpMsgHead(body.length, Common.SMGP_CONNECT_RESP, sequence_id);
	}

	public SmgpConnectResp(IoBuffer bys)
	{
		this.head=new SmgpMsgHead(bys);
		status = bys.getInt();
		try
		{
			auth_ismg = bys.getString(16,decoder);
			version = bys.get();
		} catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
	
	}
	public SmgpConnectResp(byte[] bys)
	{
		
		SubtractTools tool = new SubtractTools(bys, 0);
		status = tool.getInt();
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
