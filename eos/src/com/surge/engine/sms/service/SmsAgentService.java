package com.surge.engine.sms.service;

import java.util.List;
import java.util.Map;

import com.surge.engine.sms.common.ConnectStatus;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsRequest;
import com.surge.engine.sms.pojo.SmsResponse;

/**
 * ���ŷ������ӿ�
 * 
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public interface SmsAgentService
{

	/**
	 * 
	 * ���Ͷ���
	 * 
	 * @param smsRequest
	 * @return SmsResponse
	 * @throws
	 */
	SmsResponse sendSms(SmsRequest smsRequest);

	/***
	 * 
	 * ����·��
	 * 
	 * @param mobile
	 * @return String[]
	 * @throws
	 */
	List<SmsChannel> routing(String mobile);

	/**
	 * 
	 * �����ѷ���ͨ����ö�Ӧ���ط�ͨ�� �����","����
	 * 
	 * @param channel
	 * @return SmsChannel
	 * @throws
	 */
	String getReSendChannel(String channelId);

	/**
	 * 
	 * ��ȡ����ͨ��
	 * 
	 * @return String[]
	 * @throws
	 */
	Map<String, SmsChannel> getChannels();

	/***
	 * 
	 * ����ͨ���Ƿ���Է��Ͷ���
	 * 
	 * @param channelId
	 * @return boolean
	 * @throws
	 */
	boolean isAllowSend(String channelId);

	/***
	 * 
	 * ͨ�������͵�����
	 *
	 * @param channelId
	 * @return boolean
	 * @throws
	 */
	int getAllowSendCount(String channelId);
	
	/**
	 * ����ͨ��id���SPCODE
	 * TODO
	 *
	 * @param protocolId
	 * @return String
	 * @throws
	 */
	 String getSpCode(String protocolId);
	 
	 /**
		 * �õ��ѷ����еĴ�С
		 * TODO
		 *
		 * @param
		 * @return int
		 * @throws
		 */
	 int getSentSubmitMapSize(String channelId);
	 
	 /**
	  * <p> �õ�ϵͳ�������е�ͨ��</p>
	  * <p>@return</p>
	  * <p>@author: Administrator</p>
	  * <p>@time: 2012����02:02:15</p>
	  */
	 int getRunningChannel();

}
