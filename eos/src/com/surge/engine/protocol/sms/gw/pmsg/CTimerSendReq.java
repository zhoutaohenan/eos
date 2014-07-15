package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client ���Ͷ���Ϣ����
 */
public class CTimerSendReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_SENDTIMER;

	public String sAgent = ""; // //ԭ��ǰ6�ֽڴ���AgentID,���ڶ�Ϊ�����ţ�

	public String sRouteAgent = "";

	public String sAbleAgent = "";

	public String sFrom = ""; // �����ߺ��� (21 bytes)

	public String sTarget = ""; // �����ߺ��� (21 bytes)

	public int iReport = 0; // ��Ҫ״̬���� 0:����Ҫ״̬���棬1����Ҫ״̬���棬

	// 2:����SMC������3������Ϣ������״̬��
	// 4:������Ϣ��Я�����¼Ʒ���Ϣ�����·����û���Ҫ����״̬����

	public byte bFeeUser = 0; // �Ʒ��û�����, 0 ��Ŀ���ն˼Ʒ�,1 ��Դ�ն˼Ʒ�,2 ��SP�Ʒ�,3 ��ָ������Ʒ�

	public String sFeePhone = ""; // 21 bytes

	public String sFeeType = ""; // �������� (6 bytes)

	public int iFee; // ����(��) (4 bytes)

	public String sService = ""; // ������� (10 bytes)

	public byte bFormat; // ��Ϣ��ʽ (1 byte)

	public String predate = ""; // 20bytes��ʱ����ʱ��

	public int iLength; // ��Ϣ���� (4 bytes)

	public byte[] bMessage; // ��Ϣ�ֽ� (�� iLength ����)

	public boolean isChina = true;

	public boolean isSave = false;

	public byte[] cSign = new byte[0];

	public byte[] eSign = new byte[0];

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sRoute
	 *            ʹ�� Agent ID
	 */
	public CTimerSendReq(String tUser, String tFrom, String tTo, int tReport)
	{
		super(CMD, tUser);
		sAgent = "";
		sFrom = tFrom;
		sTarget = tTo;
		iReport = tReport;
	}

	public CTimerSendReq(String tUser)
	{
		super(CMD, tUser);
		sAgent = "";
	}

	public void setParament(byte tFeeUser, String tService, String tFeePhone, String tFeeType,
			int tFee, byte tFormat)
	{
		sFeeType = tFeeType;
		bFeeUser = tFeeUser;
		sFeePhone = tFeePhone;
		iFee = tFee;
		sService = tService;
		bFormat = tFormat;
		return;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param bFmt
	 *            ��Ϣ��ʽ
	 * @param bData
	 *            ��Ϣ����
	 * @param sOm
	 *            ԭ MO ID
	 */
	public void setMessage(byte bFmt, byte[] bData, String sOm)
	{
		bFormat = bFmt;
		if (bData != null)
		{
			iLength = bData.length;
			bMessage = bData;
		} else
		{
			bData = null;
			iLength = 0;
		}
	}

	/**
	 * ���÷��͡�����ʱ��
	 */
	public void setAddress(String s1, String s2)
	{
		sFrom = s1;
		sTarget = s2;
	}

	public void setTimer(String s1)
	{
		predate = s1;
	}

	/**
	 * �����շ�
	 */
	public void setFee(int iPay, String sCode, int iValue)
	{
		bFeeUser = (byte) iPay;
		sFeeType = sCode;
		iFee = iValue;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CTimerSendReq(byte[] b) throws Exception
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
		sFrom = DateConvertTools.b2s(b, 6, 21, (byte) 32); // �����ߺ��� (21 bytes)
		sTarget = DateConvertTools.b2s(b, 27, 21, (byte) 32); // �����ߺ��� (21 bytes)
		bFeeUser = b[48];
		sFeePhone = DateConvertTools.b2s(b, 49, 21, (byte) 32);
		sService = DateConvertTools.b2s(b, 70, 10, (byte) 32); // ������� (10 bytes)

		sFeeType = DateConvertTools.b2s(b, 80, 6, (byte) 32); // �������� (6 bytes)
		iFee = DateConvertTools.b2i(b, 86, 4); // ����(��) (4 bytes)

		bFormat = b[90]; // ��Ϣ��ʽ (1 byte)
		predate = DateConvertTools.b2s(b, 91, 20, (byte) 32); // ��Ӧ MO ID (10 bytes)

		iLength = DateConvertTools.b2i(b, 111, 4); // ��Ϣ���� (4 bytes)
		if (iLength > 0)
		{
			bMessage = new byte[iLength];
			System.arraycopy(b, 115, bMessage, 0, iLength);
		}

	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		if (bMessage == null)
		{
			iLength = 0;
		} else
		{
			iLength = bMessage.length;
		}
		if (sFrom == null)
		{
			sFrom = "";
		}
		if (sAgent == null)
		{
			sAgent = "";
		}
		if (sService == null)
		{
			sService = "SURGESMS";

		}
		int iLen = 115 + iLength;
		body = new byte[iLen];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32); // ʹ�õ� Agent/SMSC ID (6 bytes)

		DateConvertTools.s2b(sFrom, body, 6, 21, (byte) 32); // �����ߺ��� (21 bytes)
		DateConvertTools.s2b(sTarget, body, 27, 21, (byte) 32); // �����ߺ��� (21 bytes)
		body[48] = bFeeUser;

		DateConvertTools.s2b(sFeePhone, body, 49, 21, (byte) 32); // �����ֻ��� (21 bytes)

		DateConvertTools.s2b(sService, body, 70, 10, (byte) 32); // ������� (10 bytes)
		DateConvertTools.s2b(sFeeType, body, 80, 6, (byte) 32); // �������� (6 bytes)
		DateConvertTools.i2b(iFee, body, 86, 4); // ����(��) (4 bytes)
		body[90] = bFormat; // ��Ϣ��ʽ (1 byte)
		DateConvertTools.s2b(predate, body, 91, 20, (byte) 32); // ��Ӧ MO ID (10 bytes)
		DateConvertTools.i2b(iLength, body, 111, 4); // ��Ϣ���� (4 bytes)
		DateConvertTools.addb(bMessage, body, 115, iLength, (byte) 0); // ������Ϣ
	}

	public String toString()
	{
		String sTmp = null;
		try
		{
			sTmp = new String(bMessage);
			if (sTmp == null)
			{
				sTmp = "{Len=" + iLength + ",Data=" + bMessage + "}";
			}
			return "To:" + sTarget + ",From:" + sFrom + "\r\n Msg:" + sTmp;
		} catch (Exception e)
		{
			return "";
		}
	}

	// {{DECLARE_CONTROLS
	// }}
}
