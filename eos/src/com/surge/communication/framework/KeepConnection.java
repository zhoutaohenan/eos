package com.surge.communication.framework;


/**
 * �������ӣ��������ӽӿ�
 * 
 * @project: nioframe
 * @Date:2010-7-27
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface KeepConnection
{


	/**
	 * 
	 * ��ѯ�����ʧЧ����
	 * 
	 * @throws
	 */
   void checkConnect();

	/**
	 * 
	 * ��������
	 * 
	 * @throws
	 */
   void keepActive();

	/**
	 * 
	 * �������ӵ���������
	 * 
	 * @throws
	 */
	 void clearClientData();


}
