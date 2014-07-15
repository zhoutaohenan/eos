package com.surge.engine.sms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.surge.engine.sms.conf.Cmpp2Config;
import com.surge.engine.sms.conf.HttpConfig;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.engine.sms.conf.SmgpConfig;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsProtocolType;

/**
 * 短信通道管理器
 */
public class SmsChannelMgr {
    /** 每种通道对应的号段 */
    private Map<SmsChannel, String> numberChannel = new HashMap<SmsChannel, String>();

    private Map<String, SmsChannel> channels = new HashMap<String, SmsChannel>();

    private static Pattern pattern = Pattern.compile("\\d{1,}");

    private SmsConfig smsConfig = SmsConfig.getInstance();

    private static SmsChannelMgr instance;

    private SmsChannelMgr() {
        this.init();
    }

    /**
     * 添加通道及号段
     * 
     * @param smsChannel
     * @param number void
     * @throws
     */
    public void init() {
        Map<String, Cmpp2Config> cmppMap = smsConfig.getCmpp2Ismgs();
        for (Map.Entry<String, Cmpp2Config> entry : cmppMap.entrySet()) {
            Cmpp2Config cmpp2Config = entry.getValue();

            SmsChannel smsChannel = new SmsChannel(entry.getKey(),
                                                   SmsProtocolType.cmpp2,
                                                   cmpp2Config.getMtFlux() * 6,
                                                   cmpp2Config.isRun(),
                                                   cmpp2Config.getReSendChannl(),
                                                   cmpp2Config.getSpCode(),
                                                   cmpp2Config.getSmsLengthCn(),
                                                   cmpp2Config.getSmsLengthEn());
            smsChannel.setCnSign(cmpp2Config.getIsmgSignCn());
            smsChannel.setEnSign(cmpp2Config.getIsmgSignEn());
            smsChannel.setFlux(cmpp2Config.getMtFlux());
            smsChannel.setNumberSegment(cmpp2Config.getNumber());
            numberChannel.put(smsChannel, entry.getValue().getNumber());
            channels.put(smsChannel.getChanneId(), smsChannel);

        }

        Map<String, SgipConfig> sgipMap = smsConfig.getSgip12Ismgs();
        for (Map.Entry<String, SgipConfig> entry : sgipMap.entrySet()) {
            SgipConfig sgipConfig = entry.getValue();

            SmsChannel smsChannel = new SmsChannel(entry.getKey(),
                                                   SmsProtocolType.sgip,
                                                   sgipConfig.getMtFlux() * 6,
                                                   sgipConfig.isRun(),
                                                   sgipConfig.getReSendChannl(),
                                                   sgipConfig.getSpCode(),
                                                   sgipConfig.getSmsLengthCn(),
                                                   sgipConfig.getSmsLengthEn());
            smsChannel.setCnSign(sgipConfig.getIsmgSignCn());
            smsChannel.setEnSign(sgipConfig.getIsmgSignEn());
            smsChannel.setFlux(sgipConfig.getMtFlux());
            smsChannel.setNumberSegment(sgipConfig.getNumber());
            numberChannel.put(smsChannel, entry.getValue().getNumber());
            channels.put(smsChannel.getChanneId(), smsChannel);
        }

        // Map<String, GwConfig> gwMap = smsConfig.getGwConfigIsmgs();
        // for (Map.Entry<String, GwConfig> entry : gwMap.entrySet())
        // {
        // GwConfig gwConfig = entry.getValue();
        //
        // SmsChannel smsChannel = new SmsChannel(entry.getKey(),
        // SmsProtocolType.gw, gwConfig
        // .getMtFlux() * 6, gwConfig.isRun(), gwConfig.getReSendChannl(),
        // String
        // .valueOf(gwConfig.getSpCodeLength()));
        // smsChannel.setCnSign(gwConfig.getIsmgSignCn());
        // smsChannel.setEnSign(gwConfig.getIsmgSignEn());
        // smsChannel.setFlux(gwConfig.getMtFlux());
        // smsChannel.setNumberSegment(gwConfig.getNumber());
        // numberChannel.put(smsChannel, entry.getValue().getNumber());
        // channels.put(smsChannel.getChanneId(), smsChannel);
        // }

        Map<String, SmgpConfig> smgpMap = smsConfig.getSmgpConfigIsmgs();
        for (Map.Entry<String, SmgpConfig> entry : smgpMap.entrySet()) {
            SmgpConfig smgpConfig = entry.getValue();

            SmsChannel smsChannel = new SmsChannel(entry.getKey(),
                                                   SmsProtocolType.smgp,
                                                   smgpConfig.getMtFlux() * 6,
                                                   smgpConfig.isRun(),
                                                   smgpConfig.getReSendChannl(),
                                                   smgpConfig.getSpCode(),
                                                   smgpConfig.getSmsLengthCn(),
                                                   smgpConfig.getSmsLengthEn());
            smsChannel.setCnSign(smgpConfig.getIsmgSignCn());
            smsChannel.setEnSign(smgpConfig.getIsmgSignEn());
            smsChannel.setFlux(smgpConfig.getMtFlux());
            smsChannel.setNumberSegment(smgpConfig.getNumber());
            numberChannel.put(smsChannel, entry.getValue().getNumber());
            channels.put(smsChannel.getChanneId(), smsChannel);
        }

        Map<String, HttpConfig> httpMap = smsConfig.getHttpConfigs();
        for (Map.Entry<String, HttpConfig> entry : httpMap.entrySet()) {
            HttpConfig httpConfig = entry.getValue();

            SmsChannel smsChannel = new SmsChannel(entry.getKey(),
                                                   SmsProtocolType.http,
                                                   httpConfig.getMtFlux() * 6,
                                                   httpConfig.isRun(),
                                                   null,
                                                   null,
                                                   httpConfig.getSmsLengthCn(),
                                                   httpConfig.getSmsLengthEn());;
            smsChannel.setCnSign(httpConfig.getIsmgSignCn());
            smsChannel.setEnSign(httpConfig.getIsmgSignEn());
            smsChannel.setFlux(httpConfig.getMtFlux());
            smsChannel.setNumberSegment(httpConfig.getNumber());
            numberChannel.put(smsChannel, entry.getValue().getNumber());
            channels.put(smsChannel.getChanneId(), smsChannel);
        }
    }

