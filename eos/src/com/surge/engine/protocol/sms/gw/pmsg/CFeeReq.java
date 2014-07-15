package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 发送给 Server 的费用统计请求
 */
public class CFeeReq extends CParam implements Serializable
{
	private static final long serialVersionUID = -2054608445069718164L;

	public static final byte CMD = Cmd.C_FEE;

	public String sAgent; // 使用 Agent/SMSC ID (6 bytes)

	public String sFdate; // 起始日期 (8 bytes) yyyymmdd

	public String sTdate; // 收到时间 (8 bytes) yyyymmdd

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CFeeReq(String sUser, String sAgnt)
	{
		super(CMD, sUser);
		sAgent = sAgnt;
	}

	/**
	 * 设置 统计条件
	 * 
	 * @param sDf
	 *            起始日期
	 * @param sDt
	 *            截止日期
	 */
	public void setInfo(String sDf, String sDt)
	{
		sFdate = sDf;
		sTdate = sDt;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CFeeReq(byte[] b) throws Exception
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
		sFdate = DateConvertTools.b2s(b, 6, 8, (byte) 32);
		sTdate = DateConvertTools.b2s(b, 14, 8, (byte) 32);
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = new byte[22];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32);
		DateConvertTools.s2b(sFdate, body, 6, 8, (byte) 32);
		DateConvertTools.s2b(sTdate, body, 14, 8, (byte) 32);
	}

	// {{DECLARE_CONTROLS
	// }}

}