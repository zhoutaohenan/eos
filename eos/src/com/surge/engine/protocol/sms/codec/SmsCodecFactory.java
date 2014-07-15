
package com.surge.engine.protocol.sms.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class SmsCodecFactory implements ProtocolCodecFactory
{

	private ProtocolDecoder protocolDecoder;

	private ProtocolEncoder protocolEncoder;

	public SmsCodecFactory(ProtocolDecoder decoder, ProtocolEncoder encoder)
	{

		this.protocolDecoder = decoder;
		this.protocolEncoder = encoder;

	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception
	{

		return protocolDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception
	{

		return protocolEncoder;
	}

}
