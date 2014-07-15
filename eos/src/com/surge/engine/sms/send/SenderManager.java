package com.surge.engine.sms.send;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Protocol;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.dbadapter.runnable.ScanLongSmsThread;
import com.surge.engine.monitor.ChannelStatMgr;
import com.surge.engine.protocol.sms.ProtocolFactory;
import com.surge.engine.sms.conf.Cmpp2Config;
import com.surge.engine.sms.conf.HttpConfig;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.engine.sms.conf.SmgpConfig;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.receive.SmsReceiveHandler;
import com.surge.engine.sms.service.GwFeeMgr;
import com.surge.engine.sms.service.SmsChannelMgr;
import com.surge.engine.sms.service.SmsHandler;
import com.surge.engine.sms.service.SmsQueueMgr;
import com.surge.engine.util.DesEncrypter;

/**
 * sender管理
 */
public class SenderManager {
    private static Logger logger = Logger.getLogger(SenderManager.class);

    /** 短信配置 */
    private SmsConfig smsConfig = SmsConfig.getInstance();

    /** 队列管理 */
    private SmsQueueMgr smsQueueMgr = SmsQueueMgr.getInstance();

    /** 通道队列统计管理 **/
    private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;

    private List<Sender> senders = new ArrayList<Sender>();

    private SmsReceiveHandler smsReceiveHandler = null;

    private SmsChannelMgr smsChannelMgr = null;

    private SmsHandler smsHandler = null;

    private GwFeeMgr feeMgr = null;

    private boolean isStartLongMo = false;

    private ScanLongSmsThread longSmsThread;

    public SenderManager(SmsHandler smsHandler, GwFeeMgr feeMgr) {
        this.smsHandler = smsHandler;
        this.feeMgr = feeMgr;
    }

    public void start() {
        logger.info("**********启动各个通道***********");
        smsChannelMgr = SmsChannelMgr.getInstance();
        smsReceiveHandler = new SmsReceiveHandler(smsQueueMgr.getSentSubmitMap(), smsHandler);
        startCmpp2(smsConfig.getCmpp2Ismgs());
        startSgip(smsConfig.getSgip12Ismgs());
        // startGw(smsConfig.getGwConfigIsmgs());
        startSmgp(smsConfig.getSmgpConfigIsmgs());
        startHttp(smsConfig.getHttpConfigs());
        if (isStartLongMo) {
            longSmsThread = new ScanLongSmsThread(smsConfig.getLongMoTime(), smsReceiveHandler);
            longSmsThread.start();
        }
    }

    public void stop() {
        isStartLongMo = false;
        for (Sender sender : senders) {
            sender.stop();
        }
        senders.clear();
        smsQueueMgr.stopQueue();
        smsChannelMgr.stop();
        longSmsThread.stopThread();
    }

    private void startCmpp2(Map<String, Cmpp2Config> cmpp2Configs) {
        for (Map.Entry<String, Cmpp2Config> entry : cmpp2Configs.entrySet()) {
            Cmpp2Config config = entry.getValue();
            if (config.isRun) {

                // boolean isLicense = this.isLicense(config.getSpCode(),
                // config.getIsmgid(), "cmpp2",
                // config);
                // if (!isLicense)
                // {
                // logger.warn("cmpp 通道:" + config.getIsmgid() +
                // "License 鉴权失败，禁止启动");
                // continue;
                // }
                Protocol protocol = ProtocolFactory.createProtocol(config, smsReceiveHandler);
                LinkedBlockingQueue<Sms> smsMtQueue = smsQueueMgr.allocateMtQueue(config.getIsmgid(),
                                                                                  config.getMtFlux() * 6);
                Map<String, Sms> sentQueue = smsQueueMgr.allocateSentQueue(config.getIsmgid());
                Sender sender = new Sender(protocol, smsMtQueue, sentQueue, smsHandler);

                boolean flag = sender.start();
                if (flag) {
                    smsChannelMgr.getChannel(config.getIsmgid()).setStartSucc(true);
                    senders.add(sender);
                    channelStatMgr.addChannelInfo(config.getIsmgid());
                    if (config.isLongsmSupport()) {
                        isStartLongMo = true;
                    }
                    logger.info("cmpp通道:" + config.getIsmgid() + "启动成功");
                } else {
                    logger.warn("cmpp通道:" + config.getIsmgid() + "启动失败");
                }

            } else {
                channelStatMgr.removeChannelInfo(config.getIsmgid());
            }
        }
    }

