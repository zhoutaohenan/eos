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
            // smsData.message = "����ר��SGIP��ʽ����ʱ��ͨ��˫����Ϊ�ͻ��˺ͷ������ˡ����ͻ���Ҫ��������ʱ��"
            // + "������������˽������ӣ�Ȼ����������˷������������Ӧ�𣻷������˴ӿͻ��˽����������Ӧ��" +
            // "���ӽ����Ժ󣬿ͻ��˿����������Ͷ������������겢���յ�����Ӧ��󣬿ͻ���Ӧ�������Ͽ����ӡ�" +
            // "���ǣ������Ӧ��֮���ʱ��������ܳ���30��(Ĭ�ϣ�������)" +
            // "���������Ӧ��ʱ�䳬�����ȴ�ʱ�䣬��Ҫ��ͻ����ط��������";
            // smsData.message =
            // "Asdfkasdfkasdfkasdfkasdfkxxxxxxxxxxxxxxxxxxasdfkasdfkasdfkasdfkasdfkasdfkxxxxxxxxxxxxxxxxasdfkasdfkasdfkasdfkasdfkasdfkxxxxxxxxxxxxxxxxxxxxxxxxasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkas";
            // smsData.message="AsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkAsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkAsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasd";
            // smsData.message =
            // "����ϵ��ҵʮ����˾�����չʾ����ϵ��ҵʮ����˾�����չʾ����ϵ��ҵʮ����˾�����չʾ����ϵ��ҵʮ����˾�����չʾ����ϵ��ҵʮ����˾�����չʾ����ϵ��ҵʮ����˾�����չʾ����ϵ��ҵʮ����˾�����չʾ";
            // System.out.println("length==" + smsData.message.length());
            // smsData.message =
            // "���ŷ���֣����ʾ�����ڳ���ð��˳��������ͻ��ͼ����󣬸���˾������ɲ���Ӱ�졣���ϸ��չ�˾������̶��˵����б������Ͻ����ͻ���Ϣ���޹���Ա͸¶��������ͻ���ʾע��ʶ����α���Է���ƭ�������̷��涨������й¶�ͻ���Ϣ�ɹ����ַ���ҵ���ܷ����߿ɴ�7����������ͽ�̡�";
            smsData.message = "��֤���⣬����£�����������[����]!";
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

            // smsData.userSign = "��";
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
            // smsData.message = "����ר��SGIP��ʽ����ʱ��ͨ��˫����Ϊ�ͻ��˺ͷ������ˡ����ͻ���Ҫ��������ʱ��"
            // + "������������˽������ӣ�Ȼ����������˷������������Ӧ�𣻷������˴ӿͻ��˽����������Ӧ��"
            // + "���ӽ����Ժ󣬿ͻ��˿����������Ͷ������������겢���յ�����Ӧ��󣬿ͻ���Ӧ�������Ͽ����ӡ�"
            // + "���ǣ������Ӧ��֮���ʱ��������ܳ���30��(Ĭ�ϣ�������)" +
            // "���������Ӧ��ʱ�䳬�����ȴ�ʱ�䣬��Ҫ��ͻ����ط��������";
            smsData.message = "Ϊʲô˵������ѿ�����ѧ�߿���û�����ָо���";// ���Ƿ������£���ʼ����������Ǻܳ�һ�δ��룬˵��Ҫ���Ĺ����ܶ࣬���ܶ๤��װ��һ�������У��൱�ڽ��ܶ༦������һ��������Ǻ�Σ�յģ���Ҳ���б���Java��������ԭ��";
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
            // smsData.userSign = "[����]";
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
            // smsData.message = "����ר��SGIP��ʽ����ʱ��ͨ��˫����Ϊ�ͻ��˺ͷ������ˡ����ͻ���Ҫ��������ʱ��"
            // +
            // "������������˽������ӣ�Ȼ����������˷������������Ӧ�𣻷������˴ӿͻ��˽����������Ӧ��" +
            // "���ӽ����Ժ󣬿ͻ��˿����������Ͷ������������겢���յ�����Ӧ��󣬿ͻ���Ӧ�������Ͽ����ӡ�" +
            // "���ǣ������Ӧ��֮���ʱ��������ܳ���30��(Ĭ�ϣ�������)" +
            // "���������Ӧ��ʱ�䳬�����ȴ�ʱ�䣬��Ҫ��ͻ����ط��������";
            try {
                smsData.message = new String("�й�����".getBytes(), "cp936");
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
            // smsData.message = "����ר��SGIP��ʽ����ʱ��ͨ��˫����Ϊ�ͻ��˺ͷ������ˡ����ͻ���Ҫ��������ʱ��"
            // + "������������˽������ӣ�Ȼ����������˷������������Ӧ�𣻷������˴ӿͻ��˽����������Ӧ��"
            // + "���ӽ����Ժ󣬿ͻ��˿����������Ͷ������������겢���յ�����Ӧ��󣬿ͻ���Ӧ�������Ͽ����ӡ�"
            // + "���ǣ������Ӧ��֮���ʱ��������ܳ���30��(Ĭ�ϣ�������)" +
            // "���������Ӧ��ʱ�䳬�����ȴ�ʱ�䣬��Ҫ��ͻ����ط��������";
            // try
            // {
            // smsData.message = new String(gbk2utf8("�й�����"), "UTF-8"); //
            // String.valueOf("�й�����".getBytes("utf8"));
            // } catch (UnsupportedEncodingException e1)
            // {
            // // TODO Auto-generated catch block
            // e1.printStackTrace();
            // }
            // smsData.message =
            // "��ô�������Ҫ�Ӳ�һ������������ʱ������Ǻ����������൱����Ȥ��ָ�����������һ�ʹ�õ���ʾ�������ݵ�ָ�����˵�˴�����˵��ô���������Ȥ��ָ����ô�������Ҫ�Ӳ�һ����87";
            // String temp=smsData.message.getBytes("utf-8");
            // smsData.message="dfdfdfdfdfdfdfdd";
            // smsData.message="������������������������������������������������������������������������������������������������";
            // smsData.message =
            // "ABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDE";
            smsData.message = "dfdsafasfa�й�g!";
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
            smsData.channel = "�㽭����01";
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
            // ����
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
     * ����statement���� PreparedStatement
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
        // ����SMS_MT_TASK��������
        PreparedStatement pstmt = null;
        // ɾ���Ѵ�SMS_MT���ж�����¼ Ԥ�������
        PreparedStatement deleteps = null;
        // ����MT��Ϣ����
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
                    System.out.println("����100��...");
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
        // "�µ�4008205555�ͻ������������뿪ͨ���������л��ڵ��ڻ����յ������ָ����һ��ͨ�����˻������������ÿ�ת�˻��ת�˻��ת�˻�����");
        // pstmt.setString(j++, "");
        // pstmt.addBatch();
        // if (p >= 10000)
        // {
        // pstmt.executeBatch();
        // conn.commit();
        // pstmt.clearBatch();
        // p = 0;
        // //
        // System.out.println("����"+i+"����ʱ��"+String.valueOf(System.currentTimeMillis()-begin));
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
        // System.out.println("�ܼ���ʱ��" +
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
            String url = "jdbc:oracle:thin:@10.0.11.190:1521:devdb"; // orclΪ���ݿ��SID
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
            String url = "jdbc:mysql://10.0.11.194:3306/esk2"; // orclΪ���ݿ��SID
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
            // String url = "jdbc:sqlserver://10.0.11.186:1433;DatabaseName=qxw1"; // orclΪ���ݿ��SID
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
