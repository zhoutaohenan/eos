package com.surge.engine.sms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Protocol;
import com.surge.engine.protocol.sms.gw.pmsg.CFeeReq;
import com.surge.engine.sms.common.ConnectStatus;
import com.surge.engine.sms.conf.GwConfig;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.send.Sender;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.TimerPool;

/**
 * 公司平台账号费用管理
 * 
 * @project: eskprj
 * @Date:2010-8-19
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class GwFeeMgr {
    private static final Logger logger = Logger.getLogger(Sender.class);

    private SmsConfig smsConfig = SmsConfig.getInstance();

    /** 通道管理 */
    // private SmsChannelMgr smsChannelMgr = SmsChannelMgr.getInstance();

    private List<Protocol> protocols = new ArrayList<Protocol>();

    // 查询费用间隔时间
    private long periodTime = 0;

    private Map<String, GwConfig> gwConfigIsmgs;

    public GwFeeMgr() {
        // gwConfigIsmgs = smsConfig.getGwConfigIsmgs();
        TimerPool.schedule(new Wroker(), 5, 30);
    }

    class Wroker implements Runnable {

        @Override
        public void run() {
            try {
                queryFree();

            } catch (Exception e) {
                logger.error("gw查询费用出错", e);
            }
        }

        public void queryFree() {
            long now = System.currentTimeMillis() / 1000;
            for (Protocol protocol : protocols) {
                GwConfig gwConfig = gwConfigIsmgs.get(protocol.getProtocolId());
                SmsChannel smsChannel = SmsChannelMgr.getInstance().getChannel(protocol.getProtocolId());

                if (smsChannel.getConnectStatus().equals(ConnectStatus.Connect)) {
                    if ((smsChannel.getRemainCount().get() <= 0) || (now - periodTime) > 1800) {
                        logger.info("平台账号费用查询 userName:" + gwConfig.getUserName());
                        periodTime = System.currentTimeMillis() / 1000;
                        CFeeReq feeReq = new CFeeReq(gwConfig.getUserName(), null);
                        feeReq.setInfo(String.valueOf(TimeUtil.getDate()), String.valueOf(TimeUtil.getDate()));
                        protocol.sendPMessage(feeReq);
                    }
                }
            }
        }

    }

    public void addProtocol(Protocol protocol) {
        this.protocols.add(protocol);
    }

    public void stop() {
        this.protocols.clear();
    }
}
