package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;

public class SgipBind extends SgipSendBase
{
	private static final long serialVersionUID = -7175648235377655366L;
	private byte LoginType;
	private String LoginName;
	private String LoginPassowrd;
	private String reserve="";
	private byte result;

	public SgipBind()
	{
		
		this.result = 0;
	}

	public SgipBind(long nodeID, byte loginType, String loginName,
			String loginPassowrd)
	{
		this(nodeID, loginType, loginName, loginPassowrd, "");
	}

	public SgipBind(long nodeID, byte LoginType, String LoginName,
			String LoginPassowrd, String Reserve)
	{
		this.result = 0;

		this.LoginType = LoginType;
		this.LoginName = LoginName;
		this.LoginPassowrd = LoginPassowrd;
		this.reserve = Reserve;
		IoBuffer buffer = IoBuffer.allocate(41);
		// Tools1 tool = new Tools1(100);
		buffer.put(LoginType);
		// tool.appendByte(LoginType);
		try
		{
			buffer.putString(LoginName, 16, encoder);
			// tool.appendString(LoginName, 16);
			buffer.putString(LoginPassowrd, 16, encoder);
			// tool.appendString(LoginPassowrd, 16);
			buffer.putString(Reserve, 8, encoder);
			// tool.appendString(Reserve, 8);
			buffer.flip();
			this.body = buffer.array();
			this.head = new SgipMsgHead(this.body.length, 1, nodeID, Seq
					.getGlobalSeq2(), Seq.getGlobalSeq3());
		} catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
	
	}

	public SgipBind(IoBuffer buffer)
	{
		this.head=new SgipMsgHead(buffer);
		this.result = 0;
		//Tools2 t2 = new Tools2(bys, 0);
		this.LoginType = buffer.get();
		// this.LoginType = t2.getByte();
		if (this.LoginType != 2)
		{
			this.result = 4;
			return;
		}
		try
		{
			this.LoginName = buffer.getString(16, decoder);
			// this.LoginName = t2.getString(16);
			if ((this.LoginName != null) && (this.LoginName.length() > 0))
				this.LoginName = this.LoginName.trim();
			this.LoginPassowrd = buffer.getString(16, decoder);
			//this.LoginPassowrd = t2.getString(16);
			if ((this.LoginPassowrd != null) && (this.LoginPassowrd.length() > 0))
				this.LoginPassowrd = this.LoginPassowrd.trim();

//			if ((((!(pool.getUsr().equals(this.LoginName))) || (!(pool.getPwd()
//					.equals(this.LoginPassowrd)))))
//					&& (pool.getVerify() == 1))
//			{
//				this.result = 1;
//				return;
//			}
			this.reserve=buffer.getString(8, decoder);
		} catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
		//this.Reserve = t2.getString(8);
		this.result = 0;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(100);
		sb.append("LoginType:");
		sb.append(this.LoginType);
		sb.append(" LoginName:");
		sb.append(this.LoginName);
		sb.append(" LoginPassowrd:");
		sb.append(this.LoginPassowrd);
		sb.append(" result:");
		sb.append(this.result);
		return sb.toString();
	}

	public byte getResult()
	{
		return this.result;
	}

	public String getLoginName()
	{
		return this.LoginName;
	}
}