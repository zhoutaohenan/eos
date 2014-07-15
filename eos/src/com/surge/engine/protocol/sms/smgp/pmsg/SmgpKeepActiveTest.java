package com.surge.engine.protocol.sms.smgp.pmsg;

import com.surge.engine.protocol.sms.cmpp.pmsg.Common;
import com.surge.engine.util.Tools;

public class SmgpKeepActiveTest extends SmgpSendObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9132552460982632992L;

	public SmgpKeepActiveTest()
	{
		head = new SmgpMsgHead(0, Common.SMGP_ACTIVE_TEST, Tools.getSeqId());
	}
}
