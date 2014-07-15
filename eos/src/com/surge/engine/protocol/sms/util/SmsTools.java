package com.surge.engine.protocol.sms.util;

import java.util.HashMap;
import java.util.Map;

import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.util.MobileErrorUitl;

public class SmsTools
{

	/**
	 * key为stat,value为ReportMsg
	 */
	private static Map<String, ReportMsg> reportMap = new HashMap(1001);

	private static MobileErrorUitl mobileErrorUitl = MobileErrorUitl.mobileErrorUitl;

	static
	{
		int base = 1000001;

		ReportMsg report = new ReportMsg(0, "用户成功接收");
		reportMap.put("DELIVRD", report);
		report = new ReportMsg(base++, "因为用户长时间关机或者不在服务区等导致的短信息超时没有递交到用户手机上");
		reportMap.put("EXPIRED", report);
		report = new ReportMsg(base++, "信息已经被删除");
		reportMap.put("DELETED", report);
		report = new ReportMsg(base++, "用户因为状态不正确如处于停机、挂起等状态而导致的用户无法享受服务");
		reportMap.put("UNDELIV", report);
		report = new ReportMsg(base++, "信息处于被接受状态");
		reportMap.put("ACCEPTD", report);
		report = new ReportMsg(base++, "信息处于未知状态");
		reportMap.put("UNKNOWN", report);
		report = new ReportMsg(base++, "消息因为某些原因被拒绝");
		reportMap.put("REJECTD", report);
		report = new ReportMsg(base++, "查找路由失败");
		reportMap.put("NOROUTE", report);
		report = new ReportMsg(base++, "SCP不返回响应消息时的状态报告");
		reportMap.put("CA:", report);
		report = new ReportMsg(base++, "尚未建立连接");
		reportMap.put("CA:0051", report);
		report = new ReportMsg(base++, "尚未成功登录");
		reportMap.put("CA:0052", report);
		report = new ReportMsg(base++, "发送消息失败");
		reportMap.put("CA:0053", report);
		report = new ReportMsg(base++, "超时未接收到响应消息");
		reportMap.put("CA:0054", report);
		report = new ReportMsg(base++, "SCP返回错误响应消息时的状态报告");
		reportMap.put("CB:", report);
		report = new ReportMsg(base++, "非神州行预付费用户");
		reportMap.put("CB:0001:", report);
		report = new ReportMsg(base++, "数据库操作失败");
		reportMap.put("CB:0002", report);
		report = new ReportMsg(base++, "鉴权失败");
		reportMap.put("CB:0003", report);
		report = new ReportMsg(base++, "超过最大错误次数");
		reportMap.put("CB:0004", report);
		report = new ReportMsg(base++, "用户状态异常(包括未头次使用、储值卡被封锁、储值卡进入保留期、储值卡挂失)");
		reportMap.put("CB:0005:", report);
		report = new ReportMsg(base++, "服务没有激活");
		reportMap.put("CB:0006", report);
		report = new ReportMsg(base++, "用户余额不足");
		reportMap.put("CB:0007", report);
		report = new ReportMsg(base++, "超过包月费用上限");
		reportMap.put("CB:0008", report);
		report = new ReportMsg(base++, "超过最高欠费额");
		reportMap.put("CB:0009", report);
		report = new ReportMsg(base++, "用户已注册该项服务");
		reportMap.put("CB:0010", report);
		report = new ReportMsg(base++, "用户没有注册该项服务");
		reportMap.put("CB:0011", report);
		report = new ReportMsg(base++, "未登记的网关");
		reportMap.put("CB:0014", report);
		report = new ReportMsg(base++, "网关登录摘要错误");
		reportMap.put("CB:0015", report);
		report = new ReportMsg(base++, "参数错误");
		reportMap.put("CB:0016", report);
		report = new ReportMsg(base++, "服务器端数据未传完");
		reportMap.put("CB:0017", report);
		report = new ReportMsg(base++, "重复发送消息序列号msgid相同的计费请求消息");
		reportMap.put("CB:0018", report);
		report = new ReportMsg(base++, "未知错误");
		reportMap.put("CB:0020", report);
		report = new ReportMsg(base++, "数据库错误");
		reportMap.put("CB:0021", report);
		report = new ReportMsg(base++, "SCP互联失败");
		reportMap.put("CB:0022", report);
		report = new ReportMsg(base++, "数值越界");
		reportMap.put("CB:0023", report);
		report = new ReportMsg(base++, "字段超长");
		reportMap.put("CB:0024", report);
		report = new ReportMsg(base++, "无相关数据");
		reportMap.put("CB:0025", report);
		report = new ReportMsg(base++, "数据重复");
		reportMap.put("CB:0026", report);
		report = new ReportMsg(base++, "未登记的SP");
		reportMap.put("CB:0040", report);
		report = new ReportMsg(base++, "SP帐户状态异常");
		reportMap.put("CB:0041", report);
		report = new ReportMsg(base++, "SP无权限");
		reportMap.put("CB:0042", report);
		report = new ReportMsg(base++, "SP帐户已存在");
		reportMap.put("CB:0043", report);
		report = new ReportMsg(base++, "未登记的SP业务类型");
		reportMap.put("CB:0044", report);
		report = new ReportMsg(base++, "SP业务类型数据异常");
		reportMap.put("CB:0045", report);
		report = new ReportMsg(base++, "SP业务类型已存在");
		reportMap.put("CB:0046", report);
		report = new ReportMsg(base++, "梦网用户已存在");
		reportMap.put("CB:0052", report);
		report = new ReportMsg(base++, "梦网用户不存在");
		reportMap.put("CB:0053", report);
		report = new ReportMsg(base++, "梦网用户状态异常");
		reportMap.put("CB:0054", report);
		report = new ReportMsg(base++, "签约信息已存在");
		reportMap.put("CB:0055", report);
		report = new ReportMsg(base++, "签约信息不存在");
		reportMap.put("CB:0056", report);
		report = new ReportMsg(base++, "签约数据异常");
		reportMap.put("CB:0057", report);
		report = new ReportMsg(base++, "月消费超额");
		reportMap.put("CB:0061", report);
		report = new ReportMsg(base++, "单笔消费超额");
		reportMap.put("CB:0062", report);
		report = new ReportMsg(base++, "用户拒绝");
		reportMap.put("CB:0063", report);
		report = new ReportMsg(base++, "短消息编号已存在");
		reportMap.put("CB:0064", report);
		report = new ReportMsg(base++, "对应扣费请求不存在");
		reportMap.put("CB:0065", report);
		report = new ReportMsg(base++, "扣费请求已被确认");
		reportMap.put("CB:0066", report);
		report = new ReportMsg(base++, "未定义的计费类型");
		reportMap.put("CB:0067", report);
		report = new ReportMsg(base++, "未定义的编码方式");
		reportMap.put("CB:0068", report);
		report = new ReportMsg(base++, "DSMP不返回响应消息时的状态报告");
		reportMap.put("DA:", report);
		report = new ReportMsg(base++, "等待DSMP返回响应超时");
		reportMap.put("DA:0054", report);
		report = new ReportMsg(base++, "发送给DSMP失败");
		reportMap.put("DA:0320", report);
		report = new ReportMsg(base++, "接收DSMP响应失败");
		reportMap.put("DA:0330", report);
		report = new ReportMsg(base++, "与DSMP之间soap连接异常");
		reportMap.put("DA:0360", report);
		report = new ReportMsg(base++, "DSMP返回错误响应消息时的状态报告");
		reportMap.put("DB:", report);
		report = new ReportMsg(base++, "手机号码不存在");
		reportMap.put("DB:0100", report);
		report = new ReportMsg(base++, "手机号码错误");
		reportMap.put("DB:0101", report);
		report = new ReportMsg(base++, "用户停机 用户冲值后，要主动上行一条信息到SP，才能激活用户的短信接收服务");
		reportMap.put("DB:0102", report);
		report = new ReportMsg(base++, "用户欠费");
		reportMap.put("DB:0103", report);
		report = new ReportMsg(base++, "用户没有使用该业务的权限");
		reportMap.put("DB:0104", report);
		report = new ReportMsg(base++, "业务代码错误");
		reportMap.put("DB:0105", report);
		report = new ReportMsg(base++, "服务代码错误");
		reportMap.put("DB:0106", report);
		report = new ReportMsg(base++, "业务不存在");
		reportMap.put("DB:0107", report);
		report = new ReportMsg(base++, "该业务暂停服务");
		reportMap.put("DB:0108", report);
		report = new ReportMsg(base++, "该服务种类不存在");
		reportMap.put("DB:0109", report);
		report = new ReportMsg(base++, "该服务种类尚未开通");
		reportMap.put("DB:0110", report);
		report = new ReportMsg(base++, "该业务尚未开通");
		reportMap.put("DB:0111", report);
		report = new ReportMsg(base++, "SP代码错误");
		reportMap.put("DB:0112", report);
		report = new ReportMsg(base++, "SP不存在");
		reportMap.put("DB:0113", report);
		report = new ReportMsg(base++, "SP暂停服务");
		reportMap.put("DB:0114", report);
		report = new ReportMsg(base++, "用户没有定购该业务");
		reportMap.put("DB:0115", report);
		report = new ReportMsg(base++, "用户暂停定购该业务");
		reportMap.put("DB:0116", report);
		report = new ReportMsg(base++, "该业务不能对该用户开放");
		reportMap.put("DB:0117", report);
		report = new ReportMsg(base++, "用户已经订购了该业务");
		reportMap.put("DB:0118", report);
		report = new ReportMsg(base++, "用户不能取消该业务");
		reportMap.put("DB:0119", report);
		report = new ReportMsg(base++, "话单格式错误");
		reportMap.put("DB:0120", report);
		report = new ReportMsg(base++, "没有该类业务");
		reportMap.put("DB:0121", report);
		report = new ReportMsg(base++, "接收异常");
		reportMap.put("DB:0122", report);
		report = new ReportMsg(base++, "业务价格为负");
		reportMap.put("DB:0123", report);
		report = new ReportMsg(base++, "业务价格格式错误");
		reportMap.put("DB:0124", report);
		report = new ReportMsg(base++, "业务价格超出范围");
		reportMap.put("DB:0125", report);
		report = new ReportMsg(base++, "该用户不是神州行用户");
		reportMap.put("DB:0126", report);
		report = new ReportMsg(base++, "该用户没有足够的余额");
		reportMap.put("DB:0127", report);
		report = new ReportMsg(base++, "补款,冲正失败");
		reportMap.put("DB:0128", report);
		report = new ReportMsg(base++, "用户已经是梦网用户");
		reportMap.put("DB:0129", report);
		report = new ReportMsg(base++, "用户在BOSS中没有相关用户数据");
		reportMap.put("DB:0130", report);
		report = new ReportMsg(base++, "BOSS系统数据同步出错");
		reportMap.put("DB:0131", report);
		report = new ReportMsg(base++, "相关信息不存在");
		reportMap.put("DB:0132", report);
		report = new ReportMsg(base++, "用户数据同步出错");
		reportMap.put("DB:0133", report);
		report = new ReportMsg(base++, "SP数据同步出错");
		reportMap.put("DB:0134", report);
		report = new ReportMsg(base++, "业务数据同步出错");
		reportMap.put("DB:0135", report);
		report = new ReportMsg(base++, "用户密码错误");
		reportMap.put("DB:0136", report);
		report = new ReportMsg(base++, "伪码信息错误");
		reportMap.put("DB:0137", report);
		report = new ReportMsg(base++, "用户相关信息不存在");
		reportMap.put("DB:0138", report);
		report = new ReportMsg(base++, "用户未点播该业务");
		reportMap.put("DB:0140", report);
		report = new ReportMsg(base++, "网络异常");
		reportMap.put("DB:9001", report);
		report = new ReportMsg(base++, "业务网关超过限制的流量");
		reportMap.put("DB:9007", report);
		report = new ReportMsg(base++, "SP不返回响应消息时的状态报告");
		reportMap.put("SA:", report);
		report = new ReportMsg(base++, "SP返回错误响应消息时的状态报告");
		reportMap.put("SB:", report);
		report = new ReportMsg(base++, "下一级ISMG不返回响应消息时的状态报告");
		reportMap.put("IA:", report);
		report = new ReportMsg(base++, "下一级ISMG返回错误响应消息时的状态报告");
		reportMap.put("IB:", report);
		report = new ReportMsg(base++, "没有从下一级ISMG处接收到状态报告时的状态报告");
		reportMap.put("IC:", report);
		report = new ReportMsg(base++, "网关ISMG内部检测错误码");
		reportMap.put("ID:", report);
		report = new ReportMsg(base++, "业务代码错误");
		reportMap.put("ID:0007", report);
		report = new ReportMsg(base++, "计费号码路由判断错误(本网关不负责服务次计费号码)");
		reportMap.put("ID:0009", report);
		report = new ReportMsg(base++, "SPACE用户鉴权模块鉴权用户停机或欠费错误");
		reportMap.put("ID:0020", report);
		report = new ReportMsg(base++, "SPACE用户鉴权模块：用户销户错误");
		reportMap.put("ID:0021:", report);
		report = new ReportMsg(base++, "信息格式错误，一般指GB转Unicode失败");
		reportMap.put("ID:0100", report);
		report = new ReportMsg(base++, "计费类型(Fee_Type)错误");
		reportMap.put("ID:0101", report);
		report = new ReportMsg(base++, "队列满(包括保存本地MT和前转MT失败)");
		reportMap.put("ID:0111", report);
		report = new ReportMsg(base++, "神州行扣费请求失败");
		reportMap.put("ID:0113", report);
		report = new ReportMsg(base++, "MT短信在smsAgent队列中超时");
		reportMap.put("ID:0151", report);
		report = new ReportMsg(base++, "前转到外地网关，外地网关没有响应");
		reportMap.put("ID:0154", report);
		report = new ReportMsg(base++, "SMSC不返回响应消息时的状态报告");
		reportMap.put("MA:", report);
		report = new ReportMsg(base++, "SMSC返回错误响应消息时的状态报告");
		reportMap.put("MB:", report);
		report = new ReportMsg(base++, "可能是用户手机满了");
		reportMap.put("MB:0015", report);
		report = new ReportMsg(base++, "消息长度错误");
		reportMap.put("MB:0016", report);
		report = new ReportMsg(base++, "命令长度错误");
		reportMap.put("MB:0017", report);
		report = new ReportMsg(base++, "消息ID无效");
		reportMap.put("MB:0018", report);
		report = new ReportMsg(base++, "没有执行此命令的权限");
		reportMap.put("MB:0019", report);
		report = new ReportMsg(base++, "无效的SYSTEMID");
		reportMap.put("MB:0032", report);
		report = new ReportMsg(base++, "无效的密码");
		reportMap.put("MB:0033", report);
		report = new ReportMsg(base++, "无效的SYSTEMTYPE");
		reportMap.put("MB:0034", report);
		report = new ReportMsg(base++, "地址错误");
		reportMap.put("MB:0064", report);
		report = new ReportMsg(base++, "超过最大提交数");
		reportMap.put("MB:0065", report);
		report = new ReportMsg(base++, "超作最大发送次数,可能用户手机满了");
		reportMap.put("MB:0066", report);
		report = new ReportMsg(base++, "无效的用户");
		reportMap.put("MB:0067", report);
		report = new ReportMsg(base++, "无效的数据格式");
		reportMap.put("MB:0068", report);
		report = new ReportMsg(base++, "创建消息失败");
		reportMap.put("MB:0069", report);
		report = new ReportMsg(base++, "无效的短消息ID");
		reportMap.put("MB:0070", report);
		report = new ReportMsg(base++, "数据库失败");
		reportMap.put("MB:0071", report);
		report = new ReportMsg(base++, "取消消息失败");
		reportMap.put("MB:0072", report);
		report = new ReportMsg(base++, "短消息状态错误");
		reportMap.put("MB:0073", report);
		report = new ReportMsg(base++, "替换消息失败");
		reportMap.put("MB:0074", report);
		report = new ReportMsg(base++, "替换消息源地址错误");
		reportMap.put("MB:0075", report);
		report = new ReportMsg(base++, "无效的源地址TON");
		reportMap.put("MB:0096", report);
		report = new ReportMsg(base++, "无效的源地址NPI");
		reportMap.put("MB:0097", report);
		report = new ReportMsg(base++, "源地址错误");
		reportMap.put("MB:0098", report);
		report = new ReportMsg(base++, "无效的目的地址TON");
		reportMap.put("MB:0099", report);
		report = new ReportMsg(base++, "无效的目的地址NPI");
		reportMap.put("MB:0100", report);
		report = new ReportMsg(base++, "目的地址错误");
		reportMap.put("MB:0101", report);
		report = new ReportMsg(base++, "无效的定时时间");
		reportMap.put("MB:0102", report);
		report = new ReportMsg(base++, "无效的超时时间");
		reportMap.put("MB:0103", report);
		report = new ReportMsg(base++, "无效的ESM_CALSS");
		reportMap.put("MB:0104", report);
		report = new ReportMsg(base++, "无效的UDLEN");
		reportMap.put("MB:0105", report);
		report = new ReportMsg(base++, "无效的PRI");
		reportMap.put("MB:0106", report);
		report = new ReportMsg(base++, "无效的Registered_delivery_flag");
		reportMap.put("MB:0107", report);
		report = new ReportMsg(base++, "无效的Replace_if_present_flag");
		reportMap.put("MB:0108", report);
		report = new ReportMsg(base++, "指定用户已经存在");
		reportMap.put("MB:0128", report);
		report = new ReportMsg(base++, "创建用户失败");
		reportMap.put("MB:0129", report);
		report = new ReportMsg(base++, "用户ID错误");
		reportMap.put("MB:0130", report);
		report = new ReportMsg(base++, "指定用户不存在");
		reportMap.put("MB:0131", report);
		report = new ReportMsg(base++, "系统未从短信中心接收到状态报告");
		reportMap.put("MC:", report);
		report = new ReportMsg(base++, "系统未从短信中心接收到状态报告");
		reportMap.put("MC:0015", report);
		report = new ReportMsg(base++, "用户长时间关机或者不在服务区等导致的短信息超时没有递交到用户手机上");
		reportMap.put("MI:", report);
		report = new ReportMsg(base++, "用户长时间关机或者不在服务区等导致的短信息超时没有递交到用户手机上");
		reportMap.put("MI:0000", report);
		report = new ReportMsg(base++, "信息已经被删除");
		reportMap.put("MJ:", report);
		report = new ReportMsg(base++, "用户因为状态不正确如处于停机、挂起等状态而导致的用户无法享受服务");
		reportMap.put("MK:", report);
		report = new ReportMsg(base++, "用户因为状态不正确如处于停机、挂起等状态而导致的用户无法享受服务");
		reportMap.put("MK:0000", report);
		report = new ReportMsg(base++, "信息处于被接受状态");
		reportMap.put("ML:", report);
		report = new ReportMsg(base++, "信息处于未知状态");
		reportMap.put("MM:", report);
		report = new ReportMsg(base++, "消息因为某些原因被拒绝");
		reportMap.put("MN:", report);
		report = new ReportMsg(base++, "其他值");
		reportMap.put("MH:", report);
	}

