package com.surge.engine.protocol.sms.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.surge.communication.framework.AbstractProtocol;
import com.surge.communication.framework.common.PMessage;
import com.surge.engine.protocol.sms.SmsProtocolHandler;
import com.surge.engine.protocol.sms.http.psmg.HttpSubmit;
import com.surge.engine.protocol.sms.pojo.Receipt;
import com.surge.engine.protocol.sms.pojo.Response;
import com.surge.engine.sms.conf.HttpConfig;
import com.surge.engine.util.TimerPool;
import com.surge.engine.util.XmlUtils;

public class HttpProtocol extends AbstractProtocol {
    private static final Logger logger = Logger.getLogger(HttpProtocol.class);
    private HttpConfig config;
    private SmsProtocolHandler protocolHandler;
    private HttpQueueManager instance = HttpQueueManager.getInstance();

    // private MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    public HttpProtocol(String protocolId, HttpConfig config, SmsProtocolHandler protocolHandler) {

        super(protocolId);
        this.config = config;
        this.protocolHandler = protocolHandler;

    }

    @Override
    public int sendPMessage(PMessage pMessage) {
        instance.addSmsToQueue((HttpSubmit) pMessage);
        return 0;
    }

    @Override
    public boolean start() {
        logger.info(">>>>>" + config.getProtocolId() + "通道启动成功");
        TimerPool.schedule(new SendWork(), 10, 10);
        TimerPool.schedule(new ReportWork(), 10, 60);
        return true;
    }

    @Override
    public void stop() {

    }

    class SendWork implements Runnable {
        private void send() {
            HttpSubmit listSubmits = instance.getRemoveHttpSubmit(config.getIsmgId());
            if (listSubmits!=null) {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                StringBuilder builder = new StringBuilder();
               // StringBuilder builderMobiles = new StringBuilder();
                //String content = listSubmits.get(0).getContnet();
                //String mobiles = null;
                /*for (HttpSubmit submit : listSubmits) {
                    builderMobiles.append(submit.getMobile()).append(",");
                }*/
               // mobiles = builderMobiles.toString();
               // mobiles = mobiles.substring(0, mobiles.lastIndexOf(","));
                builder.append(config.getUrl()).append("?");
                // // 拼接帐号
                // builder.append(config.getNameKey()).append("=").append(config.getName());
                // // 拼接密码
                // builder.append("&").append(config.getPasswordKey()).append("=").append(config.getPassword());
                // builder.append("&").append(config.getMobileKey()).append("=").append(mobiles);
                // // 拼接内容
                // builder.append("&").append(config.getMessageKey()).append("=").append(content);

                InputStream in = null;
                InputStreamReader isr = null;
                BufferedReader br = null;
                HttpGet httpget = null;
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair(config.getNameKey(), config.getName()));
                    params.add(new BasicNameValuePair(config.getPasswordKey(), config.getPassword()));
                    params.add(new BasicNameValuePair(config.getMobileKey(), listSubmits.getMobile()));
                    params.add(new BasicNameValuePair(config.getMessageKey(), listSubmits.getContnet()));
                    String queryString = URLEncodedUtils.format(params, "gb2312");
                    builder.append(queryString);
                    // 拼接号码
                    // logger.info(">>>>>" + builder.toString());
                    httpget = new HttpGet(builder.toString());
                    HttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = null;
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        entity = response.getEntity();
                    } else {
//                        for (HttpSubmit submit : listSubmits) {
                            Response smsResponse = new Response(listSubmits.getSeqId(), 1, "", "发送失败" + status);
                            protocolHandler.doResponse(HttpProtocol.this.protocolId, smsResponse);
//                        }
                        logger.error("http请求发送短信失败,请求结果:status=" + status);
                    }
                    if (entity != null) {
                        in = entity.getContent();
                        isr = new InputStreamReader(in);
                        br = new BufferedReader(isr);
                        String line = null;
                        while (true) {
                            line = br.readLine();
                            if (line == null || "".equals(line)) {
                                continue;
                            }
                            break;
                        }
                        int result = Integer.parseInt(line);
                        if (result > 0) {
//                            for (HttpSubmit submit : listSubmits) {
                                Response smsResponse = new Response(listSubmits.getSeqId(), 0, line, "提交成功");
                                protocolHandler.doResponse(HttpProtocol.this.protocolId, smsResponse);
//                            }

                        } else {
                            logger.info("http发送生信失败:" + line);
//                            for (HttpSubmit submit : listSubmits) {
                                Response smsResponse = new Response(listSubmits.getSeqId(), 1, "", "发送失败" + line);
                                protocolHandler.doResponse(HttpProtocol.this.protocolId, smsResponse);
//                            }

                        }

                    }

                } catch (ClientProtocolException e) {
                    logger.error(e.getMessage(), e);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    httpget.releaseConnection();
                    try {
                        if (br != null) {
                            br.close();
                        }
                        if (isr != null) {
                            isr.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        httpclient.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }

        @Override
        public void run() {
            this.send();
        }

    }

    class ReportWork implements Runnable {

        @Override
        public void run() {
            getReport();
        }

        private void getReport() {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            StringBuilder builder = new StringBuilder();
            builder.append(config.getReportURL()).append("?");
            // 拼接帐号
            builder.append(config.getNameKey()).append("=").append(config.getName());
            // 拼接密码
            builder.append("&").append(config.getPasswordKey()).append("=").append(config.getPassword());
            // logger.info(builder.toString());
            HttpGet httpget = new HttpGet(builder.toString());
            InputStream in = null;
            try {

                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = null;
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    entity = response.getEntity();
                } else {
                    logger.error("http请求发送短信失败,请求结果:status=" + status);
                }
                if (entity != null) {
                    in = entity.getContent();
                    Document doc = this.getDocument(in);
                    Element root = doc.getRootElement();
                    Element reportElement = root.getChild("Report");// 若没有返回null
                    if (reportElement == null) {
                        int errorNo = XmlUtils.getInt(root, "ErrorNo");
                        String errorInfo = XmlUtils.getString(root, "ErrorInfo");
                        logger.info("查询http接口状态报告失败,原因=" + errorInfo + ",errorNoe" + errorNo);
                    } else {
                        List childrenList = root.getChildren();
                        for (int i = 0; i < childrenList.size(); i++) {
                            Element temp = (Element) childrenList.get(i);
                            String msgID = XmlUtils.getString(temp, "MsgID");
                            String mobile = XmlUtils.getString(temp, "Mobile");
                            String reportstatus = XmlUtils.getString(temp, "Status");
                            Receipt report = new Receipt();
                            report.setMessageId(msgID);
                            report.setMobile(mobile);
                            report.setState(reportstatus);
                            if("DELIVRD".equalsIgnoreCase(reportstatus))
                            {
                                report.setResult(0);
                            }
                            else
                            {
                                report.setResult(1);
                            }
                            protocolHandler.doReceipt(HttpProtocol.this.protocolId, report);
                        }
                    }

                }
            } catch (JDOMException e) {

            } catch (IOException e) {

            } catch (Exception e) {
                logger.error(e.getMessage(), e);

            } finally {
                httpget.releaseConnection();
                try {

                    if (in != null) {
                        in.close();
                    }
                    httpclient.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        private Document getDocument(InputStream is) throws JDOMException, IOException {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(is);
            return document;

        }

    }

}
