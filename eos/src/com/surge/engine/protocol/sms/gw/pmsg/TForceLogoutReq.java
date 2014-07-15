package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 修改密码
 */
public class TForceLogoutReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.T_ForceLogout;

	public static final byte BODY_LEN = 8;

	public String logoutClientid = ""; // 旧的密码 8 BYTES

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client 密码
	 */
	public TForceLogoutReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public TForceLogoutReq(byte[] b) throws Exception
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
		logoutClientid = DateConvertTools.b2s(b, 0, 8, (byte) 32);
		logoutClientid.trim();
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = new byte[BODY_LEN];
		DateConvertTools.s2b(logoutClientid, body, 0, 8, (byte) 32);
	}

	// {{DECLARE_CONTROLS
	// }}

}