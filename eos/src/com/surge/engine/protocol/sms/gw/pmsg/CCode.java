package com.surge.engine.protocol.sms.gw.pmsg;

/**
 * Client �� SMS Server ֮��ʹ�õ�״̬����
 */
public class CCode
{

	public static final byte SUCESS = 0x01; // �ɹ�

	public static final byte ERR_SYS = 0x02; // ϵͳ�ڲ�����

	public static final byte ERR_PARAM = (byte) 0x88; // ��Ϣ���ݴ���

	public static final byte ERR_ACCOUNT = 0x12; // �˻�����

	public static final byte ERR_RIGHT = 0x14; // ���κο���Ȩ��

	public static final byte ERR_IP = 0x15; // ���Ǵ�ָ����IP����¼

	public static final byte ERR_EXIST = 0x16; // �Ѿ���¼

	public static final byte ERR_VERSION = 0x17; // �汾�Ŵ���

	public static final byte ERR_DATA = (byte) 0xFF; // ��������ݰ�

	public static final byte CLOSE_CNT = 0x00; // �˳�ʱ�ر�����

	public static final byte KEEP_CNT = 0x01; // �˳�ʱ��������

	public static final byte TEXT_MSG = 0x01; // �ı���Ϣ

	public static final byte ICON_MSG = 0x02; // ͼƬ��Ϣ

	public static final byte RING_MSG = 0x03; // ������Ϣ

	public static final byte BIN_MSG = 0x04; // ��������Ϣ

	public static final byte DIS_AGENT = 0x32; // Agent ���� ������ʧ��

	public static final byte DIS_SMSC = 0x33; // SMSC ���� ������ʧ��

	public static final byte DIS_ASMS = 0x34; // Agent �� SMSC ���� ������ʧ��

	public static final byte ERR_SUBMIT = 0x35; // �ύʧ��

	public static final byte ERR_FEE = 0x36; // ��������,���� MT

	public static final byte NO_MT_RIGHT = 0x37; // �� MT Ȩ��

	public static final byte NO_MT_ROUTE = 0x38; // �� ���õ�MTͨ��

	public static final byte HAVE_DEL = 0x51; // ��ɾ��

	public static final byte CANT_DEL = 0x52; // �޷�ɾ��

	public static final byte NO_DEL_RIGHT = 0x53; // ��Ȩ��ɾ��

	public static final byte UNKNOW = 0x60; // δ֪

	public static final byte IN_QUEUE = 0x61; // ��MSSC�Ķ����еȴ�����

	public static final byte HAVE_SENT = 0x62; // ���뿪SMSC(�ѷ���)

	public static final byte HAVE_DONE = 0x63; // �ѵ����û��ֻ�

	public static final byte ERR_EXEC = 0x72; // ִ�й��̷�������

	public static final byte TARGET_PAY = 0; // ����Ϣ�����߸���

	public static final byte SOURCE_PAY = 1; // ����Ϣ�����߸���

	public static final byte SP_PAY = 2; // ����Ϣ�����߸���

	public static final int CFREEMT = 0; // Client MT ��Ϣʱ���ܶ��û��շ�

	public static final int CMT = 1; // Client MT ���͵�����

	public static final int CMO = 2; // Client MO ���͵�����

	public static final int CBUF = 3; // Client MO �������͵�����

	private static final byte[][] db_code = { // �������ֵ�����ݿ�����Ӧֵ
	{ TEXT_MSG, (byte) 1 }, { ICON_MSG, (byte) 2 }, { RING_MSG, (byte) 3 }, { BIN_MSG, (byte) 4 },

	{ SUCESS, (byte) 1 }, { // MT �ɹ��ύ
			IN_QUEUE, (byte) 2 }, { // ����Ϣ��SMSC �ȴ�����
			HAVE_SENT, (byte) 3 }, { // ����Ϣ���뿪 SMSC ,������Ŀ�ĵ�
			HAVE_DONE, (byte) 4 }, { // ����Ϣ�ѵ���Ŀ�ĵ�
			HAVE_DEL, (byte) 5 }, { // ��ɾ��
			UNKNOW, (byte) 8 }, { // δ֪״̬
			ERR_SUBMIT, (byte) 0 }, // MT �ύʧ��
	};

}
