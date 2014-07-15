package com.surge.engine.protocol.sms.smgp;

import java.util.List;

import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.surge.communication.framework.AbstractProtocol;
import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.codec.SmsCodecFactory;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.protocol.sms.smgp.client.SmgpClientIoHandler;
import com.surge.engine.protocol.sms.smgp.client.SmgpClientProcess;
import com.surge.engine.protocol.sms.smgp.codec.SmgpDecoder;
import com.surge.engine.protocol.sms.smgp.codec.SmgpEncoder;
import com.surge.engine.protocol.sms.smgp.service.SmgpKeepConnection;
import com.surge.engine.sms.conf.SmgpConfig;

public class SmgpProtocol extends AbstractProtocol
{

	private SmgpConfig config;

	private ConnectionPool pool = ConnectionPool.instance;

	private SmsProtocolHandler protocolHandler;
	
	private SmgpKeepConnection keepConnection;

	public SmgpProtocol(String protocolId, SmgpConfig config, SmsProtocolHandler protocolHandler)
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
				new SmgpDecoder(), new SmgpEncoder()));
		keepConnection=new SmgpKeepConnection(config, new SmgpClientIoHandler(new SmgpClientProcess(
				protocolHandler,config), protocolHandler), pool, codecFilter);
		return true;
	}

	@Override
	public void stop()
	{
		keepConnection.stop();
	}

}
