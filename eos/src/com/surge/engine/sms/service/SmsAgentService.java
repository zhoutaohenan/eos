package com.surge.engine.sms.service;

import java.util.List;
import java.util.Map;

import com.surge.engine.sms.common.ConnectStatus;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsRequest;
import com.surge.engine.sms.pojo.SmsResponse;

/**
 * 短信服务代理接口
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
	 * 发送短信
	 * 
	 * @param smsRequest
	 * @return SmsResponse
	 * @throws
	 */
	SmsResponse sendSms(SmsRequest smsRequest);

	/***
	 * 
	 * 号码路由
	 * 
	 * @param mobile
	 * @return String[]
	 * @throws
	 */
	List<SmsChannel> routing(String mobile);

	/**
	 * 
	 * 根据已发送通道获得对应的重发通道 多个以","隔开
	 * 
	 * @param channel
	 * @return SmsChannel
	 * @throws
	 */
	String getReSendChannel(String channelId);

	/**
	 * 
	 * 获取所有通道
	 * 
	 * @return String[]
	 * @throws
	 */
	Map<String, SmsChannel> getChannels();

	/***
	 * 
	 * 具体通道是否可以发送短信
	 * 
	 * @param channelId
	 * @return boolean
	 * @throws
	 */
	boolean isAllowSend(String channelId);

	/***
	 * 
	 * 通道允许发送的条数
	 *
	 * @param channelId
	 * @return boolean
	 * @throws
	 */
	int getAllowSendCount(String channelId);
	
	/**
	 * 根据通道id获得SPCODE
	 * TODO
	 *
	 * @param protocolId
	 * @return String
	 * @throws
	 */
	 String getSpCode(String protocolId);
	 
	 /**
		 * 得到已发队列的大小
		 * TODO
		 *
		 * @param
		 * @return int
		 * @throws
		 */
	 int getSentSubmitMapSize(String channelId);
	 
	 /**
	  * <p> 得到系统正在运行的通道</p>
	  * <p>@return</p>
	  * <p>@author: Administrator</p>
	  * <p>@time: 2012下午02:02:15</p>
	  */
	 int getRunningChannel();

}
