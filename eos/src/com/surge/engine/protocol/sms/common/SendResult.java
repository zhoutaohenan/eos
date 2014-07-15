package com.surge.engine.protocol.sms.common;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-18
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public enum SendResult
{
	/**���ӶϿ���*/
	DISCONNECECT_ERROR(-1), 
	/**�������ƴ�*/
	MTFULX_ERROR(-2),
	/**�������ڴ�*/
	WINDOWSIZE_ERROR(-3);
	
	protected int value;

	public int getValue()
	{
		return this.value;
	}

	SendResult(int value)
	{
		this.value = value;
	}

}
