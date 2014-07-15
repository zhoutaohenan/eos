package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client �޸�����
 */
public class TForceLogoutReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.T_ForceLogout;

	public static final byte BODY_LEN = 8;

	public String logoutClientid = ""; // �ɵ����� 8 BYTES

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client ����
	 */
	public TForceLogoutReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
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
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[BODY_LEN];
		DateConvertTools.s2b(logoutClientid, body, 0, 8, (byte) 32);
	}

	// {{DECLARE_CONTROLS
	// }}

}