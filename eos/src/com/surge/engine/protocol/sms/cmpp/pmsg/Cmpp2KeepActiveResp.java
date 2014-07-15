// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;


public class Cmpp2KeepActiveResp extends Cmpp2SendObject
{
	public Cmpp2KeepActiveResp(int seq_id)
	{
		body = new byte[1];
		body[0] = (byte) 0;
		head = new Cmpp2MsgHead(body.length, Common.CMPP_ACTIVE_TEST_RESP, seq_id);
	}
}
