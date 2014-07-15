package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client �޸�����
 */
public class CCancelSendReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_CANCELTIMER;

	public static final byte BODY_LEN = 59;

	public String sPhone = ""; // �����˺��� 21 BYTES

	public String sPredate = ""; // ��ʱʱ�� 20 BYTES

	public String sContent = ""; // ���ݹؼ���18 Bytes

	public CCancelSendReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CCancelSendReq(byte[] b) throws Exception
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
		sPhone = DateConvertTools.b2s(b, 0, 21, (byte) 32);
		sPredate = DateConvertTools.b2s(b, 21, 20, (byte) 32);
		sContent = DateConvertTools.b2s(b, 41, 18, (byte) 32);
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[BODY_LEN];
		DateConvertTools.s2b(sPhone, body, 0, 21, (byte) 32);
		DateConvertTools.s2b(sPredate, body, 21, 20, (byte) 32);
		DateConvertTools.s2b(sContent, body, 41, 18, (byte) 32);
	}

	// {{DECLARE_CONTROLS
	// }}

}