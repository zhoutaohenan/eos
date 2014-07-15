// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;

public class Cmpp2MsgHead implements Externalizable
{
	private static final long serialVersionUID = -4869846089382556071L;

	public static final int size = 12;

	private int total_length;
	private int cmd_id;
	private int seq_id;

	private AppendUtils appendTools = new AppendUtils(size);
	private SubtractTools subtractTools = new SubtractTools(null, 0);

	public Cmpp2MsgHead()
	{
	}

	public Cmpp2MsgHead(byte[] bys)
	{
		subtractTools.reset(bys, 0);
		total_length = subtractTools.getInt();
		cmd_id = subtractTools.getInt();
		seq_id = subtractTools.getInt();
	}

	public Cmpp2MsgHead(int body_length, int cmd_id, int seq_id)
	{
		this.total_length = body_length + size;
		this.cmd_id = cmd_id;
		this.seq_id = seq_id;
	}

	public void reset(byte[] bys)
	{
		subtractTools.reset(bys, 0);
		total_length = subtractTools.getInt();
		cmd_id = subtractTools.getInt();
		seq_id = subtractTools.getInt();
	}

	public byte[] getOutBytes()
	{
		appendTools.reset();
		appendTools.appendInt(total_length);
		appendTools.appendInt(cmd_id);
		appendTools.appendInt(seq_id);
		byte[] buf = appendTools.getOutBytes();
		return buf;
	}

	public int getCmdId()
	{
		return cmd_id;
	}

	public int getSeqId()
	{
		return seq_id;
	}

	public int getTotalLength()
	{
		return total_length;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(100);
		sb.append("total_length:");
		sb.append(total_length);
		sb.append(" cmd_id:0x");
		sb.append(Integer.toHexString(cmd_id));
		sb.append(" seq_id:");
		sb.append(seq_id);
		return sb.toString();
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.seq_id = in.readInt();
		this.cmd_id = in.readInt();
		this.total_length = in.readInt();
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(this.seq_id);
		out.writeInt(this.cmd_id);
		out.writeInt(this.total_length);
	}
}
