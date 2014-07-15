package com.surge.engine.protocol.sms.sgip.pmsg;

import java.nio.charset.CharacterCodingException;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

public class SgipBindResp extends SgipSendBase {
    private static final Logger logger = Logger.getLogger(SgipBindResp.class);

    private static final long serialVersionUID = 6830235892547317030L;

    private byte result;

    private String reserve = "";

    private String seqId;

    public SgipBindResp(IoBuffer buffer) {
        this.head = new SgipMsgHead(buffer);
        this.result = buffer.get();
        if (this.head.getTotalLength() == 29) {
            try {
                this.reserve = buffer.getString(8, decoder);
            } catch (CharacterCodingException e) {
                logger.error("解析SgipBindResp消息包出错", e);
            }
        }
    }

    public SgipBindResp(long nodeID, int datetime, int seq, byte result) {
        IoBuffer buffer = IoBuffer.allocate(9);
        buffer.put(result);
        try {
            buffer.putString("", 8, encoder);
        } catch (CharacterCodingException e) {
            logger.error("组建SgipBindResp消息包出错", e);
        }
        buffer.flip();
        this.body = buffer.array();
        this.head = new SgipMsgHead(this.body.length, -2147483647, nodeID, datetime, seq);
        this.seqId = nodeID + " " + datetime + " " + seq;
    }

    public String toString() {
        return "SgipBindResp seqId:" + this.seqId + " result:" + this.result;
    }

    public String getReserve() {
        return this.reserve;
    }

    public byte getResult() {
        return this.result;
    }
}