package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client ��¼ Server ���˳����
 */
public class CLogoutRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_LOGOUT | ((byte) 0x80);

	public byte status = -1; // ��¼���

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param bs
	 *            �˻���֤���
	 */
	public CLogoutRet(String sUser, byte bs)
	{
		super(CMD, sUser);
		status = bs;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
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
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[1];
		body[0] = status;
	}

	// {{DECLARE_CONTROLS
	// }}
}