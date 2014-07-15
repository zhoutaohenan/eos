package com.surge.engine.sms.conf;

import java.io.IOException;

import org.jdom.Element;

import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.util.XmlUtils;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-9
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SgipConfig implements ProtocolConfig
{
	private Element sgip;

	private boolean isRun;

	/** ismg编号 */
	private String id;

	/** 短信网关IP */
	private String ismgIp;

	/** 短信网关端口 */
	private int ismgPort = 8801;

	/** 登录用户名 */
	private String userName;

	/** 与ismg建立连接的密码 */
	private String pwd;

	/** 企业代码 */
	private String spId;

	/** sp_code */
	private String spCode;

	private String serviceid;

	private int connectType;

	/** 滑动窗口大小 */
	private int window_size = 16;

	/** 连接数大小 */
	private int connCount = 0;

	/** 是否支持长短信 */
	private boolean longsmSupport;

	/** 网关流量限制 */
	private int mtFlux;

	private long nodeID = 399999;// 节点编号

	private int listenPort = 8802;// 作为服务段监听端口

	private int feeType = 0; // 计费类型

	private String feeValue = "0"; // 该条短消息的收费值，单位为分

	private String givenValue = "0";// 赠送用户的话费，单位为分

	private String chargeNumber = "000000000000000000000";

	private int needRPT = 1; // 0 or 2 :不要回执， 1：需要回执

	private int agentFlag = 1;// 代收费标志，0：应收；1：实收-

	private int timeout = 60;// 连接闲置时间数（秒），超过该时间，连接关闭,默认是60秒

	private String number;

	private String reSendChannl;

	/** 中文短信签名 **/
	private String ismgSignCn;

	/** 英文短信签名 **/
	private String ismgSignEn;

	/** 一条中文短信长度 **/
	private int smsLengthCn;

	/** 一条英文短信长度 **/
	private int smsLengthEn;

	private boolean isSmsSign;
	
	/** 是否需要状态报告 **/
	private int isNeedReport;
    /**是否支持长短信**/
	private boolean isLongSms;

	public SgipConfig(Element sgip) throws IOException
	{
		this.sgip = sgip;
		this.doGetConfig();
	}

	protected void doGetConfig()
	{
		this.id = sgip.getAttributeValue("id");
		this.isRun = XmlUtils.getBoolean(sgip, "isRun");
		this.ismgIp = XmlUtils.getString(sgip, "ismgIp");
		this.ismgPort = XmlUtils.getInt(sgip, "ismgPort");
		this.connCount = XmlUtils.getInt(sgip, "connCount");
		this.spId = XmlUtils.getString(sgip, "spId");
		this.spCode = XmlUtils.getString(sgip, "spCode");
		this.userName = XmlUtils.getString(sgip, "ismgUserName");
		this.pwd = XmlUtils.getString(sgip, "ismgPwd");
		this.mtFlux = XmlUtils.getInt(sgip, "mtFlux");
		this.serviceid = XmlUtils.getString(sgip, "serviceid");

		this.connectType = XmlUtils.getInt(sgip, "connectType");

		this.listenPort = XmlUtils.getInt(sgip, "listenPort");
		this.nodeID = XmlUtils.getLong(sgip, "nodeID");
		this.longsmSupport = XmlUtils.getBoolean(sgip, "longsmSupport");
		this.chargeNumber = XmlUtils.getString(sgip, "chargeNumber");
		this.feeType = XmlUtils.getInt(sgip, "feeType");
		this.feeValue = XmlUtils.getString(sgip, "feeValue");
		this.agentFlag = XmlUtils.getInt(sgip, "agentFlag");
		this.givenValue = XmlUtils.getString(sgip, "givenValue");
		this.timeout = XmlUtils.getInt(sgip, "timeout");
		this.window_size = XmlUtils.getInt(sgip, "windowSize");
		this.number = XmlUtils.getString(sgip, "number");

		this.reSendChannl = XmlUtils.getString(sgip, "reSendChannl");

		this.isSmsSign = XmlUtils.getBoolean(sgip, "isSmsSign");
		this.ismgSignCn = XmlUtils.getString(sgip, "ismgSignCn");
		this.ismgSignEn = XmlUtils.getString(sgip, "ismgSignEn");

		this.smsLengthEn = XmlUtils.getInt(sgip, "smsLengthEn");
		this.smsLengthCn = XmlUtils.getInt(sgip, "smsLengthCn");
		this.isNeedReport = XmlUtils.getInt(sgip, "isNeedReport");
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

		return this.listenPort;
	}

	@Override
	public int getPort()
	{

		return this.ismgPort;
	}

	@Override
	public String getProtocolId()
	{

		return this.id;

	}

	public Element getSgip()
	{
		return sgip;
	}

	public String getIsmgid()
	{
		return id;
	}

	public String getIsmgIp()
	{
		return ismgIp;
	}

	public int getIsmgPort()
	{
		return ismgPort;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPwd()
	{
		return pwd;
	}

	public String getSpCode()
	{
		return spCode;
	}

	public int getWindow_size()
	{
		return window_size;
	}

	public boolean isLongsmSupport()
	{
		return longsmSupport;
	}

	public int getMtFlux()
	{
		return mtFlux;
	}

	public long getNodeID()
	{
		return nodeID;
	}

	public int getFeeType()
	{
		return feeType;
	}

	public String getFeeValue()
	{
		return feeValue;
	}

	public String getGivenValue()
	{
		return givenValue;
	}

	public int getIsNeedReceipt()
	{
		return needRPT;
	}

	public int getAgentFlag()
	{
		return agentFlag;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public String getServiceid()
	{
		return serviceid;
	}

	public String getSpId()
	{
		return spId;
	}

	public int getConnectType()
	{
		return connectType;
	}

	public String getChargeNumber()
	{
		return chargeNumber;
	}

	public boolean isRun()
	{
		return isRun;
	}

	public int getNeedRPT()
	{
		return needRPT;
	}

	public String getNumber()
	{
		return number;
	}

	public String getReSendChannl()
	{
		return reSendChannl;
	}

	public void setReSendChannl(String reSendChannl)
	{
		this.reSendChannl = reSendChannl;
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

	public boolean isSmsSign()
	{
		return isSmsSign;
	}

	public int getIsNeedReport()
	{
		return isNeedReport;
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
