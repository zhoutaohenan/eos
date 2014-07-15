package com.surge.engine.monitor.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.sms.pojo.Sms;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.service.SmsChannelMgr;
import com.surge.engine.sms.service.SmsQueueMgr;
import com.surge.engine.util.HibernateUtil;
import com.surge.engine.util.TimeUtil;

/**
 * 持久化已关闭通道队列中的数据到数据库　
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-22
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class FixScanQueueRunnable implements Runnable {

    private static final Logger logger = Logger.getLogger(FixScanQueueRunnable.class);

    @Override
    public void run() {
        SmsQueueMgr queueMgrtemp = SmsQueueMgr.getInstance();
        SmsChannelMgr channelMgrTemp = SmsChannelMgr.getInstance();
        Map<String, LinkedBlockingQueue<Sms>> mtQeueMapTmp = queueMgrtemp.getMtQeueMapTmp();
        Iterator<String> ite = mtQeueMapTmp.keySet().iterator();
        while (ite.hasNext()) {
            String temp = ite.next();
            SmsChannel channel = channelMgrTemp.getChannel(temp);
            if (channel != null && !channel.isRun()) {
                logger.info("***************清理 通道：" + temp + " 发送队列中有数据");
                LinkedBlockingQueue<Sms> blockingQueue = mtQeueMapTmp.get(temp);
                try {
                    execute(blockingQueue);
                } catch (Exception e) {
                    logger.error("", e);
                }
                mtQeueMapTmp.remove(temp);
                logger.info("***************清理 通道：" + temp + " 发送队列中有数据成功");

            }
        }

    }

    /**
     * 执行数据库操作 TODO
     * 
     * @param blockingQueue 存放的待持久化的短信
     * @throws Exception void
     * @throws
     */
    private void execute(LinkedBlockingQueue<Sms> blockingQueue) throws Exception {
        String errorInsertSql = DbConfig.instance.errorQueueInsert;
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        // 已关闭通道的数据,直接保存到SMS_SENT表
        PreparedStatement errorPs = null;
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            errorPs = conn.prepareStatement(errorInsertSql);
            for (Sms sms : blockingQueue) {
                // MT_ID, SMSID, DESTADDR, ISNEEDREPORT, SEND_FLAG,MESSAGE,
                // PK_TOTAL, PK_NUMBER, ISNEED_RESEND ,RESEND_STATUS,SEND_RET,
                // REPORT_TIME,STATUS,DESCRIPTION,EXTCODE,ORGID,SEND_USERID
                int j = 1;
                errorPs.setString(j++, sms.getMtId());
                errorPs.setString(j++, sms.getSmsid());
                errorPs.setString(j++, sms.getDest());
                // 设置是否需要状态报告:0:不需要1:需要
                errorPs.setInt(j++, 0);
                errorPs.setInt(j++, 1);
                errorPs.setString(j++, sms.getContent());
                errorPs.setInt(j++, 1);
                errorPs.setInt(j++, 1);
                errorPs.setInt(j++, 0);
                errorPs.setInt(j++, 5);
                errorPs.setInt(j++, SmsErrCode.CHANNEL_STATUS_ERROR.getValue());
                if (DbConfig.instance.isMySql_run()) {
                    errorPs.setString(j++, TimeUtil.getDateTimeStr());
                    errorPs.setString(j++, sms.getContent());
                }
                errorPs.setString(j++, sms.getExtcode());
                errorPs.setString(j++, sms.getOrgid());
                errorPs.setString(j++, sms.getUserid());
                errorPs.addBatch();
            }
            errorPs.executeBatch();
            tranction.commit();
        } catch (Exception e) {
            if (tranction != null) {
                tranction.rollback();
            }
            throw e;
        } finally {
            try {
                if (errorPs != null) {
                    errorPs.close();
                    errorPs = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
                if (session != null) {
                    session.close();
                    session = null;
                }
            } catch (Exception e) {
            }
        }
    }
}
