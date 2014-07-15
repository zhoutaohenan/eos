package com.surge.engine.sms.conf;

import java.io.IOException;

import org.jdom.Element;

import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.util.XmlUtils;

public class SmgpConfig implements ProtocolConfig
{
	/** 是否启动 */
	public boolean isRun;

	/** ismg编号 */
	public String id;

	/** 短信网关IP */
	protected String ismgIp;

	/** 短信网关端口 */
	protected int ismgPort;

	/** 登录用户名 */
	public String userName;

	/** 与ismg建立连接的密码 */
	public String pwd;

	/** 企业代码 */
	public String spId;

	/** sp_code */
	public String spCode;

	/** 滑动窗口大小 */
	public int window_size = 16;

	/** 连接数大小 */
	public int connCount = 0;

	/** 是否支持长短信 */
	public boolean longsmSupport;

	/** 网关流量限制 */
	public int mtFlux;

	public int feeUserType;

	public String feeTerminalId;

	public String feeType;

	public String feeCode;

	public String smsMessageLen;

	private String serviceid;

	private String number;

	private boolean isSmsSign;

	private String reSendChannl;

	private Element smgp;

	/** 中文短信签名 **/
	private String ismgSignCn;

	/** 英文短信签名 **/
	private String ismgSignEn;

	/** 一条中文短信长度 **/
	private int smsLengthCn;

	/** 一条英文短信长度 **/
	private int smsLengthEn;
	/** 是否需要状态报告 **/
	private int isNeedReport;
    /**是否支持长短信**/
	private boolean isLongSms;

	public SmgpConfig(Element smgp) throws IOException
	{
		this.smgp = smgp;
		this.doGetConfig();
	}

	protected void doGetConfig()
	{
		this.id = smgp.getAttributeValue("id");
		this.isRun = XmlUtils.getBoolean(smgp, "isRun");
		this.ismgIp = XmlUtils.getString(smgp, "ismgIp");
		this.ismgPort = XmlUtils.getInt(smgp, "ismgPort");
		this.connCount = XmlUtils.getInt(smgp, "connCount");
		this.feeUserType = XmlUtils.getInt(smgp, "feeUserType");
		this.feeTerminalId = XmlUtils.getString(smgp, "feeTerminalId");
		this.feeType = XmlUtils.getString(smgp, "feeType");
		this.feeCode = XmlUtils.getString(smgp, "feeCode");
		this.spId = XmlUtils.getString(smgp, "spId");
		this.spCode = XmlUtils.getString(smgp, "spCode");
		this.userName = XmlUtils.getString(smgp, "ismgUserName");
		this.pwd = XmlUtils.getString(smgp, "ismgPwd");
		this.connCount = XmlUtils.getInt(smgp, "connCount");
		this.mtFlux = XmlUtils.getInt(smgp, "mtFlux");
		this.longsmSupport = XmlUtils.getBoolean(smgp, "longsmSupport");
		this.serviceid = XmlUtils.getString(smgp, "serviceid");
		this.number = XmlUtils.getString(smgp, "number");

		this.reSendChannl = XmlUtils.getString(smgp, "reSendChannl");
		this.window_size = XmlUtils.getInt(smgp, "windowSize");

		this.isSmsSign = XmlUtils.getBoolean(smgp, "isSmsSign");
		this.ismgSignCn = XmlUtils.getString(smgp, "ismgSignCn");
		this.ismgSignEn = XmlUtils.getString(smgp, "ismgSignEn");

		this.smsLengthEn = XmlUtils.getInt(smgp, "smsLengthEn");
		this.smsLengthCn = XmlUtils.getInt(smgp, "smsLengthCn");
		this.isNeedReport=XmlUtils.getInt(smgp, "isNeedReport");
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
		return this.id;
	}

	public String getIsmgid()
	{
		return id;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPwd()
	{
		return pwd;
	}

	public String getSpID()
	{
		return spId;
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

	public int getFeeUserType()
	{
		return feeUserType;
	}

	public String getFeeTerminalId()
	{
		return feeTerminalId;
	}

	public String getFeeType()
	{
		return feeType;
	}

	public String getFeeCode()
	{
		return feeCode;
	}

	public String getSmsMessageLen()
	{
		return smsMessageLen;
	}

	public Element getSmgp()
	{
		return smgp;
	}

	public String getIsmgIp()
	{
		return ismgIp;
	}

	public int getIsmgPort()
	{
		return ismgPort;
	}

	public String getSpId()
	{
		return spId;
	}

	public String getServiceid()
	{
		return serviceid;
	}
	public boolean isRun()
	{
		return isRun;
	}

	public String getNumber()
	{
		return number;
	}

	public boolean isSmsSign()
	{
		return isSmsSign;
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
