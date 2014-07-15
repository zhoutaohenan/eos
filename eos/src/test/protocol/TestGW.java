package test.protocol;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.surge.engine.protocol.sms.gw.pmsg.CFeeReq;
import com.surge.engine.protocol.sms.gw.pmsg.CSendReq;
import com.surge.engine.protocol.sms.gw.service.GWProtocol;
import com.surge.engine.sms.conf.GwConfig;
import com.surge.engine.sms.conf.SmsConfig;
import com.surge.engine.util.TimerPool;

public class TestGW {
    private static final Logger logger = Logger.getLogger(TestGW.class);

    @Test
    public void testLogin() {
        GwConfig config = this.init();
        GWProtocol protocol = new GWProtocol("gw_0", config, null);
        TimerPool.init();
        protocol.start();
        // queryFee(protocol, config);
        sendSms(protocol, config);
        try {
            Thread.currentThread().sleep(8888888 * 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 发送短信 TODO
     * 
     * @param protocol void
     * @throws
     */
    private void sendSms(GWProtocol protocol, GwConfig config) {
        int result = protocol.sendPMessage(getSubmit(config));
        if (result != 0)

        {
            try {
                Thread.currentThread().sleep(3 * 1000);
                protocol.sendPMessage(getSubmit(config));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询短信费用 TODO
     * 
     * @param protocol
     * @param config void
     * @throws
     */
    private void queryFee(GWProtocol protocol, GwConfig config) {
        int result = protocol.sendPMessage(getFeeReq(config));
        if (result != 0)

        {
            try {
                logger.info("费用查询结果,result:" + result);
                Thread.currentThread().sleep(3 * 1000);
                protocol.sendPMessage(getFeeReq(config));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建短信费用包 TODO
     * 
     * @param config
     * @return CFeeReq
     * @throws
     */
    private CFeeReq getFeeReq(GwConfig config) {

        CFeeReq feeReq = new CFeeReq(config.getUserName(), null);
        feeReq.setInfo("20100806", "20100812");
        return feeReq;
    }

    /**
     * 构建短信包 TODO
     * 
     * @param config
     * @return CSendReq
     * @throws
     */
    private CSendReq getSubmit(GwConfig config) {
        // String spNumber=config.gets;
        // SgipSubmit submit = new SgipSubmit(1008L, config.getSpCode(),
        // chargeNumber, userNumber,
        // corpId, config.getServiceid(), (byte) config.getFeeType(),
        // config.getFeeValue(),
        // config.getGivenValue(), (byte) config.getAgentFlag(), (byte) 2,
        // (byte) 0, "", "",
        // (byte) 1, (byte) 0, (byte) 0, (byte) 15, (byte) 0,
        // content.getBytes());
        String test = "测试";
        String mySing = "surge三";
        byte mySinByte[] = mySing.getBytes();
        byte message[] = test.getBytes();
        String tUser = config.getUserName();
        String tFrom = "15013420678";
        String tTo = "15013420678";
        CSendReq sendReq = new CSendReq(tUser, tFrom, tTo, 1);
        sendReq.bFeeUser = 2;
        sendReq.sFeePhone = "15013420678";
        sendReq.sService = "SURGESMS";
        sendReq.sFeeType = "02";
        sendReq.iFee = 5;
        sendReq.bFormat = 1;
        sendReq.iLength = message.length;
        sendReq.bMessage = message;
        sendReq.bSign = mySinByte;
        sendReq.iSign = mySinByte.length;
        return sendReq;
    }

    /**
     * 初始化配置信息 TODO
     * 
     * @return GwConfig
     * @throws
     */
    private GwConfig init() {
        SmsConfig config = SmsConfig.getInstance();
        // GwConfig gwconfig = config.getGwConfigIsmgs().get("gw_0");

        // SgipConfig config = new SgipConfig();
        // config.setConnCount(1);
        // config.setIp("10.0.30.53");
        // config.setPort(8801);
        // config.setListenPort(8802);
        // config.setProtocolId("sgip");
        // config.setConnCount(1);
        // return config;
        return null;
    }
}
