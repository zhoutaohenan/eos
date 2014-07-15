package com.surge.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.pojo.SmsData;
import com.surge.engine.dbadapter.util.DBUtil;
import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.util.HibernateUtil;
import com.surge.engine.util.TimeUtil;

/**
 * ��ʱ������ʷ���� ������ɾ����ʱ����ʷ���ݣ������ڵĶ���ת�Ƶ�SMS_SENT ��
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-22
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class BackRecordsRunnable implements Runnable {

    private static final Logger logger = Logger.getLogger(BackRecordsRunnable.class);

    private DbConfig dbConfig = DbConfig.instance;

    @Override
    public void run() {
        // ɾ����ʱ����ʷ����
        deleteExceedRecords();
        // ת�ƹ��ڶ���
        distractValidSms();
    }

    /**
     * ɾ����ʱ����ʷ���� TODO void
     * 
     * @throws
     */
    @SuppressWarnings("deprecation")
    private void deleteExceedRecords() {
        String deleteSQL = null;
        if (dbConfig.isOracle_run()) {
            deleteSQL = "delete from SMS_SENT where (sysdate-SEND_TIME) > " + SysConst.getBackTime();
        } else if (dbConfig.isSqlserver_run() || dbConfig.isSybase_run()) {
            // deleteSQL = "delete from SMS_SENT where (convert(datetime,'"
            // + TimeUtil.getDateTimeStr() + "') -PRESEND_TIME ) > " +
            // SysConst.getBackTime();
            deleteSQL = "delete from SMS_SENT where dateDiff(dd,SEND_TIME,getdate()) > " + SysConst.getBackTime();
        } else {
            deleteSQL = "delete from SMS_SENT where (TO_DAYS('" + TimeUtil.getDateTimeStr()
                        + "')-TO_DAYS(SEND_TIME)) > " + SysConst.getBackTime();
        }
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        Statement deleteState = null;
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            deleteState = conn.createStatement();
            deleteState.execute(deleteSQL);
            tranction.commit();
            conn.commit();
            logger.info("ɾ��һ����ʷ����");

        } catch (Exception e) {
            if (tranction != null) {
                tranction.rollback();
            }
            logger.error("", e);
        } finally {
            DBUtil.closeConn1(deleteState, conn, session);
        }
    }

    /**
     * ת�ƹ���Ч�ڵĶ��� TODO void
     * 
     * @throws
     */
    private void distractValidSms() {
        // ��ѯ���ڶ���SQL
        String querySql = null;
        if (dbConfig.isOracle_run()) {
            querySql = "select * from SMS_MT_TASK where round((sysdate-PRESEND_TIME) * 24 * 60)" + " > "
                       + SysConst.getValidTime() + " and rownum <= 1000";

        } else if (dbConfig.isSqlserver_run()) {
            // querySql =
            // "select  top 500 * from SMS_MT_TASK where convert(datetime,'"
            // + TimeUtil.getFixTime(SysConst.getValidTime()) +
            // "') >PRESEND_TIME";
            querySql = "select  top 1000 * from SMS_MT_TASK  where dateDiff(n,PRESEND_TIME,getdate()) > "
                       + SysConst.getValidTime();
        } else if (dbConfig.isMySql_run()) {
            querySql = "select  *  from SMS_MT_TASK where unix_timestamp('"
                       + TimeUtil.getFixTime(SysConst.getValidTime()) + "') > unix_timestamp(PRESEND_TIME) limit 1000";
        } else if (dbConfig.isSybase_run()) {
            // querySql =
            // "SET ROWCOUNT select * from SMS_MT_TASK where convert(datetime,'"
            // + TimeUtil.getFixTime(SysConst.getValidTime()) +
            // "') >PRESEND_TIME";
            querySql = "SET ROWCOUNT 1000 select * from SMS_MT_TASK where dateDiff(mi,PRESEND_TIME,getdate()) > "
                       + SysConst.getValidTime();
        }
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        // ���浽SMS_SENT��������
        PreparedStatement savePs = null;
        // ɾ��SMS_MT_TASK����ת�ƶ���������
        PreparedStatement deleteps = null;
        ResultSet rs = null;
        // ����MT_ID����
        ArrayList<String> idsList = new ArrayList<String>();
        // ������ʧЧ����
        List<SmsData> errorList = new ArrayList<SmsData>();
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            rs = conn.createStatement().executeQuery(querySql);
            while (rs.next()) {
                SmsData smsData = new SmsData();
                smsData.id = rs.getString("MT_ID");
                smsData.smsid = rs.getString("SMSID");
                smsData.destaddr = rs.getString("DESTADDR");
                smsData.message = rs.getString("MESSAGE");
                smsData.isneedreport = rs.getInt("ISNEEDREPORT");
                smsData.channel = rs.getString("CHANNEL");
                smsData.priorty = rs.getInt("PRIORTY");
                smsData.excode = rs.getString("EXTCODE");
                smsData.presendtime = rs.getString("PRESEND_TIME");
                smsData.validTime = rs.getLong("VALID_TIME");
                smsData.createtime = rs.getString("CREATE_TIME");
                smsData.orgid = rs.getString("ORGID");
                smsData.userid = rs.getString("SEND_USERID");
                smsData.smssign = rs.getString("SMS_SIGN");
                errorList.add(smsData);
                idsList.add(smsData.id);
            }
            if (errorList.size() > 0) {
                String errorInsertSql = DbConfig.instance.errInsertSql;
                savePs = conn.prepareStatement(errorInsertSql);
                String description = SmsErrCode.SMS_INVALIDATION_ERROR.getValue() + "*" + "�����ѹ���Ч��!";
                DBUtil.executePreState(errorList, savePs, SmsErrCode.SMS_INVALIDATION_ERROR.getValue(), description);

            }
            // ��SMS_MT_TASK���ж�������ʱ,ͨ��IDɾ�������ļ�¼
            if (idsList.size() > 0) {
                String delSql = "delete from SMS_MT_TASK where MT_ID=? ";
                deleteps = conn.prepareStatement(delSql);
                for (String id : idsList) {
                    deleteps.setString(1, id);
                    deleteps.addBatch();
                }
                deleteps.executeBatch();
            }
            tranction.commit();
            logger.info("�ɹ�ת��һ��ʧЧ���� size=" + idsList.size());
            if (idsList.size() == 1000) {
                distractValidSms();
            } else {
                logger.info("************��ת����ʧЧ����**********");
            }
            errorList = null;
            idsList = null;
        } catch (Exception e) {
            logger.error("", e);
            if (tranction != null) {
                tranction.rollback();
            }
        } finally {
            DBUtil.closeStat(rs, savePs, deleteps, null);
            DBUtil.closeConn(conn, session);
        }
    }

}
