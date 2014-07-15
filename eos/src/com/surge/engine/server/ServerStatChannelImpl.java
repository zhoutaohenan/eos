package com.surge.engine.server;

import java.util.List;

import org.apache.log4j.Logger;

import com.surge.engine.monitor.ChannelStatMgr;

/**
 * ͨ��ͳ�Ʒ���˽ӿ�
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-24
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class ServerStatChannelImpl
{
	private static Logger logger = Logger.getLogger(ServerStatChannelImpl.class);

	private ChannelStatMgr instance = ChannelStatMgr.instance;

	/**
	 * ͳ��ͨ����Ϣ�ӿ� TODO
	 * 
	 * @param channelName
	 *            ͨ������Ϊ��ʱͳ��ϵͳ���������õ�ͨ��
	 * @return List
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List channelStat(String channelName)
	{
		logger.debug("�յ��ͻ���ͳ������");
		List infoList = instance.statChannelInfo(channelName);
		return infoList;
	}
	/**
	 * ���ͨ����ͳ����Ϣ�ӿ�
	 * 
	 * TODO
	 * 
	 * @param channelName
	 *            ��ͨ������Ϊ��ʱ������������õ�ͨ�� void
	 * @throws
	 */
	public void clearZero(String channelName)
	{
		logger.info("�յ��ͻ�����������");
		instance.clearZero(channelName);
	}
}
