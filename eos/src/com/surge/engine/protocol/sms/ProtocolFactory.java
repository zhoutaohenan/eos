package com.surge.engine.protocol.sms;

import java.lang.reflect.Constructor;

import com.surge.communication.framework.Protocol;
import com.surge.communication.framework.conf.ProtocolConfig;
import com.surge.engine.protocol.sms.cmpp.CmppProtocol;
import com.surge.engine.protocol.sms.gw.service.GWProtocol;
import com.surge.engine.protocol.sms.sgip.SgipProtocol;
import com.surge.engine.protocol.sms.smgp.SmgpProtocol;
import com.surge.engine.sms.conf.Cmpp2Config;
import com.surge.engine.sms.conf.GwConfig;
import com.surge.engine.sms.conf.HttpConfig;
import com.surge.engine.sms.conf.SgipConfig;
import com.surge.engine.sms.conf.SmgpConfig;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class ProtocolFactory {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Protocol createProtocol(ProtocolConfig protocolConfig, SmsProtocolHandler protocolHandler) {
        Protocol protocol = null;
        if (protocolConfig instanceof Cmpp2Config) {
            protocol = new CmppProtocol(protocolConfig.getProtocolId(), (Cmpp2Config) protocolConfig, protocolHandler);
        }
        if (protocolConfig instanceof SgipConfig) {
            protocol = new SgipProtocol(protocolConfig.getProtocolId(), (SgipConfig) protocolConfig, protocolHandler);
        }
        if (protocolConfig instanceof GwConfig) {
            protocol = new GWProtocol(protocolConfig.getProtocolId(), (GwConfig) protocolConfig, protocolHandler);
        }
        if (protocolConfig instanceof SmgpConfig) {
            protocol = new SmgpProtocol(protocolConfig.getProtocolId(), (SmgpConfig) protocolConfig, protocolHandler);
        }
        if (protocolConfig instanceof HttpConfig) {
            HttpConfig config = (HttpConfig) protocolConfig;
            try {
                
                Class clazz = Class.forName(config.getProtocolURL());
                Class[] type = {String.class, HttpConfig.class, SmsProtocolHandler.class};
                Object[] value = {config.getProtocolId(), config, protocolHandler};
                Constructor<Protocol> constructor = clazz.getDeclaredConstructor(type);
                protocol = (Protocol) constructor.newInstance(value);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // protocol = new HttpProtocol(protocolConfig.getProtocolId(), (HttpConfig) protocolConfig,
            // protocolHandler);
        }
        return protocol;
    }
}
