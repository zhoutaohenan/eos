package com.surge.engine.dbadapter.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.pojo.SmsData;
import com.surge.engine.util.HibernateUtil;
import com.surge.engine.util.TimeUtil;

public class DistractUtil {
    private Logger logger = Logger.getLogger(DistractUtil.class);

    private static DbConfig instance = DbConfig.instance;

    public static DistractUtil distractUtil = new DistractUtil();

    private DistractUtil() {

    }

    /**
     * 将发送记录从SMS_SENT_TEMP 转移到SMS_SENT表中 TODO
     * 
     * @param querySql 查询符合条件的SQL语句
     * @throws
     */
    public synchronized void distract(String querySql) {
        long beginL = System.currentTimeMillis();
        String insertSql = instance.insertSentSql;
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        ResultSet rs = null;
        // 插入SMS_MT_TASK表批处理
        PreparedStatement pstmt = null;
        // 删除已从SMS_MT表中读出记录 预处理语句
        PreparedStatement deleteps = null;
        // 正常MT消息队列
        List<SmsData> list = new ArrayList<SmsData>();
        ArrayList<Long> idsList = new ArrayList<Long>();
        try {
            session = HibernateUtil.getNewSession();
            tranction = session.beginTransaction();
            conn = session.connection();
            rs = conn.createStatement().executeQuery(querySql);
            while (rs.next()) {
                SmsData smsData = new SmsData();
                smsData.id = rs.getString("MT_ID");
                smsData.smsid = rs.getString("SMSID");
                smsData.destaddr = rs.getString("DESTADDR");
                smsData.createtime = rs.getString("CREATE_TIME");
                smsData.isneedreport = rs.getInt("ISNEEDREPORT");
                smsData.send_flag = rs.getInt("SEND_FLAG");
                smsData.sendtime = rs.getString("SEND_TIME");
                smsData.channel = rs.getString("CHANNEL");
                smsData.resend_channel = rs.getString("RESEND_CHANNEL");
                smsData.message = rs.getString("MESSAGE");
                smsData.split_message = rs.getString("SPLIT_MESSAGE");
                smsData.total = rs.getInt("PK_TOTAL");
                smsData.pknumber = rs.getInt("PK_NUMBER");
                smsData.isneedresend = rs.getInt("ISNEED_RESEND");
                smsData.sendstatus = rs.getInt("RESEND_STATUS");
                smsData.messageid = rs.getString("MESSAGE_ID");
                smsData.send_ret = rs.getInt("SEND_RET");
                smsData.status = rs.getString("STATUS");
                smsData.dec = rs.getString("DESCRIPTION");
                smsData.reporttime = rs.getString("REPORT_TIME");
                smsData.excode = rs.getString("EXTCODE");
                smsData.orgid = rs.getString("ORGID");
                smsData.userid = rs.getString("SEND_USERID");
                list.add(smsData);
                idsList.add(rs.getLong("SENT_TEMP_ID"));
            }
            if (list.size() > 0) {
                pstmt = conn.prepareStatement(insertSql);
                for (int i = 0; i < list.size(); i++) {
                    pstmt = setStatement(pstmt, list.get(i));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            if (idsList.size() > 0) {
                String delSql = "delete from SMS_SENT_TEMP where SENT_TEMP_ID=? ";
                deleteps = conn.prepareStatement(delSql);
                for (long id : idsList) {
                    deleteps.setLong(1, id);
                    deleteps.addBatch();
                }
                deleteps.executeBatch();

                logger.info("转移到SMS_SENT表" + idsList.size() + "条记录,用时:" + (System.currentTimeMillis() - beginL));
                tranction.commit();
            }
            if (idsList.size() == 1000) {
                distract(querySql);
            } else {
                logger.info("SMS_SENT_TEMP表中超时未返回状态报告的短信已全部转移到SMS_SENT表");
            }
            list = null;
            idsList = null;
        } catch (Exception e) {
            logger.error("转移SMS_SENT失败", e);
            if (tranction != null) {
                tranction.rollback();
            }

        } finally {
            tranction = null;
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (deleteps != null) {
                    deleteps.close();
                    deleteps = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
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

    /**
     * 将发送记录从SMS_SENT_TEMP 转移到SMS_SENT表中 TODO
     * 
     * @param querySql 查询符合条件的SQL语句
     * @throws
     */
    public synchronized void distractSQLServer(String querySql) {
        long beginL = System.currentTimeMillis();
        String insertSql = instance.insertSentSql;
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        ResultSet rs = null;
        // 插入SMS_MT_TASK表批处理
        PreparedStatement pstmt = null;
        // 删除已从SMS_MT表中读出记录 预处理语句
        PreparedStatement deleteps = null;
        // 正常MT消息队列
        List<SmsData> list = new ArrayList<SmsData>();
        ArrayList<Long> idsList = new ArrayList<Long>();
        try {
            session = HibernateUtil.getNewSession();
            tranction = session.beginTransaction();
            conn = session.connection();
            rs = conn.createStatement().executeQuery(querySql);
            while (rs.next()) {
                SmsData smsData = new SmsData();
                smsData.id = rs.getString("MT_ID");
                smsData.smsid = rs.getString("SMSID");
                smsData.destaddr = rs.getString("DESTADDR");
                smsData.createtime = rs.getString("CREATE_TIME");
                smsData.isneedreport = rs.getInt("ISNEEDREPORT");
                smsData.send_flag = rs.getInt("SEND_FLAG");
                smsData.sendtime = rs.getString("SEND_TIME");
                smsData.channel = rs.getString("CHANNEL");
                smsData.resend_channel = rs.getString("RESEND_CHANNEL");
                smsData.message = rs.getString("MESSAGE");
                smsData.split_message = rs.getString("SPLIT_MESSAGE");
                smsData.total = rs.getInt("PK_TOTAL");
                smsData.pknumber = rs.getInt("PK_NUMBER");
                smsData.isneedresend = rs.getInt("ISNEED_RESEND");
                smsData.sendstatus = rs.getInt("RESEND_STATUS");
                smsData.messageid = rs.getString("MESSAGE_ID");
                smsData.send_ret = rs.getInt("SEND_RET");
                smsData.status = rs.getString("STATUS");
                smsData.dec = rs.getString("DESCRIPTION");
                smsData.reporttime = rs.getString("REPORT_TIME");
                smsData.excode = rs.getString("EXTCODE");
                smsData.orgid = rs.getString("ORGID");
                smsData.userid = rs.getString("SEND_USERID");
                list.add(smsData);
                idsList.add(rs.getLong("SENT_TEMP_ID"));
            }
            if (list.size() > 0) {
                pstmt = conn.prepareStatement(insertSql);
                for (int i = 0; i < list.size(); i++) {
                    pstmt = setStatement(pstmt, list.get(i));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            if (idsList.size() > 0) {
                String delSql = "delete from SMS_SENT_TEMP where SENT_TEMP_ID=? ";
                deleteps = conn.prepareStatement(delSql);
                for (long id : idsList) {
                    deleteps.setLong(1, id);
                    deleteps.addBatch();
                }
                deleteps.executeBatch();

                logger.debug("转移到SMS_SENT表" + idsList.size() + "条记录,用时:" + (System.currentTimeMillis() - beginL));
                tranction.commit();
            }
            list = null;
            idsList = null;
        } catch (Exception e) {
            logger.error("转移SMS_SENT失败", e);
            if (tranction != null) {
                tranction.rollback();
            }

        } finally {
            tranction = null;
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (deleteps != null) {
                    deleteps.close();
                    deleteps = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
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

    private PreparedStatement setStatement(PreparedStatement pstmt, SmsData smsData) {

        try {
            int j = 1;
            pstmt.setString(j++, smsData.id);
            pstmt.setString(j++, smsData.smsid);
            pstmt.setString(j++, smsData.destaddr);
            pstmt.setTimestamp(j++, TimeUtil.getTimestamp(smsData.createtime));
            pstmt.setInt(j++, smsData.isneedreport);
            pstmt.setInt(j++, smsData.send_flag);
            pstmt.setTimestamp(j++, TimeUtil.getTimestamp(smsData.sendtime));
            pstmt.setString(j++, smsData.channel);
            pstmt.setString(j++, smsData.resend_channel);
            pstmt.setString(j++, smsData.message);
            pstmt.setString(j++, smsData.split_message);
            pstmt.setInt(j++, smsData.total);
            pstmt.setInt(j++, smsData.pknumber);
            pstmt.setInt(j++, smsData.isneedresend);
            pstmt.setInt(j++, smsData.sendstatus);
            pstmt.setString(j++, smsData.messageid);
            pstmt.setInt(j++, smsData.send_ret);
            pstmt.setString(j++, smsData.status);
            pstmt.setString(j++, smsData.dec);
            if (smsData.reporttime == null || smsData.reporttime.length() <= 0) {
                pstmt.setString(j++, null);
            } else {
                pstmt.setTimestamp(j++, TimeUtil.getTimestamp(smsData.reporttime));
            }
            pstmt.setString(j++, smsData.excode);
            pstmt.setString(j++, smsData.orgid);
            pstmt.setString(j++, smsData.userid);
        } catch (Exception e) {

        }
        return pstmt;
    }
}