	public static int getCmppReportResult(String stat)
	{

		int ret = 1;
		if (stat.equalsIgnoreCase("DELIVRD"))
		{
			ret = 0;
		} else if (mobileErrorUitl.getCmppList().contains(stat))
		{
			ret = SmsErrCode.USER_MOBILE_ERROR.getValue();
		}

		return ret;
	}

	public static String getSgipDetailInfo(int errorCode)
	{

		String ret = "";

		switch (errorCode)
		{

		case 0:
			ret = "0*DELIVRD:Message is delivered to destination";
			break;
		case 5:
			ret = "5*参数格式错，指命令中参数值与参数类型不符或与协议规定的范围不符。";
			break;
		case 6:
			ret = "6*非法手机号码，协议中所有手机号码字段出现非86130号码或手机号码前未加“86”时都应报错。";
			break;
		case 7:
			ret = "7*消息ID错";
			break;
		case 8:
			ret = "8*信息长度错";
			break;
		case 9:
			ret = "9*非法序列号，包括序列号重复、序列号格式错误等";
			break;
		case 10:
			ret = "10*非法操作GNS";
			break;
		case 11:
			ret = "11*节点忙，指本节点存储队列满或其他原因，暂时不能提供服务的情况";
			break;
		case 12:
			ret = "12*Fee_terminal_Id错误";
			break;
		case 13:
			ret = "13*Dest_terminal_Id错误";
			break;

		case 21:
			ret = "21*目的地址不可达，指路由表存在路由且消息路由正确但被路由的节点暂时不能提供服务的情况";
			break;
		case 22:
			ret = "22*路由错，指路由表存在路由但消息路由出错的情况，如转错SMG等";
			break;
		case 23:
			ret = "23*路由不存在，指消息路由的节点在路由表中不存在";
			break;
		case 24:
			ret = "24*计费号码无效，鉴权不成功时反馈的错误信息";
			break;
		case 25:
			ret = "25*用户不能通信（如不在服务区、未开机等情况）";
			break;
		case 26:
			ret = "26*手机内存不足";
			break;
		case 27:
			ret = "27*手机不支持短消息";
			break;
		case 28:
			ret = "28*手机接收短消息出现错误";
			break;
		case 29:
			ret = "29*不知道的用户";
			break;
		case 30:
			ret = "30*不提供此功能";
			break;
		case 31:
			ret = "31*非法设备";
			break;
		case 32:
			ret = "32*系统失败";
			break;
		case 33:
			ret = "33*短信中心队列满";
			break;
		default:
			ret = errorCode + "*其他错误";
			break;
		}
		return ret;

	}

