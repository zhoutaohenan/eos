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
    /** ���ݿ����ɵĶ���ID */
    protected String mtId;

    /** �ͻ�д��Ķ���ID */
    protected String smsId;

    /** Ŀ�ĵ�ַ */
    protected List<String> destAddrs;

    /** ��չ������� ���������չ����+��ˮ�� ����˺��볤�ȳ������Ƴ������Զ����ض� */
    protected String xCode;

    /** �������� */
    protected String message;

    /** �������ȼ�����0 ������ 0Ϊ������ȼ�������Խ�󼶱�Խ�� */
    protected int priority;

    /** �Ƿ���Ҫ״̬���� */
    protected boolean isNeedReport;

    /** ����ʱ�䣬��ʽΪ�� yyyy-MM-dd HH:mm:ss����Ϊnull��""ϵͳ��ȡ��ǰʱ�� */
    protected String sendTime;

    /** ���Ŵ���ڣ���λ�� */
    protected int validTime;

    /** ָ���ĸ�ͨ������ */
    protected SmsChannel channel;

    /** ����ʱ�䣬��ʽΪ�� yyyy-MM-dd HH:mm:ss�����ʱ�� */
    protected String createTime;

    /** �Ƿ����ط����� */
    protected boolean isRendSms;

    /** ��չ�� **/
    private String extcode;

    /** �û�ǩ�� **/
    private String smsSign;

    /** ����ID **/
    private String orgid;

    /** �û�ID **/
    private String userid;

    /** ͨ��id **/
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
