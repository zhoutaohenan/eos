package com.surge.engine.protocol.sms.gw.pmsg;

/**
 * Client 与 SMS Server 之间使用的状态常数
 */
public class CCode
{

	public static final byte SUCESS = 0x01; // 成功

	public static final byte ERR_SYS = 0x02; // 系统内部错误

	public static final byte ERR_PARAM = (byte) 0x88; // 消息内容错误

	public static final byte ERR_ACCOUNT = 0x12; // 账户错误

	public static final byte ERR_RIGHT = 0x14; // 无任何可用权限

	public static final byte ERR_IP = 0x15; // 不是从指定的IP处登录

	public static final byte ERR_EXIST = 0x16; // 已经登录

	public static final byte ERR_VERSION = 0x17; // 版本号错误

	public static final byte ERR_DATA = (byte) 0xFF; // 错误的数据包

	public static final byte CLOSE_CNT = 0x00; // 退出时关闭连接

	public static final byte KEEP_CNT = 0x01; // 退出时保持连接

	public static final byte TEXT_MSG = 0x01; // 文本消息

	public static final byte ICON_MSG = 0x02; // 图片消息

	public static final byte RING_MSG = 0x03; // 铃声消息

	public static final byte BIN_MSG = 0x04; // 二进制消息

	public static final byte DIS_AGENT = 0x32; // Agent 掉线 而发送失败

	public static final byte DIS_SMSC = 0x33; // SMSC 掉线 而发送失败

	public static final byte DIS_ASMS = 0x34; // Agent 与 SMSC 掉线 而发送失败

	public static final byte ERR_SUBMIT = 0x35; // 提交失败

	public static final byte ERR_FEE = 0x36; // 费用余额不足,不能 MT

	public static final byte NO_MT_RIGHT = 0x37; // 无 MT 权限

	public static final byte NO_MT_ROUTE = 0x38; // 无 可用的MT通道

	public static final byte HAVE_DEL = 0x51; // 已删除

	public static final byte CANT_DEL = 0x52; // 无法删除

	public static final byte NO_DEL_RIGHT = 0x53; // 无权限删除

	public static final byte UNKNOW = 0x60; // 未知

	public static final byte IN_QUEUE = 0x61; // 在MSSC的队列中等待发送

	public static final byte HAVE_SENT = 0x62; // 已离开SMSC(已发送)

	public static final byte HAVE_DONE = 0x63; // 已到达用户手机

	public static final byte ERR_EXEC = 0x72; // 执行过程发生错误

	public static final byte TARGET_PAY = 0; // 由消息接收者付费

	public static final byte SOURCE_PAY = 1; // 由消息发送者付费

	public static final byte SP_PAY = 2; // 由消息发送者付费

	public static final int CFREEMT = 0; // Client MT 消息时不能对用户收费

	public static final int CMT = 1; // Client MT 类型的连接

	public static final int CMO = 2; // Client MO 类型的连接

	public static final int CBUF = 3; // Client MO 缓存类型的连接

	private static final byte[][] db_code = { // 结果返回值和数据库代码对应值
	{ TEXT_MSG, (byte) 1 }, { ICON_MSG, (byte) 2 }, { RING_MSG, (byte) 3 }, { BIN_MSG, (byte) 4 },

	{ SUCESS, (byte) 1 }, { // MT 成功提交
			IN_QUEUE, (byte) 2 }, { // 短消息在SMSC 等待发送
			HAVE_SENT, (byte) 3 }, { // 短消息已离开 SMSC ,正发往目的地
			HAVE_DONE, (byte) 4 }, { // 短消息已到达目的地
			HAVE_DEL, (byte) 5 }, { // 已删除
			UNKNOW, (byte) 8 }, { // 未知状态
			ERR_SUBMIT, (byte) 0 }, // MT 提交失败
	};

}
