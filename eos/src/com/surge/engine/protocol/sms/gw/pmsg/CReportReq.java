//-------------------- anny 2003.12.09
package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server �յ� REPORT ��Ϣ�����ɱ�������͸� Client
 */
public class CReportReq extends CParam implements Serializable
{
	private static final long serialVersionUID = -9092777387808458712L;

	public static final byte CMD = Cmd.C_REPORT;

	public String sReqID; // MT ����Ϣ ID (10 bytes)

	public String sAgent; // ���Ͷ���Ϣʹ�õ� Agent/SMSC ID (6 bytes)

	public byte bStatus = 0x00; // ���ͽ�� (1 byte)

	public String sMsg = "";

	public String sPhone; // �ֻ�����

	public String sRid = null;

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CReportReq(String sUser, String sAgnt)
	{
		super(CMD, sUser);
		sAgent = sAgnt;
	}

	/**
	 * ���ñ�������
	 */
	public void setInfo(String sReq, String sPhone, byte bStatus, String sMsg)
	{
		this.sReqID = sReq;
		this.sPhone = sPhone;
		this.bStatus = bStatus;
		this.sMsg = sMsg;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CReportReq(byte[] b) throws Exception
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

		sReqID = DateConvertTools.b2s(b, 0, 10, (byte) 32);
		sAgent = DateConvertTools.b2s(b, 10, 6, (byte) 32);
		sPhone = DateConvertTools.b2s(b, 16, 21, (byte) 32);
		bStatus = b[37];
		try
		{
			sMsg = new String(b, 38, b.length - 38,"gbk");
		} catch (Exception e)
		{
		}
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		byte[] b = {};
		if (sMsg != null || sMsg.length() > 0)
		{
			b = sMsg.getBytes();
		}

		body = new byte[38 + b.length];

		DateConvertTools.s2b(sReqID, body, 0, 10, (byte) 32);
		DateConvertTools.s2b(sAgent, body, 10, 6, (byte) 32);
		DateConvertTools.s2b(sPhone, body, 16, 21, (byte) 32);
		body[37] = bStatus;
		for (int i = 38; i < 38 + b.length; i++)
		{
			body[i] = b[i - 38];
		}
	}

	// {{DECLARE_CONTROLS
	// }}

	public String toString()
	{

		String s = "\r\nAgent:" + sAgent + "'" + "\r\n ReqID:" + sReqID + "'" + "\r\n Phone:"
				+ sPhone + "'" + "\r\n status:" + this.bStatus + "'"; //+ "\r\n  Msg:" + sMsg;
		return s;

	}

}
