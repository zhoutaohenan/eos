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
                    eskPlatform.stop(); // 停止服务
                    System.exit(0);
                } else if (str != null && str.equalsIgnoreCase("set debug true")) {
                    // 开通调试日志
                    EskLog.setLogLevel(EskLog.DEBUG);
                } else if (str != null && str.equalsIgnoreCase("set debug false")) {
                    // 关闭调试日志
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
        // jvm退出钩子
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));

        // 初始化log4j配置
        PropertyConfigurator.configure("config/log4j.properties");
        logger = Logger.getLogger(EskPlatform.class);

        TimerPool.init();
        FixTimePool.init();

        // 加载数据库类型配置信息
        DbConfig dbconfig = DbConfig.instance;
        MobileErrorUitl errorUitl = MobileErrorUitl.mobileErrorUitl;

        try {
            errorUitl.initConfigParam();
            dbconfig.init();
        } catch (Exception e) {
            logger.error("加载数据库配置信息失败,系统退出", e);
            System.exit(0);
        }

        /** 从数据库中读取数据，初始化系统参数 **/
        InitSystemRunnable inintRunnable = new InitSystemRunnable();
        /** 先初始化系统参数 **/
        inintRunnable.init();
        /** 初始黑名单 **/
        inintRunnable.initBlackMobile();
        FixTimePool.schedule(inintRunnable, TimeUtil.getFixTime(), 24 * 60 * 60);

        smsManager = new SmsManagerImpl(new SmsHandlerImpl());
        smsManager.start();
        dbAdapterManager = new DbAdapterManagerImpl(new SmsAgentServiceImpl());
        dbAdapterManager.start();
        logger.info("启动EskPlatform成功");

        ThreadMonitor monitor = new ThreadMonitor();
        monitor.start();
        logger.info("启动监控线程成功");

        RestartAdapter statAdapter = new RestartAdapter(dbAdapterManager, smsManager);
        SenseFileModify fileModify = new SenseFileModify(statAdapter);
        FixTimePool.schedule(fileModify, 10, 10);

        // 同步通道信息到数据库
        InitSmsChannel.initChannel();
        // 启动通道状态统计侦听接口
        StandaloneServer.instance.start();
        // 启动清除历史数据定时器
        BackRecordsRunnable recordsRunnable = new BackRecordsRunnable();
        // FixTimePool.schedule(recordsRunnable, 10, 24 * 60 * 60);
        FixTimePool.schedule(recordsRunnable, TimeUtil.getFixTime(), 24 * 60 * 60);
        // 发送统计
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