	public static String getContent(byte[] bys, int msg_fmt)
	{

		if (bys == null || bys.length <= 0)
			return "";
		String content = null;
		int offset = 0;
		String appendHead = "";
		if (bys.length >= 6)
		{
			if (bys[0] == 5 && bys[1] == 0 && bys[2] == 3)
			{
				offset = 6;
				// 长短信取消头部，合成一条返回给应用
				// appendHead = "(" + bys[5] + "/" + bys[4] + ")";
			}
		}
		try
		{
			if (msg_fmt == 8 || msg_fmt == 25)
			{
				content = new String(bys, offset, bys.length - offset, "UTF-16BE");
			} else if (msg_fmt == 15)
			{
				content = new String(bys, offset, bys.length - offset, "GBK");
			} else
			{
				content = new String(bys, offset, bys.length - offset);
			}

		} catch (Exception e)
		{
			content = "" + e;
		}
		if (offset > 0)
		{
			content = appendHead + content;
		}

		return content;
	}

	public static String getCmppSubmitRespResult(int result)
	{

		if (result < 0)
		{
			result += 256;
		}
		String ret = null;
		switch (result)
		{
		case 0:
			ret = "成功";
			break;
		case 1:
			ret = "消息结构错";
			break;
		case 2:
			ret = "命令字错";
			break;
		case 3:
			ret = "消息序号重复";
			break;
		case 4:
			ret = "消息长度错";
			break;
		case 5:
			ret = "资费代码错";
			break;
		case 6:
			ret = "超过最大信息长";
			break;
		case 7:
			ret = "业务代码错";
			break;
		case 8:
			ret = "流量控制错";
			break;
		case 9:
			ret = "本网关不负责服务此计费号码";
			break;
		case 10:
			ret = "src_id错误";
			break;
		case 11:
			ret = "Msg_src错误";
			break;
		case 12:
			ret = "计费地址错";
			break;
		case 13:
			ret = "目的地址错";
			break;
		}

		if (ret == null)
		{
			ret = "submitResp其他错误";
		}

		ret = result + " " + ret;

		return ret;
	}
	public static String getCmppReportDetailResult(String stat)
	{
		String ret = stat + " ";
		ReportMsg report = reportMap.get(stat);
		if (report == null && stat.length() > 3)
		{
			String pre = stat.substring(0, 3);
			report = reportMap.get(pre);
		}
		if (report != null)
		{
			ret += report.detail;
		} else
		{
			ret += "未知错误";
		}

		return ret;
	}
	public static int getSmgpReportResult(String stat)
	{
		int ret = 1;
		if (stat.equals("DELIVRD")||stat.equals("ELIVRD"))
		{
			ret = 0;
		} else if (mobileErrorUitl.getSmgpList().contains(stat))
		{
			ret = SmsErrCode.USER_MOBILE_ERROR.getValue();
		}
		return ret;
	}
	public static int getGWResponseResult(int stat)
	{
		int ret=stat;
		if(mobileErrorUitl.getGwList().contains(String.valueOf(stat)))
		{
			ret=1000016;
		}
		return ret;
	}

