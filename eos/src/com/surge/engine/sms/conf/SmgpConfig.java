package com.surge.engine.sms.conf;

import java.io.IOException;

import org.jdom.Element;

import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.util.XmlUtils;

public class SmgpConfig implements ProtocolConfig
{
	/** �Ƿ����� */
	public boolean isRun;

	/** ismg��� */
	public String id;

	/** ��������IP */
	protected String ismgIp;

	/** �������ض˿� */
	protected int ismgPort;

	/** ��¼�û��� */
	public String userName;

	/** ��ismg�������ӵ����� */
	public String pwd;

	/** ��ҵ���� */
	public String spId;

	/** sp_code */
	public String spCode;

	/** �������ڴ�С */
	public int window_size = 16;

	/** ��������С */
	public int connCount = 0;

	/** �Ƿ�֧�ֳ����� */
	public boolean longsmSupport;

	/** ������������ */
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

	/** ���Ķ���ǩ�� **/
	private String ismgSignCn;

	/** Ӣ�Ķ���ǩ�� **/
	private String ismgSignEn;

	/** һ�����Ķ��ų��� **/
	private int smsLengthCn;

	/** һ��Ӣ�Ķ��ų��� **/
	private int smsLengthEn;
	/** �Ƿ���Ҫ״̬���� **/
	private int isNeedReport;
    /**�Ƿ�֧�ֳ�����**/
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
