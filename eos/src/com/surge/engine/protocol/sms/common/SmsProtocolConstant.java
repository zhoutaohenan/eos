package com.surge.engine.protocol.sms.common;

/**
 * 网关指信commandID常量类
 * 
 * @description
 * @project: esk
 * @Date:2010-8-6
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsProtocolConstant
{
	public static final int SGIP_BIND = 0x1;

	public static final int SGIP_BIND_RESP = 0x80000001;

	public static final int SGIP_UNBIN = 0x2;

	public static final int SGIP_UNBIND_RESP = 0x80000002;

	public static final int SGIP_SUBMIT = 0x3;

	public static final int SGIP_SUBMIT_RESP = 0x80000003;

	public static final int SGIP_DELIVER = 0x4;

	public static final int SGIP_DELIVER_RESP = 0x80000004;

	public static final int SGIP_REPORT = 0x5;

	public static final int SGIP_REPORT_RESP = 0x80000005;

	public static final int GW_LOGIN = 0x01;

	public static final byte GW_LOGIN_RESP = (byte) 0x81;

	public static final byte GW_LOGINOUT = 0x02;

	public static final byte GW_LOGINOUT_RESP = (byte) 0x82;

	public static final byte GW_LINK_CHECK = 0x03;

	public static final byte GW_LINK_CHECK_RESP = (byte) 0x83;

	public static final byte GW_SEND = 0x04;

	public static final byte GW_SEND_RESP = (byte) 0x84;

	public static final byte GW_MO = 0x05;

	public static final byte GW_MO_RESP = (byte) 0x85;

	public static final byte GW_REPORT = 0x14;

	public static final byte GW_REPORT_RESP = (byte) 0x94;

	public static final byte GW_FEE = 0x08;

	public static final byte GW_FEE_RESP = (byte) 0x88;

}
