package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;

/**
 * Client �� Server ֮����������·������
 */
public class CLinkReq extends CParam implements Serializable
{
	private static final long serialVersionUID = 3799389194889736101L;
	public static final byte CMD = Cmd.C_LINK;

	/**
	 * ���û�ID������ �������
	 * 
	 * @param sUser
	 *            Client ID
	 * @param sAgnt
	 *            Agent/SMSC ID
	 */
	public CLinkReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * �������ݰ��������
	 * 
	 * @param b
	 *            ���ݰ���(�������� 4 �ֽڱ�ʾ���ݰ��ܳ��ȵ�����)
	 */
	public CLinkReq(byte[] b) throws Exception
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
	}

	/**
	 * ʵ�� super �� Ҫ��ģ� �������ݰ� �������ֽ�
	 */
	protected void makeBody()
	{
		body = null;
	}

	// {{DECLARE_CONTROLS
	// }}

}