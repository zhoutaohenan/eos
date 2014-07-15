package com.surge.engine.sms.common;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-18
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public enum SmsErrCode {
    /** 手机号码非法 */
    INVALID_MOBILE(1000006),
    /** 已过有效期 */
    INVALID_TIME(1000008),

    /** 禁止发送时段 */
    NOTALLOWSEND_MOBILE(1000007),

    /** 十分钟未收到响应 **/
    NO_RESPONSE_ERROR(1000009),
    /** 网关连接中断超过2分钟 */
    CONNSTATUSIAGW_ERROR(1000012),
    /** 没有路由到发送通道 **/
    NO_SEND_CHANNEL(10000011),

    /** 通道状态不正常 */
    CHANNEL_STATUS_ERROR(1000013),

    /** 写入队列失败 */
    WRITE_QUEUE_ERROR(1000014),
    /** 短信过有效期 **/
    SMS_INVALIDATION_ERROR(1000015),
    /** 终端问题或是黑名单 **/
    USER_MOBILE_ERROR(1000016);

    private int value;

    SmsErrCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
