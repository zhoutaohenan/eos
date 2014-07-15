package com.surge.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.comm.StatSms;
import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.util.DBUtil;
import com.surge.engine.util.HibernateUtil;
import com.surge.engine.util.TimeUtil;

/**
 * 统计发送信息
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-3-1
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SendStatRunnable implements Runnable {

    private static Logger logger = Logger.getLogger(SendStatRunnable.class);

    private DbConfig dbconfig = DbConfig.instance;

    @Override
    public void run() {
        // 统计用户发送帐单
        int nostatcount;
        try {
            nostatcount = statUserDatil();
            // 统计机构（包括子机构）帐单
            statOrgDetail(nostatcount);

            // 返还费用
            returnFee(nostatcount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * <p>
     * 统计用户发送帐单
     * </p>
     * <p>
     * </p>
     * <p>
     * 
     * @author: ztao
     *          </p>
     *          <p>
     * @time: 2012上午11:08:04
     *        </p>
     */
    private int statUserDatil() throws Exception {
        int noStatTime = 0;
        // 最后一次统计时间
        String lastStatTime = null;
        long time = System.currentTimeMillis();
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        // 计算最后一次统计的时间
        Statement queryTimeStat = null;
        // 统计查询MT
        Statement queryMTStat = null;
        // 统计查询MO
        Statement queryMoStat = null;
        // 入库
        PreparedStatement insertPs = null;
        ResultSet rsTime = null;
        ResultSet rsMt = null;
        ResultSet rsMo = null;
        String caculateSql = null;
        if (dbconfig.isOracle_run()) {
            caculateSql = "select STAT_TIME from  SMS_STAT where rownum<=1 order by STAT_ID desc";
        } else if (dbconfig.isSqlserver_run()) {
            caculateSql = "select top 1 STAT_TIME from  SMS_STAT  order by STAT_ID desc";
        } else if (dbconfig.isMySql_run()) {
            caculateSql = "select STAT_TIME from  SMS_STAT  order by STAT_ID desc limit 1";
        } else {
            caculateSql = "select STAT_TIME from  SMS_STAT order by STAT_ID desc SET rowcount 1";
        }
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            queryTimeStat = conn.createStatement();
            rsTime = queryTimeStat.executeQuery(caculateSql);
            if (rsTime.next()) {
                lastStatTime = rsTime.getString(1);
            } else {
                lastStatTime = TimeUtil.getDateStr();
            }
            tranction.commit();
            noStatTime = TimeUtil.calcuateDays(lastStatTime);
            for (int i = 1; i < noStatTime; i++) {
                if (noStatTime - i < 3) {
                    break;
                }
                tranction = session.beginTransaction();
                String stattime = TimeUtil.getLastDay(noStatTime - i);
                logger.info("开始统计" + stattime + "的短信发送情况");
                String queryMoSql = null;
                String queryMTSql = null;
                String insertSql = null;
                if (dbconfig.isOracle_run()) {
                    queryMoSql = "select count(*) as n,USERID,CHANNEL,ORGID" + " from sms_mo "
                                 + "where RECEIVETIME between to_date('" + stattime
                                 + " 00:00:00 ','yyyy-mm-dd hh24:mi:ss')  and to_date('" + stattime
                                 + " 23:59:59','yyyy-mm-dd hh24:mi:ss') " + " group by (USERID,CHANNEL,ORGID)";
                    queryMTSql = "select  send_userid,orgid,channel,"
                                 + "sum(case when (1>0) then PK_NUMBER else 0 end) as mttotal ,"
                                 + "sum(case when(SEND_RET=1) then PK_NUMBER else 0 end) as mtfail ,"
                                 + "sum(case when(SEND_RET=1000016) then PK_NUMBER else 0 end) as mobilefail ,"
                                 + "sum(case when(SEND_RET=10000011 or SEND_RET=1000014 or  SEND_RET=1000015 or SEND_RET=1000012 or SEND_RET=1000013) then PK_NUMBER else 0 end) as otherfail,"
                                 + "sum(case when (ISNEEDREPORT=1 and SEND_RET = 0 AND STATUS IS NOT NULL) or (ISNEEDREPORT=0 and SEND_RET = 0) then PK_NUMBER else 0 end) as mtsucc, "
                                 + "sum(case when (ISNEEDREPORT=1 and SEND_RET = 0 AND STATUS IS NULL) or (SEND_RET=1000009) then PK_NUMBER else 0 end) as unknown from SMS_MT_MESSAGES_VIEW "
                                 + "where SEND_TIME between to_date('" + stattime
                                 + " 00:00:00 ','yyyy-mm-dd hh24:mi:ss')  and to_date('" + stattime
                                 + " 23:59:59','yyyy-mm-dd hh24:mi:ss') " + "group by(send_userid,orgid,channel)";

                    insertSql = "insert into SMS_STAT (STAT_ID,ORGID,USERID,CHANNEL_NAME,SUCCESS_NUM,FAIL_NUM,NO_REPORTNUM,STAT_TIME,MO_NUM,SMS_TOTAL,MOBILE_FAIL,OTHER_FAIL) values(SEQ_SMS_STAT.NextVal,?,?,?,?,?,?,?,?,?,?,?)";

                } else if (dbconfig.isSqlserver_run() || dbconfig.isSybase_run()) {
                    queryMoSql = "select count(*) as n,USERID,CHANNEL,ORGID" + " from SMS_MO "
                                 + "where RECEIVETIME between convert(datetime,'" + stattime
                                 + " 00:00:00 ')  and convert(datetime,'" + stattime + " 23:59:59') "
                                 + " group by USERID,CHANNEL,ORGID ";
                    queryMTSql = "select  SEND_USERID, ORGID, CHANNEL,"
                                 + "sum(case when (1>0) then PK_NUMBER else 0 end) as mttotal ,"
                                 + "sum(case when( SEND_RET=1) then PK_NUMBER else 0 end) as mtfail ,"
                                 + "sum(case when(SEND_RET=1000016) then PK_NUMBER else 0 end) as mobilefail ,"
                                 + "sum(case when(SEND_RET=10000011 or SEND_RET=1000014 or  SEND_RET=1000015 or SEND_RET=1000012 or SEND_RET=1000013) then PK_NUMBER else 0 end) as otherfail,"
                                 + "sum(case when (ISNEEDREPORT=1 and SEND_RET = 0 AND STATUS IS NOT NULL) or (ISNEEDREPORT=0 and SEND_RET = 0) then PK_NUMBER else 0 end) as mtsucc, "
                                 + "sum(case when (ISNEEDREPORT=1 and SEND_RET = 0 AND STATUS IS NULL) or (SEND_RET=1000009) then PK_NUMBER else 0 end) as unknown from SMS_MT_MESSAGES_VIEW "
                                 + "where SEND_TIME between convert(datetime,'" + stattime
                                 + " 00:00:00 ')  and convert(datetime,'" + stattime + " 23:59:59')"
                                 + "group by SEND_USERID, ORGID, CHANNEL";

                    insertSql = "insert into SMS_STAT (ORGID,USERID,CHANNEL_NAME,SUCCESS_NUM,FAIL_NUM,NO_REPORTNUM,STAT_TIME,MO_NUM,SMS_TOTAL,MOBILE_FAIL,OTHER_FAIL) values(?,?,?,?,?,?,?,?,?,?,?)";
                } else if (dbconfig.isMySql_run()) {
                    queryMoSql = "select count(*) as n,USERID,CHANNEL,ORGID" + " from sms_mo "
                                 + "where RECEIVETIME between '" + stattime + " 00:00:00 '  and '" + stattime
                                 + " 23:59:59' " + " group by USERID,CHANNEL,ORGID";
                    queryMTSql = "select send_userid,orgid,channel,"
                                 + "sum(case when (1>0 ) then PK_NUMBER else 0 end) as mttotal ,"
                                 + "sum(case when( SEND_RET=1) then PK_NUMBER else 0 end) as mtfail ,"
                                 + "sum(case when(SEND_RET=1000016) then PK_NUMBER else 0 end) as mobilefail ,"
                                 + "sum(case when(SEND_RET=10000011 or SEND_RET=1000014 or  SEND_RET=1000015 or SEND_RET=1000012 or SEND_RET=1000013) then PK_NUMBER else 0 end) as otherfail,"
                                 + "sum(case when (ISNEEDREPORT=1 and SEND_RET = 0 AND STATUS IS NOT NULL) or (ISNEEDREPORT=0 and SEND_RET = 0) then PK_NUMBER else 0 end) as mtsucc, "
                                 + "sum(case when (ISNEEDREPORT=1 and SEND_RET = 0 AND STATUS IS NULL) or (SEND_RET=1000009) then PK_NUMBER else 0 end) as unknown from SMS_MT_MESSAGES_VIEW "
                                 + "where SEND_TIME between '" + stattime + " 00:00:00 '  and '" + stattime
                                 + " 23:59:59'" + "group by send_userid,orgid,channel";

                    insertSql = "insert into SMS_STAT (ORGID,USERID,CHANNEL_NAME,SUCCESS_NUM,FAIL_NUM,NO_REPORTNUM,STAT_TIME,MO_NUM,SMS_TOTAL,CREATE_TIME,MOBILE_FAIL,OTHER_FAIL) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                }

                queryMoStat = conn.createStatement();
                rsMo = queryMoStat.executeQuery(queryMoSql);
                HashMap<String, StatData> dataMap = new HashMap<String, StatData>();
                List<StatData> statList = new ArrayList<StatData>();
                // 先查询MO信息
                while (rsMo.next()) {
                    StatData data = new StatData();
                    data.channel = rsMo.getString("CHANNEL");
                    data.mototal = rsMo.getInt("n");
                    data.orgid = rsMo.getString("ORGID");
                    data.userid = rsMo.getString("USERID");
                    data.stattime = stattime;
                    dataMap.put(data.userid + data.channel, data);
                    statList.add(data);
                }
                queryMTStat = conn.createStatement();
                rsMt = queryMTStat.executeQuery(queryMTSql);
                // 查询MT
                while (rsMt.next()) {
                    String userid = rsMt.getString("send_userid");
                    String channel = rsMt.getString("channel");
                    String key = userid + channel;
                    StatData data = dataMap.get(key);
                    // 若用户在该通道没有MO信息
                    if (data == null) {
                        data = new StatData();
                        data.channel = channel;
                        data.orgid = rsMt.getString("orgid");
                        data.userid = userid;
                        data.mtfail = rsMt.getInt("mtfail");
                        data.mttotal = rsMt.getInt("mttotal");
                        data.mtsucc = rsMt.getInt("mtsucc");
                        data.unknown = rsMt.getInt("unknown");
                        data.stattime = stattime;
                        data.mobileFail = rsMt.getInt("mobilefail");
                        data.otherFail = rsMt.getInt("otherfail");
                        statList.add(data);
                    } else
                    // 若用户在该通道有MO信息
                    {
                        data.mtfail = rsMt.getInt("mtfail");
                        data.mttotal = rsMt.getInt("mttotal");
                        data.mtsucc = rsMt.getInt("mtsucc");
                        data.unknown = rsMt.getInt("unknown");
                        data.mobileFail = rsMt.getInt("mobilefail");
                        data.otherFail = rsMt.getInt("otherfail");
                    }

                }
                // 若有统计数据，则持久化到数据库
                if (statList.size() > 0) {
                    insertPs = conn.prepareStatement(insertSql);
                    this.execute(insertPs, statList);
                }
                tranction.commit();
            }

        } catch (Exception e) {
            logger.error("", e);
            if (tranction != null) {
                tranction.rollback();
            }
            throw e;
        } finally {
            DBUtil.closeConn4(rsTime, queryTimeStat, null, null);
            // 关闭MO查询
            DBUtil.closeConn4(rsMo, queryMoStat, null, null);
            // 关闭MT查询
            DBUtil.closeConn4(rsMt, queryMTStat, null, null);
            // 关闭入库及连接
            DBUtil.closeConn3(insertPs, conn, session);

        }

        logger.info("统计用时：" + String.valueOf(System.currentTimeMillis() - time));
        return noStatTime;
    }

    /**
     * <p>
     * 统计机构帐单，包子机构
     * </p>
     * <p>
     * </p>
     * <p>
     * 
     * @author: Administrator
     *          </p>
     *          <p>
     * @time: 2012上午11:08:26
     *        </p>
     */
    private void statOrgDetail(int noStatTime) {
        HashMap<String, StatSms> statMap;
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        Statement queryOrg = null;// /查询所有机构Statment
        ResultSet rs = null;// 查询所有机构的ResultSet

        Statement queryOrgStat = null;// 查询某机构下的所有用户的帐单Statement;
        ResultSet queryOrgStatRs = null;// 查询某机构下的所有用户的帐单ResultSet;

        Statement sunOrgStat = null;// 查询子机构Statment
        ResultSet sunOrgRs = null;// 查询子机构ResultSet

        PreparedStatement saveOrgPreStatmet = null;// 保存统计出来的机构帐单
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            queryOrg = conn.createStatement();
            String sql = "select id from SMS_ORG order by id desc";
            String insertOrgStat = null;
            if (dbconfig.isOracle_run()) {
                // 保存纺以计好的机构帐单SQL
                insertOrgStat = "insert into SMS_ORG_STAT (id,ORGID,SUCCESS_NUM,FAIL_NUM,NO_REPORTNUM,MO_NUM,SMS_TOTAL,MOBILE_FAIL,OTHER_FAIL,STAT_TIME) values(SMS_ORG_STAT_SEQ.NextVal,?,?,?,?,?,?,?,?,?)";
            } else if (dbconfig.isMySql_run()) {
                // 保存纺以计好的机构帐单SQL
                insertOrgStat = "insert into SMS_ORG_STAT (ORGID,SUCCESS_NUM,FAIL_NUM,NO_REPORTNUM,MO_NUM,SMS_TOTAL,MOBILE_FAIL,OTHER_FAIL,STAT_TIME,CREATE_TIME) values(?,?,?,?,?,?,?,?,?,?)";
            } else {
                // 保存纺以计好的机构帐单SQL
                insertOrgStat = "insert into SMS_ORG_STAT (ORGID,SUCCESS_NUM,FAIL_NUM,NO_REPORTNUM,MO_NUM,SMS_TOTAL,MOBILE_FAIL,OTHER_FAIL,STAT_TIME) values(?,?,?,?,?,?,?,?,?)";
            }

            for (int i = 1; i < noStatTime; i++) {
                if (noStatTime - i < 3) {
                    break;
                }
                statMap = new HashMap<String, StatSms>();
                String stattime = TimeUtil.getLastDay(noStatTime - i);
                rs = queryOrg.executeQuery(sql);
                while (rs.next()) {
                    String orgid = rs.getString(1);
                    queryOrgStat = conn.createStatement();
                    // /查询该机构下已统计的帐单
                    queryOrgStatRs = queryOrgStat.executeQuery("select sum(MO_NUM),sum(SMS_TOTAL),sum(SUCCESS_NUM),sum(FAIL_NUM), sum(MOBILE_FAIL),sum(OTHER_FAIL),sum(NO_REPORTNUM) from SMS_STAT where  ORGID='"
                                                               + orgid + "' and  STAT_TIME='" + stattime + "'");
                    StatSms statSms = new StatSms();
                    while (queryOrgStatRs.next()) {
                        statSms.setMoTotal(queryOrgStatRs.getInt(1));// 取MO数
                        statSms.setMtTotal(queryOrgStatRs.getInt(2));// 取MT总数
                        statSms.setSucssessSms(queryOrgStatRs.getInt(3));// 取成功数
                        statSms.setFailSms(queryOrgStatRs.getInt(4));// 取失败短信数
                        statSms.setErrorMobleSms(queryOrgStatRs.getInt(5));// 号码异常数
                        statSms.setOtherfail(queryOrgStatRs.getInt(6));// 其它失败
                        statSms.setUnknown(queryOrgStatRs.getInt(7));// 未知
                    }
                    // 判断本机构是否有子机构
                    String querySunOrgSql = " select id from SMS_ORG where FATHER_ORGID= '" + orgid + "'";
                    sunOrgStat = conn.createStatement();
                    sunOrgRs = sunOrgStat.executeQuery(querySunOrgSql);
                    // 将子机构的数据量添加到本机里面
                    while (sunOrgRs.next()) {
                        String sunOrgid = sunOrgRs.getString(1);
                        StatSms sunStat = statMap.get(sunOrgid);
                        statSms.setErrorMobleSms(sunStat.getErrorMobleSms());
                        statSms.setFailSms(sunStat.getFailSms());
                        statSms.setMoTotal(sunStat.getMoTotal());
                        statSms.setMtTotal(sunStat.getMtTotal());
                        statSms.setOtherfail(sunStat.getOtherfail());
                        statSms.setSucssessSms(sunStat.getSucssessSms());
                        statSms.setUnknown(sunStat.getUnknown());
                    }
                    statMap.put(String.valueOf(orgid), statSms);
                }
                saveOrgPreStatmet = conn.prepareStatement(insertOrgStat);
                Iterator<String> iter = statMap.keySet().iterator();
                for (; iter.hasNext();) {
                    String orgid = iter.next();
                    StatSms statSms = statMap.get(orgid);
                    saveOrgPreStatmet.setString(1, orgid);
                    saveOrgPreStatmet.setInt(2, statSms.getSucssessSms());
                    saveOrgPreStatmet.setInt(3, statSms.getFailSms());
                    saveOrgPreStatmet.setInt(4, statSms.getUnknown());
                    saveOrgPreStatmet.setInt(5, statSms.getMoTotal());
                    saveOrgPreStatmet.setInt(6, statSms.getMtTotal());
                    saveOrgPreStatmet.setInt(7, statSms.getErrorMobleSms());
                    saveOrgPreStatmet.setInt(8, statSms.getOtherfail());
                    saveOrgPreStatmet.setString(9, stattime);
                    if (dbconfig.isMySql_run()) {
                        saveOrgPreStatmet.setString(10, TimeUtil.getDateTimeStr());
                    }
                    saveOrgPreStatmet.addBatch();
                }
                saveOrgPreStatmet.executeBatch();
            }
            tranction.commit();
            conn.commit();

        } catch (Exception e) {
            logger.error("统计机构帐单失败", e);
            tranction.rollback();

        } finally {
            try {

                if (saveOrgPreStatmet != null) {
                    saveOrgPreStatmet.close();
                }
                if (sunOrgRs != null) {
                    sunOrgRs.close();
                }
                if (sunOrgStat != null) {
                    sunOrgStat.close();
                }
                if (queryOrgStatRs != null) {
                    queryOrgStatRs.close();
                }
                if (queryOrgStat != null) {
                    queryOrgStat.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (queryOrg != null) {
                    queryOrg.close();
                }
                if (conn != null) {
                    conn.close();
                }

                if (session != null) {
                    session.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void returnFee(int noStatTime) {
        String updateSql = "update sms_user set balance = balance + ? where id= ?";
        String saveFeeSql = "insert into sms_feerecord(id,userid,fee,returnTime,operatorTime) values(?,?,?,?,?)";
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        Statement queryFee = null;// /查询返还费用
        ResultSet rsFee = null;
        PreparedStatement saveFee = null;
        PreparedStatement updateFee = null;
        List<FeeData> feeList = new ArrayList<FeeData>();
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            queryFee = conn.createStatement();
            for (int i = 1; i < noStatTime; i++) {
                if (noStatTime - i < 3) {
                    break;
                }
                String stattime = TimeUtil.getLastDay(noStatTime - i);
                String sql = "select sum(SMS_TOTAL- SUCCESS_NUM-NO_REPORTNUM) fee ,USERID from sms_stat st left join sms_user su  on  st.USERID=su.id where st.STAT_TIME='"
                             + stattime + " ' and su.feeType=1 GROUP BY st.USERID";
                rsFee = queryFee.executeQuery(sql);
                while (rsFee.next()) {
                    int fee = rsFee.getInt(1);
                    if (fee > 0) {
                        FeeData data = new FeeData();
                        data.fee = rsFee.getInt(1);
                        data.uid = rsFee.getString(2);
                        data.returnTime = stattime;
                        feeList.add(data);
                    }
                }

            }
            if (feeList.size() > 0) {
                saveFee = conn.prepareStatement(saveFeeSql);
                updateFee = conn.prepareStatement(updateSql);
                for (FeeData data : feeList) {
                    saveFee.setString(1, UUID.randomUUID().toString());
                    saveFee.setString(2, data.uid);
                    saveFee.setInt(3, data.fee);
                    saveFee.setString(4, data.returnTime);
                    saveFee.setTimestamp(5, new Timestamp(new Date().getTime()));
                    saveFee.addBatch();

                    // 更新费用
                    updateFee.setInt(1, data.fee);
                    updateFee.setString(2, data.uid);
                    logger.info("***计返还用户:" + data.uid + "," + data.fee + "条费用");
                    updateFee.addBatch();
                }

                saveFee.executeBatch();
                updateFee.executeBatch();
            }
            tranction.commit();
            conn.commit();
            logger.info("共计返还" + feeList.size() + "条费用记录");
        } catch (Exception e) {
            logger.error("返回费用失败" + e.getMessage(), e);
            tranction.rollback();
        } finally {
            try {
                if (updateFee != null) {
                    updateFee.close();
                }
                if (saveFee != null) {
                    saveFee.close();
                }
                if (rsFee != null) {
                    rsFee.close();
                }
                if (queryFee != null) {
                    queryFee.close();
                }
                if (conn != null) {
                    conn.close();
                }
                if (session != null) {
                    session.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 初始预处理值 TODO
     * 
     * @param ps
     * @param statList
     * @throws SQLException void
     * @throws
     */
    private void execute(PreparedStatement ps, List<StatData> statList) throws SQLException {
        for (StatData data : statList) {
            int j = 1;
            if (DbConfig.instance.isSybase_run()) {
                ps.setInt(j++, Integer.parseInt(data.orgid));
                ps.setInt(j++, Integer.parseInt(data.userid));
            } else {
                ps.setString(j++, data.orgid);
                ps.setString(j++, data.userid);
            }
            ps.setString(j++, data.channel);
            ps.setInt(j++, data.mtsucc);
            ps.setInt(j++, data.mtfail);
            ps.setInt(j++, data.unknown);
            ps.setString(j++, data.stattime);
            ps.setInt(j++, data.mototal);
            ps.setInt(j++, data.mttotal);
            if (dbconfig.isMySql_run()) {
                ps.setString(j++, TimeUtil.getDateTimeStr());
            }
            ps.setInt(j++, data.mobileFail);
            ps.setInt(j++, data.otherFail);
            ps.addBatch();
        }
        ps.executeBatch();
    }

    class FeeData {
        int fee;
        String uid;
        String returnTime;
    }

    class StatData {
        int mttotal = 0;

        int mtfail = 0;

        int mtsucc = 0;

        int unknown = 0;

        int mototal = 0;

        String userid;

        String orgid;

        String channel;

        String stattime;

        int mobileFail = 0;

        int otherFail = 0;

    }
}
