package com.surge.engine.protocol.sms.cmpp;

import java.util.List;

import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.surge.communication.framework.AbstractProtocol;
import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.cmpp.codec.CmppDecoder;
import com.surge.engine.protocol.sms.cmpp.codec.CmppEncoder;
import com.surge.engine.protocol.sms.cmpp.net.CmppClientIoHandler;
import com.surge.engine.protocol.sms.cmpp.net.CmppClientProcess;
import com.surge.engine.protocol.sms.cmpp.service.CmppKeepConnection;
import com.surge.engine.protocol.sms.codec.SmsCodecFactory;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.sms.conf.Cmpp2Config;

public class CmppProtocol extends AbstractProtocol
{

	private Cmpp2Config config;

	private ConnectionPool pool = ConnectionPool.instance;

	private SmsProtocolHandler protocolHandler;

	private CmppKeepConnection keepConnection;

	public CmppProtocol(String protocolId, Cmpp2Config config, SmsProtocolHandler protocolHandler)
	{

		super(protocolId);
		this.config = config;
		this.protocolHandler = protocolHandler;
	}

	@Override
	public int sendPMessage(PMessage pMessage)
	{
		List<Client> clientList = this.pool.getActiveClient(this.config.getProtocolId());
		if (clientList.size() == 0)
		{
			return SendResult.DISCONNECECT_ERROR.getValue();
		}
		Client client = clientList.get(0);
		return client.sendPMessage(pMessage);
	}

	@Override
	public boolean start()
	{
		ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(new SmsCodecFactory(
				new CmppDecoder(), new CmppEncoder()));
		keepConnection = new CmppKeepConnection(config, new CmppClientIoHandler(
				new CmppClientProcess(protocolHandler,config), protocolHandler), pool, codecFilter);
		return true;
	}

	@Override
	public void stop()
	{
		if (keepConnection != null)
		{
			keepConnection.stop();
		}

	}

}
