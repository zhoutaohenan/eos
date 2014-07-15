
package com.surge.communication.framework.common;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 一条协议消息
 * 
 * @description
 * @project: esk
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public abstract class PMessage implements java.io.Serializable
{

	private static final long serialVersionUID = -6461718415142771680L;

	/**
	 * 
	 * 消息命令ID
	 * 
	 * @return int
	 * @throws
	 */
	public abstract int getCommonId();

	/**
	 * 
	 * 打包后的字节流数据
	 * 
	 * @return byte[]
	 * @throws
	 */
	public abstract byte[] getOut();

	/**
	 * 
	 * 一个消息的seqId
	 * 
	 * @return String
	 * @throws
	 */
	public abstract String getSeqId();

	/**
	 * 
	 * 打包后的IoBuffer数据
	 * 
	 * @return IoBuffer
	 * @throws
	 */
	public abstract IoBuffer getIoBuffer();

}
