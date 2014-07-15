package com.surge.engine.comm;

import java.util.HashSet;
import java.util.Set;

public class SysCommonQueue {

    private static SysCommonQueue sysCommonQueue = new SysCommonQueue();

    // 因没有并发情况，无须用线程安全队列　
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
     * 检查是否是黑名单,是:true
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
