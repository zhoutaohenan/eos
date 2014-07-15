package com.surge.engine.protocol.sms.gw.util;

/**
 * 指令常数定义
 * C_ 为与 Client 相关的指令
 * A_ 为与 Agent 相关的指令
 */
public class Cmd{

    public static final byte C_LOGIN=0x01;  //登录
    public static final byte C_LOGOUT=0x02; //退出
    public static final byte C_LINK=0x03;   //连路检查
    public static final byte C_SEND=0x04;   //发送短信
    public static final byte C_MO=0x05;     //Client 接收 MO 信息
    public static final byte C_CANCEL=0x06; //取消发送
    public static final byte C_STATUS=0x07; //发送结果查询
    public static final byte C_FEE=0x08;    //费用查询
    public static final byte C_AGENT=0x09;  //可用的 SMSC 查询
    public static final byte C_PASSWD=0x10; //修改密码
    public static final byte C_SENDCLUSTER=0x11;//群发短信。
    public static final byte C_SENDTIMER=0x12;//定时短信。
    public static final byte C_CANCELTIMER=0x13;//取消定时短信。
    public static final byte C_REPORT=0x14;//状态报告 //--------------------- anny 2003.12.09
    public static final byte T_QueryClient=0x15;//
    public static final byte T_ForceLogout=0x16;//
    public static final byte C_SENDPROMPT=0x17;//快速发送

    public static final byte A_LOGIN=0x01;  //登录
    public static final byte A_LOGOUT=0x02; //退出
    public static final byte A_LINK=0x03;   //连路检查
    public static final byte A_SEND=0x04;   //发送短信
    public static final byte A_CANCEL=0x05; //取消发送
    public static final byte A_STATUS=0x06; //发送结果查询
    public static final byte A_MO=0x07;     //接收 MO 信息
    public static final byte A_LOG=0x08;    //接收 Agent 的发送日志
    public static final byte A_SENDCLUSTER=0x09;//群发短信。

}
