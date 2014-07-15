package com.surge.engine.dbadapter.runnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.surge.engine.dbadapter.util.DBUtil;
import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.service.SmsChannelMgr;
import com.surge.engine.util.HibernateUtil;

/**
 * 将通讯配置的信息同步到数据库表中
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-22
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class InitSmsChannel {
    private static final Logger logger = Logger.getLogger(InitSmsChannel.class);

    @SuppressWarnings("deprecation")
    public static void initChannel() {
        String deleteSql = "DELETE FROM SMS_CHANNEL";
        String insertSql = "insert into SMS_CHANNEL(CHANNEL_NAME,PROTOCOL_TYPE,PHONE_SEGMENT,SMS_FLUX,STATUS,CN_SIGN,EN_SIGN,SMSLENGTHCN,SMSLENGTHEN) VALUES(?,?,?,?,?,?,?,?,?)";
        Map<String, SmsChannel> channels = SmsChannelMgr.getInstance().getChannels();
        Session session = null;
        Connection conn = null;
        Transaction tranction = null;
        Statement de_Statement = null;
        PreparedStatement in_PrStatement = null;
        try {
            session = HibernateUtil.getNewSession();
            conn = session.connection();
            tranction = session.beginTransaction();
            de_Statement = conn.createStatement();
            de_Statement.execute(deleteSql);
            in_PrStatement = conn.prepareStatement(insertSql);
            Iterator<String> keyIter = channels.keySet().iterator();
            while (keyIter.hasNext()) {
                int j = 1;
                SmsChannel channel = channels.get(keyIter.next());
                in_PrStatement.setString(j++, channel.getChanneId());
                in_PrStatement.setString(j++, channel.getProtocolType().toString());
                in_PrStatement.setString(j++, channel.getNumberSegment());
                in_PrStatement.setInt(j++, channel.getFlux());
                if (channel.isRun()) {
                    in_PrStatement.setString(j++, "启动");
                } else {
                    in_PrStatement.setString(j++, "关闭");
                }
                in_PrStatement.setString(j++, channel.getCnSign());
                in_PrStatement.setString(j++, channel.getEnSign());

                in_PrStatement.setInt(j++, channel.getSmsLengthCn());
                in_PrStatement.setInt(j++, channel.getSmsLengthEn());
                in_PrStatement.addBatch();
            }
            in_PrStatement.executeBatch();
            tranction.commit();

        } catch (Exception e) {
            if (tranction != null) {
                tranction.commit();
            }
            logger.error("", e);
        } finally {
            DBUtil.closeConn2(de_Statement, in_PrStatement, conn, session);
        }

    }
}
