package test.sms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.surge.engine.sms.pojo.SmsChannel;
import com.surge.engine.sms.pojo.SmsRequest;
import com.surge.engine.sms.service.SmsAgentService;
import com.surge.engine.sms.service.SmsAgentServiceImpl;
import com.surge.engine.sms.service.SmsManager;
import com.surge.engine.sms.service.SmsManagerImpl;
import com.surge.engine.util.TimeUtil;
import com.surge.engine.util.TimerPool;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-20
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class SendSms {

    @Test
    public void sendSms() {
        SmsAgentService smsAgent = new SmsAgentServiceImpl();
        SmsChannel smsChannel = smsAgent.getChannels().get("cmpp2_0");
        try {
            for (int i = 0; i < 100; i++) {
                if (i % 2000 == 0) {
                    Thread.sleep(2000);
                }
                SmsRequest smsRequest = new SmsRequest();
                smsRequest.setSmsId(String.valueOf(i));
                smsRequest.setNeedReport(true);
                List<String> destAddrs = new ArrayList<String>();
                destAddrs.add("13699998898");
                smsRequest.setDestAddrs(destAddrs);
                smsRequest.setMessage("你好.........");
                smsRequest.setPriority(9);
                smsRequest.setSendTime(TimeUtil.getDateTimeStr());
                smsRequest.setChannel(smsChannel);
                smsRequest.setValidTime(0);
                smsRequest.setCreateTime(TimeUtil.getDateTimeStr());
                // 若IS_RESEND字段为1,则表明是重发短信

                smsRequest.setRendSms(false);
                smsRequest.setMtId(String.valueOf(i));
                smsAgent.sendSms(smsRequest);
            }

            Thread.sleep(6000000);
        } catch (InterruptedException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    @BeforeMethod
    public void init() {
        // 初始化log4j配置
        PropertyConfigurator.configure("config/log4j.properties");
        TimerPool.init();

        // 初始话hibernate配置
        File f = new File("config/hibernate.cfg.xml");
        Configuration cfg = new Configuration().configure(f);

        SmsManager smsManager = new SmsManagerImpl(new SmsHandlerImplTest());
        smsManager.start();
    }

    public static void main(String[] args) {
        System.out.println(1 % 200);
    }
}
