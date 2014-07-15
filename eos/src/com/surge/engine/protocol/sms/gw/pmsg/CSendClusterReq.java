package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 发送短消息对象
 */
public class CSendClusterReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_SENDCLUSTER;

	public int iErrorCode = 1; // 参数验证标志，0：成功，1：参数未验证，2：参数有误,3:接口保留

	public String sErrorCode = ""; // 错误原因.

	public String sAgent = ""; // 使用的 Agent/SMSC ID (6 bytes)

	public String sRouteAgent = "";

	public String sAbleAgent = "";

	public String sPolicyId = "";

	public String sFrom = ""; // 发送者号码 (21 bytes)

	public int iTarget = 0;

	public String[] gTarget = null; // 接收者号码 (21 bytes)

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

	public boolean isSave = false;

	public boolean priority = false;// 通道优先独占 2008.12.17

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
	public CSendClusterReq(String tUser, String tFrom, int tReport)
	{
		super(CMD, tUser);
		sAgent = "";
		sFrom = tFrom;
		iReport = tReport;
		iErrorCode = 1;
		sErrorCode = "参数未进行验证";
	}

	public CSendClusterReq(String tUser)
	{
		super(CMD, tUser);
		gTarget = new String[100];
		sAgent = "";
		iErrorCode = 1;
		sErrorCode = "参数未进行验证";
	}

	public void SetTarget(int tnTarget, String[] tTarget)
	{
		iTarget = tnTarget;
		if ((iTarget < 1) || (iTarget > 101))
		{
			iErrorCode = 2;
			sErrorCode = "参数(iTarget<1)||(iTarget>101)有误.";
			return;
		}
		if (gTarget == null)
		{
			gTarget = new String[iTarget];
		}
		for (int i = 0; i < iTarget; i++)
		{
			gTarget[i] = tTarget[i];
		}
	}

	public void setParament(byte tFeeUser, String tService, String tFeePhone, String tFeeType,
			int tFee, byte tFormat)
	{
		if (tFeeType == "01")
		{
			sFeeType = tFeeType;
			bFeeUser = 0;
			iFee = 0;
			sService = "SURGESMS";
			sFeePhone = "0";
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
	public CSendClusterReq(byte[] b) throws Exception
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
		int iCurid = 0;
		byte[] b = body;
		sAgent = DateConvertTools.b2s(b, 0, 6, (byte) 32); // 使用的 Agent/SMSC ID (6 bytes)
		sFrom = DateConvertTools.b2s(b, 6, 21, (byte) 32); // 发送者号码 (21 bytes)
		iTarget = DateConvertTools.b2i(b, 27, 1); // 接收号码个数
		iCurid = 28;
		if (iTarget > 0)
		{
			gTarget = new String[iTarget];
			for (int i = 0; i < iTarget; i++)
			{
				gTarget[i] = DateConvertTools.b2s(b, 28 + 21 * i, 21, (byte) 32).trim(); // 接收者号码
																				// (21
																				// bytes)
				if (gTarget[i].startsWith("86"))
				{
					gTarget[i] = gTarget[i].substring(2); // 2007.01.27
				}
			}
			iCurid = 28 + 21 * iTarget;
		} else if (iTarget == 0)
		{
			gTarget = new String[1];
			gTarget[0] = DateConvertTools.b2s(b, 28, 21, (byte) 32).trim(); // 接收者号码 (21
																	// bytes)
			if (gTarget[0].startsWith("86"))
			{
				gTarget[0] = gTarget[0].substring(2); // 2007.01.27
			}
			iCurid = 28 + 21;
		}
		bFeeUser = b[iCurid];
		sFeePhone = DateConvertTools.b2s(b, iCurid + 1, 21, (byte) 32);
		sService = DateConvertTools.b2s(b, iCurid + 22, 10, (byte) 32); // 服务代码 (10 bytes)
		sFeeType = DateConvertTools.b2s(b, iCurid + 32, 6, (byte) 32); // 费用类型 (6 bytes)
		iFee = DateConvertTools.b2i(b, iCurid + 38, 4); // 费用(分) (4 bytes)

		bFormat = b[iCurid + 42]; // 消息格式 (1 byte)
		sMoid = DateConvertTools.b2s(b, iCurid + 43, 10, (byte) 32); // 对应 MO ID (10 bytes)
		iLength = DateConvertTools.b2i(b, iCurid + 53, 4); // 消息长度 (4 bytes)
		if (iLength > 0)
		{
			bMessage = new byte[iLength];
			System.arraycopy(b, iCurid + 57, bMessage, 0, iLength);
		}

		// 2007.04.16
		int iPot = iCurid + 57 + iLength;
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
		int iLen = 0;
		if (bMessage == null)
		{
			iLength = 0;
		} else
		{
			iLength = bMessage.length;
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
		if (iTarget == 0)
		{
			iLen = 106 + iLength;
		} else if (iTarget > 0)
		{
			iLen = 85 + iLength + 21 * iTarget;
		}

		if (iSign > 0)
		{
			iLen = iLen + 4 + iSign;
		}
		body = new byte[iLen];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32); // 使用的 Agent/SMSC ID (6 bytes)
		DateConvertTools.s2b(sFrom, body, 6, 21, (byte) 32); // 发送者号码 (21 bytes)

		int iCurid = 28;
		if (iTarget > 0)
		{
			if (gTarget.length < iTarget)
			{
				iTarget = gTarget.length;
				DateConvertTools.i2b(iTarget, body, 27, 1); // 接收号码个数
			} else
			{
				DateConvertTools.i2b(iTarget, body, 27, 1); // 接收号码个数
			}
			for (int i = 0; i < iTarget; i++)
			{
				DateConvertTools.s2b(gTarget[i], body, 28 + 21 * i, 21, (byte) 32); // 接收者号码
																			// (21
																			// bytes)
			}
			iCurid = 28 + 21 * iTarget;
		} else if (iTarget == 0)
		{
			DateConvertTools.i2b(iTarget, body, 27, 1); // 接收号码个数
			DateConvertTools.s2b(gTarget[0], body, 28, 21, (byte) 32); // 接收者号码 (21 bytes)
			iCurid = 28 + 21;
		}
		body[iCurid] = bFeeUser;
		DateConvertTools.s2b(sFeePhone, body, iCurid + 1, 21, (byte) 32); // 付费手机号 (21
																// bytes)
		DateConvertTools.s2b(sService, body, iCurid + 22, 10, (byte) 32); // 服务代码 (10
																// bytes)
		DateConvertTools.s2b(sFeeType, body, iCurid + 32, 6, (byte) 32); // 费用类型 (6 bytes)
		DateConvertTools.i2b(iFee, body, iCurid + 38, 4); // 费用(分) (4 bytes)
		body[iCurid + 42] = bFormat; // 消息格式 (1 byte)
		DateConvertTools.s2b(sMoid, body, iCurid + 43, 10, (byte) 32); // 对应 MO ID (10
															// bytes)
		DateConvertTools.i2b(iLength, body, iCurid + 53, 4); // 消息长度 (4 bytes)
		DateConvertTools.addb(bMessage, body, iCurid + 57, iLength, (byte) 0); // 具体消息*/

		if (iSign > 0)
		{ // 2007.04.16
			DateConvertTools.i2b(iSign, body, iCurid + 57 + iLength, 4); // 消息长度 (4 bytes)
			DateConvertTools.addb(bSign, body, iCurid + 61 + iLength, iSign, (byte) 0); // 具体消息*/
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
		return "\r\n Route:" + sAgent + ",From:" + sFrom + ",Msg:" + sTmp;
	}

	// {{DECLARE_CONTROLS
	// }}
}
