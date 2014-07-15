package com.surge.engine.protocol.sms.smgp.pmsg;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.communication.framework.common.PMessage;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-9-17
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */

public class SmgpSendObject extends PMessage
{

	private static final long serialVersionUID = 1406461064922881953L;

	protected SmgpMsgHead head;

	protected byte[] body;

	protected transient CharsetEncoder encoder = Charset.defaultCharset().newEncoder();

	protected transient CharsetDecoder decoder = Charset.defaultCharset().newDecoder();

	@Override
	public int getCommonId()
	{
		return this.head.getCommandID();
	}

	@Override
	public IoBuffer getIoBuffer()
	{
		IoBuffer buffer = IoBuffer.allocate(head.getMsgLength()).setAutoExpand(true);
		buffer.put(head.getBuffer());
		if(body!=null)
		{
			buffer.put(body);
		}
		return buffer;
	}

	@Override
	public byte[] getOut()
	{
		IoBuffer buffer = IoBuffer.allocate(head.getMsgLength()).setAutoExpand(true);
		buffer.setAutoShrink(true);
		buffer.put(head.getBuffer());
		if (body != null)
			buffer.put(body);
		return this.getBytesFromBuffer(buffer);
	}

	@Override
	public String getSeqId()
	{
		return String.valueOf(this.head.getSeqID());
	}

	public byte[] getBytesFromBuffer(IoBuffer buffer)
	{
		buffer.flip();
		byte[] bytes = new byte[buffer.limit()];
		buffer.get(bytes);
		return bytes;
	}

}
