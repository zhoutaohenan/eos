package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client 修改密码 结果
 */
public class CPasswdRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_PASSWD | ((byte) 0x80);

	public byte bStatus;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client 密码
	 */
	public CPasswdRet(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CPasswdRet(byte[] b) throws Exception
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
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = new byte[1];
		body[0] = bStatus;
	}

	public String toString()
	{
		return "status=" + (int) bStatus;
	}

	// {{DECLARE_CONTROLS
	// }}

}