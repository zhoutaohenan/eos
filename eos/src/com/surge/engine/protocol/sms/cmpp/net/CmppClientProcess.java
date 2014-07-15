package com.surge.engine.protocol.sms.cmpp.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Processor;
import com.surge.communication.framework.common.PMessage;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.communication.framework.net.Client;
import com.surge.engine.monitor.ChannelStatMgr;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2ConnectResp;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2Deliver;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2DeliverResp;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2KeepActive;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2KeepActiveResp;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2Report;
import com.surge.engine.protocol.sms.cmpp.pmsg.Cmpp2SubmitResp;
import com.surge.engine.protocol.sms.pojo.Mo;
import com.surge.engine.protocol.sms.pojo.Receipt;
import com.surge.engine.protocol.sms.pojo.Response;
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
public class CmppClientProcess implements Processor {

    private static final Logger logger = Logger.getLogger(CmppClientProcess.class);

    /** 通道队列统计管理 **/
    private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;

    private ProtocolConfig config;

    private SmsProtocolHandler protocolHandler;

    private SmsQueueMgr queueMgr = SmsQueueMgr.getInstance();

    public CmppClientProcess(SmsProtocolHandler protocolHandler, ProtocolConfig config) {

        this.protocolHandler = protocolHandler;
        this.config = config;
    }

    @Override
    public void doProcess(Client client, PMessage pMessage) {

        if (pMessage instanceof Cmpp2ConnectResp) {
            doConnectResp(client, (Cmpp2ConnectResp) pMessage);
        } else if (pMessage instanceof Cmpp2SubmitResp) {
            doSubmitResp(client, (Cmpp2SubmitResp) pMessage);
        } else if (pMessage instanceof Cmpp2KeepActive) {
            Cmpp2KeepActiveResp activeResp = new Cmpp2KeepActiveResp(Integer.parseInt(pMessage.getSeqId()));
            client.sendPMessage(activeResp);
            if (!client.isLogined()) {
                client.setUnAvailable();
            }
        } else if (pMessage instanceof Cmpp2KeepActiveResp) {
            if (!client.isLogined()) {
                client.setUnAvailable();
            }
        } else if (pMessage instanceof Cmpp2Deliver) {
            doDeliver(client, (Cmpp2Deliver) pMessage);
        }
    }

    private void doConnectResp(Client client, Cmpp2ConnectResp cmppConnectResp) {

        int state = cmppConnectResp.getStatus();
        if (state == 0) {
            if (logger.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\r\n*****************************************");
                sb.append("\r\nCMPP_ISMG_" + client.getProtocolId() + ":创建连接成功！");
                sb.append("\r\n*****************************************");
                logger.info(sb.toString());
            }
            client.setLogined(true);
            protocolHandler.doConnectStatus(client.getProtocolId(), true);
        } else {
            String retDesc = "";
            switch (state) {
                case 1:
                    retDesc = "消息结构错误";
                    break;
                case 2:
                    retDesc = "源地址非法";
                    break;
                case 3:
                    retDesc = "认证失败";
                    break;
                case 4:
                    retDesc = "CMPP版本太高";
                    break;
                default:
                    retDesc = "其他错误";
                    break;
            }
            client.setUnAvailable();
            logger.error("CMPP_ISMG_" + client.getProtocolId() + "创建连接失败，错误描述为:" + retDesc + "(" + state + ")。");
            protocolHandler.doConnectStatus(client.getProtocolId(), false);
        }
    }

    private void doDeliver(Client client, Cmpp2Deliver deliver) {

        Cmpp2DeliverResp deliverResp = new Cmpp2DeliverResp(Integer.parseInt(deliver.getSeqId()),
                                                            deliver.getMsgId(),
                                                            (byte) 0);
        client.sendPMessage(deliverResp);
        // 是回执
        if (deliver.isReport()) {

            Cmpp2Report report = deliver.getReport();
            logger.debug("channel:" + client.getProtocolId() + report.toString());
            this.doReport(client, report);
        }
        // 是MO
        else {
            logger.debug("channel:" + client.getProtocolId() + deliver.toString());
            deliver.setCurrTime(System.currentTimeMillis());
            if (config.isSupportLongMoSms() && deliver.getTotalCout() > 0) {
                Cmpp2Deliver deliver_temp = this.doLongMo(deliver);
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
    private Cmpp2Deliver doLongMo(Cmpp2Deliver deliver) {
        Map<String, PMessage> moMap = queueMgr.getLongMoSMSMap().get(config.getProtocolId());
        if (moMap == null) {
            moMap = new ConcurrentHashMap<String, PMessage>();
            queueMgr.getLongMoSMSMap().put(config.getProtocolId(), moMap);
        }
        Cmpp2Deliver passMo = (Cmpp2Deliver) moMap.get(deliver.getSrcTerminalId());
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

    private void doReport(Client client, Cmpp2Report cmppReport) {

        Receipt receipt = new Receipt(String.valueOf(cmppReport.getMsgId()),
                                      cmppReport.getDestId(),
                                      cmppReport.getResult(),
                                      cmppReport.getStat(),
                                      cmppReport.getDetail_result());
        if (cmppReport.getResult() == 0) {
            channelStatMgr.addSuccReports(client.getProtocolId());

        } else {
            channelStatMgr.addFailReports(client.getProtocolId());
        }
        protocolHandler.doReceipt(client.getProtocolId(), receipt);
    }

    public void doSubmitResp(Client client, Cmpp2SubmitResp submitResp) {

        logger.debug("channel:" + client.getProtocolId() + submitResp.toString());
        int result = submitResp.getResult();
        if (result != 0) {
            result = 1;
        }
        Response response = new Response(submitResp.getSeqId(),
                                         result,
                                         String.valueOf(submitResp.getMsgId()),
                                         submitResp.getDetail_result());
        CmppClient cmppClient = (CmppClient) client;
        cmppClient.removeSeqId(submitResp.getSeqId());
        protocolHandler.doResponse(client.getProtocolId(), response);

    }
}
