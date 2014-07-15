package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.communication.framework.common.PMessage;

public class SgipSendBase extends PMessage
{
	private static final long serialVersionUID = 7640181143593177663L;

	protected SgipMsgHead head;

	protected byte[] body;

	protected transient CharsetEncoder encoder = Charset.defaultCharset().newEncoder();

	protected transient CharsetDecoder decoder = Charset.defaultCharset().newDecoder();

	@Override
	public int getCommonId()
	{
		return this.head.getCommandId();
	}

	@Override
	public byte[] getOut()
	{
		IoBuffer buffer = IoBuffer.allocate(head.getTotalLength()).setAutoExpand(true);
		buffer.setAutoShrink(true);
		buffer.put(head.getBuffer());
		buffer.put(body);
		return this.getBytesFromBuffer(buffer);
	}

	@Override
	public String getSeqId()
	{
		return String.valueOf(head.getseqID());
	}

	public SgipMsgHead getHead()
	{
		return head;
	}

	@Override
	public IoBuffer getIoBuffer()
	{
		IoBuffer buffer = IoBuffer.allocate(head.getTotalLength()).setAutoExpand(true);
		buffer.put(head.getBuffer());
		buffer.put(body);
		return buffer;
	}
	public byte[] getBytesFromBuffer(IoBuffer buffer)
	{
		buffer.flip();
		byte[] bytes = new byte[buffer.limit()];
		buffer.get(bytes);
		return bytes;
	}


}