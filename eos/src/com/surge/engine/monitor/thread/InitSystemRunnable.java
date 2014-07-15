package com.surge.engine.monitor.thread;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.SysConst;
import com.surge.engine.comm.SysCommonQueue;
import com.surge.engine.dbadapter.util.DBUtil;
import com.surge.engine.util.HibernateUtil;

public class InitSystemRunnable implements Runnable {
    private static final Logger logger = Logger.getLogger(InitSystemRunnable.class);
    private SysCommonQueue blackQueue = SysCommonQueue.getInstance();

    public InitSystemRunnable() {
        logger.info("系统初始化定时器启动成功");
    }

    @Override
    public void run() {
        init();
        initBlackMobile();
    }

    public void initBlackMobile() {
        String blacksql = "select mobile from sms_blackmobile";
        Session session = null;
        Connection conn = null;
        Statement state = null;
        Transaction transaction = null;
        ResultSet rs = null;
        try {
            session = HibernateUtil.getNewSession();
            transaction = session.beginTransaction();
            conn = session.connection();
            state = conn.createStatement();
            rs = state.executeQuery(blacksql);
            while (rs.next()) {
                blackQueue.add(rs.getString(1));
            }
            transaction.commit();
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            DBUtil.closeConn4(rs, state, conn, session);
        }
    }

    public void init() {
        String querySql = "select PARAM_NAME,PARAM_VALUE from SMS_SYSPARAM";
        String updateSql = " update SMS_SYSPARAM set PARAM_STATE=0";

        Session session = null;
        Connection conn = null;
        Statement state = null;
        Statement updateState = null;
        Transaction transaction = null;
        ResultSet rs = null;
        try {
            session = HibernateUtil.getNewSession();
            transaction = session.beginTransaction();
            conn = session.connection();
            state = conn.createStatement();
            rs = state.executeQuery(querySql);
            while (rs.next()) {
                String temp = rs.getString(1);
                if (temp.equalsIgnoreCase("BACK_TIME")) {
                    SysConst.setBackTime(rs.getInt(2));
                } else if (temp.equalsIgnoreCase("SMS_VALID")) {
                    SysConst.setValidTime(rs.getInt(2));
                }
            }
            updateState = conn.createStatement();
            updateState.executeUpdate(updateSql);
            transaction.commit();

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            DBUtil.closeConn1(updateState, null, null);
            DBUtil.closeConn4(rs, state, conn, session);
        }
    }
}
