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

	/** ismg��� */
	private String id;

	/** ��������IP */
	private String ismgIp;

	/** �������ض˿� */
	private int ismgPort = 8801;

	/** ��¼�û��� */
	private String userName;

	/** ��ismg�������ӵ����� */
	private String pwd;

	/** ��ҵ���� */
	private String spId;

	/** sp_code */
	private String spCode;

	private String serviceid;

	private int connectType;

	/** �������ڴ�С */
	private int window_size = 16;

	/** ��������С */
	private int connCount = 0;

	/** �Ƿ�֧�ֳ����� */
	private boolean longsmSupport;

	/** ������������ */
	private int mtFlux;

	private long nodeID = 399999;// �ڵ���

	private int listenPort = 8802;// ��Ϊ����μ����˿�

	private int feeType = 0; // �Ʒ�����

	private String feeValue = "0"; // ��������Ϣ���շ�ֵ����λΪ��

	private String givenValue = "0";// �����û��Ļ��ѣ���λΪ��

	private String chargeNumber = "000000000000000000000";

	private int needRPT = 1; // 0 or 2 :��Ҫ��ִ�� 1����Ҫ��ִ

	private int agentFlag = 1;// ���շѱ�־��0��Ӧ�գ�1��ʵ��-

	private int timeout = 60;// ��������ʱ�������룩��������ʱ�䣬���ӹر�,Ĭ����60��

	private String number;

	private String reSendChannl;

	/** ���Ķ���ǩ�� **/
	private String ismgSignCn;

	/** Ӣ�Ķ���ǩ�� **/
	private String ismgSignEn;

	/** һ�����Ķ��ų��� **/
	private int smsLengthCn;

	/** һ��Ӣ�Ķ��ų��� **/
	private int smsLengthEn;

	private boolean isSmsSign;
	
	/** �Ƿ���Ҫ״̬���� **/
	private int isNeedReport;
    /**�Ƿ�֧�ֳ�����**/
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
