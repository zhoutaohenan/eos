package com.surge.engine.sms.conf;

import java.io.IOException;

import org.jdom.Element;

import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.util.XmlUtils;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class Cmpp2Config implements ProtocolConfig
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

	private Element cmpp;

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
    /**�Ƿ�֧�ֳ�MO����**/
	private boolean isLongMo;
	public Cmpp2Config(Element cmpp) throws IOException
	{
		this.cmpp = cmpp;
		this.doGetConfig();
	}

	protected void doGetConfig()
	{
		this.id = cmpp.getAttributeValue("id");
		this.isRun = XmlUtils.getBoolean(cmpp, "isRun");
		this.ismgIp = XmlUtils.getString(cmpp, "ismgIp");
		this.ismgPort = XmlUtils.getInt(cmpp, "ismgPort");
		this.connCount = XmlUtils.getInt(cmpp, "connCount");
		this.feeUserType = XmlUtils.getInt(cmpp, "feeUserType");
		this.feeTerminalId = XmlUtils.getString(cmpp, "feeTerminalId");
		this.feeType = XmlUtils.getString(cmpp, "feeType");
		this.feeCode = XmlUtils.getString(cmpp, "feeCode");
		this.spId = XmlUtils.getString(cmpp, "spId");
		this.spCode = XmlUtils.getString(cmpp, "spCode");
		this.userName = XmlUtils.getString(cmpp, "ismgUserName");
		this.pwd = XmlUtils.getString(cmpp, "ismgPwd");
		this.connCount = XmlUtils.getInt(cmpp, "connCount");
		this.mtFlux = XmlUtils.getInt(cmpp, "mtFlux");
		this.longsmSupport = XmlUtils.getBoolean(cmpp, "longsmSupport");
		this.serviceid = XmlUtils.getString(cmpp, "serviceid");
		this.number = XmlUtils.getString(cmpp, "number");

		this.reSendChannl = XmlUtils.getString(cmpp, "reSendChannl");
		this.window_size = XmlUtils.getInt(cmpp, "windowSize");
		
		this.isSmsSign = XmlUtils.getBoolean(cmpp, "isSmsSign");
		this.ismgSignCn = XmlUtils.getString(cmpp, "ismgSignCn");
		this.ismgSignEn = XmlUtils.getString(cmpp, "ismgSignEn");

		this.smsLengthEn = XmlUtils.getInt(cmpp, "smsLengthEn");
		this.smsLengthCn = XmlUtils.getInt(cmpp, "smsLengthCn");
		this.isNeedReport = XmlUtils.getInt(cmpp, "isNeedReport");
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

	public Element getCmpp()
	{
		return cmpp;
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
		
		return isLongMo;
	}
	public void setLongMoSms(boolean isSms)
	{
		this.isLongMo=isSms;
	}

}
