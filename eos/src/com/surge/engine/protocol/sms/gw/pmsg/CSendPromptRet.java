package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server 发送短消 后返回给 Client
 */
public class CSendPromptRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_SENDPROMPT | ((byte) 0x80);

	public String sReqID; // MT 短消息 ID (10 bytes)

	public String sAgent; // 发送短消息使用的 Agent/SMSC ID (6 bytes)

	public byte bStatus = 0x00; // 发送结果 (1 byte)

	public String sMsg = "";

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 */
	public CSendPromptRet(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CSendPromptRet(byte[] b) throws Exception
	{
		super(b);
		if (bCommand != CMD)
		{
			throw new Exception("Invalid command!");
		}
		init();
	}

	protected void init()
	{
		byte[] b = body;
		sReqID = DateConvertTools.b2s(b, 0, 10, (byte) 32);
		sAgent = DateConvertTools.b2s(b, 10, 6, (byte) 32);
		bStatus = b[16];
		try
		{
			sMsg = new String(b, 17, b.length - 17);
		} catch (Exception e)
		{
		}
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		byte[] b = {};
		if (sMsg != null || sMsg.length() > 0)
		{
			b = sMsg.getBytes();
		}
		body = new byte[17 + b.length];
		DateConvertTools.s2b(sReqID, body, 0, 10, (byte) 32);
		DateConvertTools.s2b(sAgent, body, 10, 6, (byte) 32);
		body[16] = bStatus;
		for (int i = 17; i < 17 + b.length; i++)
		{
			body[i] = b[i - 17];
		}
	}

	public String toString()
	{
		return "Status=" + (int) bStatus + ",ReqID=" + sReqID + ",Route:" + sAgent + ",Msg=" + sMsg;
	}

	// {{DECLARE_CONTROLS
	// }}
}
