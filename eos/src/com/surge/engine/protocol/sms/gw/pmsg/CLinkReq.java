package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client 和 Server 之间检查连接线路的请求
 */
public class CLinkReq extends CParam implements Serializable
{
	private static final long serialVersionUID = 3799389194889736101L;
	public static final byte CMD = Cmd.C_LINK;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CLinkReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CLinkReq(byte[] b) throws Exception
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