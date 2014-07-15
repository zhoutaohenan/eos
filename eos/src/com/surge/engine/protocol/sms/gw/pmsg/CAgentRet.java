package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 向 Server 发出查询可用的 Agent/SMSC 返回结果
 */
public class CAgentRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_AGENT | ((byte) 0x80);

	public int iNumAgent = 0; // 4 bytes

	public String[] sAgent = null;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CAgentRet(String sUser)
	{
		super(CMD, sUser);

		// {{INIT_CONTROLS
		// }}
	}

	/**
	 * 设置可用的 Agent/SMSC
	 */
	public void setAgent(String[] s)
	{
		iNumAgent = 0;
		if (s == null)
		{
			sAgent = null;
		} else
		{
			iNumAgent = s.length;
			sAgent = new String[s.length];
			for (int i = 0; i < s.length; i++)
			{
				sAgent[i] = s[i];
			}
		}
	}

	public int getAgentNum()
	{
		return iNumAgent;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CAgentRet(byte[] b) throws Exception
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
		int iNum = DateConvertTools.b2i(b, 0, 4);
		if (iNum < 1)
		{
			sAgent = null;
			iNumAgent = 0;
		} else
		{
			iNumAgent = iNum;
			sAgent = new String[iNum];
			for (int i = 0; i < iNum; i++)
			{
				sAgent[i] = DateConvertTools.b2s(b, 4 + i * 6, 6, (byte) 32);
			}
		}
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = null;
		int iNum = 0;
		if (sAgent != null)
		{
			iNum = sAgent.length;
		}
		body = new byte[4 + iNum * 10];

		DateConvertTools.i2b(iNum, body, 0, 4);
		for (int i = 0; i < iNum; i++)
		{
			DateConvertTools.s2b(sAgent[i], body, 4 + i * 6, 6, (byte) 32);
		}
	}

	public String toString()
	{
		String s = "NumAgent:" + iNumAgent + ",List:";
		if (sAgent != null)
		{
			for (int i = 0; i < sAgent.length; i++)
			{
				s = s + sAgent[i] + ",";
			}
		} else
		{
			s = s + "null";
		}
		return s;
	}

	// {{DECLARE_CONTROLS
	// }}

}