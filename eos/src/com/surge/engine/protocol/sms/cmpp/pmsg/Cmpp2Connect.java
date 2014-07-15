
package com.surge.engine.protocol.sms.cmpp.pmsg;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.Tools;

public class Cmpp2Connect extends Cmpp2SendObject
{

	private String source_addr;

	private byte[] md5sp;

	private byte version;

	private int timestamp;

	private byte result = 0;

	public Cmpp2Connect(String source_addr, String pwd, byte version)
			throws NoSuchAlgorithmException
	{

		long timestamp = TimeUtil.getDateTime();
		timestamp %= 10000000000L;
		String datetime = "" + timestamp;
		if (datetime.length() < 10)
			datetime = "0" + datetime;

		AppendUtils appendUtils = new AppendUtils(100);
		appendUtils.appendString(source_addr);
		appendUtils.appendString("", 9);
		appendUtils.appendString(pwd);
		appendUtils.appendString(datetime);

		md5sp = Tools.md5Encode(appendUtils.getOutBytes());

		appendUtils.reset();
		appendUtils.appendString(source_addr, 6);
		appendUtils.appendBytes(md5sp, 16);
		appendUtils.appendByte(version);
		appendUtils.appendInt((int) timestamp);

		body = appendUtils.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_CONNECT, Tools
				.getSeqId());
	}

	public Cmpp2Connect(byte[] bys)
	{

		SubtractTools t2 = new SubtractTools(bys, 0);
		source_addr = t2.getString(6).trim();
		md5sp = t2.getBytes(16);
		version = t2.getByte();
		timestamp = t2.getInt();
	}

	public byte getResult(byte[] pwd) throws NoSuchAlgorithmException
	{

		// md5验证
		String tt = "" + timestamp;
		if (tt.length() < 10)
		{
			tt = "0" + tt;
		}
		// byte[] pwd = CmppServer.getCorpPwd(source_addr);
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(source_addr.getBytes());
		md5.update(new byte[9]);
		if (pwd != null)
		{
			md5.update(pwd);
		}
		md5.update(tt.getBytes());
		byte[] md5out = md5.digest();

		for (int i = 0; i < 16; i++)
		{
			if (md5sp[i] != md5out[i])
			{
				result = (byte) 3;
				break;
			}
		}

		return result;
	}

	public String getSourceAddr()
	{

		return source_addr;
	}

	public String toString()
	{

		StringBuilder sb = new StringBuilder(100);
		sb.append("source_addr:");
		sb.append(source_addr);
		sb.append(" md5:");
		sb.append(new String(md5sp));
		sb.append(" version:");
		sb.append(version);
		sb.append(" timestamp:");
		sb.append(timestamp);
		sb.append(" result:");
		sb.append(result);

		return sb.toString();
	}

}
