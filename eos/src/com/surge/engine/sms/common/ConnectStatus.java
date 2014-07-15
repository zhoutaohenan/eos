package com.surge.engine.sms.common;

/**
 * 
 * 连接状态
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */

public enum ConnectStatus
{

	/**连接*/
	Connect(1),

	/**断开连接*/
	Disconnect(0);

	private int value;

	ConnectStatus(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public static ConnectStatus valueOf(int value)
	{
		ConnectStatus retVal = null;
		switch (value)
		{
		case 0:
			retVal = Disconnect;
			break;
		case 1:
			retVal = Connect;
			break;
		default:
			break;
		}

		return retVal;
	}
}
