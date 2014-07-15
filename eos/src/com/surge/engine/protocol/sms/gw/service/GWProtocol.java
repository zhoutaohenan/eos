package com.surge.engine.protocol.sms.gw.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import com.surge.communication.framework.AbstractProtocol;
import com.surge.communication.framework.ConnectionPool;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.codec.SmsCodecFactory;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.protocol.sms.gw.client.GwClientIoHandler;
import com.surge.engine.protocol.sms.gw.client.GwClientProcess;
import com.surge.engine.protocol.sms.gw.codec.GwDecoder;
import com.surge.engine.protocol.sms.gw.codec.GwEncoder;
import com.surge.engine.sms.conf.GwConfig;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class GWProtocol extends AbstractProtocol
{
	private static  Logger logger = Logger.getLogger(GWProtocol.class);

	private GwConfig config;

	private ConnectionPool pool = ConnectionPool.instance;

	private SmsProtocolHandler protocolHandler;

	private GwClientKeepConnection keepConnection;

	public GWProtocol(String protocolId, GwConfig gwConfig, SmsProtocolHandler protocolHandler)
	{
		super(protocolId);
		this.config = gwConfig;
		this.protocolHandler = protocolHandler;

	}

	/**
	 * GWЭ��㷢����Ϣ�ӿ�ʵ��
	 * 
	 * return 0: �ɹ� ,-1 ������Ч���õ�����, -2���������ƴ�,-3:����������
	 */
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
	public synchronized boolean start()
	{
		ProtocolCodecFilter codecFilter = new ProtocolCodecFilter(new SmsCodecFactory(
				new GwDecoder(), new GwEncoder()));
		keepConnection=new GwClientKeepConnection(config, new GwClientIoHandler(new GwClientProcess(
				protocolHandler), protocolHandler, config, pool), pool, codecFilter);
		return true;
	}

	@Override
	public void stop()
	{
		logger.info("��ʼ�ر�"+config.getProtocolId()+"ͨ��");
		keepConnection.stop();
	}
}
