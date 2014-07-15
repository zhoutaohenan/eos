package com.surge.engine.protocol.sms.sgip.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.surge.engine.protocol.sms.sgip.pmsg.SgipSendBase;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SgipEncoder extends ProtocolEncoderAdapter
{
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception
	{
		SgipSendBase sgipPackage = (SgipSendBase) message;
		IoBuffer buf = sgipPackage.getIoBuffer();
		buf.flip();
		out.write(buf);
	}

}
