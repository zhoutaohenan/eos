package com.surge.engine.protocol.sms.cmpp.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.surge.communication.framework.common.PMessage;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class CmppEncoder extends ProtocolEncoderAdapter
{
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception
	{
		PMessage pMessage = (PMessage) message;
		IoBuffer buf = IoBuffer.wrap(pMessage.getOut());
		out.write(buf);
	}
}
