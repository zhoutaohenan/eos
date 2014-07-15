package com.surge.engine.sms.pojo;

import java.util.concurrent.atomic.AtomicInteger;

import com.surge.engine.sms.common.ConnectStatus;

/**
 * 一条短信通道
 * 
 * @project: eskprj
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsChannel implements java.io.Serializable {
    private static final long serialVersionUID = 5059539189347768127L;

    /** 通道ID */
    private String channeId;

    /** 通道协议类型 */
    private SmsProtocolType protocolType;

    /** 通道初始化MT队列大小 */
    private int initMtQueueSize;

    /** 通道连接状态 */
    private ConnectStatus connectStatus = ConnectStatus.Disconnect;

    /** 通道是否运行（配置项） */
    private boolean isRun;

    /** 是否启动成功 */
    private boolean isStartSucc;

    /** 对应的重发通道 */
    private String reSendChannels;

    /** 平台网关协议专用，根据剩余费用额度计算出的剩余条数 */
    private AtomicInteger remainCount = new AtomicInteger();

    /** 特服号 **/
    private String spcode;

    /** 中文签名 **/
    private String cnSign;

    /** 英文签名 **/
    private String enSign;
    /** 流量 **/
    private int flux;
    /** 号段 **/
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
