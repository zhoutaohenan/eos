package com.surge.engine.protocol.sms.gw.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.surge.engine.protocol.sms.gw.pmsg.CParam;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class GwEncoder extends ProtocolEncoderAdapter
{
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception
	{
		CParam cParam = (CParam) message;
		IoBuffer buf = IoBuffer.wrap(cParam.getOut());
		out.write(buf);
	}

}