    public List<SmsChannel> routing(String mobile) {
        List<SmsChannel> ret = new ArrayList<SmsChannel>();
        // 空,不是数字
        if (mobile == null || mobile.equals("") || !pattern.matcher(mobile).matches() || mobile.length() < 3) {
            return ret;
        }
        mobile = mobile.startsWith("086") ? mobile.substring(3) : mobile;
        mobile = mobile.startsWith("86") ? mobile.substring(2) : mobile;
        mobile = mobile.startsWith("+86") ? mobile.substring(3) : mobile;
        for (Map.Entry<SmsChannel, String> entry : numberChannel.entrySet()) {
            SmsChannel key = entry.getKey();
            String value = entry.getValue();
            String first3Num = mobile.substring(0, 3);
            if (value.indexOf(first3Num) != -1) {
                ret.add(key);
            }
        }
        return ret;
    }

    public SmsChannel getChannel(String smsChannelId) {
        return channels.get(smsChannelId);
    }

    public Map<String, SmsChannel> getChannels() {
        return channels;
    }

    public static synchronized SmsChannelMgr getInstance() {
        if (instance == null) {
            instance = new SmsChannelMgr();
        }
        return instance;
    }

    public void stop() {
        numberChannel.clear();
        channels.clear();
        if (instance != null) {
            instance = null;
        }
    }

    public Map<SmsChannel, String> getNumberChannel() {
        return numberChannel;
    }

    public void setNumberChannel(Map<SmsChannel, String> numberChannel) {
        this.numberChannel = numberChannel;
    }

}
