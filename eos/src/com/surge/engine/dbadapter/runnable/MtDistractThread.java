package com.surge.engine.dbadapter.runnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.BaseThread;
import com.surge.engine.comm.SysCommonQueue;
import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.pojo.SmsData;
import com.surge.engine.dbadapter.util.DBUtil;
import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.service.SmsAgentService;
import com.surge.engine.util.HibernateUtil;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.Tools;

/**
 * 将sms_mt表 转移至sms_mt_task,主要工作是根据号码路由出通道 若没有发送通道，则直接保存到sms_sent表
 * 
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class MtDistractThread extends BaseThread {
    private static final Logger logger = Logger.getLogger(MtDistractThread.class);

    private static String querySql = DbConfig.instance.querySql;

    private final SmsAgentService smsAgent;

    private long activeTime = System.currentTimeMillis();

    private SmsSendThread smsSendThread = null;

    private SysCommonQueue blackQueue = SysCommonQueue.getInstance();

    public MtDistractThread(SmsAgentService smsAgent) {
        this.smsAgent = smsAgent;
        smsSendThread = new SmsSendThread(smsAgent);
    }

    @Override
    public void run() {
        this.setName("MtDistractThread");
        logger.info("MtDistractThread线程启动成功");
        while (!this.isInterrupted()) {
            try {
                activeTime = System.currentTimeMillis();
                Tools.csleep(50);
                distract();
                Tools.csleep(50);
                smsSendThread.run();
                activeTime = System.currentTimeMillis();
            } catch (InterruptedException e) {
                logger.info("MtDistractThread线程中断");
                this.interrupt();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void distract() {
        long beginL = System.currentTimeMillis();
        String inSql = null;
        if (DbConfig.instance.isMySql_run()) {
            inSql = "insert into SMS_MT_TASK "
                    + "(MT_ID,SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,EXTCODE,PRESEND_TIME,VALID_TIME,CREATE_TIME,SMS_SIGN,ORGID,SEND_USERID) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        } else {
            inSql = "insert into SMS_MT_TASK "
                    + "(MT_ID,SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,EXTCODE,PRESEND_TIME,VALID_TIME,CREATE_TIME,SMS_SIGN,ORGID,SEND_USERID) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        }

        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        ResultSet rs = null;
        // 插入SMS_MT_TASK表批处理
        PreparedStatement pstmt = null;
        // 删除已从SMS_MT表中读出记录 预处理语句
        PreparedStatement deleteps = null;
        // 没有路由到通道或其它错误，直接保存到SMS_SENT表
        PreparedStatement errorPs = null;

        Statement statement = null;
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            rs = conn.createStatement().executeQuery(querySql);
            // 正常MT消息队列
            List<SmsData> list = new ArrayList<SmsData>();
            // 异常MT消息队列
            List<SmsData> errorList = new ArrayList<SmsData>();
            List<SmsData> blackList = new ArrayList<SmsData>();
            ArrayList<String> idsList = new ArrayList<String>();
            while (rs.next()) {
                SmsData smsData = new SmsData();
                smsData.id = rs.getString("MT_ID");
                smsData.smsid = rs.getString("SMSID");
                smsData.destaddr = rs.getString("DESTADDR");
                smsData.message = rs.getString("MESSAGE");
                smsData.isneedreport = rs.getInt("ISNEEDREPORT");
                smsData.channel = rs.getString("CHANNEL");
                if (smsData.channel == null || smsData.channel.trim().length() <= 0) {
                    StringBuffer sb = new StringBuffer();
                    // 号码路由
                    List<SmsChannel> channels = this.smsAgent.routing(smsData.destaddr);
                    for (SmsChannel smsChannel : channels) {
                        sb.append(smsChannel.getChanneId()).append(",");
                    }
                    smsData.channel = sb.toString();
                }
                smsData.priorty = rs.getInt("PRIORTY");
                smsData.excode = rs.getString("EXTCODE");
                smsData.presendtime = rs.getString("PRESEND_TIME");
                smsData.validTime = rs.getLong("VALID_TIME");
                smsData.createtime = rs.getString("CREATE_TIME");
                smsData.orgid = rs.getString("ORGID");
                smsData.userid = rs.getString("SEND_USERID");
                smsData.smssign = rs.getString("SMS_SIGN");
                if (smsData.channel == null || smsData.channel.length() <= 1) {
                    errorList.add(smsData);
                } else {
                    if (blackQueue.isBlackMobile(smsData.destaddr)) {
                        blackList.add(smsData);
                    } else {
                        list.add(smsData);
                    }

                }
                idsList.add(smsData.id);

            }
            // 从SMS_MT表中读到数据时,通过ID删除读出的记录
            if (idsList.size() > 0) {
                String delSql = "delete from SMS_MT where MT_ID=? ";
                deleteps = conn.prepareStatement(delSql);
                for (String id : idsList) {
                    deleteps.setString(1, id);
                    deleteps.addBatch();
                }
                deleteps.executeBatch();
            }
            // 从SMS_MT表中读到数据时,向SMS_MT_TASK表批量插入从SMS_MT表读到的数据
            if (list.size() > 0) {
                pstmt = conn.prepareStatement(inSql);
                for (int i = 0; i < list.size(); i++) {
                    pstmt = setStatement(pstmt, list.get(i));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                logger.debug("成功转移正常 mt:" + list.size() + " time=" + (System.currentTimeMillis() - beginL));
            }

            // 从SMS_MT表中读到数据时,向sms_sent表批量插入从SMS_MT表读到的数据
            if (errorList.size() > 0) {
                String errorInsertSql = DbConfig.instance.errInsertSql;
                errorPs = conn.prepareStatement(errorInsertSql);
                String description = SmsErrCode.NO_SEND_CHANNEL.getValue() + "*" + "无发送通道";
                DBUtil.executePreState(errorList, errorPs, SmsErrCode.NO_SEND_CHANNEL.getValue(), description);
                logger.debug("成功转移无发送通道 mt:" + errorList.size());
            }

            if (blackList.size() > 0) {
                String errorInsertSql = DbConfig.instance.errInsertSql;
                errorPs = conn.prepareStatement(errorInsertSql);
                String description = SmsErrCode.USER_MOBILE_ERROR.getValue() + "*" + "黑名单";
                DBUtil.executePreState(blackList, errorPs, SmsErrCode.USER_MOBILE_ERROR.getValue(), description);
                logger.debug("成功转移黑名单短信 mt:" + blackList.size());
            }
            if (errorList.size() > 0 || list.size() > 0 || blackList.size() > 0) {
                // 线程中断，事务回滚
                if (this.isInterrupted()) {
                    logger.debug("－－－－>因线程中断，进行事务回滚....");
                    tranction.rollback();
                } else
                // 提交事务
                {
                    tranction.commit();
                }

            }
        } catch (Exception e) {
            logger.error("转移MT失败", e);
            if (tranction != null) {
                tranction.rollback();
            }

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DBUtil.closeStat(rs, deleteps, pstmt, errorPs);
            DBUtil.closeConn(conn, session);
        }
    }

    /**
     * 设置statement参数 PreparedStatement
     * 
     * @param pstmt
     * @param smsBill
     * @return
     * @throws SQLException
     */
    private PreparedStatement setStatement(PreparedStatement pstmt, SmsData smsData) throws SQLException {
        int j = 1;
        /* if (!DbConfig.instance.isMySql_run()) {
             pstmt.setString(j++, smsData.id);
         }*/
        pstmt.setString(j++, smsData.id);
        pstmt.setString(j++, smsData.smsid);
        pstmt.setString(j++, smsData.destaddr);
        pstmt.setString(j++, smsData.message);
        pstmt.setInt(j++, smsData.isneedreport);
        pstmt.setString(j++, smsData.channel);
        pstmt.setInt(j++, smsData.priorty);
        pstmt.setString(j++, smsData.excode);
        pstmt.setTimestamp(j++, TimeUtil.getTimestamp(smsData.presendtime));
        pstmt.setLong(j++, smsData.validTime);
        pstmt.setTimestamp(j++, TimeUtil.getTimestamp(smsData.createtime));
        pstmt.setString(j++, smsData.smssign);
        pstmt.setString(j++, smsData.orgid);
        pstmt.setString(j++, smsData.userid);
        return pstmt;

    }

    @Override
    public long getLastActiveTime() {
        return this.activeTime;
    }

    public SmsAgentService getSmsAgent() {
        return smsAgent;
    }

}
