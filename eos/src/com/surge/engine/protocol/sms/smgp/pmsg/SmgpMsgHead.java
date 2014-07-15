/*
 * Title: mas20
 * @author Administrator
 * Created on 2008-5-19 
 */

package com.surge.engine.protocol.sms.smgp.pmsg;

import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

public class SmgpMsgHead implements Serializable
{

	private static final long serialVersionUID = -3439299846970905993L;

	public static final int size = 12;

	// Buffer buf=BufferFactory.allocate(12);
	private int msgLength;

	private int commandID;

	private int seqID;

	// private AppendTools appendTools = new AppendTools(size);
	// private SubtractTools subtractTools = new SubtractTools(null, 0);

	public SmgpMsgHead()
	{
	}

	public SmgpMsgHead(IoBuffer bf)
	{
		this.msgLength = bf.getInt();
		this.commandID = bf.getInt();
		this.seqID = bf.getInt();
	}

	public SmgpMsgHead(int body_length, int cmd_id, int seq_id)
	{
		this.msgLength = body_length + size;
		this.commandID = cmd_id;
		this.seqID = seq_id;
	}

	public IoBuffer getBuffer()
	{
		IoBuffer buffer = IoBuffer.allocate(20);
		buffer.putInt(this.msgLength);
		buffer.putInt(this.commandID);
		buffer.putInt(this.seqID);
		buffer.flip();
		return buffer;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(100);
		sb.append("msgLength:");
		sb.append(msgLength);
		sb.append(" cmd_id:0x");
		sb.append(Integer.toHexString(commandID));
		sb.append(" seq_id:");
		sb.append(seqID);
		return sb.toString();
	}

	public int getMsgLength()
	{
		return msgLength;
	}

	public void setMsgLength(int msgLength)
	{
		this.msgLength = msgLength;
	}

	public int getCommandID()
	{
		return commandID;
	}

	public void setCommandID(int commandID)
	{
		this.commandID = commandID;
	}

	public int getSeqID()
	{
		return seqID;
	}

	public void setSeqID(int seqID)
	{
		this.seqID = seqID;
	}
}
