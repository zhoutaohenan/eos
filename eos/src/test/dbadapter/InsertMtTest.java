package test.dbadapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import com.surge.engine.util.StringUtils;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.Tools;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-10
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class InsertMtTest {

    @Test
    public void oracleInsert() {
        List<SmsData> list = new ArrayList<SmsData>();
        for (int i = 0; i < 1; i++) {
            SmsData smsData = new SmsData();
            smsData.id = 123456;
            smsData.smsid = "123456" + i;
            // smsData.destaddr = "13724289305";
            smsData.destaddr = "15568007137";
            // smsData.destaddr = "1849999";
            // smsData.message = "采用专用SGIP方式承载时，通信双方互为客户端和服务器端。当客户端要发送命令时，"
            // + "主动向服务器端建立连接，然后向服务器端发送命令，并接收应答；服务器端从客户端接收命令，返回应答。" +
            // "连接建立以后，客户端可以连续发送多条命令。命令发送完并接收到所有应答后，客户端应该主动断开连接。" +
            // "但是，命令及其应答之间的时间间隔最大不能超过30秒(默认，可配置)" +
            // "（如果命令应答时间超过最大等待时间，则要求客户端重发该条命令！";
            // smsData.message =
            // "Asdfkasdfkasdfkasdfkasdfkxxxxxxxxxxxxxxxxxxasdfkasdfkasdfkasdfkasdfkasdfkxxxxxxxxxxxxxxxxasdfkasdfkasdfkasdfkasdfkasdfkxxxxxxxxxxxxxxxxxxxxxxxxasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkas";
            // smsData.message="AsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkAsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkAsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasd";
            // smsData.message =
            // "巨澜系企业十周年司庆既是展示巨澜系企业十周年司庆既是展示巨澜系企业十周年司庆既是展示巨澜系企业十周年司庆既是展示巨澜系企业十周年司庆既是展示巨澜系企业十周年司庆既是展示巨澜系企业十周年司庆既是展示";
            // System.out.println("length==" + smsData.message.length());
            // smsData.message =
            // "集团法务处郑重提示：近期出现冒充顺丰名义向客户送件现象，给公司声誉造成不利影响。请严格按照公司操作规程对运单进行保护，严禁将客户信息向无关人员透露；并请向客户提示注意识别真伪，以防被骗。根据刑法规定：擅自泄露客户信息可构成侵犯商业秘密犯罪，最高可处7年以下有期徒刑。";
            smsData.message = "验证问题，请见谅－－－－－，[周涛]!";
            smsData.isneedreport = 1;
            smsData.requesttime = TimeUtil.getDateTimeStr();
            // smsData.channel = "smgp_0";
            // if (i % 2 == 0)
            // {
            // smsData.channel = "cmpp2_0";
            // } else
            // {
            // // smsData.channel = "cmpp2_0";
            // }
            smsData.channel = "sgip_0";
            // smsData.channel = "cmpp2_0";
            // smsData.channel = "gw_0";
            // }
            if (i % 2 == 0) {
                smsData.priorty = 9;
            } else {
                smsData.priorty = 0;
            }
            // smsData.priorty = 5;
            smsData.presendtime = TimeUtil.getDateTimeStr();
            smsData.validTime = 10000;
            smsData.orgid = "24a1d741-2209-4d5b-9fdf-c4a136bf9920";
            if (i % 2 == 0) {
                smsData.userid = "0";
            } else {
                smsData.userid = "3163530e-0f0c-43ae-bc0f-0c353d317c75";
            }

            // smsData.userSign = "马超";
            list.add(smsData);
            if (list.size() > 1000) {
                this.insertDB(list);
                try {
                    Tools.csleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                list = new ArrayList<SmsData>();
            }
        }
        this.insertDB(list);

    }

    @SuppressWarnings("static-access")
    @Test
    public void sqlServerInsert() {
        int t = 0;
        List<SmsData> list = new ArrayList<SmsData>();

        // while (true)
        // {
        // if (t == 3000)
        // {
        // try
        // {
        // Thread.currentThread().sleep(1000);
        // } catch (InterruptedException e)
        // {
        // e.printStackTrace();
        // }
        // }

        for (int i = 0; i < 10; i++) {
            t++;
            SmsData smsData = new SmsData();
            smsData.id = 123456;
            smsData.smsid = "123456" + i;
            smsData.destaddr = "13526015805";
            // smsData.destaddr = "1849999";
            // smsData.message = "采用专用SGIP方式承载时，通信双方互为客户端和服务器端。当客户端要发送命令时，"
            // + "主动向服务器端建立连接，然后向服务器端发送命令，并接收应答；服务器端从客户端接收命令，返回应答。"
            // + "连接建立以后，客户端可以连续发送多条命令。命令发送完并接收到所有应答后，客户端应该主动断开连接。"
            // + "但是，命令及其应答之间的时间间隔最大不能超过30秒(默认，可配置)" +
            // "（如果命令应答时间超过最大等待时间，则要求客户端重发该条命令！";
            smsData.message = "为什么说代码很难看，初学者可能没有这种感觉，";// 我们分析如下，初始化工作如果是很长一段代码，说明要做的工作很多，将很多工作装入一个方法中，相当于将很多鸡蛋放在一个篮子里，是很危险的，这也是有背于Java面向对象的原则";
            smsData.isneedreport = 1;
            smsData.requesttime = TimeUtil.getDateTimeStr();
            // smsData.channel = "cmpp2_0";
            // smsData.channel = "cmpp2_0";
            // smsData.channel = "sgip_0";
            // smsData.channel = "smgp_0";
            // }
            if (i % 2 == 0) {
                smsData.priorty = 1;
            } else {
                smsData.priorty = 0;
            }
            smsData.presendtime = TimeUtil.getDateTimeStr();
            smsData.validTime = 10000;
            smsData.orgid = "24a1d741-2209-4d5b-9fdf-c4a136bf9920";
            smsData.userid = "3163530e-0f0c-43ae-bc0f-0c353d317c75";
            // smsData.userSign = "[测试]";
            list.add(smsData);
            if (list.size() > 1000) {
                this.sqlSeverInsertDB(list);
                // try
                // {
                // Tools.csleep(10);
                // } catch (InterruptedException e)
                // {
                // e.printStackTrace();
                // }

                list = new ArrayList<SmsData>();
            }
        }
        this.sqlSeverInsertDB(list);
        try {
            Tools.csleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // }//

    @Test
    public void sybaseInsert() {
        List<SmsData> list = new ArrayList<SmsData>();
        for (int i = 0; i < 100; i++) {
            SmsData smsData = new SmsData();
            smsData.id = 123456;
            smsData.smsid = "123456" + i;
            smsData.destaddr = "13699999999";
            // smsData.destaddr = "1849999";
            // smsData.message = "采用专用SGIP方式承载时，通信双方互为客户端和服务器端。当客户端要发送命令时，"
            // +
            // "主动向服务器端建立连接，然后向服务器端发送命令，并接收应答；服务器端从客户端接收命令，返回应答。" +
            // "连接建立以后，客户端可以连续发送多条命令。命令发送完并接收到所有应答后，客户端应该主动断开连接。" +
            // "但是，命令及其应答之间的时间间隔最大不能超过30秒(默认，可配置)" +
            // "（如果命令应答时间超过最大等待时间，则要求客户端重发该条命令！";
            try {
                smsData.message = new String("中国人民".getBytes(), "cp936");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            smsData.isneedreport = 1;
            smsData.requesttime = TimeUtil.getDateTimeStr();
            // if(i%2==0)
            // {
            // smsData.channel = "smgp_0";
            // }
            // else
            // {
            // smsData.channel = "sgip_0";
            smsData.channel = "cmpp2_0";
            // }
            if (i % 2 == 0) {
                smsData.priorty = 1;
            } else {
                smsData.priorty = 0;
            }
            // smsData.priorty = 5;
            smsData.presendtime = TimeUtil.getDateTimeStr();
            smsData.validTime = 10000;
            list.add(smsData);
            if (list.size() > 1000) {
                this.sybaseInsertDB(list);
                try {
                    Tools.csleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                list = new ArrayList<SmsData>();
            }
        }

        this.sybaseInsertDB(list);

    }

    private void sybaseInsertDB(List<SmsData> list) {
        String insertSql = "insert into SMS_MT (SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,SMS_SIGN,ORGID,SEND_USERID) values(?,?,?,?,?,?,?,?,?)";
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            PreparedStatement pstmt = null;
            pstmt = session.connection().prepareStatement(insertSql);
            for (int i = 0; i < list.size(); i++) {
                pstmt = setStatement(pstmt, list.get(i));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            HibernateUtil.commitTransaction();

        } catch (Exception e) {
            HibernateUtil.rollbackTransaction();
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Test
    public void sybaseQueryDb() {
        try {
            String strQ = "SET ROWCOUNT 2  select * from SMS_MT  order by PRIORTY ";
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            PreparedStatement pstmt = session.connection().prepareStatement(strQ);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String message = rs.getString("message");
                System.out.println("iso_1:" + StringUtils.strConvertEncode(message, "ISO8859_1", "gbk"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sqlSeverInsertDB(List<SmsData> list) {
        String insertSql = "insert into SMS_MT (SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,SMS_SIGN,ORGID,SEND_USERID) values(?,?,?,?,?,?,?,?,?)";
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            PreparedStatement pstmt = null;
            pstmt = session.connection().prepareStatement(insertSql);
            for (int i = 0; i < list.size(); i++) {
                pstmt = setStatement(pstmt, list.get(i));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            HibernateUtil.commitTransaction();

        } catch (Exception e) {
            HibernateUtil.rollbackTransaction();
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Test
    public void mySQLInsert() {
        int t = 0;
        // while (true)
        // {
        //
        // if (t == 200000)
        // {
        // break;
        // }
        List<SmsData> list = new ArrayList<SmsData>();
        for (int i = 0; i < 1; i++) {
            t++;
            SmsData smsData = new SmsData();
            smsData.id = 123456;
            smsData.smsid = "123456" + i;
            // smsData.destaddr = "13147047334";
            smsData.destaddr = "18922858338";
            // smsData.destaddr = "18922858338";
            // smsData.destaddr = "1849999";
            // smsData.destaddr = "18922858338";
            // smsData.message = "采用专用SGIP方式承载时，通信双方互为客户端和服务器端。当客户端要发送命令时，"
            // + "主动向服务器端建立连接，然后向服务器端发送命令，并接收应答；服务器端从客户端接收命令，返回应答。"
            // + "连接建立以后，客户端可以连续发送多条命令。命令发送完并接收到所有应答后，客户端应该主动断开连接。"
            // + "但是，命令及其应答之间的时间间隔最大不能超过30秒(默认，可配置)" +
            // "（如果命令应答时间超过最大等待时间，则要求客户端重发该条命令！";
            // try
            // {
            // smsData.message = new String(gbk2utf8("中国人民"), "UTF-8"); //
            // String.valueOf("中国人民".getBytes("utf8"));
            // } catch (UnsupportedEncodingException e1)
            // {
            // // TODO Auto-generated catch block
            // e1.printStackTrace();
            // }
            // smsData.message =
            // "那么如果我们要视察一个档案的内容时该如何是好呢这里有相当多有趣的指令可以来分享一最常使用的显示档案内容的指令可以说了此外以说那么如果我们有趣的指令那么如果我们要视察一个档87";
            // String temp=smsData.message.getBytes("utf-8");
            // smsData.message="dfdfdfdfdfdfdfdd";
            // smsData.message="你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好";
            // smsData.message =
            // "ABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDE";
            smsData.message = "dfdsafasfa中国g!";
            smsData.message.length();
            smsData.isneedreport = 1;
            smsData.requesttime = TimeUtil.getDateTimeStr();
            // if (i % 2 == 0)
            // {
            // smsData.channel = "sgip_0";
            // } else
            // {
            // smsData.channel = "cmpp2_0";
            //
            // }
            smsData.channel = "浙江电信01";
            smsData.priorty = 1;
            smsData.excode = "12123";
            // if (i % 2 == 0)
            // {
            //
            // smsData.channel = "cmpp2_0";
            // } else
            // {
            // smsData.channel = "sgip_0";
            // }
            // smsData.channel = "gw_0";
            // smsData.priorty = 5;
            smsData.presendtime = TimeUtil.getDateTimeStr();
            smsData.validTime = 10000;
            smsData.orgid = "24a1d741-2209-4d5b-9fdf-c4a136bf9920";
            smsData.userid = "3163530e-0f0c-43ae-bc0f-0c353d317c75";
            // smsData.userSign = "";
            list.add(smsData);
            if (list.size() > 1000) {
                this.sqlMysqlInsertDB(list);
                try {
                    Tools.csleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                list = new ArrayList<SmsData>();
            }
        }
        this.sqlMysqlInsertDB(list);
        // try
        // {
        // Tools.csleep(2 * 1000);
        // } catch (InterruptedException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // }

    }

    public byte[] gbk2utf8(String chenese) {
        char c[] = chenese.toCharArray();
        byte[] fullByte = new byte[3 * c.length];
        for (int i = 0; i < c.length; i++) {
            int m = (int) c[i];
            String word = Integer.toBinaryString(m);
            // System.out.println(word);

            StringBuffer sb = new StringBuffer();
            int len = 16 - word.length();
            // 补零
            for (int j = 0; j < len; j++) {
                sb.append("0");
            }
            sb.append(word);
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");

            // System.out.println(sb.toString());

            String s1 = sb.substring(0, 8);
            String s2 = sb.substring(8, 16);
            String s3 = sb.substring(16);

            byte b0 = Integer.valueOf(s1, 2).byteValue();
            byte b1 = Integer.valueOf(s2, 2).byteValue();
            byte b2 = Integer.valueOf(s3, 2).byteValue();
            byte[] bf = new byte[3];
            bf[0] = b0;
            fullByte[i * 3] = bf[0];
            bf[1] = b1;
            fullByte[i * 3 + 1] = bf[1];
            bf[2] = b2;
            fullByte[i * 3 + 2] = bf[2];

        }
        return fullByte;
    }

    @Test
    private void sqlMysqlInsertDB(List<SmsData> list) {
        String insertSql = "insert into SMS_MT (MT_ID,SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,CREATE_TIME,PRESEND_TIME,ORGID,SEND_USERID) values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            PreparedStatement pstmt = null;
            pstmt = session.connection().prepareStatement(insertSql);
            for (int i = 0; i < list.size(); i++) {
                pstmt = setMySqlStatement(pstmt, list.get(i));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            HibernateUtil.commitTransaction();

        } catch (Exception e) {
            HibernateUtil.rollbackTransaction();
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Test
    private void testMySqlEncode() {
        String sql = "select * from SMS_MT";
        try {
            HibernateUtil.beginTransaction();

            Session session = HibernateUtil.getSession();
            ResultSet rs = session.connection().createStatement().executeQuery(sql);
            rs.next();
            String m1 = rs.getString("MESSAGE");
            String m2 = new String(m1.getBytes("utf8"), "utf-8");
            System.out.println(m1);
            System.out.println("gbk:" + m2);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void insertDB(List<SmsData> list) {
        String insertSql = "insert into SMS_MT (MT_ID,SMSID,DESTADDR,MESSAGE,ISNEEDREPORT,CHANNEL,PRIORTY,SMS_SIGN,ORGID,SEND_USERID) values(MT_ID.NextVal,?,?,?,?,?,?,?,?,?)";
        try {
            HibernateUtil.beginTransaction();
            Session session = HibernateUtil.getSession();
            PreparedStatement pstmt = null;
            pstmt = session.connection().prepareStatement(insertSql);
            for (int i = 0; i < list.size(); i++) {
                pstmt = setStatement(pstmt, list.get(i));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            HibernateUtil.commitTransaction();

        } catch (Exception e) {
            HibernateUtil.rollbackTransaction();
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public PreparedStatement setMySqlStatement(PreparedStatement pstmt, SmsData smsData) throws SQLException {
        int j = 1;
        pstmt.setString(j++, UUID.randomUUID().toString());
        pstmt.setString(j++, smsData.smsid);
        pstmt.setString(j++, smsData.destaddr);
        pstmt.setString(j++, smsData.message);
        pstmt.setInt(j++, smsData.isneedreport);
        pstmt.setString(j++, smsData.channel);
        pstmt.setInt(j++, smsData.priorty);
        pstmt.setTimestamp(j++, TimeUtil.getTimestamp(TimeUtil.getDateTimeStr()));
        pstmt.setTimestamp(j++, TimeUtil.getTimestamp(TimeUtil.getDateTimeStr()));
        // pstmt.setString(j++, smsData.userSign);
        pstmt.setString(j++, String.valueOf(smsData.orgid));
        pstmt.setString(j++, smsData.userid);
        return pstmt;

    }

    class SmsData {
        long id;

        String smsid;

        String destaddr;

        String message;

        int isneedreport;

        String requesttime;

        String channel;

        int priorty;

        String excode;

        String presendtime;

        int validTime;

        String orgid;

        String userid;

        String userSign;
    }

    /**
     * 设置statement参数 PreparedStatement
     * 
     * @param pstmt
     * @param smsBill
     * @return
     * @throws SQLException
     */
    public PreparedStatement setStatement(PreparedStatement pstmt, SmsData smsData) throws SQLException {
        int j = 1;
        pstmt.setString(j++, smsData.smsid);
        pstmt.setString(j++, smsData.destaddr);
        pstmt.setString(j++, smsData.message);
        pstmt.setInt(j++, smsData.isneedreport);
        pstmt.setString(j++, smsData.channel);
        pstmt.setInt(j++, smsData.priorty);
        pstmt.setString(j++, smsData.userSign);
        pstmt.setString(j++, smsData.orgid);
        pstmt.setString(j++, smsData.userid);
        return pstmt;

    }

    public void distract() {
        long beginL = System.currentTimeMillis();
        String querySql = "select * from SMS_SENT_TEMP "
                          + "where ((ISNEEDREPORT=1 and status is not null) or (ISNEEDREPORT=0)) and rownum < 1000 ";
        String insertSql = "insert into SMS_SENT "
                           + "(SENT_ID,MT_ID, SMSID, DESTADDR, CREATE_TIME, ISNEEDREPORT, SEND_FLAG, SEND_TIME,"
                           + "CHANNEL, RESEND_CHANNEL, MESSAGE, SPLIT_MESSAGE, PK_TOTAL, PK_NUMBER, ISNEED_RESEND,"
                           + "RESEND_STATUS, MESSAGE_ID,SEND_RET,STATUS,DESCRIPTION,REPORT_TIME) select "
                           + "SENT_ID.NextVal,MT_ID, SMSID, DESTADDR, CREATE_TIME, ISNEEDREPORT, SEND_FLAG, "
                           + "SEND_TIME,CHANNEL, RESEND_CHANNEL, MESSAGE, SPLIT_MESSAGE, PK_TOTAL, PK_NUMBER, "
                           + "ISNEED_RESEND,RESEND_STATUS, MESSAGE_ID,SEND_RET,STATUS,DESCRIPTION,REPORT_TIME from SMS_SENT_TEMP "
                           + "where ((ISNEEDREPORT=1 and status is not null) or (ISNEEDREPORT=0) or(SEND_FLAG=0)) and rownum < 1000 ";
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
            conn = session.connection();
            tranction = session.beginTransaction();
            rs = conn.createStatement().executeQuery(querySql);

        } catch (Exception e) {
            if (tranction != null) {
                tranction.rollback();
            }

        } finally {
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

                if (session != null) {
                    session.close();
                    session = null;
                }
            } catch (Exception e) {
            }

        }
    }

    @Test
    public void InsertCmppTest() {
        File file = new File("d://d.txt");

        PreparedStatement pstmt = null;
        Connection conn = createOracleConnection();
        // String insertSql =
        // "insert into CMPP (CMPP_ID,PK_TOTAL,PK_NUMBER,REGISTERED_DELIVERY,MSG_LEVEL,SERVICE_ID,FEE_USERTYPE,FEE_TERMINAL_ID,TP_PID,TP_UDHI,MSG_FMT,MSG_SRC,FEETYPE,FEECODE,VALID_TIME,AT_TIME,SRC_ID,DESTUSR_TL,DEST_TERMINAL_ID,MSG_LENGTH,MSG_CONTENT,RESERVE) "
        // +
        // "values(CMPP_SEQ.NextVal,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String insertSql = "insert into mt_count(time,mt_number,spend_time) values (?,?,?)";

        FileReader fr = null;
        BufferedReader br = null;
        try {
            conn.setAutoCommit(false);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            pstmt = conn.prepareStatement(insertSql);
            String s = null;
            int p = 1;
            while ((s = br.readLine()) != null) {
                String sarray[] = s.split(" ");
                pstmt.setString(1, sarray[0]);
                pstmt.setString(2, sarray[2]);
                pstmt.setString(3, sarray[3]);
                pstmt.addBatch();
                p++;
                if (p % 100 == 0) {
                    System.out.println("插入100条...");
                    pstmt.executeBatch();
                    conn.commit();
                }

            }
            pstmt.executeBatch();
            conn.commit();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // List<Cmpp> list = new ArrayList<Cmpp>();
        // long begin = System.currentTimeMillis();
        // int p = 0;
        // try
        // {
        // for (int i = 0; i < 10000; i++)
        // {
        // p++;
        // int j = 1;
        // pstmt.setInt(j++, 1);
        // pstmt.setInt(j++, 1);
        // pstmt.setInt(j++, 1);
        // pstmt.setInt(j++, 0);
        // pstmt.setString(j++, "123585ascd");
        // pstmt.setInt(j++, 2);
        // pstmt.setString(j++, "13855225523");
        // pstmt.setInt(j++, 1);
        // pstmt.setInt(j++, 2);
        // pstmt.setInt(j++, 15);
        // pstmt.setString(j++, "4190022");
        // pstmt.setString(j++, "01");
        // pstmt.setString(j++, "02");
        // pstmt.setString(j++, "2011-08-31 10:10:11");
        // pstmt.setString(j++, "2011-08-31 10:10:11");
        // pstmt.setString(j++, "106573025497");
        // pstmt.setInt(j++, 99);
        // pstmt.setString(j++, "13855555555");
        // pstmt.setInt(j++, 140);
        // pstmt.setString(j++,
        // "致电4008205555客户服务热线申请开通。招商银行会于到期还款日当天从您指定的一卡通活期账户中向您的信用卡转账还款卡转账还款卡转账还款周");
        // pstmt.setString(j++, "");
        // pstmt.addBatch();
        // if (p >= 10000)
        // {
        // pstmt.executeBatch();
        // conn.commit();
        // pstmt.clearBatch();
        // p = 0;
        // //
        // System.out.println("保存"+i+"条用时："+String.valueOf(System.currentTimeMillis()-begin));
        // try
        // {
        // Thread.sleep(1);
        // } catch (InterruptedException e)
        // {
        // e.printStackTrace();
        // }
        // // list = new ArrayList<Cmpp>();
        // }
        // }
        // } catch (Exception e)
        // {
        // e.printStackTrace();
        // }
        // System.out.println("总计用时：" +
        // String.valueOf(System.currentTimeMillis() - begin));
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {

        }
    }

    private void testCmppInsert(List<Cmpp> list, Connection conn, PreparedStatement pstmt) {

        try {

            for (Cmpp data : list) {
                int j = 1;
                pstmt.setInt(j++, data.Pk_total);
                pstmt.setInt(j++, data.Pk_number);
                pstmt.setInt(j++, data.Registered_Delivery);
                pstmt.setInt(j++, data.Msg_level);
                pstmt.setString(j++, data.Service_Id);
                pstmt.setInt(j++, data.Fee_UserType);
                pstmt.setString(j++, data.Fee_terminal_Id);
                pstmt.setInt(j++, data.TP_pId);
                pstmt.setInt(j++, data.TP_udhi);
                pstmt.setInt(j++, data.Msg_Fmt);
                pstmt.setString(j++, data.Msg_src);
                pstmt.setString(j++, data.FeeType);
                pstmt.setString(j++, data.FeeCode);
                pstmt.setString(j++, data.ValId_Time);
                pstmt.setString(j++, data.At_Time);
                pstmt.setString(j++, data.Src_Id);
                pstmt.setInt(j++, data.DestUsr_tl);
                pstmt.setString(j++, data.Dest_terminal_Id);
                pstmt.setInt(j++, data.Msg_Length);
                pstmt.setString(j++, data.Msg_Content);
                pstmt.setString(j++, data.Reserve);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.clearBatch();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {

        }

    }

    public static Connection createOracleConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            String url = "jdbc:oracle:thin:@10.0.11.190:1521:devdb"; // orcl为数据库的SID
            String user = "qxw";
            String password = "qxw";
            conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection createMysqlConn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://10.0.11.194:3306/esk2"; // orcl为数据库的SID
            String user = "root";
            String password = "12345678";
            conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }

    private static Connection createSqlSeverConnection() {
        Connection conn = null;
        try {
            // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            // String url = "jdbc:sqlserver://10.0.11.186:1433;DatabaseName=qxw1"; // orcl为数据库的SID
            // String user = "sa";
            // String password = "123456";
            DriverManager.getDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }

    public static void main(String[] args) {
        Connection conn = createSqlSeverConnection();
        // try
        // {
        // conn.setAutoCommit(false);
        // PreparedStatement ps=conn.prepareStatement("delect from sms_mt where mt_id=?");
        // for(int i=1;i<10;i++)
        // {
        // ps.setInt(i, 121+i );
        // Thread.currentThread().sleep(10000);
        // i++;
        // ps.addBatch();
        // }
        // ps.executeBatch();
        // } catch (Exception e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    class Cmpp {
        int Msg_Id;

        int Pk_total;

        int Pk_number;

        int Registered_Delivery;

        int Msg_level;

        String Service_Id;

        int Fee_UserType;

        String Fee_terminal_Id;

        int TP_pId;

        int TP_udhi;

        int Msg_Fmt;

        String Msg_src;

        String FeeType;

        String FeeCode;

        String ValId_Time;

        String At_Time;

        String Src_Id;

        int DestUsr_tl;

        String Dest_terminal_Id;

        int Msg_Length;

        String Msg_Content;

        String Reserve;
    }

}
