package com.surge.engine.sms.common;

public enum ErrCode
{
	
	S1000("ϵͳ����");
	private String desc;

	ErrCode(String desc)
	{
		this.desc = desc;
	}

	public String getDesc()
	{
		return this.desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}
}
