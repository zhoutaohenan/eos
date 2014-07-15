package com.surge.engine.sms.send;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.surge.communication.framework.Protocol;
import com.surge.communication.framework.common.PMessage;
import com.surge.engine.monitor.ChannelStatMgr;
import com.surge.engine.protocol.sms.common.SendResult;
import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.service.SmsHandler;
import com.surge.engine.util.EskLog;
import com.surge.engine.util.ThreadFactory;
import com.surge.engine.util.Tools;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class Sender {

    private String senderId;

    private static final Logger logger = Logger.getLogger(Sender.class);

    /** mt�����Ͷ��� */
    private final LinkedBlockingQueue<Sms> mtQueue;

    /** mt�ѷ��Ͷ��� */
    private final Map<String, Sms> sentSubmits;

    private final Protocol protocol;

    private SendWroker sendWroker = new SendWroker();

    private static final int CONNECT_TIME = 2 * 60 * 1000;
    // private static final int CONNECT_TIME = 30 * 1000;

    private long disConnectTime = 0;

    private AtomicBoolean isConnect = new AtomicBoolean(true);

    /** ͨ������ͳ�ƹ��� **/
    private ChannelStatMgr channelStatMgr = ChannelStatMgr.instance;

    /** ���ŷ�����Ϣ�ص� */
    private SmsHandler smsHandler;

    public Sender(Protocol protocol, LinkedBlockingQueue<Sms> mtQueue, Map<String, Sms> sentSubmits,
                  SmsHandler smsHandler) {
        this.protocol = protocol;
        this.senderId = protocol.getProtocolId();
        this.mtQueue = mtQueue;
        this.sentSubmits = sentSubmits;
        this.smsHandler = smsHandler;

    }

    class SendWroker implements Runnable {
        private volatile boolean cancelled;

        private volatile Thread thread;

        @Override
        public void run() {
            thread = Thread.currentThread();
            int bit = 1500;
            int tmp = bit;

            while (!cancelled) {
                try {
                    // ȡ��һ������
                    Sms sms = mtQueue.take();
                    if (--tmp == 0) {
                        Tools.csleep(50);
                        tmp = bit;
                    }
                    SmsEncoder smsEncoder = new SmsEncoder(sms);
                    smsEncoder.encoder();
                    for (int i = 0; i < sms.getpMessages().size(); i++) {
                        PMessage pMessage = sms.getpMessages().get(i);
                        Sms smsNew = sms.clone();
                        smsNew.setSmsIndex(i + 1);
                        int ret = 0;
                        while ((ret = protocol.sendPMessage(pMessage)) < 0) {
                            if (EskLog.isDebugEnabled()) {
                                logger.debug(">>>>>>>>>��ʱ�����·�������:" + ret);
                            }
                            if (ret == SendResult.DISCONNECECT_ERROR.getValue() && isConnectTimeOut()) {
                                smsNew.setSendResult(SmsErrCode.CONNSTATUSIAGW_ERROR.getValue());
                                smsNew.setDesc("���������жϳ���2����");
                                smsHandler.notifyResp(smsNew);
                                Tools.csleep(1000);
                                break;
                            }
                            Tools.csleep(30);
                        }
                        if (ret == 0) {
                            isConnect.set(true);
                            smsNew.setSubmit2IsmgTime(System.currentTimeMillis());
                            sentSubmits.put(pMessage.getSeqId(), smsNew);
                            channelStatMgr.addSmsTotal(smsNew.getChannel().getChanneId());
                            logger.debug("�ύ������ ��channel:" + senderId + " mtId" + smsNew.getMtId() + " seqId:"
                                         + pMessage.getSeqId() + " mobile:" + smsNew.getDest() + " content:"
                                         + smsNew.getContents()[i] + " index:" + smsNew.getSmsIndex());
                        }

                    }
                } catch (InterruptedException ie) {
                    logger.info("�̱߳��ж�");
                    thread.interrupt();
                    break;
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
        }

        public void cancel() {
            cancelled = true;
            Thread thread = this.thread;
            if (thread != null) {
                thread.interrupt();
            }
        }

    }

    public boolean start() {
        if (protocol.start()) {
            ThreadFactory.newThread(senderId + "_Thread", sendWroker).start();
            return true;
        }
        return false;
    }

    public void stop() {
        if (this.sendWroker != null) {
            sendWroker.cancel();
        }
        if (this.protocol != null) {
            protocol.stop();
            logger.info("�ر�Э��" + protocol.getProtocolId());
        }
    }

    public boolean isConnectTimeOut() {
        if (isConnect.get()) {
            isConnect.set(false);
            disConnectTime = System.currentTimeMillis();
            return false;
        } else {
            if (disConnectTime != 0 && (System.currentTimeMillis() - disConnectTime > CONNECT_TIME)) {
                isConnect.set(true);
                return true;
            }
        }
        return false;
    }
}
