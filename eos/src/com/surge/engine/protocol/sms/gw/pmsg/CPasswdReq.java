package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client �޸�����
 */
public class CPasswdReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_PASSWD;

	public static final byte BODY_LEN = 16;

	public String sPasswd0 = ""; // �ɵ����� 8 BYTES

	public String sPasswd1 = ""; // �µ����� 8 BYTES

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client ����
	 */
	public CPasswdReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CPasswdReq(byte[] b) throws Exception
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
		sPasswd0 = DateConvertTools.b2s(b, 0, 8, (byte) 32);
		sPasswd1 = DateConvertTools.b2s(b, 8, 8, (byte) 32);
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[BODY_LEN];
		DateConvertTools.s2b(sPasswd0, body, 0, 8, (byte) 32);
		DateConvertTools.s2b(sPasswd1, body, 8, 8, (byte) 32);
	}

	// {{DECLARE_CONTROLS
	// }}

}
