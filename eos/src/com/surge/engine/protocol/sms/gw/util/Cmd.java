package com.surge.engine.protocol.sms.gw.util;

/**
 * ָ�������
 * C_ Ϊ�� Client ��ص�ָ��
 * A_ Ϊ�� Agent ��ص�ָ��
 */
public class Cmd{

    public static final byte C_LOGIN=0x01;  //��¼
    public static final byte C_LOGOUT=0x02; //�˳�
    public static final byte C_LINK=0x03;   //��·���
    public static final byte C_SEND=0x04;   //���Ͷ���
    public static final byte C_MO=0x05;     //Client ���� MO ��Ϣ
    public static final byte C_CANCEL=0x06; //ȡ������
    public static final byte C_STATUS=0x07; //���ͽ����ѯ
    public static final byte C_FEE=0x08;    //���ò�ѯ
    public static final byte C_AGENT=0x09;  //���õ� SMSC ��ѯ
    public static final byte C_PASSWD=0x10; //�޸�����
    public static final byte C_SENDCLUSTER=0x11;//Ⱥ�����š�
    public static final byte C_SENDTIMER=0x12;//��ʱ���š�
    public static final byte C_CANCELTIMER=0x13;//ȡ����ʱ���š�
    public static final byte C_REPORT=0x14;//״̬���� //--------------------- anny 2003.12.09
    public static final byte T_QueryClient=0x15;//
    public static final byte T_ForceLogout=0x16;//
    public static final byte C_SENDPROMPT=0x17;//���ٷ���

    public static final byte A_LOGIN=0x01;  //��¼
    public static final byte A_LOGOUT=0x02; //�˳�
    public static final byte A_LINK=0x03;   //��·���
    public static final byte A_SEND=0x04;   //���Ͷ���
    public static final byte A_CANCEL=0x05; //ȡ������
    public static final byte A_STATUS=0x06; //���ͽ����ѯ
    public static final byte A_MO=0x07;     //���� MO ��Ϣ
    public static final byte A_LOG=0x08;    //���� Agent �ķ�����־
    public static final byte A_SENDCLUSTER=0x09;//Ⱥ�����š�

}
