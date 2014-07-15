package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.CharacterCodingException;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
/**
 * 
 * @description 
 * @project: esk
 * @Date:2010-8-10
 * @version  1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipDeliverResp extends SgipSendBase
{
	private static final long serialVersionUID = -3007650248773920383L;
	private static final Logger logger = Logger
			.getLogger(SgipDeliverResp.class);
	private int result;
	private String Reserve = "";

	public SgipDeliverResp(long nodeID, int datetime, int seq, byte result)
	{
		this.result = result;
//		this.Reserve = this.Reserve;
		// Tools1 tool = new Tools1(
		// 9);
		IoBuffer buffer = IoBuffer.allocate(9);
		buffer.put(result);
		// tool.appendByte(result);
		try
		{
			buffer.putString(this.Reserve, 8, encoder);
			// tool.appendString(this.Reserve, 8);
			this.body = buffer.array();// tool.getOutBytes();
			this.head = new SgipMsgHead(this.body.length, -2147483644, nodeID,
					datetime, seq);
		} catch (CharacterCodingException e)
		{
			logger.error("组SgipDeliverResp消息包失败", e);
		}

	}

	public int getResult()
	{
		return this.result;
	}

	public String getReserve()
	{
		return this.Reserve;
	}

	public String toString()
	{
		return toString() + " Result:" + this.result;
	}
}