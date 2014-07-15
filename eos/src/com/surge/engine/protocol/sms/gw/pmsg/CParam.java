package com.surge.engine.protocol.sms.gw.pmsg;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;
import com.surge.engine.protocol.sms.gw.util.GwTools;

/**
 * Server �� Client ֮�����ݰ��ĳ��࣬������ֻʵ���˹��� ���ݰ�ͷ��Server �յ��������ݺ󣬹��챾��Ķ��󣬶�ÿ���� Server ����
 * �� Agent �� SMSC ��ģ�飬����ʵ�ֽ��������ת���ɸ� SMSC/Agent ����Ҫ ��Э�顣�� MO �����������������������
 */

public abstract class CParam extends PMessage
{
	public final static int HEAD_LEN = 27;

	public int iTotalLen = 0; // 4 bytes , ���ݰ��ܳ���

	public byte bCommand = 0; // ָ������, 1 BYTE

	public String sUserID = ""; // 8 bytes,����β���ÿո���, �û�ID

	public String sSerialNo = ""; // (10
									// bytes)���ݰ����кţ��з������ݰ���һ��ָ����Ӧ�𷽵����кű���ͷ����ṩ�����к�һ��

	// public static String sTimeStamp="";//2004-01-15 anny
	public int iTimeStamp = 0;

	public int iContentLen = 0; // �����峤��, 4 BYTE

	public byte[] body = null; // ����ʵ��

	public String sReqIDs = ""; // anny 2004-09-17

	public String smscID = "";

	// ���
	public boolean isClose = false;

	public boolean isLog = false;

	public long logID = 0;

	public int sendNum = 0;

	// ���
	/**
	 * �����ݰ��������
	 * 
	 * @param ���ݰ��ֽ�����
	 *            ,ǰ�� 4 �ֽڱ�ʾ���ܳ����ֽڳ���
	 */
	public CParam(byte[] b) throws Exception
	{
		// System.out.println("--init 0--");

		if (b == null || b.length < HEAD_LEN - 4)
		{
			throw new Exception("Invalid data package(1)!");
		}
		iTotalLen = 4 + b.length;

		bCommand = b[0];
		sUserID = DateConvertTools.b2s(b, 1, 8, (byte) 32);
		sSerialNo = DateConvertTools.b2s(b, 9, 10, (byte) 32);
		iContentLen = DateConvertTools.b2i(b, 19, 4);

		// System.out.println("User="+sUserID+",Cmd="+(int)bCommand+",CntLen="+iContentLen);
		if (iContentLen + HEAD_LEN - 4 > b.length)
		{
			throw new Exception("Invalid data package(2)!");
		} else if (iContentLen > 0)
		{
			body = new byte[iContentLen];
			System.arraycopy(b, HEAD_LEN - 4, body, 0, iContentLen);
		}
	}

	/**
	 * ���캯��
	 * 
	 * @param sUser
	 *            ���ݰ��û�(ClientID/AgentID)
	 * @param iCmd
	 *            ���ݰ�ָ������
	 */
	public CParam(byte iCmd, String sUser)
	{
		sUserID = sUser;
		if (sUserID == null)
		{
			sUser = "";
		}
		bCommand = iCmd;
		sSerialNo=GwTools.getSequenceStr();
	}

	/**
	 * �������ݰ���ʵ������
	 * 
	 * @param b
	 *            �ֽ�����
	 * @return ���ݰ��ܳ���
	 */
	public int setBody(byte[] b)
	{
		body = b;
		return getTotal();
	}

	/**
	 * ȡ�����ݰ��ܳ���
	 */
	public int getTotal()
	{
		if (body == null)
		{
			iContentLen = 0;
		} else
		{
			iContentLen = body.length;
		}
		iTotalLen = HEAD_LEN + iContentLen;
		return iTotalLen;
	}

	/**
	 * ��֯���ݱ� ��������
	 */
	protected abstract void makeBody();

	/**
	 * ����һ������ϵͳЭ���ԭʼ���ݱ�
	 */
	public byte[] getOut()
	{
		makeBody();
		int iLen = getTotal();
		byte[] b = new byte[iLen];
		DateConvertTools.i2b(iLen, b, 0, 4); // from 0
		b[4] = bCommand; // from 5
		DateConvertTools.s2b(sUserID, b, 5, 8, (byte) 32); // from 5
		DateConvertTools.s2b(sSerialNo, b, 13, 10, (byte) 32); // from 13
		DateConvertTools.i2b(iContentLen, b, 23, 4); // from 23

		if (iContentLen > 0)
		{
			System.arraycopy(body, 0, b, HEAD_LEN, body.length);
		}
		return b;
	}

	@Override
	public int getCommonId()
	{
		return this.bCommand;
	}

	@Override
	public IoBuffer getIoBuffer()
	{
		return null;
	}

	@Override
	public String getSeqId()
	{
		return sSerialNo;
	}
}
