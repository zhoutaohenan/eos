package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client ��¼ Server ���������
 */
public class CLoginReq extends CParam implements Serializable
{
	private static final long serialVersionUID = 7191427679324196941L;

	public static final byte CMD = Cmd.C_LOGIN;

	public static final byte BODY_LEN = 117;

	public String sPasswd = ""; // ���� 8 BYTES

	public String sMethod = ""; // ���λỰʹ�õļ����㷨, 8 BYTES

	public int iType = CCode.CMO; // �������� 1 MT, 2 MO , 1 byte

	public String sVersion = "";// ��Ʒ���

	public String sAgentList; // Ҫ���� MO ��Ϣ�� Agent ID,�м��� ���ŷֿ�, ����Ϊ 100

	// bytes,���㲹�ո�

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sPass
	 *            Client ����
	 */
	public CLoginReq(String sUser, String sPass)
	{
		super(CMD, sUser);
		sPasswd = sPass;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CLoginReq(byte[] b) throws Exception
	{
		super(b);
		if (bCommand != CMD)
		{
			throw new Exception("Invalid command!");
		}
		init();
	}

	/**
	 * ����ʹ�õļ����㷨
	 */
	public void setMethod(String sMo)
	{
		sMethod = sMo;
	}

	public String getMethod()
	{
		return sMethod;
	}

	protected void init()
	{
		byte[] b = body;
		sPasswd = DateConvertTools.b2s(b, 0, 8, (byte) 32);
		sMethod = DateConvertTools.b2s(b, 8, 8, (byte) 32);
		iType = DateConvertTools.b2i(b, 16, 1);
		sVersion = DateConvertTools.b2s(b, 17, 10, (byte) 32);
		sAgentList = DateConvertTools.b2s(b, 27, 90, (byte) 32);
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[BODY_LEN];
		DateConvertTools.s2b(sPasswd, body, 0, 8, (byte) 32);
		DateConvertTools.s2b(sMethod, body, 8, 8, (byte) 32);
		DateConvertTools.i2b(iType, body, 16, 1);
		DateConvertTools.s2b(sVersion, body, 17, 10, (byte) 32);
		DateConvertTools.s2b(sAgentList, body, 27, 90, (byte) 32);
	}

}