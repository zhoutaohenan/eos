package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Server ���Ͷ��� �󷵻ظ� Client
 */
public class CSendPromptRet extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_SENDPROMPT | ((byte) 0x80);

	public String sReqID; // MT ����Ϣ ID (10 bytes)

	public String sAgent; // ���Ͷ���Ϣʹ�õ� Agent/SMSC ID (6 bytes)

	public byte bStatus = 0x00; // ���ͽ�� (1 byte)

	public String sMsg = "";

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 */
	public CSendPromptRet(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CSendPromptRet(byte[] b) throws Exception
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
		bStatus = b[16];
		try
		{
			sMsg = new String(b, 17, b.length - 17);
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
		body = new byte[17 + b.length];
		DateConvertTools.s2b(sReqID, body, 0, 10, (byte) 32);
		DateConvertTools.s2b(sAgent, body, 10, 6, (byte) 32);
		body[16] = bStatus;
		for (int i = 17; i < 17 + b.length; i++)
		{
			body[i] = b[i - 17];
		}
	}

	public String toString()
	{
		return "Status=" + (int) bStatus + ",ReqID=" + sReqID + ",Route:" + sAgent + ",Msg=" + sMsg;
	}

	// {{DECLARE_CONTROLS
	// }}
}
