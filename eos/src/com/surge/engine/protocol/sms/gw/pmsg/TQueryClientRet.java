package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 修改密码 结果
 */
public class TQueryClientRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.T_QueryClient | ((byte) 0x80);

	public byte bStatus;

	public int iNumClient = 0; // 4 bytes

	public String[] sClient = null;

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client 密码
	 */
	public TQueryClientRet(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public TQueryClientRet(byte[] b) throws Exception
	{
		super(b);
		if (bCommand != CMD)
		{
			throw new Exception("Invalid command!");
		}
		init();
	}

	public void setClient(String[] s)
	{
		iNumClient = 0;
		if (s == null)
		{
			sClient = null;
		} else
		{
			iNumClient = s.length;
			sClient = new String[s.length];
			for (int i = 0; i < s.length; i++)
			{
				sClient[i] = s[i];
			}
		}
	}

	public int getClientNum()
	{
		return iNumClient;
	}

	protected void init()
	{
		byte[] b = body;
		bStatus = body[0];
		int iNum = DateConvertTools.b2i(b, 1, 4);
		if (iNum < 1)
		{
			sClient = null;
			iNumClient = 0;
		} else
		{
			iNumClient = iNum;
			sClient = new String[iNum];
			for (int i = 0; i < iNum; i++)
			{
				sClient[i] = DateConvertTools.b2s(b, 5 + i * 8, 8, (byte) 32);
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
		if (sClient != null)
		{
			iNum = sClient.length;
		}
		body = new byte[5 + iNum * 10];
		body[0] = bStatus;
		DateConvertTools.i2b(iNum, body, 1, 4);
		for (int i = 0; i < iNum; i++)
		{
			DateConvertTools.s2b(sClient[i], body, 5 + i * 8, 8, (byte) 32);
		}
	}

	public String toString()
	{
		String s = "Status:" + bStatus + ",NumClient:" + iNumClient + ",List:";
		if (sClient != null)
		{
			for (int i = 0; i < sClient.length; i++)
			{
				s = s + sClient[i] + ",";
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