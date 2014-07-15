package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client 向 Server 发出查询可用的 Agent/SMSC
 */
public class CAgentReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_AGENT;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CAgentReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CAgentReq(byte[] b) throws Exception
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

}