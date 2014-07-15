package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client �� Server ������ѯ���õ� Agent/SMSC ���ؽ��
 */
public class CAgentRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_AGENT | ((byte) 0x80);

	public int iNumAgent = 0; // 4 bytes

	public String[] sAgent = null;

	/**
	 * ���û�ID������ �������
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
	 * ���ÿ��õ� Agent/SMSC
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
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
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
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
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