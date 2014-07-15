package com.surge.engine.comm;

import java.util.HashSet;
import java.util.Set;

public class SysCommonQueue {

    private static SysCommonQueue sysCommonQueue = new SysCommonQueue();

    // ��û�в���������������̰߳�ȫ���С�
    private Set<String> blackMobileSet = new HashSet<String>();

    private SysCommonQueue() {

    }

    public static SysCommonQueue getInstance() {
        return sysCommonQueue;
    }

    public void add(String mobile) {
        this.blackMobileSet.add(mobile);
    }

    /**
     * ����Ƿ��Ǻ�����,��:true
     * 
     * @param mobile
     * @return
     */
    public boolean isBlackMobile(String mobile) {
        boolean result = false;
        for (String m : blackMobileSet) {
            if (m.equalsIgnoreCase(mobile))
                result = true;
        }
        return result;
    }
}
