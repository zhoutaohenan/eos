package com.surge.engine.server;

import java.util.List;

import org.apache.log4j.Logger;

import com.surge.engine.monitor.ChannelStatMgr;

/**
 * 通道统计服务端接口
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
	 * 统计通道信息接口 TODO
	 * 
	 * @param channelName
	 *            通道名，为空时统计系统中所有启用的通道
	 * @return List
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List channelStat(String channelName)
	{
		logger.debug("收到客户端统计请求");
		List infoList = instance.statChannelInfo(channelName);
		return infoList;
	}
	/**
	 * 清空通道已统计信息接口
	 * 
	 * TODO
	 * 
	 * @param channelName
	 *            　通道名，为空时，清空所有启用的通道 void
	 * @throws
	 */
	public void clearZero(String channelName)
	{
		logger.info("收到客户端清零请求");
		instance.clearZero(channelName);
	}
}
