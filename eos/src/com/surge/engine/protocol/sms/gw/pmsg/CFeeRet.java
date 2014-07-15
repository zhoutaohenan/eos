package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server 发送给 Client 的费用统计
 */
public class CFeeRet extends CParam implements Serializable
{
	private static final long serialVersionUID = 2485254370550340183L;

	public static final byte CMD = Cmd.C_FEE | ((byte) 0x80);

	public byte bStatus; // 指令执行结果(1 byte)

	public String sFeeLeft; // 费用与额(分为单位) (10 bytes)

	public String sFee; // 发送一条消息的费用 (6 bytes)

	public int iNumRec = 0; // 状态数 4 byte

	public String[] sCode = null; // 状态编号 2 byte

	public String[] sNum = null; // 状态对应数量 10 byte

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param bs
	 *            结果
	 */
	public CFeeRet(String sUser, byte bs)
	{
		super(CMD, sUser);
		bStatus = bs;
	}

	/**
	 * 设置 统计结果
	 * 
	 * @param sN1
	 *            费用余额
	 */
	public void setInfo(String sN1, String[] sc, String[] sd)
	{
		sFeeLeft = sN1;
		if (sc == null || sd == null || sc.length != sd.length)
		{
			iNumRec = 0;
			return;
		}
		iNumRec = sc.length;
		sCode = new String[iNumRec];
		sNum = new String[iNumRec];

		for (int i = 0; i < iNumRec; i++)
		{
			sCode[i] = sc[i];
			sNum[i] = sd[i];
		}
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CFeeRet(byte[] b) throws Exception
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
		bStatus = b[0];
		sFeeLeft = DateConvertTools.b2s(b, 1, 10, (byte) 32);
		sFee = DateConvertTools.b2s(b, 11, 6, (byte) 32);

		iNumRec = DateConvertTools.b2i(b, 17, 4);
		if (iNumRec < 1 || iNumRec > 100)
		{
			sCode = null;
			sNum = null;
			return;
		}
		sCode = new String[iNumRec];
		sNum = new String[iNumRec];
		int iFrom = 21;
		for (int i = 0; i < iNumRec; i++)
		{
			sCode[i] = DateConvertTools.b2s(b, iFrom, 2, (byte) 32);
			sNum[i] = DateConvertTools.b2s(b, iFrom + 2, 10, (byte) 32);
			iFrom += 12;
		}
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		// body=new byte[41];
		body = new byte[21 + 12 * iNumRec];
		body[0] = bStatus;
		DateConvertTools.s2b(sFeeLeft, body, 1, 10, (byte) 32);
		DateConvertTools.s2b(sFee, body, 11, 6, (byte) 32);

		DateConvertTools.i2b(iNumRec, body, 17, 4);
		if (iNumRec < 1)
		{
			return;
		}
		int iFrom = 21;
		for (int i = 0; i < iNumRec; i++)
		{
			DateConvertTools.s2b(sCode[i], body, iFrom, 2, (byte) 32);
			DateConvertTools.s2b(sNum[i], body, iFrom + 2, 10, (byte) 32);
			iFrom += 12;
		}
	}

	public String toString()
	{
		return "User=" + sUserID + ",FeeLeft=" + sFeeLeft + ",Fee=" + sFee;
	}

	// {{DECLARE_CONTROLS
	// }}

}