	/**
	 * SMGP响应结果转换
	 * 
	 * @param result
	 * @return String
	 * @throws
	 */
	public static String getSmgpSubmitRespResult(int result)
	{
		String ret = "";
		switch (result)
		{
		case 0:
			ret = "0*成功";
			break;
		case 1:
			ret = "1*系统忙";
			break;
		case 2:
			ret = "2*超过最大连接";
			break;
		case 10:
			ret = "10*消息结构错";
			break;
		case 11:
			ret = "11*命令字错";
			break;
		case 12:
			ret = "12*序列号重复";
			break;
		case 20:
			ret = "20*IP地址错";
			break;
		case 21:
			ret = "21*认证错";
			break;
		case 22:
			ret = "22*版本太高";
			break;
		case 30:
			ret = "30*非法消息类型(MsgType)";
			break;
		case 31:
			ret = "31*非法优先级(Priority)";
			break;
		case 32:
			ret = "32*非法资费类型(FeeType)";
			break;
		case 33:
			ret = "33*非法资费代码(FeeCode)";
			break;
		case 34:
			ret = "34*非法消息格式(MsgFormat)";
			break;
		case 35:
			ret = "36*非法时间格式";
			break;
		case 36:
			ret = "非法短信长度(Msg_Length)";
			break;
		case 37:
			ret = "37*有效期已过";
			break;
		case 43:
			ret = "43*非法服务代码(ServiceId)";
			break;
		case 44:
			ret = "44*非法有效期(ValidTime)";
			break;
		case 45:
			ret = "45*非法定时发送(AtTime)";
			break;
		case 46:
			ret = "46*非法发送用户号码(SrcTermid)";
			break;
		case 47:
			ret = "47*非法接收用户号码(DestTermid)";
			break;
		case 48:
			ret = "48*非法计费号码(ChargeTermid)";
			break;
		case 49:
			ret = "49*非法SP服务代码(SPCode)";
			break;
		case 69:
			ret = "69*非法SP企业代码(SPID)";
			break;
		default:
			ret = result + "*未知错误";
		}
		return ret;
	}

}

class ReportMsg
{

	int result;

	String detail;

	ReportMsg(int result, String detail)
	{

		this.result = result;
		this.detail = detail;
	}
}
