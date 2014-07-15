// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;

import com.surge.engine.util.Tools;

public class Cmpp2KeepActive extends Cmpp2SendObject
{
	public Cmpp2KeepActive()
	{
		head = new Cmpp2MsgHead(0, Common.CMPP_ACTIVE_TEST, Tools.getSeqId());
	}
}
