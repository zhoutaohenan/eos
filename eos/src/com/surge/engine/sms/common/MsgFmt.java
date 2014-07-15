package com.surge.engine.sms.common;


/**
 * @description
 * @project: eskprj
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public enum MsgFmt
{
	ASCII(0), UCS2(8), GB18030(16), GBK(15), Binary(4);

	protected int value;

	public int getValue()
	{
		return this.value;
	}

	MsgFmt(int value)
	{
		this.value = value;
	}

	public static MsgFmt valueOf(int format)
	{
		MsgFmt retVal = null;
		switch (format)
		{
		case 0:
			retVal = ASCII;
			break;
		case 8:
			retVal = UCS2;
			break;
		case 15:
			retVal = GBK;
			break;
		case 16:
			retVal = GB18030;
			break;
		case 4:
			retVal = Binary;
			break;
		default:
			break;
		}
		return retVal;
	}
}
