package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server �յ� MO ��Ϣ�����ɱ�������͸� Client
 */
public class CMoReq extends CParam implements Serializable
{
	private static final long serialVersionUID = -1820125457135059365L;

	public static final byte CMD = Cmd.C_MO;

	public String sAgent; // MO ��Ϣ ��Դ Agent/SMSC ID (6 bytes)

	public String sMoid; // MO ��¼ID (10 bytes)

	public String sTimeSend; // ����ʱ�� (14 bytes) yyyymmddhhmmss

	public String sTimeLand; // �յ�ʱ�� (14 bytes) yyyymmddhhmmss

	public String sFrom; // Դ�ֻ����� (21 bytes)

	public String sTarget; // Ŀ���ֻ����� (21 bytes)

	public String sService; // ҵ����� (10 bytes)

	public byte bFormat; // ��Ϣ��ʽ (1 bytes)

	public int iLength; // ��Ϣ���� (4 bytes)

	public byte[] bMessage; // ��Ϣ (n bytes)

	public String sRid = null;

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CMoReq(String sUser, String sAgnt)
	{
		super(CMD, sUser);
		sAgent = sAgnt;
	}

	/**
	 * ����MO ��Ϣ������
	 * 
	 * @param sId
	 *            MO ID
	 * @param sWork
	 *            �������
	 * @param bFmt
	 *            ��Ϣ��ʽ
	 * @param bMsg
	 *            ��Ϣ����
	 */
	public void setMessage(String sId, String sWork, byte bFmt, byte[] bMsg)
	{
		sMoid = sId;
		sService = sWork;
		bFormat = bFmt;
		if (bMsg == null)
		{
			iLength = 0;
			bMessage = null;
		} else
		{
			iLength = bMsg.length;
			bMessage = new byte[iLength];
			System.arraycopy(bMsg, 0, bMessage, 0, iLength);
		}
	}

	/**
	 * ����MO ��Ϣ����
	 * 
	 * @param sTs
	 *            ��Ϣ����ʱ��
	 * @param sTr
	 *            ��Ϣ�յ�ʱ��
	 * @param sFrom
	 *            ��ϢԴ�ֻ�����
	 * @param sTo
	 *            ��Ϣ�������ֻ�����
	 */
	public void setInfo(String sTs, String sTr, String sFr, String sTo)
	{
		sTimeSend = sTs;
		sTimeLand = sTr;
		sFrom = sFr;
		sTarget = sTo;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CMoReq(byte[] b) throws Exception
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
		sAgent = DateConvertTools.b2s(b, 0, 6, (byte) 32); // MO ��Ϣ ��Դ Agent/SMSC ID (6
												// bytes)
		sMoid = DateConvertTools.b2s(b, 6, 10, (byte) 32); // MO ��¼ID (10 bytes)
		sTimeSend = DateConvertTools.b2s(b, 16, 14, (byte) 32); // ����ʱ�� (14 bytes)
														// yyyymmddhhmmss
		sTimeLand = DateConvertTools.b2s(b, 30, 14, (byte) 32); // �յ�ʱ�� (14 bytes)
														// yyyymmddhhmmss
		sFrom = DateConvertTools.b2s(b, 44, 21, (byte) 32); // Դ�ֻ����� (21 bytes)
		sTarget = DateConvertTools.b2s(b, 65, 21, (byte) 32); // Ŀ���ֻ����� (21 bytes)
		sService = DateConvertTools.b2s(b, 86, 10, (byte) 32); // ҵ����� (10 bytes)
		bFormat = b[96]; // ��Ϣ��ʽ (1 bytes)
		iLength = DateConvertTools.b2i(b, 97, 4); // ��Ϣ���� (4 bytes)
		if (iLength < 1)
		{
			bMessage = null;
		} else
		{
			bMessage = new byte[iLength];
			System.arraycopy(b, 101, bMessage, 0, iLength);
		}
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[101 + iLength];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32);
		DateConvertTools.s2b(sMoid, body, 6, 10, (byte) 32);
		DateConvertTools.s2b(sTimeSend, body, 16, 14, (byte) 32);
		DateConvertTools.s2b(sTimeLand, body, 30, 14, (byte) 32);
		DateConvertTools.s2b(sFrom, body, 44, 21, (byte) 32);
		DateConvertTools.s2b(sTarget, body, 65, 21, (byte) 32);
		DateConvertTools.s2b(sService, body, 86, 10, (byte) 32);
		body[96] = bFormat;
		DateConvertTools.i2b(iLength, body, 97, 4);
		DateConvertTools.addb(bMessage, body, 101, iLength, (byte) 0);
	}

	// {{DECLARE_CONTROLS
	// }}

	public String toString()
	{
		String sTmp = null;
		try
		{
			sTmp = new String(bMessage);
		} catch (Exception e)
		{
		}
		if (sTmp == null)
		{
			sTmp = "{Len=" + iLength + ",Data=" + bMessage + "}";
		}

		String s = "\r\nAgent:" + sAgent + "'" + "\r\n From:" + sFrom + "'" + "\r\n   To:"
				+ sTarget + "'" + "\r\n  Msg:" + sTmp;
		return s;

	}

}