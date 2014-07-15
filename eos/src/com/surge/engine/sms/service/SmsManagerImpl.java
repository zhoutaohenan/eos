package com.surge.engine.sms.service;

import org.apache.log4j.Logger;

import com.surge.engine.sms.send.SenderManager;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-9
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsManagerImpl implements SmsManager
{
	private static Logger logger = Logger.getLogger(SmsManagerImpl.class);

	private SenderManager senderManager = null;

	private GwFeeMgr gwFeeMgr=null ;
	
	private SmsHandler smsHandler;

	public SmsManagerImpl(SmsHandler smsHandler)
	{
		this.smsHandler=smsHandler;
	}
	@Override
	public void start()
	{
		gwFeeMgr = new GwFeeMgr();
		senderManager = new SenderManager(smsHandler,gwFeeMgr);
		senderManager.start();
		logger.info("SmsManager start success......");
	}

	@Override
	public void stop()
	{
		if (senderManager != null)
		{
			senderManager.stop();
		}
		logger.info("SmsManager stop success......");
	}

}
