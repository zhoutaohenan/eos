
package com.surge.communication.framework.common;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * һ��Э����Ϣ
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
	 * ��Ϣ����ID
	 * 
	 * @return int
	 * @throws
	 */
	public abstract int getCommonId();

	/**
	 * 
	 * �������ֽ�������
	 * 
	 * @return byte[]
	 * @throws
	 */
	public abstract byte[] getOut();

	/**
	 * 
	 * һ����Ϣ��seqId
	 * 
	 * @return String
	 * @throws
	 */
	public abstract String getSeqId();

	/**
	 * 
	 * ������IoBuffer����
	 * 
	 * @return IoBuffer
	 * @throws
	 */
	public abstract IoBuffer getIoBuffer();

}
