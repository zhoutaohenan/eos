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
	/**连接断开错*/
	DISCONNECECT_ERROR(-1), 
	/**流量控制错*/
	MTFULX_ERROR(-2),
	/**滑动窗口错*/
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
