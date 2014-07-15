//-------------------- anny 2003.12.09
package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server 收到 REPORT 消息后，生成本类对象发送给 Client
 */
public class CReportReq extends CParam implements Serializable
{
	private static final long serialVersionUID = -9092777387808458712L;

	public static final byte CMD = Cmd.C_REPORT;

	public String sReqID; // MT 短消息 ID (10 bytes)

	public String sAgent; // 发送短消息使用的 Agent/SMSC ID (6 bytes)

	public byte bStatus = 0x00; // 发送结果 (1 byte)

	public String sMsg = "";

	public String sPhone; // 手机号码

	public String sRid = null;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CReportReq(String sUser, String sAgnt)
	{
		super(CMD, sUser);
		sAgent = sAgnt;
	}

	/**
	 * 设置报告内容
	 */
	public void setInfo(String sReq, String sPhone, byte bStatus, String sMsg)
	{
		this.sReqID = sReq;
		this.sPhone = sPhone;
		this.bStatus = bStatus;
		this.sMsg = sMsg;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CReportReq(byte[] b) throws Exception
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
		sPhone = DateConvertTools.b2s(b, 16, 21, (byte) 32);
		bStatus = b[37];
		try
		{
			sMsg = new String(b, 38, b.length - 38,"gbk");
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

		body = new byte[38 + b.length];

		DateConvertTools.s2b(sReqID, body, 0, 10, (byte) 32);
		DateConvertTools.s2b(sAgent, body, 10, 6, (byte) 32);
		DateConvertTools.s2b(sPhone, body, 16, 21, (byte) 32);
		body[37] = bStatus;
		for (int i = 38; i < 38 + b.length; i++)
		{
			body[i] = b[i - 38];
		}
	}

	// {{DECLARE_CONTROLS
	// }}

	public String toString()
	{

		String s = "\r\nAgent:" + sAgent + "'" + "\r\n ReqID:" + sReqID + "'" + "\r\n Phone:"
				+ sPhone + "'" + "\r\n status:" + this.bStatus + "'"; //+ "\r\n  Msg:" + sMsg;
		return s;

	}

}
