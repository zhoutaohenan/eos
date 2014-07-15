package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server 收到 MO 消息后，生成本类对象发送给 Client
 */
public class CMoReq extends CParam implements Serializable
{
	private static final long serialVersionUID = -1820125457135059365L;

	public static final byte CMD = Cmd.C_MO;

	public String sAgent; // MO 消息 来源 Agent/SMSC ID (6 bytes)

	public String sMoid; // MO 记录ID (10 bytes)

	public String sTimeSend; // 发送时间 (14 bytes) yyyymmddhhmmss

	public String sTimeLand; // 收到时间 (14 bytes) yyyymmddhhmmss

	public String sFrom; // 源手机号码 (21 bytes)

	public String sTarget; // 目的手机号码 (21 bytes)

	public String sService; // 业务类别 (10 bytes)

	public byte bFormat; // 消息格式 (1 bytes)

	public int iLength; // 消息长度 (4 bytes)

	public byte[] bMessage; // 消息 (n bytes)

	public String sRid = null;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CMoReq(String sUser, String sAgnt)
	{
		super(CMD, sUser);
		sAgent = sAgnt;
	}

	/**
	 * 设置MO 消息体内容
	 * 
	 * @param sId
	 *            MO ID
	 * @param sWork
	 *            服务代码
	 * @param bFmt
	 *            消息格式
	 * @param bMsg
	 *            消息内容
	 */
	public void setMessage(String sId, String sWork, byte bFmt, byte[] bMsg)
	{
		sMoid = sId;
		sService = sWork;
		bFormat = bFmt;
		if (bMsg == null)
		{
			iLength = 0;
			bMessage = null;
		} else
		{
			iLength = bMsg.length;
			bMessage = new byte[iLength];
			System.arraycopy(bMsg, 0, bMessage, 0, iLength);
		}
	}

	/**
	 * 设置MO 消息内容
	 * 
	 * @param sTs
	 *            消息发送时间
	 * @param sTr
	 *            消息收到时间
	 * @param sFrom
	 *            消息源手机号码
	 * @param sTo
	 *            消息接收者手机号码
	 */
	public void setInfo(String sTs, String sTr, String sFr, String sTo)
	{
		sTimeSend = sTs;
		sTimeLand = sTr;
		sFrom = sFr;
		sTarget = sTo;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CMoReq(byte[] b) throws Exception
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
		sAgent = DateConvertTools.b2s(b, 0, 6, (byte) 32); // MO 消息 来源 Agent/SMSC ID (6
												// bytes)
		sMoid = DateConvertTools.b2s(b, 6, 10, (byte) 32); // MO 记录ID (10 bytes)
		sTimeSend = DateConvertTools.b2s(b, 16, 14, (byte) 32); // 发送时间 (14 bytes)
														// yyyymmddhhmmss
		sTimeLand = DateConvertTools.b2s(b, 30, 14, (byte) 32); // 收到时间 (14 bytes)
														// yyyymmddhhmmss
		sFrom = DateConvertTools.b2s(b, 44, 21, (byte) 32); // 源手机号码 (21 bytes)
		sTarget = DateConvertTools.b2s(b, 65, 21, (byte) 32); // 目的手机号码 (21 bytes)
		sService = DateConvertTools.b2s(b, 86, 10, (byte) 32); // 业务类别 (10 bytes)
		bFormat = b[96]; // 消息格式 (1 bytes)
		iLength = DateConvertTools.b2i(b, 97, 4); // 消息长度 (4 bytes)
		if (iLength < 1)
		{
			bMessage = null;
		} else
		{
			bMessage = new byte[iLength];
			System.arraycopy(b, 101, bMessage, 0, iLength);
		}
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = new byte[101 + iLength];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32);
		DateConvertTools.s2b(sMoid, body, 6, 10, (byte) 32);
		DateConvertTools.s2b(sTimeSend, body, 16, 14, (byte) 32);
		DateConvertTools.s2b(sTimeLand, body, 30, 14, (byte) 32);
		DateConvertTools.s2b(sFrom, body, 44, 21, (byte) 32);
		DateConvertTools.s2b(sTarget, body, 65, 21, (byte) 32);
		DateConvertTools.s2b(sService, body, 86, 10, (byte) 32);
		body[96] = bFormat;
		DateConvertTools.i2b(iLength, body, 97, 4);
		DateConvertTools.addb(bMessage, body, 101, iLength, (byte) 0);
	}

	// {{DECLARE_CONTROLS
	// }}

	public String toString()
	{
		String sTmp = null;
		try
		{
			sTmp = new String(bMessage);
		} catch (Exception e)
		{
		}
		if (sTmp == null)
		{
			sTmp = "{Len=" + iLength + ",Data=" + bMessage + "}";
		}

		String s = "\r\nAgent:" + sAgent + "'" + "\r\n From:" + sFrom + "'" + "\r\n   To:"
				+ sTarget + "'" + "\r\n  Msg:" + sTmp;
		return s;

	}

}