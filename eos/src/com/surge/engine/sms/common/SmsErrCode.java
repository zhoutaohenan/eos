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
    /** �ֻ�����Ƿ� */
    INVALID_MOBILE(1000006),
    /** �ѹ���Ч�� */
    INVALID_TIME(1000008),

    /** ��ֹ����ʱ�� */
    NOTALLOWSEND_MOBILE(1000007),

    /** ʮ����δ�յ���Ӧ **/
    NO_RESPONSE_ERROR(1000009),
    /** ���������жϳ���2���� */
    CONNSTATUSIAGW_ERROR(1000012),
    /** û��·�ɵ�����ͨ�� **/
    NO_SEND_CHANNEL(10000011),

    /** ͨ��״̬������ */
    CHANNEL_STATUS_ERROR(1000013),

    /** д�����ʧ�� */
    WRITE_QUEUE_ERROR(1000014),
    /** ���Ź���Ч�� **/
    SMS_INVALIDATION_ERROR(1000015),
    /** �ն�������Ǻ����� **/
    USER_MOBILE_ERROR(1000016);

    private int value;

    SmsErrCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
