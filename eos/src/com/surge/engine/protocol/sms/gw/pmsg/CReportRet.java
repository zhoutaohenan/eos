//-------------------- anny 2003.12.09
package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client �յ� Report ��Ϣ�����ɱ�������͸� Server
 */
public class CReportRet extends CParam implements Serializable
{
	private static final long serialVersionUID = -8652476323581920607L;

	public static final byte CMD = Cmd.C_REPORT | ((byte) 0x80);

	public byte bStatus; // ״̬ (1 bytes)

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param st
	 *            ״̬
	 */
	public CReportRet(String sUser, byte st)
	{
		super(CMD, sUser);
		bStatus = st;
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CReportRet(byte[] b) throws Exception
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

	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = new byte[1];
		body[0] = bStatus;
	}

	// {{DECLARE_CONTROLS
	// }}

}