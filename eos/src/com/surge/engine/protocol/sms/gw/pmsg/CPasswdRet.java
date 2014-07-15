package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client �޸����� ���
 */
public class CPasswdRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_PASSWD | ((byte) 0x80);

	public byte bStatus;

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client ����
	 */
	public CPasswdRet(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
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
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
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