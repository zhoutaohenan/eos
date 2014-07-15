package com.surge.engine.protocol.sms.smgp.pmsg;

import com.surge.engine.protocol.sms.cmpp.pmsg.Common;

public class SmgpKeepActiveTestResp extends SmgpSendObject
{
	private static final long serialVersionUID = 4698644121420935553L;

	public SmgpKeepActiveTestResp(int seq_id)
	{
		//body = new byte[1];
		//body[0] = (byte) 0;
		head = new SmgpMsgHead(0, Common.SMGP_ACTIVE_TEST_RESP, seq_id);
	}
	public SmgpKeepActiveTestResp()
	{

	}
}
