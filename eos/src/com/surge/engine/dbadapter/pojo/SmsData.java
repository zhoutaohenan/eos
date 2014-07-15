package com.surge.engine.dbadapter.pojo;

/**
 * 数据库间转移数据用的值至象
 * 
 * @description
 * @project: esk2.0
 * @Date:2011-2-23
 * @version 1.0
 * @Company: 33e9
 * @author ztao
 */
public class SmsData {
    /**
     * ID
     */
    public String id;

    /**
     * 客户端短信ID
     */
    public String smsid;

    /**
     * 目的地址
     */
    public String destaddr;

    /**
     * 短信内容
     */
    public String message;

    /**
     * 是否需查状态报告
     */
    public int isneedreport;

    /**
     * 请求时间
     */
    public String requesttime;

    /**
     * 通道
     */
    public String channel;

    /**
     * 优先级
     */
    public int priorty;

    /**
     * 扩展码
     */
    public String excode;

    /**
     * 定时时间
     */
    public String presendtime;

    /**
     * 有效时间
     */
    public long validTime;

    /**
     * 创建时间
     */
    public String createtime;

    /**
     * 机构ID
     */
    public String orgid;

    /**
     * 用户ID
     */
    public String userid;

    /**
     * 用户签名
     */
    public String smssign;

    /**
     * 临时ID
     */
    public long tempid;

    /**
     * 拆分后单条短信内容
     */
    public String split_message;

    /**
     * 重发通道
     */
    public String resend_channel;

    /**
     * 响应标识
     */
    public int send_flag;

    /**
     * 发送时间
     */
    public String sendtime;

    /**
     * 状态报告状态
     */
    public String status;

    /**
     * 状态报告时间
     */
    public String reporttime;

    /**
     * 发送结果
     */
    public int send_ret;

    /**
     * 一条长短信总条数
     */
    public int total;

    /**
     * 当前是第几条
     */
    public int pknumber;

    /**
     * 是否需要重发
     */
    public int isneedresend;

    /**
     * 重发状态
     */
    public int sendstatus;

    /**
     * 消息ID
     */
    public String messageid;

    /**
     * 状态报告手描述
     */
    public String dec;

}
