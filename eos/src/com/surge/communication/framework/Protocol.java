package com.surge.communication.framework;

import com.surge.communication.framework.common.PMessage;

/**
 * һ��Э���˺�
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface Protocol {

	/**
	 * 
	 * ����Protocol void
	 * 
	 * @throws
	 */
	boolean start();

	/**
	 * 
	 * ֹͣProtocol void
	 * 
	 * @throws
	 */
	void stop();

	/***
	 * 
	 * ����һPMessage����Ϣ
	 * 
	 * @param pMessage
	 * @return int
	 * @throws
	 */
	int sendPMessage(PMessage pMessage);


	/**
	 * 
	 * ��ȡЭ���˺�ID
	 * 
	 * @return String
	 * @throws
	 */
	 String getProtocolId();

}
