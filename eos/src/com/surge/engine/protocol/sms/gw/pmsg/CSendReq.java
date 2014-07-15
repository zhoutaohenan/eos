package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 发送短消息对象
 */
public class CSendReq extends CParam implements Serializable
{
	private static final long serialVersionUID = 2427553989019465651L;

	public static final byte CMD = Cmd.C_SEND;

	public int iErrorCode = 1; // 参数验证标志，0：成功，1：参数未验证，2：参数有误,3:接口保留

	public String sErrorCode = ""; // 错误原因.

	public String sAgent = ""; // //原先前6字节代表AgentID,现在定为不开放！

	public String sRouteAgent = "";

	public String sAbleAgent = "";

	public String sPolicyId = "";

	public String sFrom = ""; // 发送者号码 (21 bytes)

	public String sTarget = ""; // 接收者号码 (21 bytes)

	public int iReport = 0; // 需要状态报告 0:不需要状态报告，1，需要状态报告，

	// 2:产生SMC话单，3：该信息出错返回状态。
	// 4:该条消息仅携带包月计费信息，不下发给用户，要返回状态报告

	public byte bFeeUser = 0; // 计费用户类型, 0 对目的终端计费,1 对源终端计费,2 对SP计费,3 对指定号码计费

	public String sFeePhone = ""; // 21 bytes

	public String sFeeType = ""; // 费用类型 (6 bytes)

	public int iFee; // 费用(分) (4 bytes)

	public String sService = ""; // 服务代码 (10 bytes)

	public byte bFormat; // 消息格式 (1 byte)

	public String sMoid = ""; // 对应 MO ID (10 bytes)

	public int iLength; // 消息长度 (4 bytes)

	public byte[] bMessage; // 消息字节 (由 iLength 决定)

	public int iSign; // 签名长度

	public byte[] bSign; // 签名

	public boolean isChina = true;

	public boolean isPriority = false;

	public boolean priority = false;// 通道优先独占 2008.12.17

	public boolean isSave = false;

	public byte[] cSign = new byte[0];

	public byte[] eSign = new byte[0];

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sRoute
	 *            使用 Agent ID
	 */
	public CSendReq(String tUser, String tFrom, String tTo, int tReport)
	{
		super(CMD, tUser);
		sAgent = "";
		sFrom = tFrom;
		sTarget = tTo;
		iReport = tReport;
		iErrorCode = 1;
		sErrorCode = "参数未进行验证";
	}

	public CSendReq(String tUser)
	{
		super(CMD, tUser);
		sAgent = "";
		iErrorCode = 1;
		sErrorCode = "参数未进行验证";
	}

	public void setParament(byte tFeeUser, String tService, String tFeePhone, String tFeeType,
			int tFee, byte tFormat)
	{
		if (tFeeType == "01")
		{
			sFeeType = tFeeType;
			bFeeUser = 0;
			iFee = 0;
			sFeePhone = "";
			sService = tService;
		} else if ((tFeeType == "02") || (tFeeType == "03"))
		{
			sFeeType = tFeeType;
			bFeeUser = tFeeUser;
			if (bFeeUser == 2)
			{
				iErrorCode = 2;
				sErrorCode = "参数[sFeeUser]有误.";
				return;
			}
			sFeePhone = tFeePhone;
			iFee = tFee;
			sService = tService;
		} else
		{
			iErrorCode = 3;
			sErrorCode = "接口保留,tFeeType!=01/02/03";
			return;
		}
		bFormat = tFormat;
		iErrorCode = 0;
		sErrorCode = "参数正确.";
		return;
	}

	/**
	 * 设置消息
	 * 
	 * @param bFmt
	 *            消息格式
	 * @param bData
	 *            消息数据
	 * @param sOm
	 *            原 MO ID
	 */
	public void setMessage(byte bFmt, byte[] bData, String sOm)
	{
		bFormat = bFmt;
		if (bData != null)
		{
			iLength = bData.length;
			bMessage = bData;
		} else
		{
			bData = null;
			iLength = 0;
		}
		sMoid = sOm;
	}

	/**
	 * 设置发送、到期时间
	 */
	public void setAddress(String s1, String s2)
	{
		sFrom = s1;
		sTarget = s2;
	}

