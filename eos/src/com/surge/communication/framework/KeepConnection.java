package com.surge.communication.framework;


/**
 * 创建连接，保持连接接口
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
	 * 轮询并清除失效连接
	 * 
	 * @throws
	 */
   void checkConnect();

	/**
	 * 
	 * 保持心跳
	 * 
	 * @throws
	 */
   void keepActive();

	/**
	 * 
	 * 清理连接的垃圾数据
	 * 
	 * @throws
	 */
	 void clearClientData();


}
