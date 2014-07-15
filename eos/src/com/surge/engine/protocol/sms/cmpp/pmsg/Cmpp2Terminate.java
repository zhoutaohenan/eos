// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;

import com.surge.engine.util.Tools;

public class Cmpp2Terminate extends Cmpp2SendObject
{
	public Cmpp2Terminate()
	{
		head = new Cmpp2MsgHead(0, Common.CMPP_TERMINATE, Tools.getSeqId());
	}
}
