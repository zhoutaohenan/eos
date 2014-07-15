package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server ���͸� Client �ķ���ͳ��
 */
public class CFeeRet extends CParam implements Serializable
{
	private static final long serialVersionUID = 2485254370550340183L;

	public static final byte CMD = Cmd.C_FEE | ((byte) 0x80);

	public byte bStatus; // ָ��ִ�н��(1 byte)

	public String sFeeLeft; // �������(��Ϊ��λ) (10 bytes)

	public String sFee; // ����һ����Ϣ�ķ��� (6 bytes)

	public int iNumRec = 0; // ״̬�� 4 byte

	public String[] sCode = null; // ״̬��� 2 byte

	public String[] sNum = null; // ״̬��Ӧ���� 10 byte

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param bs
	 *            ���
	 */
	public CFeeRet(String sUser, byte bs)
	{
		super(CMD, sUser);
		bStatus = bs;
	}

	/**
	 * ���� ͳ�ƽ��
	 * 
	 * @param sN1
	 *            �������
	 */
	public void setInfo(String sN1, String[] sc, String[] sd)
	{
		sFeeLeft = sN1;
		if (sc == null || sd == null || sc.length != sd.length)
		{
			iNumRec = 0;
			return;
		}
		iNumRec = sc.length;
		sCode = new String[iNumRec];
		sNum = new String[iNumRec];

		for (int i = 0; i < iNumRec; i++)
		{
			sCode[i] = sc[i];
			sNum[i] = sd[i];
		}
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CFeeRet(byte[] b) throws Exception
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
		sFeeLeft = DateConvertTools.b2s(b, 1, 10, (byte) 32);
		sFee = DateConvertTools.b2s(b, 11, 6, (byte) 32);

		iNumRec = DateConvertTools.b2i(b, 17, 4);
		if (iNumRec < 1 || iNumRec > 100)
		{
			sCode = null;
			sNum = null;
			return;
		}
		sCode = new String[iNumRec];
		sNum = new String[iNumRec];
		int iFrom = 21;
		for (int i = 0; i < iNumRec; i++)
		{
			sCode[i] = DateConvertTools.b2s(b, iFrom, 2, (byte) 32);
			sNum[i] = DateConvertTools.b2s(b, iFrom + 2, 10, (byte) 32);
			iFrom += 12;
		}
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		// body=new byte[41];
		body = new byte[21 + 12 * iNumRec];
		body[0] = bStatus;
		DateConvertTools.s2b(sFeeLeft, body, 1, 10, (byte) 32);
		DateConvertTools.s2b(sFee, body, 11, 6, (byte) 32);

		DateConvertTools.i2b(iNumRec, body, 17, 4);
		if (iNumRec < 1)
		{
			return;
		}
		int iFrom = 21;
		for (int i = 0; i < iNumRec; i++)
		{
			DateConvertTools.s2b(sCode[i], body, iFrom, 2, (byte) 32);
			DateConvertTools.s2b(sNum[i], body, iFrom + 2, 10, (byte) 32);
			iFrom += 12;
		}
	}

	public String toString()
	{
		return "User=" + sUserID + ",FeeLeft=" + sFeeLeft + ",Fee=" + sFee;
	}

	// {{DECLARE_CONTROLS
	// }}

}