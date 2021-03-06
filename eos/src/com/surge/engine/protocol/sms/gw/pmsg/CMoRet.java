package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client 收到 MO 消息后，生成本类对象发送给 Server
 */
public class CMoRet extends CParam implements Serializable
{
	private static final long serialVersionUID = 4468921738113026050L;

	public static final byte CMD = Cmd.C_MO | ((byte) 0x80);

	public byte bStatus; // 状态 (1 bytes)

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param st
	 *            状态
	 */
	public CMoRet(String sUser, byte st)
	{
		super(CMD, sUser);
		bStatus = st;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CMoRet(byte[] b) throws Exception
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

	// {{DECLARE_CONTROLS
	// }}

}