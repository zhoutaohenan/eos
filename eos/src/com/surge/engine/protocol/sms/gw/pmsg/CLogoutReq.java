package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client 登录 Server 的请求对象
 */
public class CLogoutReq extends CParam implements Serializable
{
	public static final byte CLOSE = 0X00;

	public static final byte OPEN = 0X01;

	public static final byte CMD = Cmd.C_LOGOUT;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param bConn
	 *            保持连接, true - 退出后关闭, false - 退出后不关闭连接
	 */
	public CLogoutReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CLogoutReq(byte[] b) throws Exception
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
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = null;
	}

	// {{DECLARE_CONTROLS
	// }}
}