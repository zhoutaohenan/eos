package com.surge.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.surge.engine.util.DesEncrypter;
import com.surge.engine.util.TimeUtil;

public class HttpDemo implements Runnable {
    private void sendSms() throws Exception {
        String httpurl = "http://localhost:8080/EOSM/sendSmsHttp/sendSms.html";
        // String httpurl = "http://112.124.34.193:8080/EOSM/sendSmsHttp/sendPHPSms.html";
        // String httpurl = "http://112.124.34.193:8080/EOSM/sendSmsHttp/sendCSms.html";
        String[] paramName = {"userName", "password", "mobiles", "content"};
        String[] paramVal = {DesEncrypter.encrypt("cmpptest"), DesEncrypter.encrypt("cmpptest"), "18922858338", "有信网-电信测试"+TimeUtil.getDateTimeStr()};
       // String[] paramVal = {"BDD1032F6A35A18D", "0A6F789F156C16E7", "111", "测试仪133"};
        HttpURLConnection conn = null;
        try {
            URL url = new URL(httpurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Create the form content
            OutputStream out = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            for (int i = 0; i < paramName.length; i++) {
                writer.write(paramName[i]);
                writer.write("=");
                writer.write(URLEncoder.encode(paramVal[i], "UTF-8"));
                writer.write("&");
            }
            writer.close();
            out.close();
            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = rd.readLine();
            System.out.println(result);
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            conn.disconnect();
        }
    }

    @Override
    public void run() {
        try {
            this.sendSms();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
