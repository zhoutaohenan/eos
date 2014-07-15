/*
 * Title: mas20
 * @author Administrator
 * Created on 2008-5-19 
 */

package com.surge.engine.protocol.sms.smgp.pmsg;
import com.surge.engine.protocol.sms.cmpp.pmsg.Common;


public class SmgpTerminateResp extends SmgpSendObject {
	private static final long serialVersionUID = 2511938098625330489L;

	public SmgpTerminateResp(int seq_id) {
        head = new SmgpMsgHead(0, Common.SMGP_TERMINATE_RESP, seq_id);
    }
}
