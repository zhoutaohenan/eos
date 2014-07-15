package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client 登录 Server 的退出结果
 */
public class CLogoutRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_LOGOUT | ((byte) 0x80);

	public byte status = -1; // 登录结果

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param bs
	 *            账户验证结果
	 */
	public CLogoutRet(String sUser, byte bs)
	{
		super(CMD, sUser);
		status = bs;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CLogoutRet(byte[] b) throws Exception
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
		status = b[0];
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = new byte[1];
		body[0] = status;
	}

	// {{DECLARE_CONTROLS
	// }}
}