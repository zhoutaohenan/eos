package com.surge.engine.dbadapter.runnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.SysConst;
import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.mgr.ReportQueueMgr;
import com.surge.engine.dbadapter.pojo.SmsData;
import com.surge.engine.dbadapter.util.DBUtil;
import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsRequest;
import com.surge.engine.sms.service.SmsAgentService;
import com.surge.engine.sms.service.SmsChannelMgr;
import com.surge.engine.util.EskLog;
import com.surge.engine.util.HibernateUtil;
import com.surge.engine.util.StringUtils;
import com.surge.engine.util.TimeUtil;

/**
 * 短信发送线程
 * 
 * @description
 * @project: eskprj
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsSendThread // extends BaseThread
{
    private static final Logger logger = Logger.getLogger(SmsSendThread.class);

    private final SmsAgentService smsAgent;

    private DbConfig dbConfig = DbConfig.instance;

    private AtomicBoolean isQuery = new AtomicBoolean(false);// 是否清空bufferSet

    private Set<String> bufferSet = new HashSet<String>();// 暂不取的通道ID

    private final float A_DAY_MINUTES = 60 * 24;

    public SmsSendThread(SmsAgentService smsAgent) {
        this.smsAgent = smsAgent;
    }

    public void run() throws InterruptedException {
        try {
            int respsMaxSize = SmsConfig.getInstance().getRespsSize();
            if (ReportQueueMgr.instance.getRespsQueue().size() < respsMaxSize) {
                sendSms();
            } else {
                logger.debug("<------>响应队列满,暂停发送,队列大小为：" + ReportQueueMgr.instance.getRespsQueue().size() + ",配置为："
                             + respsMaxSize);
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            logger.debug("", e);
            // throw e;
        }
    }

    public void sendSms() throws Exception {
        // Map<String, SmsChannel> allChannel = smsAgent.getChannels();
        // for (Map.Entry<String, SmsChannel> entry : allChannel.entrySet()) {
        // time = System.currentTimeMillis();
        // String channelId = entry.getKey();
        // SmsChannel channel = entry.getValue();
        // // 通道最大条数
        // int allowSendCount = smsAgent.getAllowSendCount(channelId);
        // if (smsAgent.isAllowSend(channelId) && (allowSendCount > 0)) {
        // if (smsAgent.getSentSubmitMapSize(channelId) > SmsConfig.getInstance().getSentSize()) {
        // logger.debug("<------>通道:" + channelId + "已发列队列满,暂停发送,队列大小为："
        // + String.valueOf(smsAgent.getSentSubmitMapSize(channelId)) + ",配置大小为："
        // + SmsConfig.getInstance().getSentSize() + "<------>");
        // continue;
        // }
        // List<SmsRequest> retList = this.getRequests(channel, allowSendCount);
        //
        // for (SmsRequest smsRequest : retList) {
        // this.smsAgent.sendSms(smsRequest);
        // if (channel.getProtocolType().equals(SmsProtocolType.gw)) {
        //
        // int smsLength = smsRequest.getMessage().length();
        // int smsStandCnLength = 0;
        //
        // GwConfig config = SmsConfig.getInstance().getGwConfigIsmgs().get(channel.getChanneId());
        // // 长短信长度
        // int longsmsLength = config.getLongsmsLength();
        // /** 默认取中文签名长度 **/
        // int smsSignLength = config.getIsmgSignCn().length();
        // /** 内容是否是纯英文的 */
        // boolean isEnglish = CharsetTools.isStringValidate(smsRequest.getMessage());
        // /** 英文短信的网关签名是否是纯英文的 */
        // boolean ismgSignEnIsEn = CharsetTools.isStringValidate(config.getIsmgSignEn());
        // /** 是英文短信，取英文签名长度 **/
        // if (isEnglish && ismgSignEnIsEn) {
        // smsSignLength = config.getIsmgSignEn().length();
        // }
        // // 计费条数=(短信内容长度+短信签名长度)/长短信长度
        // smsStandCnLength = (smsLength + smsSignLength) / longsmsLength;
        // if ((smsLength + smsSignLength) % longsmsLength != 0) {
        // smsStandCnLength += 1;
        // }
        // // 如果通道账号协议是平台协议，则可发送条数减一
        // channel.getRemainCount().addAndGet(-smsStandCnLength);
        // }
        // }
        //
        // }
        // }
        long beginL = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        if (isQuery.get()) {
            isQuery.set(false);
            bufferSet.clear();
        }
        if (bufferSet.size() > 0) {
            for (String ch : bufferSet) {
                buffer.append("and CHANNEL!='" + ch + "' ");
            }
            isQuery.set(true);
        }
        int runningChannel = this.smsAgent.getRunningChannel();
        if (runningChannel > bufferSet.size()) {

            List<SmsRequest> queryList = queryRequest(runningChannel * 150, buffer);
            List<String> idList = new ArrayList<String>();
            ArrayList<SmsRequest> deleteList = new ArrayList<SmsRequest>();
            ArrayList<SmsData> nonSendList = new ArrayList<SmsData>();
            Map<String, Integer> sendNumMap = new HashMap<String, Integer>();
            if (queryList.size() > 0) {
                // logger.info("从数据库取出" + queryList.size() + "条短信");
            }
            for (SmsRequest smsRequest : queryList) {
                boolean isPut = false;
                boolean isHasChannel = false;
                List<String> channelList = smsRequest.getChannelList();
                for (String channelId : channelList) {
                    SmsChannel channel = smsAgent.getChannels().get(channelId);
                    if (channel != null && channel.isRun()) {
                        // 通道最大条数
                        int allowSendCount = smsAgent.getAllowSendCount(channelId) - 1;
                        if (sendNumMap.get(channelId) != null) {
                            allowSendCount = allowSendCount - sendNumMap.get(channelId);
                        }
                        if (smsAgent.isAllowSend(channelId) && (allowSendCount > 0)) {// 判断此通道是否可以发送，迸且可发送的数量
                            //
                            if (smsAgent.getSentSubmitMapSize(channelId) > SmsConfig.getInstance().getSentSize()) {
                                logger.debug("<------>通道:" + channelId + "已发列队列满,暂停发送,队列大小为："
                                             + String.valueOf(smsAgent.getSentSubmitMapSize(channelId)) + ",配置大小为："
                                             + SmsConfig.getInstance().getSentSize() + "<------>");
                                bufferSet.add(channelId);
                                isHasChannel = true;
                                continue;
                            } else {

                                String phoneSeg[] = SmsChannelMgr.getInstance().getNumberChannel().get(channel).split(",");
                                for (String phon : phoneSeg) {
                                    if (smsRequest.getDestAddrs().get(0).length() > 4
                                        && smsRequest.getDestAddrs().get(0).substring(0, 3).equals(phon)) {
                                        smsRequest.setChannel(channel);
                                        deleteList.add(smsRequest);
                                        idList.add(smsRequest.getMtId());
                                        isPut = true;
                                        isHasChannel = true;
                                        Integer num = sendNumMap.get(channelId);
                                        if (num == null) {
                                            sendNumMap.put(channelId, new Integer(1));
                                        } else {

                                            num++;
                                            sendNumMap.put(channelId, num);
                                            // logger.info("通道"+channelId+"已发送量:"+sendNumMap.get(channelId)+"本通道已有量"+smsAgent.getAllowSendCount(channelId));
                                        }
                                        break;
                                    }
                                }
                                if (isPut) {
                                    break;
                                }
                            }
                        } else {
                            // 此通道因待发送队列满，下次不再发送
                            bufferSet.add(channelId);
                            isHasChannel = true;
                        }
                    }
                }
                if (!isPut && !isHasChannel)

                {
                    SmsData smsData = new SmsData();
                    smsData.id = smsRequest.getMtId();
                    smsData.smsid = smsRequest.getSmsId();
                    smsData.destaddr = smsRequest.getDestAddrs().get(0);
                    smsData.message = smsRequest.getMessage();
                    smsData.isneedreport = smsRequest.isNeedReport() ? 1 : 0;
                    smsData.priorty = smsRequest.getPriority();
                    smsData.excode = smsRequest.getExtcode();
                    smsData.createtime = smsRequest.getCreateTime();
                    smsData.orgid = smsRequest.getOrgid();
                    smsData.userid = smsRequest.getUserid();
                    smsData.smssign = smsRequest.getSmsSign();
                    nonSendList.add(smsData);
                }
            }
            // 从SMS_MT表中读到数据时,向sms_sent表批量插入从SMS_MT表读到的数据

            if (idList.size() > 0 || nonSendList.size() > 0) {
                Session session = null;
                Connection conn = null;
                Transaction tranction = null;
                PreparedStatement deleteps = null;
                ResultSet rs = null;
                PreparedStatement errorPs = null;

                try {
                    session = HibernateUtil.getNewSession();
                    conn = session.connection();
                    conn.setAutoCommit(false);
                    tranction = session.beginTransaction();

                    if (nonSendList.size() > 0) {
                        String errorInsertSql = DbConfig.instance.errInsertSql;
                        errorPs = conn.prepareStatement(errorInsertSql);
                        String description = SmsErrCode.NO_SEND_CHANNEL.getValue() + "*" + "无发送通道";
                        DBUtil.executePreState(nonSendList, errorPs, SmsErrCode.NO_SEND_CHANNEL.getValue(), description);
                        logger.debug("成功转移无发送通道 mt:" + nonSendList.size());
                    }

                    // 删除SMS_MT_TASK中已经读取的短信数据
                    String delSql = "delete from SMS_MT_TASK where MT_ID=? ";
                    deleteps = conn.prepareStatement(delSql);
                    for (String id : idList) {
                        deleteps.setString(1, id);
                        deleteps.addBatch();
                    }
                    for (SmsData data : nonSendList) {
                        deleteps.setString(1, data.id);
                        deleteps.addBatch();
                    }
                    deleteps.executeBatch();
                    tranction.commit();
                    conn.commit();
                    // if (EskLog.isDebugEnabled()) {
                    // logger.info("删除一批数据,size= " + (idList.size() + nonSendList.size()));
                    // }
                    if (conn != null) {
                        conn.close();
                    }
                    if (EskLog.isDebugEnabled()) {
                        logger.debug("转移一批数据,size= " + idList.size() + nonSendList.size() + " time="
                                     + (System.currentTimeMillis() - beginL));
                    }

                } catch (Exception e) {
                    logger.error("", e);
                    if (tranction != null) {
                        idList.clear();
                        deleteList.clear();
                        tranction.rollback();
                    }
                } finally {
                    DBUtil.closeStat(rs, deleteps, null, null);
                    DBUtil.closeConn(conn, session);
                }
            }
            // 当已删除的短信List大于零时，可以进行发送
            if (deleteList.size() > 0) {
                // logger.info("将发送" + deleteList.size() + "条短信");
                for (SmsRequest smsRequest : deleteList) {
                    this.smsAgent.sendSms(smsRequest);
                    /*			if (smsRequest.getChannel().getProtocolType().equals(SmsProtocolType.gw)) {

                    				int smsLength = smsRequest.getMessage().length();
                    				int smsStandCnLength = 0;

                    				GwConfig config = SmsConfig.getInstance().getGwConfigIsmgs().get(
                    						smsRequest.getChannel().getChanneId());
                    				// 长短信长度
                    				int longsmsLength = config.getLongsmsLength();
                    				*//** 默认取中文签名长度 **/
                    /*
                    int smsSignLength = config.getIsmgSignCn().length();
                    *//** 内容是否是纯英文的 */
                    /*
                    boolean isEnglish = CharsetTools.isStringValidate(smsRequest.getMessage());
                    *//** 英文短信的网关签名是否是纯英文的 */
                    /*
                    boolean ismgSignEnIsEn = CharsetTools.isStringValidate(config.getIsmgSignEn());
                    *//** 是英文短信，取英文签名长度 **/
                    /*
                    if (isEnglish && ismgSignEnIsEn) {
                    smsSignLength = config.getIsmgSignEn().length();
                    }
                    // 计费条数=(短信内容长度+短信签名长度)/长短信长度
                    smsStandCnLength = (smsLength + smsSignLength) / longsmsLength;
                    if ((smsLength + smsSignLength) % longsmsLength != 0) {
                    smsStandCnLength += 1;
                    }
                    // 如果通道账号协议是平台协议，则可发送条数减一
                    smsRequest.getChannel().getRemainCount().addAndGet(-smsStandCnLength);
                    }*/
                }
            }

        }

    }

    public List<SmsRequest> queryRequest(int count, StringBuffer buffer) {
        long beginL = System.currentTimeMillis();
        List<SmsRequest> retList = new ArrayList<SmsRequest>();
        String sqlStr = null;
        if (dbConfig.isOracle_run()) {
            if (buffer.length() > 4) {
                sqlStr = "select * from (select * from SMS_MT_TASK  where PRESEND_TIME <= sysdate  AND  PRESEND_TIME > sysdate-"
                         + SysConst.getValidTime()
                         / A_DAY_MINUTES
                         + " AND priorty >= 0 order by priorty desc) where  rownum <=" + count;
            } else {
                sqlStr = "select * from (select * from SMS_MT_TASK  where PRESEND_TIME <= sysdate  AND  PRESEND_TIME > sysdate-"
                         + SysConst.getValidTime()
                         / A_DAY_MINUTES
                         + " AND priorty >= 0"
                         + buffer.toString()
                         + " order by priorty desc) where  rownum <=" + count;
            }

            // 查出通道重发时间小于系统时间且创建时间小于短信有效时间取目标号码前3位
            // sqlStr = "select * from (select * from SMS_MT_TASK  where CHANNEL like '%" + channel.getChanneId() + "%'"
            // + " and PRESEND_TIME <= sysdate  AND " + SysConst.getValidTime()
            // + ">= round((sysdate-PRESEND_TIME) * 24 * 60)" + "  and substr(DESTADDR, 0, 3) in (" + segNumber
            // + ")  order by priorty desc) where  rownum <=" + count;
        } else if (dbConfig.isSqlserver_run()) {
            if (buffer.length() > 4) {
                sqlStr = " select top " + count
                         + " * from SMS_MT_TASK  WITH (NOLOCK)  where  PRESEND_TIME <=  getdate() and "
                         + SysConst.getValidTime() + " >= dateDiff(n,PRESEND_TIME,getdate()) " + buffer.toString()
                         + " order by PRIORTY desc";
            } else {
                sqlStr = " select top " + count
                         + " * from SMS_MT_TASK  WITH (NOLOCK)  where  PRESEND_TIME <=  getdate() and "
                         + SysConst.getValidTime() + " >= dateDiff(n,PRESEND_TIME,getdate()) order by PRIORTY desc";
            }
        } else if (dbConfig.isMySql_run()) {
            if (buffer.length() > 4) {
                sqlStr = " select * from SMS_MT_TASK  where unix_timestamp(PRESEND_TIME) <= unix_timestamp('"
                         + TimeUtil.getDateTimeStr() + "') and unix_timestamp('"
                         + TimeUtil.getFixTime(SysConst.getValidTime()) + "') <=unix_timestamp(PRESEND_TIME) "
                         + buffer.toString() + " order by PRIORTY desc limit " + count;
            } else {
                sqlStr = " select * from SMS_MT_TASK  where unix_timestamp(PRESEND_TIME) <= unix_timestamp('"
                         + TimeUtil.getDateTimeStr() + "') and unix_timestamp('"
                         + TimeUtil.getFixTime(SysConst.getValidTime())
                         + "') <=unix_timestamp(PRESEND_TIME)  order by PRIORTY desc limit " + count;
            }
        } else if (dbConfig.isSybase_run()) {
            sqlStr = "SET ROWCOUNT " + count + " select * from SMS_MT_TASK  where  getdate() >=PRESEND_TIME and "
                     + SysConst.getValidTime() + " >= dateDiff(mi,PRESEND_TIME,getdate())" + buffer.toString()
                     + " order by PRIORTY desc";

        }
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        ResultSet rs = null;
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            tranction = session.beginTransaction();
            rs = conn.createStatement().executeQuery(sqlStr);
            while (rs.next()) {
                SmsRequest smsRequest = new SmsRequest();
                smsRequest.setSmsId(rs.getString("SMSID"));
                smsRequest.setNeedReport(rs.getInt("ISNEEDREPORT") == 1 ? true : false);
                List<String> destAddrs = new ArrayList<String>();
                destAddrs.add(rs.getString("DESTADDR"));
                smsRequest.setDestAddrs(destAddrs);
                /* logger.info("原始" + rs.getString("MESSAGE").trim());
                 logger.info("************"
                             + StringUtils.strConvertEncode(rs.getString("MESSAGE").trim(),
                                                            dbConfig.dbcode,
                                                            dbConfig.syscode));*/
                smsRequest.setMessage(StringUtils.strConvertEncode(rs.getString("MESSAGE").trim(),
                                                                   dbConfig.dbcode,
                                                                   dbConfig.syscode));
                smsRequest.setPriority(rs.getInt("PRIORTY"));
                smsRequest.setSendTime(TimeUtil.getDateTimeStr());
                smsRequest.setValidTime(rs.getInt("VALID_TIME"));
                smsRequest.setCreateTime(rs.getString("CREATE_TIME"));
                smsRequest.setExtcode(rs.getString("EXTCODE"));
                // 若IS_RESEND字段为1,则表明是重发短信
                if (rs.getInt("IS_RESEND") == 1) {
                    smsRequest.setRendSms(true);
                } else {
                    smsRequest.setRendSms(false);
                }
                String mt_id = rs.getString("MT_ID");
                smsRequest.setMtId(mt_id);
                smsRequest.setSmsSign(rs.getString("SMS_SIGN"));
                smsRequest.setOrgid(rs.getString("ORGID"));
                smsRequest.setUserid(rs.getString("SEND_USERID"));
                String[] channelArray = rs.getString("CHANNEL").split(",");
                for (String ch : channelArray) {
                    smsRequest.getChannelList().add(ch);
                }
                retList.add(smsRequest);
            }
            tranction.commit();
            conn.commit();
            if (conn != null) {
                conn.close();
            }
            // logger.info("查询一批数据,size= " + retList.size() + " time=" + (System.currentTimeMillis() - beginL));
        } catch (Exception e) {
            logger.error("", e);
            if (tranction != null) {
                retList.clear();
                tranction.rollback();
            }
        } finally {
            DBUtil.closeStat(rs, null, null, null);
            DBUtil.closeConn(conn, session);
        }

        return retList;
    }

    /** 从SMS_MT_TASK表中读取短信放入LIST中 */
    @SuppressWarnings("deprecation")
    public List<SmsRequest> getRequests(SmsChannel channel, int count) {
        long beginL = System.currentTimeMillis();
        List<SmsRequest> retList = new ArrayList<SmsRequest>();
        String sqlStr = null;
        String phoneSeg[] = SmsChannelMgr.getInstance().getNumberChannel().get(channel).split(",");
        StringBuffer sb = new StringBuffer();
        for (String seg : phoneSeg) {
            sb.append("'").append(seg).append("',");
        }
        String segNumber = sb.substring(0, sb.lastIndexOf(","));
        if (dbConfig.isOracle_run()) {
            sqlStr = "select * from (select * from SMS_MT_TASK  where PRESEND_TIME <= sysdate  AND "
                     + SysConst.getValidTime()
                     + ">= round((sysdate-PRESEND_TIME) * 24 * 60)order by priorty desc) where  rownum <=" + count;

            // 查出通道重发时间小于系统时间且创建时间小于短信有效时间取目标号码前3位
            // sqlStr = "select * from (select * from SMS_MT_TASK  where CHANNEL like '%" + channel.getChanneId() + "%'"
            // + " and PRESEND_TIME <= sysdate  AND " + SysConst.getValidTime()
            // + ">= round((sysdate-PRESEND_TIME) * 24 * 60)" + "  and substr(DESTADDR, 0, 3) in (" + segNumber
            // + ")  order by priorty desc) where  rownum <=" + count;
        } else if (dbConfig.isSybase_run()) {
            sqlStr = "SET ROWCOUNT " + count + " select * from SMS_MT_TASK  where CHANNEL like '%"
                     + channel.getChanneId() + "%'"
                     + " and PRESEND_TIME <=  getdate() and dateDiff(mi,PRESEND_TIME,getdate()) <= "
                     + SysConst.getValidTime() + "  and SUBSTRING (DESTADDR, 1, 3) in (" + segNumber
                     + ") order by PRIORTY desc";

        } else if (dbConfig.isSqlserver_run()) {
            sqlStr = " select top " + count + "* from SMS_MT_TASK  WITH (NOLOCK)  where CHANNEL like '%"
                     + channel.getChanneId() + "%'"
                     + " and PRESEND_TIME <=  getdate() and dateDiff(n,PRESEND_TIME,getdate()) <= "
                     + SysConst.getValidTime() + " and SUBSTRING (DESTADDR, 1, 3) in (" + segNumber
                     + ") order by PRIORTY desc";
        } else if (dbConfig.isMySql_run()) {
            sqlStr = " select * from SMS_MT_TASK  where CHANNEL like '%" + channel.getChanneId() + "%'"
                     + " and unix_timestamp(PRESEND_TIME) <= unix_timestamp('" + TimeUtil.getDateTimeStr()
                     + "') and unix_timestamp('" + TimeUtil.getFixTime(SysConst.getValidTime())
                     + "') <=unix_timestamp(PRESEND_TIME)" + "and substr(DESTADDR, 1, 3) in (" + segNumber
                     + ") order by PRIORTY desc limit " + count;
        }
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        PreparedStatement deleteps = null;
        ResultSet rs = null;
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            tranction = session.beginTransaction();
            rs = conn.createStatement().executeQuery(sqlStr);
            ArrayList<String> idsList = new ArrayList<String>();
            while (rs.next()) {
                SmsRequest smsRequest = new SmsRequest();
                smsRequest.setSmsId(rs.getString("SMSID"));
                smsRequest.setNeedReport(rs.getInt("ISNEEDREPORT") == 1 ? true : false);
                List<String> destAddrs = new ArrayList<String>();
                destAddrs.add(rs.getString("DESTADDR"));
                smsRequest.setDestAddrs(destAddrs);
                smsRequest.setMessage(StringUtils.strConvertEncode(rs.getString("MESSAGE"),
                                                                   dbConfig.dbcode,
                                                                   dbConfig.syscode));
                smsRequest.setPriority(rs.getInt("PRIORTY"));
                smsRequest.setSendTime(TimeUtil.getDateTimeStr());
                smsRequest.setChannel(channel);
                smsRequest.setValidTime(rs.getInt("VALID_TIME"));
                smsRequest.setCreateTime(rs.getString("CREATE_TIME"));
                smsRequest.setExtcode(rs.getString("EXTCODE"));
                // 若IS_RESEND字段为1,则表明是重发短信
                if (rs.getInt("IS_RESEND") == 1) {
                    smsRequest.setRendSms(true);
                } else {
                    smsRequest.setRendSms(false);
                }
                String mt_id = rs.getString("MT_ID");
                smsRequest.setMtId(mt_id);
                smsRequest.setSmsSign(rs.getString("SMS_SIGN"));
                smsRequest.setOrgid(rs.getString("ORGID"));
                smsRequest.setUserid(rs.getString("SEND_USERID"));
                retList.add(smsRequest);
                idsList.add(mt_id);
            }
            if (idsList.size() >= 1) {
                // 删除SMS_MT_TASK中已经读取的短信数据
                String delSql = "delete from SMS_MT_TASK where MT_ID=? ";
                deleteps = conn.prepareStatement(delSql);
                for (String id : idsList) {
                    deleteps.setString(1, id);
                    deleteps.addBatch();
                }
                deleteps.executeBatch();
                tranction.commit();
                conn.commit();
            }

            if (EskLog.isDebugEnabled()) {
                logger.debug("转移一批" + channel.getChanneId() + "数据,size= " + idsList.size() + " time="
                             + (System.currentTimeMillis() - beginL));
            }
            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            logger.error("", e);
            if (tranction != null) {
                retList.clear();
                tranction.rollback();
            }
        } finally {
            DBUtil.closeStat(rs, deleteps, null, null);
            DBUtil.closeConn(conn, session);
        }
        return retList;
    }

    public SmsAgentService getSmsAgent() {
        return smsAgent;
    }
}
