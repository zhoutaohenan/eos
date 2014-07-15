package com.surge.engine.protocol.sms.smgp.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.communication.framework.net.Client;
import com.surge.engine.monitor.ChannelStatMgr;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.pojo.Mo;
import com.surge.engine.protocol.sms.pojo.Receipt;
import com.surge.engine.protocol.sms.pojo.Response;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpConnectResp;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpDeliver;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpDeliverResp;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpKeepActiveTest;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpKeepActiveTestResp;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpReport;
import com.surge.engine.protocol.sms.smgp.pmsg.SmgpSubmitResp;
import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.sms.service.SmsQueueMgr;

/**
 * 
 * @description
 * @project: esk
 * @Date:2010-8-11
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmgpClientProcess implements Processor {

    private static final Logger logger = Logger.getLogger(SmgpClientProcess.class);

    private SmsProtocolHandler protocolHandler;

    /** 通道队列统计管理 **/
    private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;

    private SmsQueueMgr queueMgr = SmsQueueMgr.getInstance();

    private ProtocolConfig config;

    public SmgpClientProcess(SmsProtocolHandler protocolHandler, ProtocolConfig config) {

        this.protocolHandler = protocolHandler;
        this.config = config;
    }

    @Override
    public void doProcess(Client client, PMessage pMessage) {

        if (pMessage instanceof SmgpConnectResp) {
            doConnectResp(client, (SmgpConnectResp) pMessage);
        } else if (pMessage instanceof SmgpSubmitResp) {
            doSubmitResp(client, (SmgpSubmitResp) pMessage);
        } else if (pMessage instanceof SmgpKeepActiveTest) {
            SmgpKeepActiveTestResp activeResp = new SmgpKeepActiveTestResp(Integer.parseInt(pMessage.getSeqId()));
            client.sendPMessage(activeResp);
            if (!client.isLogined()) {
                client.setUnAvailable();
            }
        } else if (pMessage instanceof SmgpKeepActiveTestResp) {
            if (!client.isLogined()) {
                client.setUnAvailable();
            }
        } else if (pMessage instanceof SmgpDeliver) {
            doDeliver(client, (SmgpDeliver) pMessage);
        }
    }

    private void doConnectResp(Client client, SmgpConnectResp smgpConnectResp) {

        int state = smgpConnectResp.getStatus();
        if (state == 0) {
            if (logger.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\r\n*****************************************");
                sb.append("\r\nSMGP_ISMG_" + client.getProtocolId() + ":创建连接成功！");
                sb.append("\r\n*****************************************");
                logger.info(sb.toString());
            }
            client.setLogined(true);
            protocolHandler.doConnectStatus(client.getProtocolId(), true);
        } else {

            String retDesc = SmsTools.getSmgpSubmitRespResult(state);
            logger.error("SMGP_ISMG_" + client.getProtocolId() + "创建连接失败，错误描述为:" + retDesc + "(" + state + ")。");
            protocolHandler.doConnectStatus(client.getProtocolId(), false);
            client.setUnAvailable();
        }
    }

    private void doDeliver(Client client, SmgpDeliver deliver) {
        SmgpDeliverResp deliverResp = new SmgpDeliverResp(Integer.parseInt(deliver.getSeqId()),
                                                          deliver.getMsgId(),
                                                          (byte) 0);
        client.sendPMessage(deliverResp);
        // 是回执
        if (deliver.isReport()) {

            SmgpReport report = deliver.getReport();
            logger.debug("channel:" + client.getProtocolId() + report.toString());
            this.doReport(client, report);
        }
        // 是MO
        else {
            logger.debug("channel:" + client.getProtocolId() + deliver.toString());
            deliver.setCurrTime(System.currentTimeMillis());
            if (config.isSupportLongMoSms() && deliver.getTotalCout() > 0) {
                SmgpDeliver deliver_temp = this.doLongMo(deliver);
                if (deliver_temp != null) {
                    Mo mo = new Mo(String.valueOf(deliver_temp.getMsgId()),
                                   deliver_temp.getSrcTerminalId(),
                                   deliver_temp.getDestId(),
                                   deliver_temp.getContent());
                    protocolHandler.doMo(client.getProtocolId(), mo);
                }
            } else {
                Mo mo = new Mo(String.valueOf(deliver.getMsgId()),
                               deliver.getSrcTerminalId(),
                               deliver.getDestId(),
                               deliver.getContent());
                protocolHandler.doMo(client.getProtocolId(), mo);
            }

        }

    }

    /**
     * 处理长短信 TODO
     * 
     * @param deliver void
     * @throws
     */
    private SmgpDeliver doLongMo(SmgpDeliver deliver) {
        Map<String, PMessage> moMap = queueMgr.getLongMoSMSMap().get(config.getProtocolId());
        if (moMap == null) {
            moMap = new ConcurrentHashMap<String, PMessage>();
            queueMgr.getLongMoSMSMap().put(config.getProtocolId(), moMap);
        }
        SmgpDeliver passMo = (SmgpDeliver) moMap.get(deliver.getSrcTerminalId());
        if (passMo != null) {

            passMo.getLongContent()[deliver.getCurrCout() - 1] = deliver.getContent();
            StringBuilder build = new StringBuilder();
            // 遍历长短信内容数组
            for (String st : passMo.getLongContent()) {
                build.append(st);
                // 如果一个内容没有回来，就先缓存 起来
                if (st == null) {
                    passMo.setCurrTime(System.currentTimeMillis());
                    return null;
                }
            }
            // 组合内容
            passMo.setContent(build.toString());
            moMap.remove(deliver.getSrcTerminalId());
            return passMo;
        } else {
            // 编辑内容
            String content[] = new String[deliver.getTotalCout()];
            content[deliver.getCurrCout() - 1] = deliver.getContent();
            deliver.setLongContent(content);
            moMap.put(deliver.getSrcTerminalId(), deliver);
            queueMgr.getLongMoSMSMap().put(config.getProtocolId(), moMap);
        }
        return null;
    }

    private void doReport(Client client, SmgpReport smgpReport) {

        Receipt receipt = new Receipt(String.valueOf(smgpReport.getMsgId()),
                                      smgpReport.getDest_id(),
                                      smgpReport.getResult(),
                                      smgpReport.getStat(),
                                      smgpReport.getErr());
        if (smgpReport.getResult() == 0) {
            channelStatMgr.addSuccReports(client.getProtocolId());
        } else {
            channelStatMgr.addFailReports(client.getProtocolId());
        }
        protocolHandler.doReceipt(client.getProtocolId(), receipt);
    }

    public void doSubmitResp(Client client, SmgpSubmitResp submitResp) {

        logger.debug("channel:" + client.getProtocolId() + submitResp.toString());
        int result = submitResp.getResult();
        if (result != 0) {
            result = 1;
        }
        Response response = new Response(submitResp.getSeqId(),
                                         result,
                                         String.valueOf(submitResp.getMsgId()),
                                         submitResp.getDetail_result());
        SmgpClient smgpClient = (SmgpClient) client;
        smgpClient.removeSeqId(submitResp.getSeqId());
        protocolHandler.doResponse(client.getProtocolId(), response);

    }
}
