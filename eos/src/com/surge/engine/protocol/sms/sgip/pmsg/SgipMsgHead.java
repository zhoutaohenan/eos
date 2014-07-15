package com.surge.engine.protocol.sms.sgip.pmsg;

import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

public class SgipMsgHead implements Serializable
{
	private static final long serialVersionUID = -5153903006855736939L;

	public static final int size = 20;

	private int msgLength;

	private int commandID;

	private String seqNum;

	private long nodeID;

	private int datetime;

	private int seqID;

	public SgipMsgHead(IoBuffer buffer)
	{
		// IoBuffer buffer = IoBuffer.wrap(bys);

		// Tools2 tool = new Tools2(bys, 0);
		this.msgLength = buffer.getInt();
		// this.msgLength = tool.getInt();
		this.commandID = buffer.getInt();
		// this.commandID = tool.getInt();

		// this.nodeID = tool.getInt2();
		this.nodeID = buffer.getInt();
		this.datetime = buffer.getInt();
		// this.datetime = tool.getInt();
		// this.seqID = tool.getInt();
		this.seqID = buffer.getInt();
		this.seqNum = String.valueOf(this.nodeID) + " " + String.valueOf(this.datetime) + " "
				+ String.valueOf(this.seqID);
	}

	public SgipMsgHead(int body_length, int commandID, long nodeID, int datetime, int seqID)
	{
		this.msgLength = (body_length + 20);
		this.commandID = commandID;
		this.nodeID = nodeID;
		this.datetime = datetime;
		this.seqID = seqID;
		this.seqNum = String.valueOf(nodeID) + " " + String.valueOf(datetime) + " "
				+ String.valueOf(seqID);
	}

	public IoBuffer getBuffer()
	{
		IoBuffer buffer = IoBuffer.allocate(20);
		buffer.putInt(this.msgLength);
		buffer.putInt(this.commandID);
		buffer.putInt((int) this.nodeID);
		buffer.putInt(this.datetime);
		buffer.putInt(this.seqID);
		buffer.flip();
		return buffer;
	}

	public int getCommandId()
	{
		return this.commandID;
	}

	public int getTotalLength()
	{
		return this.msgLength;
	}

	public int bodySize()
	{
		return (this.msgLength - 20);
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(101).append("msgLength:").append(this.msgLength).append(
				" commandID:").append(this.commandID).append(" seqNum:").append(this.seqNum)
				.append(" nodeID:").append(this.nodeID).append(" datetime:").append(this.datetime)
				.append(" seqID:").append(this.seqID);

		return sb.toString();
	}

	public String getSeqNum()
	{
		return this.seqNum;
	}

	public long getNodeID()
	{
		return this.nodeID;
	}

	public int getseqID()
	{
		return this.seqID;
	}

	public int getDatetime()
	{
		return this.datetime;
	}
}