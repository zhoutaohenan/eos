package com.surge.engine.protocol.sms.gw.pmsg;

import java.io.Serializable;

import com.surge.engine.protocol.sms.gw.util.Cmd;
import com.surge.engine.protocol.sms.gw.util.DateConvertTools;

/**
 * Client 登录 Server 的登录结果
 */
public class CLoginRet extends CParam implements Serializable
{
	private static final long serialVersionUID = 5148636631316630682L;

	public static final byte CMD = Cmd.C_LOGIN | ((byte) 0x80);

	public byte bStatus = -1; // 登录结果

	public byte[] passwd = null; // 使用的加密密码 (16 bytes),

	// 如果为 null 表示不支持客户提出的加密算法，对数据不加密

	/**
	 * 有用户ID、密码 构造对象
	 * 
	 * @param sUser
	 *            Client ID
	 * @param bs
	 *            账户验证结果
	 */
	public CLoginRet(String sUser, byte bs)
	{
		super(CMD, sUser);
		bStatus = bs;
	}

	public void setPasswd(byte[] b)
	{
		if (b != null)
		{
			passwd = new byte[b.length];
			for (int i = 0; i < b.length; i++)
			{
				passwd[i] = b[i];
			}
		}
	}

	public byte[] getPasswd()
	{
		return passwd;
	}

	/**
	 * 根据数据包构造对象
	 * 
	 * @param b
	 *            数据包，(不包括首 4 字节表示数据包总长度的数据)
	 */
	public CLoginRet(byte[] b) throws Exception
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
		passwd = DateConvertTools.cutb(b, 1, 16, (byte) 0);
	}

	/**
	 * 实现 super 类 要求的－ 产生数据包 数据体字节
	 */
	protected void makeBody()
	{
		body = new byte[17];
		body[0] = bStatus;
		DateConvertTools.addb(passwd, body, 1, 16, (byte) 0);
	}

	public String toString()
	{
		String s = super.toString();
		s = s + "\r\n" + "STUS=" + (int) bStatus + "\r\n";

		if (passwd == null)
		{
			s = s + "Pwd=null";
		} else
		{
			s = s + "Pwd=" + new String(passwd);
		}

		return s;
	}

	// {{DECLARE_CONTROLS
	// }}
}