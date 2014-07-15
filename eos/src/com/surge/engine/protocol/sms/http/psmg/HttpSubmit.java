package com.surge.engine.protocol.sms.http.psmg;

import org.apache.mina.core.buffer.IoBuffer;

import com.surge.communication.framework.common.PMessage;

public class HttpSubmit extends PMessage {
    /**
     * 
     */
    private static final long serialVersionUID = -6008311752406603783L;
    /**
     * ÕÊºÅ
     */
    private String loginName;
    /**
     * ÃÜÂë
     */
    private String password;
    /**
     * ¶ÌÐÅÄÚÈÝ
     */
    private String contnet;

    private String seqId;
    
    private String smsId;

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContnet() {
        return contnet;
    }

    public void setContnet(String contnet) {
        this.contnet = contnet;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Ä¿µÄºÅÂë
     */
    private String mobile;

    @Override
    public int getCommonId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IoBuffer getIoBuffer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] getOut() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSeqId() {
        // TODO Auto-generated method stub
        return this.seqId;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
}
