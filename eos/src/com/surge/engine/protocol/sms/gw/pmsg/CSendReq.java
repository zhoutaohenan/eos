package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client ���Ͷ���Ϣ����
 */
public class CSendReq extends CParam implements Serializable
{
	private static final long serialVersionUID = 2427553989019465651L;

	public static final byte CMD = Cmd.C_SEND;

	public int iErrorCode = 1; // ������֤��־��0���ɹ���1������δ��֤��2����������,3:�ӿڱ���

	public String sErrorCode = ""; // ����ԭ��.

	public String sAgent = ""; // //ԭ��ǰ6�ֽڴ���AgentID,���ڶ�Ϊ�����ţ�

	public String sRouteAgent = "";

	public String sAbleAgent = "";

	public String sPolicyId = "";

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

	public String sMoid = ""; // ��Ӧ MO ID (10 bytes)

	public int iLength; // ��Ϣ���� (4 bytes)

	public byte[] bMessage; // ��Ϣ�ֽ� (�� iLength ����)

	public int iSign; // ǩ������

	public byte[] bSign; // ǩ��

	public boolean isChina = true;

	public boolean isPriority = false;

	public boolean priority = false;// ͨ�����ȶ�ռ 2008.12.17

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
	public CSendReq(String tUser, String tFrom, String tTo, int tReport)
	{
		super(CMD, tUser);
		sAgent = "";
		sFrom = tFrom;
		sTarget = tTo;
		iReport = tReport;
		iErrorCode = 1;
		sErrorCode = "����δ������֤";
	}

	public CSendReq(String tUser)
	{
		super(CMD, tUser);
		sAgent = "";
		iErrorCode = 1;
		sErrorCode = "����δ������֤";
	}

	public void setParament(byte tFeeUser, String tService, String tFeePhone, String tFeeType,
			int tFee, byte tFormat)
	{
		if (tFeeType == "01")
		{
			sFeeType = tFeeType;
			bFeeUser = 0;
			iFee = 0;
			sFeePhone = "";
			sService = tService;
		} else if ((tFeeType == "02") || (tFeeType == "03"))
		{
			sFeeType = tFeeType;
			bFeeUser = tFeeUser;
			if (bFeeUser == 2)
			{
				iErrorCode = 2;
				sErrorCode = "����[sFeeUser]����.";
				return;
			}
			sFeePhone = tFeePhone;
			iFee = tFee;
			sService = tService;
		} else
		{
			iErrorCode = 3;
			sErrorCode = "�ӿڱ���,tFeeType!=01/02/03";
			return;
		}
		bFormat = tFormat;
		iErrorCode = 0;
		sErrorCode = "������ȷ.";
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
		sMoid = sOm;
	}

	/**
	 * ���÷��͡�����ʱ��
	 */
	public void setAddress(String s1, String s2)
	{
		sFrom = s1;
		sTarget = s2;
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
	public CSendReq(byte[] b) throws Exception
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

		sTarget = DateConvertTools.b2s(b, 27, 21, (byte) 32).trim(); // �����ߺ��� (21 bytes)
		if (sTarget.startsWith("86"))
		{
			sTarget = sTarget.substring(2); // 2007.01.27
		}
		bFeeUser = b[48];
		sFeePhone = DateConvertTools.b2s(b, 49, 21, (byte) 32);
		sService = DateConvertTools.b2s(b, 70, 10, (byte) 32); // ������� (10 bytes)

		sFeeType = DateConvertTools.b2s(b, 80, 6, (byte) 32); // �������� (6 bytes)
		iFee = DateConvertTools.b2i(b, 86, 4); // ����(��) (4 bytes)

		bFormat = b[90]; // ��Ϣ��ʽ (1 byte)
		sMoid = DateConvertTools.b2s(b, 91, 10, (byte) 32); // ��Ӧ MO ID (10 bytes)

		iLength = DateConvertTools.b2i(b, 101, 4); // ��Ϣ���� (4 bytes)
		if (iLength > 0)
		{
			bMessage = new byte[iLength];
			System.arraycopy(b, 105, bMessage, 0, iLength);
		}
		// 2007.04.16
		int iPot = 105 + iLength;
		if (b.length > iPot)
		{
			iSign = DateConvertTools.b2i(b, iPot, 4); // ǩ������ (4 bytes)

			if (iSign > 0)
			{
				bSign = new byte[iSign];
				System.arraycopy(b, iPot + 4, bSign, 0, iSign);
			}
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

		if (bSign == null)
		{
			iSign = 0;
		} else
		{
			iSign = bSign.length;
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

		int iLen = 0;
		if (iSign > 0)
		{ // 2007.04.16
			iLen = 109 + iLength + iSign;
		} else
		{
			iLen = 105 + iLength;
		}
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
		DateConvertTools.s2b(sMoid, body, 91, 10, (byte) 32); // ��Ӧ MO ID (10 bytes)
		DateConvertTools.i2b(iLength, body, 101, 4); // ��Ϣ���� (4 bytes)
		DateConvertTools.addb(bMessage, body, 105, iLength, (byte) 0); // ������Ϣ

		if (iSign > 0)
		{ // 2007.04.16
			DateConvertTools.i2b(iSign, body, 105 + iLength, 4); // ��Ϣ���� (4 bytes)
			DateConvertTools.addb(bSign, body, 105 + iLength + 4, iSign, (byte) 0); // ������Ϣ
		}
	}

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

		String s = "To:" + sTarget + ",From:" + sFrom + "\r\n Msg:" + sTmp;
		return s;
	}

	// {{DECLARE_CONTROLS
	// }}
}
