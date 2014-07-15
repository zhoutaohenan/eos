package com.surge.engine.sms.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.surge.engine.sms.common.ConnectStatus;
import com.surge.engine.sms.common.ErrCode;
import com.surge.engine.sms.common.MsgFmt;
import com.surge.engine.sms.conf.Cmpp2Config;
import com.surge.engine.sms.conf.HttpConfig;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.engine.sms.conf.SmgpConfig;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsProtocolType;
import com.surge.engine.sms.pojo.SmsRequest;
import com.surge.engine.sms.pojo.SmsResponse;
import com.surge.engine.sms.pojo.SplitMsg;
import com.surge.engine.sms.util.SmsSplit;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-9
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SmsAgentServiceImpl implements SmsAgentService {
    // private static Logger logger =
    // Logger.getLogger(SmsAgentServiceImpl.class);

    /** 队列管理 **/
    // private SmsQueueMgr smsQueueMgr = SmsQueueMgr.getInstance();

    /** 配置管理 */
    // private SmsConfig smsConfig = SmsConfig.getInstance();

    public SmsAgentServiceImpl() {

    }

    @Override
    public SmsResponse sendSms(SmsRequest smsRequest) {
        SmsResponse smsResponse = new SmsResponse();
        // logger.debug("channel:" + smsRequest.getChannel().getChanneId() +
        // " request:sismsid:"
        // + smsRequest.getSmsId() + " mtId:" + smsRequest.getMtId() +
        // " mobile:"
        // + smsRequest.getDestAddrs().get(0) + " message:" +
        // smsRequest.getMessage());
        ErrCode errCode = null;

        // 将短信内容拆分
        SplitMsg splitMsg = split(smsRequest.getMessage(), smsRequest.getChannel(), smsRequest.getSmsSign());
        List<byte[]> bysList = getBytes(splitMsg.getMsgs(), splitMsg.getMsgFmt());
        Sms sms = new Sms();
        sms.read(smsRequest.getMtId(),
                 smsRequest.getSmsId(),
                 smsRequest.getDestAddrs().get(0),
                 smsRequest.getxCode(),
                 smsRequest.getMessage(),
                 splitMsg.getMsgFmt().getValue(),
                 smsRequest.isNeedReport(),
                 smsRequest.getValidTime(),
                 smsRequest.getCreateTime(),
                 smsRequest.getPriority(),
                 smsRequest.getChannel(),
                 smsRequest.getExtcode());
        sms.setContents(splitMsg.getMsgs());
        sms.setContentbytesList(bysList);
        sms.setLongSms(splitMsg.isLongSms());
        sms.setEnglish(splitMsg.isEnglish());
        sms.setReSend(smsRequest.isRendSms());
        sms.setOrgid(smsRequest.getOrgid());
        sms.setUserid(smsRequest.getUserid());
        sms.setUserSign(smsRequest.getSmsSign());
        SmsQueueMgr.getInstance().putSmsMt(smsRequest.getChannel().getChanneId(), sms);

        // 暂时提交都成功，以后可扩展提交失败错误码
        smsResponse.setErrCode(errCode);
        return smsResponse;
    }

    @Override
    public List<SmsChannel> routing(String mobile) {
        return SmsChannelMgr.getInstance().routing(mobile);
    }

    @Override
    public Map<String, SmsChannel> getChannels() {
        return SmsChannelMgr.getInstance().getChannels();
    }

    /**
     * 得到拆分后的字符串内容，包括移动，短信猫两种方式的拆分
     * 
     * @param message
     * @param url
     * @param smsType
     * @param msgFmt
     * @return
     */
    private SplitMsg split(String message, SmsChannel channel, String userSign) {
        SplitMsg splitMsg = null;
        if (channel.getProtocolType().equals(SmsProtocolType.cmpp2)) {
            Cmpp2Config cmpp2Config = SmsConfig.getInstance().getCmpp2Ismgs().get(channel.getChanneId());
            SmsSplit smsSplit = new SmsSplit(message,
                                             cmpp2Config.isLongsmSupport(),
                                             cmpp2Config.getSmsLengthCn(),
                                             cmpp2Config.getSmsLengthEn(),
                                             cmpp2Config.isSmsSign(),
                                             cmpp2Config.getIsmgSignCn(),
                                             cmpp2Config.getIsmgSignEn(),
                                             userSign);
            splitMsg = smsSplit.splitSm();
        } else if (channel.getProtocolType().equals(SmsProtocolType.sgip)) {
            SgipConfig sgipConfig = SmsConfig.getInstance().getSgip12Ismgs().get(channel.getChanneId());
            SmsSplit smsSplit = new SmsSplit(message,
                                             sgipConfig.isLongsmSupport(),
                                             sgipConfig.getSmsLengthCn(),
                                             sgipConfig.getSmsLengthEn(),
                                             sgipConfig.isSmsSign(),
                                             sgipConfig.getIsmgSignCn(),
                                             sgipConfig.getIsmgSignEn(),
                                             userSign);
            splitMsg = smsSplit.splitSm();

        } /*else if (channel.getProtocolType().equals(SmsProtocolType.gw))
          {
          GwConfig gwConfig = SmsConfig.getInstance().getGwConfigIsmgs().get(
          		channel.getChanneId());
          SmsSplit smsSplit = new SmsSplit(message, gwConfig.isLongsmSupport(), gwConfig
          		.getSmsLengthCn(), gwConfig.getSmsLengthEn(), gwConfig.isSmsSign(), gwConfig
          		.getIsmgSignCn(), gwConfig.getIsmgSignEn(), userSign);
          splitMsg = smsSplit.splitSm();
          } */
        else if (channel.getProtocolType().equals(SmsProtocolType.smgp)) {
            SmgpConfig smgpConfig = SmsConfig.getInstance().getSmgpConfigIsmgs().get(channel.getChanneId());
            SmsSplit smsSplit = new SmsSplit(message,
                                             smgpConfig.isLongsmSupport(),
                                             smgpConfig.getSmsLengthCn(),
                                             smgpConfig.getSmsLengthEn(),
                                             smgpConfig.isSmsSign(),
                                             smgpConfig.getIsmgSignCn(),
                                             smgpConfig.getIsmgSignEn(),
                                             userSign);
            splitMsg = smsSplit.splitSm();
        } else if (channel.getProtocolType().equals(SmsProtocolType.http)) {
            HttpConfig cmpp2Config = SmsConfig.getInstance().getHttpConfigs().get(channel.getChanneId());
            SmsSplit smsSplit = new SmsSplit(message,
                                             false,
                                             cmpp2Config.getSmsLengthCn(),
                                             cmpp2Config.getSmsLengthEn(),
                                             cmpp2Config.isSmsSign(),
                                             cmpp2Config.getIsmgSignCn(),
                                             cmpp2Config.getIsmgSignEn(),
                                             userSign);
            splitMsg = smsSplit.splitSm();
        }
        return splitMsg;
    }

    /**
     * 根据短信内容格式取得相应字节数组
     * 
     * @param message
     * @param msgFmt
     * @return
     */
    public static List<byte[]> getBytes(String[] messageList, MsgFmt msgFmt) {
        byte[] retVal = null;
        List<byte[]> byteslist = new ArrayList<byte[]>();
        try {
            for (int i = 0; i < messageList.length; i++) {
                String message = messageList[i];

                // 将短信内容转换成字节数组
                switch (msgFmt) {
                    case ASCII:
                        retVal = message.getBytes("ISO-8859-1");
                        break;
                    case GB18030:
                        retVal = message.getBytes("GB18030");
                        break;
                    case GBK:
                        retVal = message.getBytes("GBK");
                        break;
                    case UCS2:
                        retVal = message.getBytes("UTF-16BE");
                        break;
                    case Binary:
                        retVal = message.getBytes("ISO-8859-1");
                        break;
                    default:
                        retVal = message.getBytes("ISO-8859-1");
                        break;
                }
                byteslist.add(retVal);
            }
        } catch (UnsupportedEncodingException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return byteslist;
    }

    @Override
    public String getReSendChannel(String channelId) {
        String ret = "";
        SmsChannel channel = SmsChannelMgr.getInstance().getChannel(channelId);
        if (channel != null) {
            ret = channel.getReSendChannels();
        }
        return ret;

    }

    public boolean isAllowSend(String channelId) {
        SmsChannel channel = SmsChannelMgr.getInstance().getChannel(channelId);
        if (channel == null) {
            return false;
        }
        if (!channel.isRun()) {
            return false;
        }
        if (!channel.isStartSucc()) {
            return false;
        }
        if ((!channel.getProtocolType().equals(SmsProtocolType.sgip))
            && (!channel.getProtocolType().equals(SmsProtocolType.http))
            && !channel.getConnectStatus().equals(ConnectStatus.Connect))

        {
            return false;
        }
        if (channel.getProtocolType().equals(SmsProtocolType.gw) && channel.getRemainCount().get() <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public int getAllowSendCount(String channelId) {
        int ret = 0;
        SmsChannel channel = SmsChannelMgr.getInstance().getChannel(channelId);
        LinkedBlockingQueue<Sms> mtQueue = SmsQueueMgr.getInstance().getMtQueue(channelId);
        if (mtQueue != null) {
            int currentMtQueueSize = mtQueue.size();
            int initSzie = channel.getInitMtQueueSize();
            // 初始化队列空间大小减当前队列数据数量
            ret = initSzie - currentMtQueueSize;
            //
        }
        if (channel.getProtocolType().equals(SmsProtocolType.gw)) {
            int remainCount = channel.getRemainCount().get();
            ret = Math.min(ret, remainCount);
        }
        if (ret > 300) {
            ret = 300;
        }
        return ret - 10;
    }

    @Override
    public String getSpCode(String protocolId) {
        SmsChannel channel = SmsChannelMgr.getInstance().getChannel(protocolId);
        if (channel != null) {
            return channel.getSpcode();
        }
        return null;

    }

    public int getSentSubmitMapSize(String channelId) {
        // 返回已发队列大小
        return SmsQueueMgr.getInstance().getSentSubmitMap().get(channelId).size();
    }

    @Override
    public int getRunningChannel() {
        int total = 0;
        Map<String, SmsChannel> allChannel = this.getChannels();
        for (Map.Entry<String, SmsChannel> entry : allChannel.entrySet()) {
            SmsChannel channel = entry.getValue();
            if (channel != null && channel.isRun()) {
                total++;
            }
        }
        return total;
    }
}
