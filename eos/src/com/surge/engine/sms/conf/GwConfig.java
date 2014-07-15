package com.surge.engine.sms.conf;

import java.io.IOException;

import org.jdom.Element;

import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.util.XmlUtils;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class GwConfig implements ProtocolConfig
{

	private Element gw;

	/** 短信网关IP */
	private String ismgIp;

	/** 短信网关端口 */
	private int ismgPort = 8801;

	/** 登录用户名 */
	private String userName;

	/** 与ismg建立连接的密码 */
	private String pwd;

	/** 滑动窗口大小 */
	private int window_size = 16;

	/** 连接数大小 */
	private int connCount = 0;

	/** 网关流量限制 */
	private int mtFlux;

	/** 是否启动网关 **/
	private boolean isRun;

	/** ismg编号 */
	private String id;

	/** 路由号码段 **/
	private String number;

	/** 0 or 2 :不要回执， 1：需要回执 **/
	private int needRPT = 1;

	/** reSendChannl */
	private String reSendChannl;

	/** 源号码 **/
	private String fromPhone;

	/** 计费号码 **/
	private String feePhone;

	/** 业务代码 **/
	private String serviceID;

	/** 收费类型：默认02 **/
	private String feeType;

	/** 资费，以分为单位 **/
	private int feeValue;

	/** 网关是否支持长短信 **/
	private boolean longsmSupport;

	/** 中文短信签名 **/
	private String ismgSignCn;

	/** 英文短信签名 **/
	private String ismgSignEn;

	/** 一条中文短信长度 **/
	private int smsLengthCn;

	/** 一条英文短信长度 **/
	private int smsLengthEn;

	/** 是否每条短信都需要签名 */
	private boolean isSmsSign;

	/** SPCODE长度 **/
	private int spCodeLength;

	/** 是否需要状态报告 **/
	private int isNeedReport;

	/** 长短信拆分字数 */
	private int longsmsLength;
    /**是否支持长短信**/
	private boolean isLongSms;

	public GwConfig(Element gw) throws IOException
	{
		this.gw = gw;
		doGetConfig();
	}

	protected void doGetConfig()
	{
		this.id = gw.getAttributeValue("id");
		this.isRun = XmlUtils.getBoolean(gw, "isRun");
		this.ismgIp = XmlUtils.getString(gw, "ismgIp");
		this.ismgPort = XmlUtils.getInt(gw, "ismgPort");
		this.connCount = XmlUtils.getInt(gw, "connCount");
		this.userName = XmlUtils.getString(gw, "ismgUserName");
		this.pwd = XmlUtils.getString(gw, "ismgPwd");
		this.mtFlux = XmlUtils.getInt(gw, "mtFlux");
		this.window_size = XmlUtils.getInt(gw, "windowSize");
		this.number = XmlUtils.getString(gw, "number");
		this.reSendChannl = XmlUtils.getString(gw, "reSendChannl");
		this.fromPhone = XmlUtils.getString(gw, "fromPhone");
		this.feePhone = XmlUtils.getString(gw, "feePhone");
		this.serviceID = XmlUtils.getString(gw, "serviceID");
		this.feeType = XmlUtils.getString(gw, "feeType");
		this.feeValue = XmlUtils.getInt(gw, "feeValue");
		this.longsmSupport = XmlUtils.getBoolean(gw, "longsmSupport");
		this.isSmsSign = XmlUtils.getBoolean(gw, "isSmsSign");
		this.ismgSignCn = XmlUtils.getString(gw, "ismgSignCn");
		this.ismgSignEn = XmlUtils.getString(gw, "ismgSignEn");
		this.smsLengthCn = XmlUtils.getInt(gw, "smsLengthCn");
		this.smsLengthEn = XmlUtils.getInt(gw, "smsLengthEn");
		this.spCodeLength = XmlUtils.getInt(gw, "spCodeLength");
		this.isNeedReport = XmlUtils.getInt(gw, "isNeedReport");
		this.longsmsLength = XmlUtils.getInt(gw, "longsmsLength");
	}
	public int getMtFlux()
	{
		return mtFlux;
	}

	public void setMtFlux(int mtFlux)
	{
		this.mtFlux = mtFlux;
	}

	public String getIsmgIp()
	{
		return ismgIp;
	}

	public void setIsmgIp(String ismgIp)
	{
		this.ismgIp = ismgIp;
	}

	public int getIsmgPort()
	{
		return ismgPort;
	}

	public void setIsmgPort(int ismgPort)
	{
		this.ismgPort = ismgPort;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

	public int getWindow_size()
	{
		return window_size;
	}

	public void setWindow_size(int windowSize)
	{
		window_size = windowSize;
	}

	public void setConnCount(int connCount)
	{
		this.connCount = connCount;
	}

	@Override
	public int getConnCount()
	{
		return this.connCount;
	}

	@Override
	public String getIp()
	{
		return this.ismgIp;
	}

	@Override
	public int getListenPort()
	{
		return 0;
	}

	@Override
	public int getPort()
	{
		return this.ismgPort;
	}

	@Override
	public String getProtocolId()
	{
		return id;
	}
	public String getReSendChannl()
	{
		return reSendChannl;
	}

	public void setReSendChannl(String reSendChannl)
	{
		this.reSendChannl = reSendChannl;
	}

	public String getFromPhone()
	{
		return fromPhone;
	}

	public void setFromPhone(String fromPhone)
	{
		this.fromPhone = fromPhone;
	}

	public String getFeePhone()
	{
		return feePhone;
	}

	public void setFeePhone(String feePhone)
	{
		this.feePhone = feePhone;
	}

	public boolean isRun()
	{
		return isRun;
	}

	public Element getGw()
	{
		return gw;
	}

	public String getId()
	{
		return id;
	}

	public String getNumber()
	{
		return number;
	}

	public int getNeedRPT()
	{
		return needRPT;
	}

	public String getServiceID()
	{
		return serviceID;
	}

	public String getFeeType()
	{
		return feeType;
	}

	public int getFeeValue()
	{
		return feeValue;
	}

	public boolean isLongsmSupport()
	{
		return longsmSupport;
	}

	public boolean isSmsSign()
	{
		return isSmsSign;
	}

	public void setSmsSign(boolean isSmsSign)
	{
		this.isSmsSign = isSmsSign;
	}

	public String getIsmgSignCn()
	{
		return ismgSignCn;
	}

	public String getIsmgSignEn()
	{
		return ismgSignEn;
	}

	public int getSmsLengthCn()
	{
		return smsLengthCn;
	}

	public int getSmsLengthEn()
	{
		return smsLengthEn;
	}

	public int getSpCodeLength()
	{
		return spCodeLength;
	}

	public void setSpCodeLength(int spCodeLength)
	{
		this.spCodeLength = spCodeLength;
	}

	public int getIsNeedReport()
	{
		return isNeedReport;
	}

	public void setIsNeedReport(int isNeedReport)
	{
		this.isNeedReport = isNeedReport;
	}

	public int getLongsmsLength()
	{
		return longsmsLength;
	}

	public void setLongsmsLength(int longsmsLength)
	{
		this.longsmsLength = longsmsLength;
	}
	public boolean isSupportLongMoSms()
	{
		
		return isLongSms;
	}
	public void setLongMoSms(boolean isSms)
	{
		this.isLongSms=isSms;
	}

}
