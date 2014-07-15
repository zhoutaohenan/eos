package com.surge.communication.framework.conf;

/**
 * Э��������Ϣ(����Э��������Ϣ��Ҫʵ�ֲ���չ)
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface ProtocolConfig
{

	/**
	 * 
	 * ��ȡЭ���˺�ID
	 * 
	 * @return String
	 * @throws
	 */
	String getProtocolId();


	/**
	 * 
	 * ��ȡIP
	 * 
	 * @return String
	 * @throws
	 */
	String getIp();


	/**
	 * 
	 * ��ȡ�˿�
	 * 
	 * @return int
	 * @throws
	 */
	int getPort();


	/**
	 * 
	 * ��ȡ������
	 * 
	 * @return int
	 * @throws
	 */
	int getConnCount();


	
	/**
	 * 
	 * ��ȡ�˿�
	 * 
	 * @return int
	 * @throws
	 */
	int getListenPort();
	
	/**
	 * �Ƿ�֧�ֳ�����
	 * TODO
	 *
	 * @return boolean
	 * @throws
	 */
	
	boolean isSupportLongMoSms();
	/**
	 * �����Ƿ�֧�ֳ�����
	 * TODO
	 *
	 * @param isSms void
	 * @throws
	 */
	void setLongMoSms(boolean isSms);


}
