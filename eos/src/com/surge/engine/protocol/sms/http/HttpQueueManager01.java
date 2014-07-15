package com.surge.engine.protocol.sms.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.surge.engine.protocol.sms.http.psmg.HttpSubmit;

public class HttpQueueManager01 {

    private static HttpQueueManager01 instance = new HttpQueueManager01();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<HttpSubmit>> httpSendQueeu = new ConcurrentHashMap<String, ConcurrentLinkedQueue<HttpSubmit>>();

    public void addSmsToQueue(HttpSubmit submit) {
        ConcurrentLinkedQueue<HttpSubmit> temp = httpSendQueeu.get(submit.getSmsId());
        if (temp == null) {
            temp = new ConcurrentLinkedQueue<HttpSubmit>();
            httpSendQueeu.put(submit.getSmsId(), temp);
        }
        temp.add(submit);

    }

    private HttpQueueManager01() {

    }

    public static HttpQueueManager01 getInstance() {
        return instance;
    }

    public List<HttpSubmit> getRemoveHttpSubmit() {
        List<HttpSubmit> list = new ArrayList<HttpSubmit>();
        Set<String> keys = httpSendQueeu.keySet();
        for (String key : keys) {
            ConcurrentLinkedQueue<HttpSubmit> temp = httpSendQueeu.get(key);
            if (temp.size() <= 0) {
                httpSendQueeu.remove(key);
                continue;
            }
            for (HttpSubmit submit : temp) {
                if (list.size() > 500) {
                    return list;
                } else {
                    temp.remove(submit);// 移除掉队列中的submit包
                    list.add(submit);
                }
            }
            break;
        }
        return list;

    }
}
