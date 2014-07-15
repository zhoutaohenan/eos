package com.surge.engine.sms.pojo;

import java.util.concurrent.atomic.AtomicInteger;

import com.surge.engine.sms.common.ConnectStatus;

/**
 * һ������ͨ��
 * 
 * @project: eskprj
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsChannel implements java.io.Serializable {
    private static final long serialVersionUID = 5059539189347768127L;

    /** ͨ��ID */
    private String channeId;

    /** ͨ��Э������ */
    private SmsProtocolType protocolType;

    /** ͨ����ʼ��MT���д�С */
    private int initMtQueueSize;

    /** ͨ������״̬ */
    private ConnectStatus connectStatus = ConnectStatus.Disconnect;

    /** ͨ���Ƿ����У������ */
    private boolean isRun;

    /** �Ƿ������ɹ� */
    private boolean isStartSucc;

    /** ��Ӧ���ط�ͨ�� */
    private String reSendChannels;

    /** ƽ̨����Э��ר�ã�����ʣ����ö�ȼ������ʣ������ */
    private AtomicInteger remainCount = new AtomicInteger();

    /** �ط��� **/
    private String spcode;

    /** ����ǩ�� **/
    private String cnSign;

    /** Ӣ��ǩ�� **/
    private String enSign;
    /** ���� **/
    private int flux;
    /** �Ŷ� **/
    private String numberSegment;
    /**
	 */
    private int smsLengthCn;
    private int smsLengthEn;

    public SmsChannel(String channeId, SmsProtocolType protocolType, int initMtQueueSize, boolean isRun,
                      String reSendChannels, String spcode,int smsLengthCn,int smsLengthEn) {
        this.channeId = channeId;
        this.protocolType = protocolType;
        this.initMtQueueSize = initMtQueueSize;
        this.isRun = isRun;
        this.reSendChannels = reSendChannels;
        this.spcode = spcode;
        this.smsLengthCn=smsLengthCn;
        this.smsLengthEn=smsLengthEn;
    }

    public String getChanneId() {
        return channeId;
    }

    public void setChanneId(String channeId) {
        this.channeId = channeId;
    }

    public SmsProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(SmsProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public ConnectStatus getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(ConnectStatus connectStatus) {
        this.connectStatus = connectStatus;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    public int getInitMtQueueSize() {
        return initMtQueueSize;
    }

    public void setInitMtQueueSize(int initMtQueueSize) {
        this.initMtQueueSize = initMtQueueSize;
    }

    public String getReSendChannels() {
        return reSendChannels;
    }

    public void setReSendChannels(String reSendChannels) {
        this.reSendChannels = reSendChannels;
    }

    public boolean isStartSucc() {
        return isStartSucc;
    }

    public void setStartSucc(boolean isStartSucc) {
        this.isStartSucc = isStartSucc;
    }

    public AtomicInteger getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(AtomicInteger remainCount) {
        this.remainCount = remainCount;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public String getCnSign() {
        return cnSign;
    }

    public void setCnSign(String cnSign) {
        this.cnSign = cnSign;
    }

    public String getEnSign() {
        return enSign;
    }

    public void setEnSign(String enSign) {
        this.enSign = enSign;
    }

    public int getFlux() {
        return flux;
    }

    public void setFlux(int flux) {
        this.flux = flux;
    }

    public String getNumberSegment() {
        return numberSegment;
    }

    public void setNumberSegment(String numberSegment) {
        this.numberSegment = numberSegment;
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

}
