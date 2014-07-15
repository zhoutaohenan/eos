package com.surge.engine.protocol.sms;

import com.surge.communication.framework.ProtocolHandler;
import com.surge.engine.protocol.sms.pojo.Mo;
import com.surge.engine.protocol.sms.pojo.Receipt;
import com.surge.engine.protocol.sms.pojo.Response;
import com.surge.engine.protocol.sms.pojo.SmsFee;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface SmsProtocolHandler extends ProtocolHandler
{
	/**
	 * 
	 * ��Ӧ�ص�
	 * 
	 * @param response
	 *            void
	 * @throws
	 */
	void doResponse(String protocolId, Response response);

	/**
	 * 
	 * Mo�ص�
	 * 
	 * @param mo
	 *            void
	 * @throws
	 */
	void doMo(String protocolId, Mo mo);

	/**
	 * 
	 * �ײ�״̬����ص�
	 * 
	 * @param receipt
	 *            void
	 * @throws
	 */
	void doReceipt(String protocolId, Receipt receipt);
	/**
	 * 
	 * TODO
	 * 
	 * @param protocolId
	 * @param smsFee
	 *            void
	 * @throws
	 */
	void doFeeNotify(String protocolId, SmsFee smsFee);

	/**
	 * 
	 * ��������״̬�ص�
	 * 
	 * @param status
	 *            void
	 * @throws
	 */
	void doConnectStatus(String protocolId, boolean status);

}
