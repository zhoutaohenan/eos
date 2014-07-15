package com.surge.communication.framework.net;

import org.apache.mina.core.session.IoSession;

import com.surge.communication.framework.common.PMessage;

/**
 * 
 * һ������
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface Client
{

	/**
	 * ����һ����Ϣ
	 * 
	 * @param pMessage
	 * @return
	 */
	int sendPMessage(PMessage pMessage);

	/**
	 * 
	 * TCP�����Ƿ���Ч
	 * 
	 * @return boolean
	 * @throws
	 */
	boolean isAvailable();

	/**
	 * 
	 * ����tcp�����Ƿ���Ч
	 * 
	 * @param available
	 *            void
	 * @throws
	 */
	void setUnAvailable();

	/**
	 * 
	 * �Ƿ��¼����˳ɹ�
	 * 
	 * @return boolean
	 * @throws
	 */
	boolean isLogined();

	/**
	 * 
	 * �����Ƿ��¼�ɹ�
	 * 
	 * @param logined
	 *            void
	 * @throws
	 */
	void setLogined(boolean logined);

	/**
	 * 
	 * ��ȡ���һ�ν���ʱ��
	 * 
	 * @return long
	 * @throws
	 */
	long getLastReceveTime();

	/**
	 * 
	 * �������һ�ν���ʱ��
	 * 
	 * @param lastReceveTime
	 *            void
	 * @throws
	 */
	void setLastReceveTime(long lastReceveTime);

	/**
	 * 
	 * ��ȡ�����������Ǹ�һЭ���˺�
	 * 
	 * @return String
	 * @throws
	 */
	String getProtocolId();

	/**
	 * 
	 * ����IoSession void
	 * 
	 * @throws
	 */
	void setIoSession(IoSession ioSession);

}
