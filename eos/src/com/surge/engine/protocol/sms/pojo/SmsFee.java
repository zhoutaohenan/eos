package com.surge.engine.protocol.sms.pojo;

/**
 * ISPϵͳ��������ز�ѯĳ�������ڷ��Ͷ���Ϣ�ķ����嵥��¼
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
	/** ��ѯ����״̬ ��0x01 �ɹ� ,0x72 ִ�й����д���, 0xFF �����ָ����ʽ **/
	private byte status;

	/** �������(��Ϊ��λ) **/
	private String sFeeLeft;

	/** ����һ����Ϣ�ķ��� **/
	private String sFee;

	/** ״̬�� **/
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
