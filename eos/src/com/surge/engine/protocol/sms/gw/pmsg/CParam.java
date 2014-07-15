package com.surge.engine.protocol.sms.gw.pmsg;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;
import com.surge.engine.protocol.sms.gw.util.GwTools;

/**
 * Server 与 Client 之间数据包的超类，本部分只实现了构造 数据包头，Server 收到请求数据后，构造本类的对象，对每个和 Server 象连
 * 的 Agent 或 SMSC 的模块，必须实现将本类对象转化成各 SMSC/Agent 所需要 的协议。对 MO 数据作反向处理，产生本类对象
 */

public abstract class CParam extends PMessage
{
	public final static int HEAD_LEN = 27;

	public int iTotalLen = 0; // 4 bytes , 数据包总长度

	public byte bCommand = 0; // 指令类型, 1 BYTE

	public String sUserID = ""; // 8 bytes,不足尾部用空格填, 用户ID

	public String sSerialNo = ""; // (10
									// bytes)数据包序列号，有发起数据包的一方指定，应答方的序列号必须和发起方提供的序列号一致

	// public static String sTimeStamp="";//2004-01-15 anny
	public int iTimeStamp = 0;

	public int iContentLen = 0; // 数据体长度, 4 BYTE

	public byte[] body = null; // 数据实体

	public String sReqIDs = ""; // anny 2004-09-17

	public String smscID = "";

	// 审计
	public boolean isClose = false;

	public boolean isLog = false;

	public long logID = 0;

	public int sendNum = 0;

	// 审计
	/**
	 * 以数据包构造对象
	 * 
	 * @param 数据包字节数组
	 *            ,前面 4 字节表示包总长度字节除外
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
	 * 构造函数
	 * 
	 * @param sUser
	 *            数据包用户(ClientID/AgentID)
	 * @param iCmd
	 *            数据包指令类型
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
	 * 设置数据包的实体数据
	 * 
	 * @param b
	 *            字节数据
	 * @return 数据包总长度
	 */
	public int setBody(byte[] b)
	{
		body = b;
		return getTotal();
	}

	/**
	 * 取得数据包总长度
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
	 * 组织数据报 具体内容
	 */
	protected abstract void makeBody();

	/**
	 * 生成一个符合系统协议的原始数据报
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
