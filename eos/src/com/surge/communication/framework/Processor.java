package com.surge.communication.framework;

import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.net.Client;

/**
 * Э���������
 * 
 * @author Administrator
 * 
 */
public interface Processor
{

	/***
	 * ����������ӻ�������Ϣ��
	 * 
	 * @param client
	 * @param pMessage
	 */
	public void doProcess(Client client, PMessage pMessage);
}
