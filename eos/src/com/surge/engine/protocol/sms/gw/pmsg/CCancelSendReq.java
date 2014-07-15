package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 修改密码
 */
public class CCancelSendReq extends CParam implements Serializable
{
	public static final byte CMD = Cmd.C_CANCELTIMER;

	public static final byte BODY_LEN = 59;

	public String sPhone = ""; // 接受人号码 21 BYTES

	public String sPredate = ""; // 定时时间 20 BYTES

	public String sContent = ""; // 内容关键字18 Bytes

	public CCancelSendReq(String sUser)
	{
		super(CMD, sUser);
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
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
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
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