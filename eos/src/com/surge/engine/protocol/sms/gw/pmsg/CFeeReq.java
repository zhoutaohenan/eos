package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client ���͸� Server �ķ���ͳ������
 */
public class CFeeReq extends CParam implements Serializable
{
	private static final long serialVersionUID = -2054608445069718164L;

	public static final byte CMD = Cmd.C_FEE;

	public String sAgent; // ʹ�� Agent/SMSC ID (6 bytes)

	public String sFdate; // ��ʼ���� (8 bytes) yyyymmdd

	public String sTdate; // �յ�ʱ�� (8 bytes) yyyymmdd

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CFeeReq(String sUser, String sAgnt)
	{
		super(CMD, sUser);
		sAgent = sAgnt;
	}

	/**
	 * ���� ͳ������
	 * 
	 * @param sDf
	 *            ��ʼ����
	 * @param sDt
	 *            ��ֹ����
	 */
	public void setInfo(String sDf, String sDt)
	{
		sFdate = sDf;
		sTdate = sDt;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CFeeReq(byte[] b) throws Exception
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
		sAgent = DateConvertTools.b2s(b, 0, 6, (byte) 32);
		sFdate = DateConvertTools.b2s(b, 6, 8, (byte) 32);
		sTdate = DateConvertTools.b2s(b, 14, 8, (byte) 32);
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[22];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32);
		DateConvertTools.s2b(sFdate, body, 6, 8, (byte) 32);
		DateConvertTools.s2b(sTdate, body, 14, 8, (byte) 32);
	}

	// {{DECLARE_CONTROLS
	// }}

}