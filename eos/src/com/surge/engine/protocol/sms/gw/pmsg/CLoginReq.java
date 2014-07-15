package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 登录 Server 的请求对象
 */
public class CLoginReq extends CParam implements Serializable
{
	private static final long serialVersionUID = 7191427679324196941L;

	public static final byte CMD = Cmd.C_LOGIN;

	public static final byte BODY_LEN = 117;

	public String sPasswd = ""; // 密码 8 BYTES

	public String sMethod = ""; // 本次会话使用的加密算法, 8 BYTES

	public int iType = CCode.CMO; // 连接类型 1 MT, 2 MO , 1 byte

	public String sVersion = "";// 产品编号

	public String sAgentList; // 要接收 MO 信息的 Agent ID,中间用 逗号分开, 长度为 100

	// bytes,不足补空格

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client 密码
	 */
	public CLoginReq(String sUser, String sPass)
	{
		super(CMD, sUser);
		sPasswd = sPass;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CLoginReq(byte[] b) throws Exception
	{
		super(b);
		if (bCommand != CMD)
		{
			throw new Exception("Invalid command!");
		}
		init();
	}

	/**
	 * 设置使用的加密算法
	 */
	public void setMethod(String sMo)
	{
		sMethod = sMo;
	}

	public String getMethod()
	{
		return sMethod;
	}

	protected void init()
	{
		byte[] b = body;
		sPasswd = DateConvertTools.b2s(b, 0, 8, (byte) 32);
		sMethod = DateConvertTools.b2s(b, 8, 8, (byte) 32);
		iType = DateConvertTools.b2i(b, 16, 1);
		sVersion = DateConvertTools.b2s(b, 17, 10, (byte) 32);
		sAgentList = DateConvertTools.b2s(b, 27, 90, (byte) 32);
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = new byte[BODY_LEN];
		DateConvertTools.s2b(sPasswd, body, 0, 8, (byte) 32);
		DateConvertTools.s2b(sMethod, body, 8, 8, (byte) 32);
		DateConvertTools.i2b(iType, body, 16, 1);
		DateConvertTools.s2b(sVersion, body, 17, 10, (byte) 32);
		DateConvertTools.s2b(sAgentList, body, 27, 90, (byte) 32);
	}

}