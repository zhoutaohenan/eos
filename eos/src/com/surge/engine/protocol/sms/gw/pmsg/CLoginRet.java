package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client ��¼ Server �ĵ�¼���
 */
public class CLoginRet extends CParam implements Serializable
{
	private static final long serialVersionUID = 5148636631316630682L;

	public static final byte CMD = Cmd.C_LOGIN | ((byte) 0x80);

	public byte bStatus = -1; // ��¼���

	public byte[] passwd = null; // ʹ�õļ������� (16 bytes),

	// ���Ϊ null ��ʾ��֧�ֿͻ�����ļ����㷨�������ݲ�����

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param bs
	 *            �˻���֤���
	 */
	public CLoginRet(String sUser, byte bs)
	{
		super(CMD, sUser);
		bStatus = bs;
	}

	public void setPasswd(byte[] b)
	{
		if (b != null)
		{
			passwd = new byte[b.length];
			for (int i = 0; i < b.length; i++)
			{
				passwd[i] = b[i];
			}
		}
	}

	public byte[] getPasswd()
	{
		return passwd;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CLoginRet(byte[] b) throws Exception
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
		passwd = DateConvertTools.cutb(b, 1, 16, (byte) 0);
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[17];
		body[0] = bStatus;
		DateConvertTools.addb(passwd, body, 1, 16, (byte) 0);
	}

	public String toString()
	{
		String s = super.toString();
		s = s + "\r\n" + "STUS=" + (int) bStatus + "\r\n";

		if (passwd == null)
		{
			s = s + "Pwd=null";
		} else
		{
			s = s + "Pwd=" + new String(passwd);
		}

		return s;
	}

	// {{DECLARE_CONTROLS
	// }}
}