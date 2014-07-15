package com.surge.engine.protocol.sms.pojo;

/**
 * ISP系统向短信网关查询某段日期内发送短消息的费用清单记录
 * 
 * @description
 * @project: esk
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsFee
{
	/** 查询费用状态 ：0x01 成功 ,0x72 执行过程有错误, 0xFF 错误的指令或格式 **/
	private byte status;

	/** 费用与额(分为单位) **/
	private String sFeeLeft;

	/** 发送一条消息的费用 **/
	private String sFee;

	/** 状态数 **/
	private int iNumRec = 0;

	public SmsFee()
	{
	}
	public SmsFee(byte status, String sFeeLeft, String sFee, int iNumRec)
	{
		this.sFee = sFee;
		this.status = status;
		this.sFeeLeft = sFeeLeft;
		this.iNumRec = iNumRec;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public String getsFeeLeft()
	{
		return sFeeLeft;
	}

	public void setsFeeLeft(String sFeeLeft)
	{
		this.sFeeLeft = sFeeLeft;
	}

	public String getsFee()
	{
		return sFee;
	}

	public void setsFee(String sFee)
	{
		this.sFee = sFee;
	}

	public int getiNumRec()
	{
		return iNumRec;
	}

	public void setiNumRec(int iNumRec)
	{
		this.iNumRec = iNumRec;
	}

}
