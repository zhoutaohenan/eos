package com.surge.engine.sms.send;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2Submit;
import com.surge.engine.protocol.sms.http.psmg.HttpSubmit;
import com.surge.engine.protocol.sms.sgip.pmsg.SgipSubmit;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpSubmit;
import com.surge.engine.sms.conf.Cmpp2Config;
import com.surge.engine.sms.conf.HttpConfig;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.engine.sms.conf.SmgpConfig;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsProtocolType;
import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.Tools;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsEncoder {

    private static int LONG_SMS_FLAG_SEQ = 0;

    private Sms sms;

    private SmsChannel smsChannel;

    private SmsConfig smsConfig = SmsConfig.getInstance();

    public SmsEncoder(Sms sms) {

        this.sms = sms;
        this.smsChannel = sms.getChannel();
    }

    public void encoder() throws UnsupportedEncodingException {

        SmsProtocolType protocolType = smsChannel.getProtocolType();
        switch (protocolType) {

            case sgip:
                sgipEncoder();
                break;
            case cmpp2:
                cmpp2Encoder();
                break;
            /*   case gw:
                   gwEncoder();
                   break;*/
            case smgp:
                smgpEncoder();
                break;
            case http:
                httpEncoder();
                break;
            default:
                break;
        }

    }

    /**
     * http通道编码
     */
    private void httpEncoder() {
        List<PMessage> pMessages = new ArrayList<PMessage>();
        int seq = getLongSmsSeq();

        HttpConfig config = smsConfig.getHttpConfigs().get(this.smsChannel.getChanneId());
        sms.setIsNeedReport(config.getIsNeedReport());
        HttpSubmit httpSubmit = new HttpSubmit();
        if (null != config.getIsmgSignCn() && !config.getIsmgSignCn().equalsIgnoreCase("")) {
            httpSubmit.setContnet(sms.getContent() + config.getIsmgSignCn());
        } else {
            if (null != sms.getUserSign() && !sms.getUserSign().equals("")) {
                httpSubmit.setContnet(sms.getContent() + sms.getUserSign());
            }

        }
        if (httpSubmit.getContnet() == null) {
            httpSubmit.setContnet(sms.getContent());
        }
        httpSubmit.setMobile(sms.getDest());
        httpSubmit.setLoginName(config.getName());
        httpSubmit.setPassword(config.getPassword());
        httpSubmit.setSeqId(String.valueOf(seq));
        httpSubmit.setSmsId(config.getIsmgId());
        pMessages.add(httpSubmit);
        sms.setpMessages(pMessages);
    }

    private void smgpEncoder() throws UnsupportedEncodingException {
        SmgpConfig smgpConfig = smsConfig.getSmgpConfigIsmgs().get(this.smsChannel.getChanneId());
        int pk_total = sms.getContents().length;
        sms.setIsNeedReport(smgpConfig.getIsNeedReport());
        int msgFmt = 15;
        int tp_udhi = 0;
        byte[] but = null;
        List<PMessage> pMessages = new ArrayList<PMessage>();
        int seq = getLongSmsSeq();
        for (int pk_number = 1; pk_number <= pk_total; pk_number++) {
            but = sms.getContentbytesList().get(pk_number - 1);
            if (sms.isLongSms()) {
                msgFmt = 8;
                tp_udhi = 1;

                but = this.formatLongSm(sms.getContentbytesList().get(pk_number - 1), pk_total, pk_number, (byte) seq);

            } else if (sms.isEnglish()) {
                msgFmt = 0;
            }
            int seq_id = Tools.getSeqId();
            String spcode = smgpConfig.getSpCode() + sms.getExtcode();
            if (spcode.length() > 20) {
                spcode = spcode.substring(0, 20);
            }
            PMessage sub = new SmgpSubmit(seq_id,
                                          (byte) pk_total,
                                          (byte) pk_number,
                                          (byte) smgpConfig.getIsNeedReport(),
                                          smgpConfig.getServiceid(),
                                          (byte) smgpConfig.getFeeUserType(),
                                          smgpConfig.getFeeTerminalId(),
                                          (byte) 0,
                                          (byte) tp_udhi,
                                          (byte) msgFmt,
                                          smgpConfig.getSpID(),
                                          smgpConfig.getFeeType(),
                                          smgpConfig.getFeeCode(),
                                          spcode,
                                          sms.getDest(),
                                          but,
                                          0,
                                          but.length,
                                          6);
            pMessages.add(sub);
        }
        sms.setpMessages(pMessages);
    }

    private void cmpp2Encoder() throws UnsupportedEncodingException {

        Cmpp2Config cmpp2Config = smsConfig.getCmpp2Ismgs().get(this.smsChannel.getChanneId());
        int pk_total = sms.getContents().length;
        sms.setIsNeedReport(cmpp2Config.getIsNeedReport());
        int msgFmt = 15;
        int tp_udhi = 0;
        byte[] but = null;
        List<PMessage> pMessages = new ArrayList<PMessage>();
        int seq = getLongSmsSeq();
        for (int pk_number = 1; pk_number <= pk_total; pk_number++) {
            but = sms.getContentbytesList().get(pk_number - 1);
            if (sms.isLongSms()) {
                msgFmt = 8;
                tp_udhi = 1;

                but = this.formatLongSm(sms.getContentbytesList().get(pk_number - 1), pk_total, pk_number, (byte) seq);

            } else if (sms.isEnglish()) {
                msgFmt = 0;
            }
            int seq_id = Tools.getSeqId();
            String spcode = cmpp2Config.getSpCode() + sms.getExtcode();
            if (spcode.length() > 20) {
                spcode = spcode.substring(0, 20);
            }
            PMessage sub = new Cmpp2Submit(seq_id,
                                           pk_number,
                                           (byte) pk_total,
                                           (byte) pk_number,
                                           (byte) cmpp2Config.getIsNeedReport(),
                                           cmpp2Config.getServiceid(),
                                           (byte) cmpp2Config.getFeeUserType(),
                                           cmpp2Config.getFeeTerminalId(),
                                           (byte) 0,
                                           (byte) tp_udhi,
                                           (byte) msgFmt,
                                           cmpp2Config.getSpID(),
                                           cmpp2Config.getFeeType(),
                                           cmpp2Config.getFeeCode(),
                                           spcode,
                                           sms.getDest(),
                                           but,
                                           0,
                                           but.length);
            pMessages.add(sub);
        }
        sms.setpMessages(pMessages);
    }

    /*private void gwEncoder()
    {
    	GwConfig gwConfig = smsConfig.getGwConfigIsmgs().get(this.smsChannel.getChanneId());
    	int pk_total = sms.getContents().length;
    	sms.setIsNeedReport(gwConfig.getIsNeedReport());
    	List<PMessage> pMessages = new ArrayList<PMessage>();
    	for (int pk_number = 1; pk_number <= pk_total; pk_number++)
    	{
    		// String sgin = gwConfig.getSign();
    		// byte mySinByte[] = sgin.getBytes();
    		byte message[] = sms.getContentbytesList().get(pk_number - 1);
    		String spcode = gwConfig.getFromPhone() + sms.getExtcode();
    		if (spcode.length() > 20)
    		{
    			spcode = spcode.substring(0, 20);
    		}
    		CSendReq sendReq = new CSendReq(gwConfig.getUserName(), spcode, sms.getDest(), 1);
    		sendReq.bFeeUser = 2;
    		sendReq.sFeePhone = gwConfig.getFeePhone();
    		sendReq.sService = gwConfig.getServiceID();
    		sendReq.sFeeType = gwConfig.getFeeType();
    		sendReq.iFee = gwConfig.getFeeValue();
    		sendReq.bFormat = 1;
    		sendReq.iLength = message.length;
    		sendReq.bMessage = message;
    		// sendReq.bSign = mySinByte;
    		// sendReq.iSign = mySinByte.length;
    		pMessages.add(sendReq);
    	}
    	sms.setpMessages(pMessages);
    }*/
    private void sgipEncoder() throws UnsupportedEncodingException {

        SgipConfig sgipConfig = smsConfig.getSgip12Ismgs().get(this.smsChannel.getChanneId());
        byte morelatetoMTFlag = 2;
        String expireTime = "";
        String scheduleTime = "";
        // byte reportFlag = (byte) ((sgipConfig.getIsNeedReceipt() == 1) ? 1 :
        // 2);
        byte reportFlag = (byte) ((sgipConfig.getIsNeedReport() == 1) ? 1 : 2);
        List<PMessage> pMessages = new ArrayList<PMessage>();
        int pk_total = sms.getContents().length;
        sms.setIsNeedReport(sgipConfig.getIsNeedReport());
        int msgFmt = 15;
        int tp_udhi = 0;
        byte[] but = null;
        int seq = getLongSmsSeq();
        for (int pk_number = 1; pk_number <= pk_total; pk_number++) {
            but = sms.getContentbytesList().get(pk_number - 1);
            if (sms.isLongSms()) {
                msgFmt = 8;
                tp_udhi = 1;

                but = this.formatLongSm(sms.getContentbytesList().get(pk_number - 1), pk_total, pk_number, (byte) seq);

            } else if (sms.isEnglish()) {
                msgFmt = 0;
            }
            String spcode = sgipConfig.getSpCode() + sms.getExtcode();
            if (spcode.length() > 20) {
                spcode = spcode.substring(0, 20);
            }
            PMessage pMessage = new SgipSubmit(sgipConfig.getNodeID(),
                                               spcode,
                                               sgipConfig.getChargeNumber(),
                                               sms.getDest(),
                                               sgipConfig.getSpId(),
                                               sgipConfig.getServiceid(),
                                               (byte) sgipConfig.getFeeType(),
                                               sgipConfig.getFeeValue(),
                                               sgipConfig.getGivenValue(),
                                               (byte) sgipConfig.getAgentFlag(),
                                               morelatetoMTFlag,
                                               (byte) 0,
                                               expireTime,
                                               scheduleTime,
                                               reportFlag,
                                               (byte) 0,
                                               (byte) tp_udhi,
                                               (byte) msgFmt,
                                               (byte) 0,
                                               but);
            pMessages.add(pMessage);
        }
        sms.setpMessages(pMessages);
    }

    private byte[] formatLongSm(byte[] content, int total, int curr, byte seq) throws UnsupportedEncodingException {

        AppendUtils t1 = new AppendUtils(1024);

        // 6位协议头格式：05 00 03 XX MM NN
        t1.appendByte((byte) 0x05);// 表示剩余协议头的长度
        t1.appendByte((byte) 0x00);// 这个值在GSM。

        t1.appendByte((byte) 0x03);// 这个值表示剩下短信标识的长度
        t1.appendByte(seq);// 这批短信的唯一标志，事实上，SME(手机或者SP)把消息合并完之后，就重新记录，

        t1.appendByte((byte) total);// 这批短信的数量。如果一个超长短信总共5条，这里的值就是5
        t1.appendByte((byte) curr);// 这批短信的数量。如果当前短信是这批短信中的第一条的值是1，第二条的值是2
        t1.appendBytes(content);

        return t1.getOutBytes();
    }

    public static synchronized int getLongSmsSeq() {
        LONG_SMS_FLAG_SEQ += 1;
        if (LONG_SMS_FLAG_SEQ == 127) {
            LONG_SMS_FLAG_SEQ = 0;
        }
        return LONG_SMS_FLAG_SEQ;
    }
}
