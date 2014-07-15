/*
 * Title: mas20
 * @author Administrator
 * Created on 2008-5-19 
 */

package com.surge.engine.protocol.sms.smgp.pmsg;
import com.surge.engine.protocol.sms.cmpp.pmsg.Common;
import com.surge.engine.util.Tools;

public class SmgpTerminate extends SmgpSendObject {
	private static final long serialVersionUID = -4761671697195508108L;

	public SmgpTerminate() {
        head = new SmgpMsgHead(0, Common.SMGP_TERMINATE, Tools.getSeqId());
    }
}