    private void startSgip(Map<String, SgipConfig> sgipConfigs) {
        for (Map.Entry<String, SgipConfig> entry : sgipConfigs.entrySet()) {
            SgipConfig config = entry.getValue();
            if (config.isRun()) {
                // boolean isLicense = this.isLicense(config.getSpCode(),
                // config.getIsmgid(), "sgip",
                // config);
                // if (!isLicense)
                // {
                // logger.warn("sgip通道:" + config.getIsmgid() +
                // " license鉴权失败，禁止启动");
                // continue;
                // }
                Protocol protocol = ProtocolFactory.createProtocol(config, smsReceiveHandler);
                LinkedBlockingQueue<Sms> smsMtQueue = smsQueueMgr.allocateMtQueue(config.getIsmgid(),
                                                                                  config.getMtFlux() * 6);
                Map<String, Sms> sentQueue = smsQueueMgr.allocateSentQueue(config.getIsmgid());
                Sender sender = new Sender(protocol, smsMtQueue, sentQueue, smsHandler);
                boolean flag = sender.start();
                if (flag) {
                    smsChannelMgr.getChannel(config.getIsmgid()).setStartSucc(true);
                    senders.add(sender);
                    if (config.isLongsmSupport()) {
                        isStartLongMo = true;
                    }
                    channelStatMgr.addChannelInfo(config.getIsmgid());
                    logger.info("sgip 通道:" + config.getIsmgid() + "启动成功");
                } else
                    logger.info("sgip通道:" + config.getIsmgid() + "启动失败");

            } else {
                channelStatMgr.removeChannelInfo(config.getIsmgid());
            }
        }
    }

    private void startSmgp(Map<String, SmgpConfig> smgpConfig) {
        for (Map.Entry<String, SmgpConfig> entry : smgpConfig.entrySet()) {
            SmgpConfig config = entry.getValue();
            if (config.isRun()) {
                // boolean isLicense = this.isLicense(config.getSpCode(),
                // config.getIsmgid(), "smgp",
                // config);
                // if (!isLicense)
                // {
                // logger.warn("smgp通道:" + config.getIsmgid() +
                // " license鉴权失败，禁止启动");
                // continue;
                // }
                Protocol protocol = ProtocolFactory.createProtocol(config, smsReceiveHandler);
                LinkedBlockingQueue<Sms> smsMtQueue = smsQueueMgr.allocateMtQueue(config.getIsmgid(),
                                                                                  config.getMtFlux() * 6);
                Map<String, Sms> sentQueue = smsQueueMgr.allocateSentQueue(config.getIsmgid());
                Sender sender = new Sender(protocol, smsMtQueue, sentQueue, smsHandler);
                boolean flag = sender.start();
                if (flag) {
                    smsChannelMgr.getChannel(config.getIsmgid()).setStartSucc(true);
                    senders.add(sender);
                    if (config.isLongsmSupport()) {
                        isStartLongMo = true;
                    }
                    channelStatMgr.addChannelInfo(config.getIsmgid());
                    logger.info("smgp 通道:" + config.getIsmgid() + "启动成功");
                } else
                    logger.info("smgp通道:" + config.getIsmgid() + "启动失败");

            } else {
                channelStatMgr.removeChannelInfo(config.getIsmgid());
            }
        }
    }

