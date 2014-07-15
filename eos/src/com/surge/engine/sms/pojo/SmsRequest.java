package com.surge.engine.sms.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsRequest {
    /** 数据库生成的短信ID */
    protected String mtId;

    /** 客户写入的短信ID */
    protected String smsId;

    /** 目的地址 */
    protected List<String> destAddrs;

    /** 扩展服务代码 插件短信扩展号码+流水号 如果此号码长度超过限制长度则自动被截断 */
    protected String xCode;

    /** 短信内容 */
    protected String message;

    /** 短信优先级大于0 的整数 0为最高优先级，数字越大级别越低 */
    protected int priority;

    /** 是否需要状态报告 */
    protected boolean isNeedReport;

    /** 发送时间，格式为： yyyy-MM-dd HH:mm:ss。如为null或""系统则取当前时间 */
    protected String sendTime;

    /** 短信存活期，单位秒 */
    protected int validTime;

    /** 指定哪个通道发送 */
    protected SmsChannel channel;

    /** 发送时间，格式为： yyyy-MM-dd HH:mm:ss。入库时间 */
    protected String createTime;

    /** 是否是重发短信 */
    protected boolean isRendSms;

    /** 扩展码 **/
    private String extcode;

    /** 用户签名 **/
    private String smsSign;

    /** 机构ID **/
    private String orgid;

    /** 用户ID **/
    private String userid;

    /** 通道id **/
    private List<String> channelList = new ArrayList<String>();

    public List<String> getDestAddrs() {
        return destAddrs;
    }

    public void setDestAddrs(List<String> destAddrs) {
        this.destAddrs = destAddrs;
    }

    public String getxCode() {
        return xCode;
    }

    public void setxCode(String xCode) {
        this.xCode = xCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isNeedReport() {
        return isNeedReport;
    }

    public void setNeedReport(boolean isNeedReport) {
        this.isNeedReport = isNeedReport;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getValidTime() {
        return validTime;
    }

    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public SmsChannel getChannel() {
        return channel;
    }

    public void setChannel(SmsChannel channel) {
        this.channel = channel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isRendSms() {
        return isRendSms;
    }

    public void setRendSms(boolean isRendSms) {
        this.isRendSms = isRendSms;
    }

    public String getMtId() {
        return mtId;
    }

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getExtcode() {
        return extcode;
    }

    public void setExtcode(String extcode) {
        this.extcode = extcode;
    }

    public String getSmsSign() {
        return smsSign;
    }

    public void setSmsSign(String smsSign) {
        this.smsSign = smsSign;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<String> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<String> channelList) {
        this.channelList = channelList;
    }
}
