package com.surge.engine.sms.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.surge.engine.util.XmlUtils;

public class SmsConfig extends Config {
    private static Logger logger = Logger.getLogger(SmsConfig.class);

    private Element root;

    private Element ismg;

    private int listenPort;

    private static final String CONFIG_FILE = "config/smsconfig.xml";

    private Map<String, Cmpp2Config> cmpp2Ismgs;

    private Map<String, SgipConfig> sgipIsmgs;

    // private Map<String, GwConfig> gwConfigIsmgs;

    private Map<String, SmgpConfig> smgpConfigIsmgs;
    private Map<String, HttpConfig> httpConfigs;
    private static SmsConfig instance;

    /** 已发队列大小 **/
    private static int sentSize = 5000;
    /** 响应等待队列大小 **/
    private static int respsSize = 30000;
    /** 状态报告等待队列大小 **/
    private static int reportsSize = 50000;

    /** 长短信保留时间，默认1分钟 **/
    private static int longMoTime = 1;

    protected SmsConfig() {
        super(CONFIG_FILE);
    }

    public void stop() {
        cmpp2Ismgs.clear();
        sgipIsmgs.clear();
        // gwConfigIsmgs.clear();
        smgpConfigIsmgs.clear();
        if (instance != null) {
            instance = null;
        }
    }

    public synchronized static SmsConfig getInstance() {
        if (instance == null) {
            instance = new SmsConfig();
        }
        return instance;
    }

    @Override
    protected void doGetConfig() {

        root = document.getRootElement();
        ismg = root.getChild("ismg");
        this.listenPort = XmlUtils.getInt(ismg, "listenPort");
        this.sentSize = XmlUtils.getInt(ismg, "sentSubmitSize");
        this.respsSize = XmlUtils.getInt(ismg, "respsQueueSize");
        this.reportsSize = XmlUtils.getInt(ismg, "reportsQueueSize");
        this.longMoTime = XmlUtils.getInt(ismg, "longMoTime");
        List cmpplist = ismg.getChildren("cmpp2");

        this.cmpp2Ismgs = new HashMap<String, Cmpp2Config>();

        try {
            for (Object tmp : cmpplist) {
                Cmpp2Config config = new Cmpp2Config(((Element) tmp));
                this.cmpp2Ismgs.put(config.getIsmgid(), config);
            }
        } catch (Exception e) {
            logger.error("加载短信服务配置出错:", e);
        }

        List sgiplist = ismg.getChildren("sgip");
        this.sgipIsmgs = new HashMap<String, SgipConfig>();

        try {
            for (Object tmp : sgiplist) {
                SgipConfig sgipConfig = new SgipConfig(((Element) tmp));
                this.sgipIsmgs.put(sgipConfig.getIsmgid(), sgipConfig);
            }
        } catch (Exception e) {
            logger.error("加载短信服务配置出错:", e);
        }
        // List gwList = ismg.getChildren("gw");
        // this.gwConfigIsmgs = new HashMap<String, GwConfig>();
        // try
        // {
        // for (Object tmp : gwList)
        // {
        // GwConfig gwConfig = new GwConfig(((Element) tmp));
        // this.gwConfigIsmgs.put(gwConfig.getProtocolId(), gwConfig);
        // }
        // } catch (Exception e)
        // {
        // logger.error("加载短信服务配置出错:", e);
        // }

        List smgpList = ismg.getChildren("smgp");
        this.smgpConfigIsmgs = new HashMap<String, SmgpConfig>();
        try {
            for (Object tmp : smgpList) {
                SmgpConfig smgpConfig = new SmgpConfig(((Element) tmp));
                this.smgpConfigIsmgs.put(smgpConfig.getProtocolId(), smgpConfig);
            }
        } catch (Exception e) {
            logger.error("加载短信服务配置出错:", e);
        }
        List httpList = ismg.getChildren("http");
        this.httpConfigs = new HashMap<String, HttpConfig>();
        try {
            for (Object tmp : httpList) {
                HttpConfig httpConfig = new HttpConfig(((Element) tmp));
                this.httpConfigs.put(httpConfig.getProtocolId(), httpConfig);
            }
        } catch (Exception e) {
            logger.error("加载短信服务配置出错:", e);
        }

    }

    public static void reLoadConfig() {
        instance = new SmsConfig();
    }

    public Map<String, Cmpp2Config> getCmpp2Ismgs() {
        return cmpp2Ismgs;
    }

    public void setCmpp2Ismgs(Map<String, Cmpp2Config> cmpp2Ismgs) {
        this.cmpp2Ismgs = cmpp2Ismgs;
    }

    public Map<String, SgipConfig> getSgip12Ismgs() {
        return sgipIsmgs;
    }

    public void setSgip12Ismgs(Map<String, SgipConfig> sgip12Ismgs) {
        this.sgipIsmgs = sgip12Ismgs;
    }

    // public Map<String, GwConfig> getGwConfigIsmgs()
    // {
    // return gwConfigIsmgs;
    // }
    //
    // public void setGwConfigIsmgs(Map<String, GwConfig> gwConfigIsmgs)
    // {
    // this.gwConfigIsmgs = gwConfigIsmgs;
    // }

    public Map<String, SmgpConfig> getSmgpConfigIsmgs() {
        return smgpConfigIsmgs;
    }

    public void setSmgpConfigIsmgs(Map<String, SmgpConfig> smgpConfigIsmgs) {
        this.smgpConfigIsmgs = smgpConfigIsmgs;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getSentSize() {
        return sentSize;
    }

    public void setSentSize(int sentSize) {
        this.sentSize = sentSize;
    }

    public int getRespsSize() {
        return respsSize;
    }

    public void setRespsSize(int respsSize) {
        this.respsSize = respsSize;
    }

    public int getReportsSize() {
        return reportsSize;
    }

    public void setReportsSize(int reportsSize) {
        this.reportsSize = reportsSize;
    }

    public static int getLongMoTime() {
        return longMoTime;
    }

    public Map<String, HttpConfig> getHttpConfigs() {
        return httpConfigs;
    }

    public void setHttpConfigs(Map<String, HttpConfig> httpConfigs) {
        this.httpConfigs = httpConfigs;
    }
}
