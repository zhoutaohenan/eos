package com.surge.engine.sms.conf;

import org.jdom.Element;

import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.util.XmlUtils;

public class HttpConfig implements ProtocolConfig {

    private boolean isRun;
    /**
     * 通道标识
     */
    private String ismgId;

    /**
     * 服务端URL
     */
    private String url;
    /**
     * 网关帐号登录名
     */
    private String name;
    /**
     * 网关帐号密码
     */
    private String password;
    /**
     * 发送速度
     */
    private int mtFlux;

    /**
     * 帐号字段名
     */
    private String nameKey;
    /**
     * 密码字段名
     */
    private String passwordKey;
    /**
     * 短信内容字段名
     */
    private String messageKey;
    /**
     * 号码字段名
     */
    private String mobileKey;

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
    private boolean isSmsSign;
    // 状态报告URL
    private String reportURL;
    // MO　URL
    private String moURL;
    /**
     * 发送号段
     */
    private String number;
    private int longsmsLength;
    private String protocolURL;

    protected void doConfig() {
        this.ismgId = http.getAttributeValue("id");
        this.isRun = XmlUtils.getBoolean(http, "isRun");
        this.url = XmlUtils.getString(http, "url");
        this.name = XmlUtils.getString(http, "name");
        this.password = XmlUtils.getString(http, "password");
        this.mtFlux = XmlUtils.getInt(http, "flux");
        this.messageKey = XmlUtils.getString(http, "messageKey");
        this.nameKey = XmlUtils.getString(http, "nameKey");
        this.mobileKey = XmlUtils.getString(http, "mobileKey");
        this.passwordKey = XmlUtils.getString(http, "passwordKey");
        this.isSmsSign = XmlUtils.getBoolean(http, "isSmsSign");
        this.ismgSignCn = XmlUtils.getString(http, "ismgSignCn");
        this.ismgSignEn = XmlUtils.getString(http, "ismgSignEn");

        this.smsLengthEn = XmlUtils.getInt(http, "smsLengthEn");
        this.smsLengthCn = XmlUtils.getInt(http, "smsLengthCn");
        this.isNeedReport = XmlUtils.getInt(http, "isNeedReport");
        this.number = XmlUtils.getString(http, "number");
        this.longsmsLength = XmlUtils.getInt(http, "longsmsLength");
        this.reportURL = XmlUtils.getString(http, "reportURL");
        this.moURL = XmlUtils.getString(http, "moURL");
        this.protocolURL=XmlUtils.getString(http, "protocolName");
    }

    public int getLongsmsLength() {
        return longsmsLength;
    }

    public void setLongsmsLength(int longsmsLength) {
        this.longsmsLength = longsmsLength;
    }

    public String getIsmgSignCn() {
        return ismgSignCn;
    }

    public void setIsmgSignCn(String ismgSignCn) {
        this.ismgSignCn = ismgSignCn;
    }

    public String getIsmgSignEn() {
        return ismgSignEn;
    }

    public void setIsmgSignEn(String ismgSignEn) {
        this.ismgSignEn = ismgSignEn;
    }

    public int getSmsLengthCn() {
        return smsLengthCn;
    }

    public void setSmsLengthCn(int smsLengthCn) {
        this.smsLengthCn = smsLengthCn;
    }

    public int getSmsLengthEn() {
        return smsLengthEn;
    }

    public void setSmsLengthEn(int smsLengthEn) {
        this.smsLengthEn = smsLengthEn;
    }

    public int getIsNeedReport() {
        return isNeedReport;
    }

    public void setIsNeedReport(int isNeedReport) {
        this.isNeedReport = isNeedReport;
    }

    private Element http;

    public HttpConfig(Element http) {
        this.http = http;
        this.doConfig();
    }

    public int getMtFlux() {
        return mtFlux;
    }

    public void setMtFlux(int mtFlux) {
        this.mtFlux = mtFlux;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    public String getIsmgId() {
        return ismgId;
    }

    public void setIsmgId(String ismgId) {
        this.ismgId = ismgId;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getPasswordKey() {
        return passwordKey;
    }

    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMobileKey() {
        return mobileKey;
    }

    public void setMobileKey(String mobileKey) {
        this.mobileKey = mobileKey;
    }

    public boolean isSmsSign() {
        return isSmsSign;
    }

    public void setSmsSign(boolean isSmsSign) {
        this.isSmsSign = isSmsSign;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int getConnCount() {
        return 0;
    }

    @Override
    public String getIp() {
        return null;
    }

    @Override
    public int getListenPort() {
        return 0;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getProtocolId() {
        return this.ismgId;
    }

    @Override
    public boolean isSupportLongMoSms() {
        return false;
    }

    @Override
    public void setLongMoSms(boolean isSms) {

    }

    public String getReportURL() {
        return reportURL;
    }

    public void setReportURL(String reportURL) {
        this.reportURL = reportURL;
    }

    public String getMoURL() {
        return moURL;
    }

    public void setMoURL(String moURL) {
        this.moURL = moURL;
    }

    public String getProtocolURL() {
        return protocolURL;
    }

    public void setProtocolURL(String protocolURL) {
        this.protocolURL = protocolURL;
    }

}
