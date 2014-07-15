package com.surge.engine.sms.pojo;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.surge.communication.framework.common.PMessage;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-3
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class Sms implements Comparable<Sms>, Serializable, Cloneable {

    private static final long serialVersionUID = 6250587665926986934L;

    private transient Logger logger;

    /** ���ݿ����ɵĶ���ID */
    private String mtId;

    /** ��ҵ�ͻ�������� */
    private String smsid;

    /** �ֻ����� */
    private String dest;

    /** 0,����Ҫ״̬����,1��Ҫ״̬���� */
    private int isNeedReport;

    /** ҵ����� */
    private String service_id;

    /** MT��ˮ�� */
    private String src_id = "";

    /** �������� msg_fmt��0��8��15ʱ�������ݼ������� */
    private String content;

    /** �ύ����ǰ��ֺ�Ķ��������б�,����� */
    private List<byte[]> contentbytesList;

    /** �ύ����ǰ ��ֺ�Ķ��������б� */
    private String[] contents;

    /** ���뷽ʽ */
    private int msg_fmt = 15;// �������ͣ�6����˸ 7������

    /** �������ȼ� */
    private int priority;

    /** ��������ʱ�� */
    private String createTime;

    /** ָ�ύ�����ص�ʱ�� */
    private long submit2IsmgTime;

    /** ���Ŵ���ڣ���λ�� */
    private int validTime;

    /** ��ֺ�Ķ������� */
    private int smsIndex;

    /** ���������Ƿ��ǳ����� */
    private boolean isLongSms;

    /** ���������Ƿ���ȫӢ�Ķ��� */
    private boolean isEnglish;

    /** �Ƿ����ط����� */
    private boolean isReSend;

    /** ��ֱ�������Ϣ�� */
    private List<PMessage> pMessages;

    /** ������Ӧ��� **/
    private int sendResult;

    /** �ύ�����ط��ص���ϢID */
    private String messageId;

    /** ����ͨ�� */
    private SmsChannel channel;

    /** �յ���Ӧʱ�� */
    private long respTime;

    /** ��Ӧ���� */
    private String desc;

    /** ������Ӧ���ȷ���Ƿ���Ҫ�ط� */
    private boolean isNeedReSend = false;

    /** ��չ�� **/
    private String extcode;

    /** ����ID **/
    private String orgid;

    /** �û�ID **/
    private String userid;

    /** �û�ǩ�� **/
    private String userSign;

    public Sms() {

        this.logger = Logger.getLogger(this.getClass());
    }

    public int compareTo(Sms o) {

        return this.priority - o.priority;
    }

    public String getContent() {

        return content;
    }

    public void read(String mtId, String smsid, String dest, String src_id, String content, int msg_fmt,
                     boolean isNeedReport, int validTime, String createTime, int priority, SmsChannel smsChannel,
                     String extcode) {

        this.mtId = mtId;
        this.smsid = smsid;
        this.dest = dest;
        this.src_id = src_id;
        this.content = content;
        this.msg_fmt = msg_fmt;
        this.isNeedReport = isNeedReport ? 1 : 0;
        this.validTime = validTime;
        this.createTime = createTime;
        this.priority = priority;
        this.channel = smsChannel;
        this.extcode = (extcode != null ? extcode : "");
    }

    public void setContent(String content) {

        this.content = content;
    }

    public String getDest() {

        return dest;
    }

    public void setDest(String dest) {

        this.dest = dest;
    }

    public int getPriority() {

        return priority;
    }

    public void setPriority(int priority) {

        this.priority = priority;
    }

    public long getSubmit2IsmgTime() {

        return submit2IsmgTime;
    }

    public int getRegisteredDelivery() {

        return isNeedReport;
    }

    public void setRegisteredDelivery(int registered_delivery) {

        this.isNeedReport = registered_delivery;
    }

    public String getServiceId() {

        return service_id;
    }

    public void setServiceId(String service_id) {

        this.service_id = service_id;
    }

    public Sms clone() {

        Sms obj = null;
        try {
            obj = (Sms) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error("", e);
        }

        return obj;
    }

    public List<byte[]> getContentbytesList() {

        return contentbytesList;
    }

    public void setContentbytesList(List<byte[]> contentbytesList) {

        this.contentbytesList = contentbytesList;
    }

    public String[] getContents() {

        return contents;
    }

    public void setContents(String[] contents) {

        this.contents = contents;
    }

    public SmsChannel getChannel() {

        return channel;
    }

    public void setChannel(SmsChannel channel) {

        this.channel = channel;
    }

    public boolean isLongSms() {

        return isLongSms;
    }

    public void setLongSms(boolean isLongSms) {

        this.isLongSms = isLongSms;
    }

    public boolean isEnglish() {

        return isEnglish;
    }

    public void setEnglish(boolean isEnglish) {

        this.isEnglish = isEnglish;
    }

    public List<PMessage> getpMessages() {

        return pMessages;
    }

    public void setpMessages(List<PMessage> pMessages) {

        this.pMessages = pMessages;
    }

    public int getSendResult() {

        return sendResult;
    }

    public void setSendResult(int sendResult) {

        this.sendResult = sendResult;
    }

    public int getSmsIndex() {

        return smsIndex;
    }

    public void setSmsIndex(int smsIndex) {

        this.smsIndex = smsIndex;
    }

    public String getSmsid() {

        return smsid;
    }

    public void setSmsid(String smsid) {

        this.smsid = smsid;
    }

    public int getIsNeedReport() {

        return isNeedReport;
    }

    public void setIsNeedReport(int isNeedReport) {

        this.isNeedReport = isNeedReport;
    }

    public String getService_id() {

        return service_id;
    }

    public void setService_id(String serviceId) {

        service_id = serviceId;
    }

    public String getSrc_id() {

        return src_id;
    }

    public void setSrc_id(String srcId) {

        this.src_id = srcId;
    }

    public int getMsg_fmt() {

        return msg_fmt;
    }

    public void setMsg_fmt(int msgFmt) {

        this.msg_fmt = msgFmt;
    }

    public String getCreateTime() {

        return createTime;
    }

    public void setCreateTime(String createTime) {

        this.createTime = createTime;
    }

    public int getValidTime() {

        return validTime;
    }

    public void setValidTime(int validTime) {

        this.validTime = validTime;
    }

    public boolean isReSend() {

        return isReSend;
    }

    public void setReSend(boolean isReSend) {

        this.isReSend = isReSend;
    }

    public String getMessageId() {

        return messageId;
    }

    public void setMessageId(String messageId) {

        this.messageId = messageId;
    }

    public void setSubmit2IsmgTime(long submit2IsmgTime) {

        this.submit2IsmgTime = submit2IsmgTime;
    }

    public String getMtId() {

        return mtId;
    }

    public void setMtId(String mtId) {

        this.mtId = mtId;
    }

    public long getRespTime() {
        return respTime;
    }

    public void setRespTime(long respTime) {
        this.respTime = respTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isNeedReSend() {
        return isNeedReSend;
    }

    public void setNeedReSend(boolean isNeedReSend) {
        this.isNeedReSend = isNeedReSend;
    }

    public String getExtcode() {
        return extcode;
    }

    public void setExtcode(String extcode) {
        this.extcode = extcode;
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

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

}