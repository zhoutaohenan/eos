package com.surge.engine.dbadapter.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;

import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.pojo.SmsData;
import com.surge.engine.util.TimeUtil;

public class DBUtil {

    /**
     * 关闭预处理 TODO
     * 
     * @param rs
     * @param ps1
     * @param ps2
     * @param ps3 void
     * @throws
     */
    public static void closeStat(ResultSet rs, PreparedStatement ps1, PreparedStatement ps2, PreparedStatement ps3) {

        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (ps1 != null) {
                ps1.close();
                ps1 = null;
            }
            if (ps2 != null) {
                ps2.close();
                ps2 = null;
            }
            if (ps3 != null) {
                ps3.close();
                ps3 = null;
            }

        } catch (Exception e) {
        }
    }

    /**
     * 关闭数据库连接 TODO
     * 
     * @param conn
     * @param session void
     * @throws
     */
    public static void closeConn(Connection conn, Session session) {
        try {
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

    /**
     * 关闭连接，方法重载 TODO
     * 
     * @param stae
     * @param conn
     * @param session void
     * @throws
     */
    public static void closeConn1(Statement state, Connection conn, Session session) {
        try {
            if (state != null) {
                state.close();
                state = null;
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

    /**
     * 关闭连接，方法重载 TODO
     * 
     * @param stae
     * @param conn
     * @param session void
     * @throws
     */
    public static void closeConn2(Statement state, PreparedStatement ps, Connection conn, Session session) {
        try {
            if (state != null) {
                state.close();
                state = null;
            }
            if (ps != null) {
                ps.close();
                ps = null;
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

    /**
     * 关闭连接，方法重载 TODO
     * 
     * @param stae
     * @param conn
     * @param session void
     * @throws
     */
    public static void closeConn3(PreparedStatement state, Connection conn, Session session) {
        try {
            if (state != null) {
                state.close();
                state = null;
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

    /**
     * 
     * TODO
     * 
     * @param rs ResultSet
     * @param state Statement
     * @param conn Connection
     * @param session Session void
     * @throws
     */
    public static void closeConn4(ResultSet rs, Statement state, Connection conn, Session session) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (state != null) {
                state.close();
                state = null;
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

    /**
     * 失败短信构造假状态报告，预处理 TODO
     * 
     * @param errorList
     * @param ps
     * @param errorType
     * @param description
     * @throws Exception void
     * @throws
     */
    public static void executePreState(List<SmsData> errorList, PreparedStatement ps, int errorType, String description)
            throws Exception {
        PreparedStatement errorPs = ps;
        for (SmsData data : errorList) {
            int j = 1;
            errorPs.setString(j++, data.id);
            errorPs.setString(j++, data.smsid);
            errorPs.setString(j++, data.destaddr);
            // 设置是否需要状态报告:0:不需要1:需要
            errorPs.setInt(j++, 0);
            errorPs.setInt(j++, 1);
            errorPs.setString(j++, data.message);
            errorPs.setInt(j++, 1);
            errorPs.setInt(j++, 1);
            errorPs.setInt(j++, 0);
            errorPs.setInt(j++, 5);
            errorPs.setInt(j++, errorType);
            if (DbConfig.instance.isMySql_run()) {
                errorPs.setString(j++, TimeUtil.getDateTimeStr());
                errorPs.setString(j++, TimeUtil.getDateTimeStr());
                // errorPs.setString(j++, data.message);
                errorPs.setString(j++, TimeUtil.getDateTimeStr());
                errorPs.setString(j++, "UNDELIVED");
                errorPs.setString(j++, description);
            } else {
                errorPs.setTimestamp(j++, new Timestamp(System.currentTimeMillis()));
                errorPs.setString(j++, "UNDELIVED");
                errorPs.setString(j++, description);
            }
            errorPs.setString(j++, data.excode);
            errorPs.setString(j++, data.orgid);
            errorPs.setString(j++, data.userid);
            errorPs.setString(j++, data.message);
            errorPs.addBatch();
        }
        errorPs.executeBatch();
    }
}