    // private void startGw(Map<String, GwConfig> gwConfigs)
    // {
    // for (Map.Entry<String, GwConfig> entry : gwConfigs.entrySet())
    // {
    // GwConfig config = entry.getValue();
    // if (config.isRun())
    // {
    // Protocol protocol = ProtocolFactory.createProtocol(config,
    // smsReceiveHandler);
    // LinkedBlockingQueue<Sms> smsMtQueue = smsQueueMgr
    // .allocateMtQueue(config.getProtocolId(), config
    // .getMtFlux() * 6);
    // Map<String, Sms> sentQueue = smsQueueMgr
    // .allocateSentQueue(config.getProtocolId());
    // Sender sender = new Sender(protocol, smsMtQueue, sentQueue,
    // smsHandler);
    //
    // boolean flag = sender.start();
    // if (flag)
    // {
    // smsChannelMgr.getChannel(config.getProtocolId())
    // .setStartSucc(true);
    // senders.add(sender);
    // feeMgr.addProtocol(protocol);
    // logger.info("gw 通道:" + config.getProtocolId() + "启动成功");
    // } else
    // {
    // logger.warn("gw通道:" + config.getProtocolId() + "启动失败");
    // }
    // channelStatMgr.addChannelInfo(config.getProtocolId());
    // } else
    // {
    // channelStatMgr.removeChannelInfo(config.getProtocolId());
    // }
    // }
    // }

    private void startHttp(Map<String, HttpConfig> httpConfig) {
        for (Map.Entry<String, HttpConfig> entry : httpConfig.entrySet()) {
            HttpConfig config = entry.getValue();
            if (config.isRun()) {

                Protocol protocol = ProtocolFactory.createProtocol(config, smsReceiveHandler);
                LinkedBlockingQueue<Sms> smsMtQueue = smsQueueMgr.allocateMtQueue(config.getIsmgId(),
                                                                                  config.getMtFlux() * 6);
                Map<String, Sms> sentQueue = smsQueueMgr.allocateSentQueue(config.getIsmgId());
                Sender sender = new Sender(protocol, smsMtQueue, sentQueue, smsHandler);

                boolean flag = sender.start();
                if (flag) {
                    smsChannelMgr.getChannel(config.getIsmgId()).setStartSucc(true);
                    senders.add(sender);
                    channelStatMgr.addChannelInfo(config.getIsmgId());

                    logger.info("http 通道:" + config.getIsmgId() + "启动成功");
                } else {
                    logger.warn("http 通道:" + config.getIsmgId() + "启动失败");
                }

            } else {
                channelStatMgr.removeChannelInfo(config.getIsmgId());
            }
        }
    }

    private String encode(String str) throws NoSuchAlgorithmException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes());

        return encodeHex(md5.digest());
    }

    private String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        int i;

        for (i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

    private boolean isLicense(String spcode, String channel, String protocol, ProtocolConfig config) {
        String fileName = "license/" + protocol + "_" + spcode + ".license";
        String licensedata = "";
        BufferedReader br = null;

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                logger.error("channel:" + channel + " " + spcode + ".license 不存在");
                return false;
            }
            br = new BufferedReader(new FileReader(file));
            licensedata = br.readLine();
            // 长短信
            String datalong = "33e92010@#$_1" + spcode;
            // 非长短信
            String data = "33e92010@#$" + spcode;
            datalong = DesEncrypter.encrypt(encode(datalong));// md5加密
            data = DesEncrypter.encrypt(encode(data));// md5加密
            if (licensedata != null && licensedata.length() > 0) {
                if (datalong.equals(licensedata.trim())) {
                    config.setLongMoSms(true);
                    logger.info("channel:" + channel + " spcode=" + spcode + "支持上行长短信********");
                    return true;
                } else if (data.equals(licensedata.trim())) {
                    config.setLongMoSms(false);
                    logger.info("channel:" + channel + " spcode=" + spcode + "不支持上行长短信********");
                    return true;
                }

            } else {
                logger.error("channel:" + channel + " " + spcode + ".license 内容校验失败");
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