	/**
	 * 设置收费
	 */
	public void setFee(int iPay, String sCode, int iValue)
	{
		bFeeUser = (byte) iPay;
		sFeeType = sCode;
		iFee = iValue;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CSendReq(byte[] b) throws Exception
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
		sAgent = DateConvertTools.b2s(b, 0, 6, (byte) 32);
		sFrom = DateConvertTools.b2s(b, 6, 21, (byte) 32); // 发送者号码 (21 bytes)

		sTarget = DateConvertTools.b2s(b, 27, 21, (byte) 32).trim(); // 接收者号码 (21 bytes)
		if (sTarget.startsWith("86"))
		{
			sTarget = sTarget.substring(2); // 2007.01.27
		}
		bFeeUser = b[48];
		sFeePhone = DateConvertTools.b2s(b, 49, 21, (byte) 32);
		sService = DateConvertTools.b2s(b, 70, 10, (byte) 32); // 服务代码 (10 bytes)

		sFeeType = DateConvertTools.b2s(b, 80, 6, (byte) 32); // 费用类型 (6 bytes)
		iFee = DateConvertTools.b2i(b, 86, 4); // 费用(分) (4 bytes)

		bFormat = b[90]; // 消息格式 (1 byte)
		sMoid = DateConvertTools.b2s(b, 91, 10, (byte) 32); // 对应 MO ID (10 bytes)

		iLength = DateConvertTools.b2i(b, 101, 4); // 消息长度 (4 bytes)
		if (iLength > 0)
		{
			bMessage = new byte[iLength];
			System.arraycopy(b, 105, bMessage, 0, iLength);
		}
		// 2007.04.16
		int iPot = 105 + iLength;
		if (b.length > iPot)
		{
			iSign = DateConvertTools.b2i(b, iPot, 4); // 签名长度 (4 bytes)

			if (iSign > 0)
			{
				bSign = new byte[iSign];
				System.arraycopy(b, iPot + 4, bSign, 0, iSign);
			}
		}
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		if (bMessage == null)
		{
			iLength = 0;
		} else
		{
			iLength = bMessage.length;
		}

		if (bSign == null)
		{
			iSign = 0;
		} else
		{
			iSign = bSign.length;
		}
		if (sFrom == null)
		{
			sFrom = "";
		}
		if (sAgent == null)
		{
			sAgent = "";
		}
		if (sService == null)
		{
			sService = "SURGESMS";
		}

		int iLen = 0;
		if (iSign > 0)
		{ // 2007.04.16
			iLen = 109 + iLength + iSign;
		} else
		{
			iLen = 105 + iLength;
		}
		body = new byte[iLen];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32); // 使用的 Agent/SMSC ID (6 bytes)

		DateConvertTools.s2b(sFrom, body, 6, 21, (byte) 32); // 发送者号码 (21 bytes)
		DateConvertTools.s2b(sTarget, body, 27, 21, (byte) 32); // 接收者号码 (21 bytes)
		body[48] = bFeeUser;

		DateConvertTools.s2b(sFeePhone, body, 49, 21, (byte) 32); // 付费手机号 (21 bytes)

		DateConvertTools.s2b(sService, body, 70, 10, (byte) 32); // 服务代码 (10 bytes)
		DateConvertTools.s2b(sFeeType, body, 80, 6, (byte) 32); // 费用类型 (6 bytes)
		DateConvertTools.i2b(iFee, body, 86, 4); // 费用(分) (4 bytes)
		body[90] = bFormat; // 消息格式 (1 byte)
		DateConvertTools.s2b(sMoid, body, 91, 10, (byte) 32); // 对应 MO ID (10 bytes)
		DateConvertTools.i2b(iLength, body, 101, 4); // 消息长度 (4 bytes)
		DateConvertTools.addb(bMessage, body, 105, iLength, (byte) 0); // 具体消息

		if (iSign > 0)
		{ // 2007.04.16
			DateConvertTools.i2b(iSign, body, 105 + iLength, 4); // 消息长度 (4 bytes)
			DateConvertTools.addb(bSign, body, 105 + iLength + 4, iSign, (byte) 0); // 具体消息
		}
	}

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

		String s = "To:" + sTarget + ",From:" + sFrom + "\r\n Msg:" + sTmp;
		return s;
	}

	// {{DECLARE_CONTROLS
	// }}
}
