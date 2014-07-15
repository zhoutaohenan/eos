package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client ���Ͷ���Ϣ����
 */
public class CSendClusterReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_SENDCLUSTER;

	public int iErrorCode = 1; // ������֤��־��0���ɹ���1������δ��֤��2����������,3:�ӿڱ���

	public String sErrorCode = ""; // ����ԭ��.

	public String sAgent = ""; // ʹ�õ� Agent/SMSC ID (6 bytes)

	public String sRouteAgent = "";

	public String sAbleAgent = "";

	public String sPolicyId = "";

	public String sFrom = ""; // �����ߺ��� (21 bytes)

	public int iTarget = 0;

	public String[] gTarget = null; // �����ߺ��� (21 bytes)

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

	public boolean isSave = false;

	public boolean priority = false;// ͨ�����ȶ�ռ 2008.12.17

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
	public CSendClusterReq(String tUser, String tFrom, int tReport)
	{
		super(CMD, tUser);
		sAgent = "";
		sFrom = tFrom;
		iReport = tReport;
		iErrorCode = 1;
		sErrorCode = "����δ������֤";
	}

	public CSendClusterReq(String tUser)
	{
		super(CMD, tUser);
		gTarget = new String[100];
		sAgent = "";
		iErrorCode = 1;
		sErrorCode = "����δ������֤";
	}

	public void SetTarget(int tnTarget, String[] tTarget)
	{
		iTarget = tnTarget;
		if ((iTarget < 1) || (iTarget > 101))
		{
			iErrorCode = 2;
			sErrorCode = "����(iTarget<1)||(iTarget>101)����.";
			return;
		}
		if (gTarget == null)
		{
			gTarget = new String[iTarget];
		}
		for (int i = 0; i < iTarget; i++)
		{
			gTarget[i] = tTarget[i];
		}
	}

	public void setParament(byte tFeeUser, String tService, String tFeePhone, String tFeeType,
			int tFee, byte tFormat)
	{
		if (tFeeType == "01")
		{
			sFeeType = tFeeType;
			bFeeUser = 0;
			iFee = 0;
			sService = "SURGESMS";
			sFeePhone = "0";
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
	public CSendClusterReq(byte[] b) throws Exception
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
		int iCurid = 0;
		byte[] b = body;
		sAgent = DateConvertTools.b2s(b, 0, 6, (byte) 32); // ʹ�õ� Agent/SMSC ID (6 bytes)
		sFrom = DateConvertTools.b2s(b, 6, 21, (byte) 32); // �����ߺ��� (21 bytes)
		iTarget = DateConvertTools.b2i(b, 27, 1); // ���պ������
		iCurid = 28;
		if (iTarget > 0)
		{
			gTarget = new String[iTarget];
			for (int i = 0; i < iTarget; i++)
			{
				gTarget[i] = DateConvertTools.b2s(b, 28 + 21 * i, 21, (byte) 32).trim(); // �����ߺ���
																				// (21
																				// bytes)
				if (gTarget[i].startsWith("86"))
				{
					gTarget[i] = gTarget[i].substring(2); // 2007.01.27
				}
			}
			iCurid = 28 + 21 * iTarget;
		} else if (iTarget == 0)
		{
			gTarget = new String[1];
			gTarget[0] = DateConvertTools.b2s(b, 28, 21, (byte) 32).trim(); // �����ߺ��� (21
																	// bytes)
			if (gTarget[0].startsWith("86"))
			{
				gTarget[0] = gTarget[0].substring(2); // 2007.01.27
			}
			iCurid = 28 + 21;
		}
		bFeeUser = b[iCurid];
		sFeePhone = DateConvertTools.b2s(b, iCurid + 1, 21, (byte) 32);
		sService = DateConvertTools.b2s(b, iCurid + 22, 10, (byte) 32); // ������� (10 bytes)
		sFeeType = DateConvertTools.b2s(b, iCurid + 32, 6, (byte) 32); // �������� (6 bytes)
		iFee = DateConvertTools.b2i(b, iCurid + 38, 4); // ����(��) (4 bytes)

		bFormat = b[iCurid + 42]; // ��Ϣ��ʽ (1 byte)
		sMoid = DateConvertTools.b2s(b, iCurid + 43, 10, (byte) 32); // ��Ӧ MO ID (10 bytes)
		iLength = DateConvertTools.b2i(b, iCurid + 53, 4); // ��Ϣ���� (4 bytes)
		if (iLength > 0)
		{
			bMessage = new byte[iLength];
			System.arraycopy(b, iCurid + 57, bMessage, 0, iLength);
		}

		// 2007.04.16
		int iPot = iCurid + 57 + iLength;
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
		int iLen = 0;
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
		if (iTarget == 0)
		{
			iLen = 106 + iLength;
		} else if (iTarget > 0)
		{
			iLen = 85 + iLength + 21 * iTarget;
		}

		if (iSign > 0)
		{
			iLen = iLen + 4 + iSign;
		}
		body = new byte[iLen];
		DateConvertTools.s2b(sAgent, body, 0, 6, (byte) 32); // ʹ�õ� Agent/SMSC ID (6 bytes)
		DateConvertTools.s2b(sFrom, body, 6, 21, (byte) 32); // �����ߺ��� (21 bytes)

		int iCurid = 28;
		if (iTarget > 0)
		{
			if (gTarget.length < iTarget)
			{
				iTarget = gTarget.length;
				DateConvertTools.i2b(iTarget, body, 27, 1); // ���պ������
			} else
			{
				DateConvertTools.i2b(iTarget, body, 27, 1); // ���պ������
			}
			for (int i = 0; i < iTarget; i++)
			{
				DateConvertTools.s2b(gTarget[i], body, 28 + 21 * i, 21, (byte) 32); // �����ߺ���
																			// (21
																			// bytes)
			}
			iCurid = 28 + 21 * iTarget;
		} else if (iTarget == 0)
		{
			DateConvertTools.i2b(iTarget, body, 27, 1); // ���պ������
			DateConvertTools.s2b(gTarget[0], body, 28, 21, (byte) 32); // �����ߺ��� (21 bytes)
			iCurid = 28 + 21;
		}
		body[iCurid] = bFeeUser;
		DateConvertTools.s2b(sFeePhone, body, iCurid + 1, 21, (byte) 32); // �����ֻ��� (21
																// bytes)
		DateConvertTools.s2b(sService, body, iCurid + 22, 10, (byte) 32); // ������� (10
																// bytes)
		DateConvertTools.s2b(sFeeType, body, iCurid + 32, 6, (byte) 32); // �������� (6 bytes)
		DateConvertTools.i2b(iFee, body, iCurid + 38, 4); // ����(��) (4 bytes)
		body[iCurid + 42] = bFormat; // ��Ϣ��ʽ (1 byte)
		DateConvertTools.s2b(sMoid, body, iCurid + 43, 10, (byte) 32); // ��Ӧ MO ID (10
															// bytes)
		DateConvertTools.i2b(iLength, body, iCurid + 53, 4); // ��Ϣ���� (4 bytes)
		DateConvertTools.addb(bMessage, body, iCurid + 57, iLength, (byte) 0); // ������Ϣ*/

		if (iSign > 0)
		{ // 2007.04.16
			DateConvertTools.i2b(iSign, body, iCurid + 57 + iLength, 4); // ��Ϣ���� (4 bytes)
			DateConvertTools.addb(bSign, body, iCurid + 61 + iLength, iSign, (byte) 0); // ������Ϣ*/
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
		return "\r\n Route:" + sAgent + ",From:" + sFrom + ",Msg:" + sTmp;
	}

	// {{DECLARE_CONTROLS
	// }}
}
