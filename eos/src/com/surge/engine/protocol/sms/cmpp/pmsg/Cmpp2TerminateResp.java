// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;


public class Cmpp2TerminateResp extends Cmpp2SendObject
{
	private static final long serialVersionUID = 1163801703390910226L;

	public Cmpp2TerminateResp()
	{
	}

	public Cmpp2TerminateResp(int seq_id)
	{
		head = new Cmpp2MsgHead(0, Common.CMPP_TERMINATE_RESP, seq_id);
	}
}
