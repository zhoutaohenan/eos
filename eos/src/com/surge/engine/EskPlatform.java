package com.surge.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.surge.engine.dbadapter.SmsHandlerImpl;
import com.surge.engine.dbadapter.config.DbConfig;
import com.surge.engine.dbadapter.runnable.InitSmsChannel;
import com.surge.engine.dbadapter.service.DbAdapterManager;
import com.surge.engine.dbadapter.service.DbAdapterManagerImpl;
import com.surge.engine.dbadapter.util.FixTimePool;
import com.surge.engine.monitor.RestartAdapter;
import com.surge.engine.monitor.SenseFileModify;
import com.surge.engine.monitor.thread.InitSystemRunnable;
import com.surge.engine.server.StandaloneServer;
import com.surge.engine.sms.service.SmsAgentServiceImpl;
import com.surge.engine.sms.service.SmsManagerImpl;
import com.surge.engine.util.EskLog;
import com.surge.engine.util.MobileErrorUitl;
import com.surge.engine.util.ShutdownHook;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.TimerPool;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-9
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class EskPlatform {
    private static Logger logger = null;

    private SmsManagerImpl smsManager = null;

    private DbAdapterManager dbAdapterManager = null;

    public static void main(String[] args) {
        EskPlatform eskPlatform = new EskPlatform();
        eskPlatform.start();
        while (true) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String str;
                str = br.readLine();
                logger.info("received command \"" + str + "\" from console.");

                if (str != null && str.equalsIgnoreCase("exit")) {
                    eskPlatform.stop(); // ֹͣ����
                    System.exit(0);
                } else if (str != null && str.equalsIgnoreCase("set debug true")) {
                    // ��ͨ������־
                    EskLog.setLogLevel(EskLog.DEBUG);
                } else if (str != null && str.equalsIgnoreCase("set debug false")) {
                    // �رյ�����־
                    EskLog.setLogLevel(EskLog.INFO);
                } else {
                    String info = "the right command of stopping this service is \"exit\". ignore bad command \"" + str
                                  + "\".";
                    logger.info(info);
                }
            } catch (Exception e) {
                eskPlatform.stop();
                e.printStackTrace();
            }
        }
    }

    public void start() {
        // jvm�˳�����
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));

        // ��ʼ��log4j����
        PropertyConfigurator.configure("config/log4j.properties");
        logger = Logger.getLogger(EskPlatform.class);

        TimerPool.init();
        FixTimePool.init();

        // �������ݿ�����������Ϣ
        DbConfig dbconfig = DbConfig.instance;
        MobileErrorUitl errorUitl = MobileErrorUitl.mobileErrorUitl;

        try {
            errorUitl.initConfigParam();
            dbconfig.init();
        } catch (Exception e) {
            logger.error("�������ݿ�������Ϣʧ��,ϵͳ�˳�", e);
            System.exit(0);
        }

        /** �����ݿ��ж�ȡ���ݣ���ʼ��ϵͳ���� **/
        InitSystemRunnable inintRunnable = new InitSystemRunnable();
        /** �ȳ�ʼ��ϵͳ���� **/
        inintRunnable.init();
        /** ��ʼ������ **/
        inintRunnable.initBlackMobile();
        FixTimePool.schedule(inintRunnable, TimeUtil.getFixTime(), 24 * 60 * 60);

        smsManager = new SmsManagerImpl(new SmsHandlerImpl());
        smsManager.start();
        dbAdapterManager = new DbAdapterManagerImpl(new SmsAgentServiceImpl());
        dbAdapterManager.start();
        logger.info("����EskPlatform�ɹ�");

        ThreadMonitor monitor = new ThreadMonitor();
        monitor.start();
        logger.info("��������̳߳ɹ�");

        RestartAdapter statAdapter = new RestartAdapter(dbAdapterManager, smsManager);
        SenseFileModify fileModify = new SenseFileModify(statAdapter);
        FixTimePool.schedule(fileModify, 10, 10);

        // ͬ��ͨ����Ϣ�����ݿ�
        InitSmsChannel.initChannel();
        // ����ͨ��״̬ͳ�������ӿ�
        StandaloneServer.instance.start();
        // ���������ʷ���ݶ�ʱ��
        BackRecordsRunnable recordsRunnable = new BackRecordsRunnable();
        // FixTimePool.schedule(recordsRunnable, 10, 24 * 60 * 60);
        FixTimePool.schedule(recordsRunnable, TimeUtil.getFixTime(), 24 * 60 * 60);
        // ����ͳ��
        SendStatRunnable sendStatRunnable = new SendStatRunnable();
        FixTimePool.schedule(sendStatRunnable, TimeUtil.getFixTime(), 24 * 60 * 60);
        // FixTimePool.schedule(sendStatRunnable, 0, 24 * 60 * 60);
        
        FixTimePool.schedule(new HttpDemo(), 60*60,60*60);
    }

    public void stop() {
        if (smsManager != null) {
            smsManager.stop();
        }
        if (dbAdapterManager != null) {
            dbAdapterManager.stop();
        }
        StandaloneServer.instance.stop();
        TimerPool.destroy();
        FixTimePool.destroy();
        LogManager.shutdown();
        System.exit(0);
    }

}
