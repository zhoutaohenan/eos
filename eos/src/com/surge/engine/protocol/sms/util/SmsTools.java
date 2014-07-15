package com.surge.engine.protocol.sms.util;

import java.util.HashMap;
import java.util.Map;

import com.surge.engine.sms.common.SmsErrCode;
import com.surge.engine.util.MobileErrorUitl;

public class SmsTools
{

	/**
	 * keyΪstat,valueΪReportMsg
	 */
	private static Map<String, ReportMsg> reportMap = new HashMap(1001);

	private static MobileErrorUitl mobileErrorUitl = MobileErrorUitl.mobileErrorUitl;

	static
	{
		int base = 1000001;

		ReportMsg report = new ReportMsg(0, "�û��ɹ�����");
		reportMap.put("DELIVRD", report);
		report = new ReportMsg(base++, "��Ϊ�û���ʱ��ػ����߲��ڷ������ȵ��µĶ���Ϣ��ʱû�еݽ����û��ֻ���");
		reportMap.put("EXPIRED", report);
		report = new ReportMsg(base++, "��Ϣ�Ѿ���ɾ��");
		reportMap.put("DELETED", report);
		report = new ReportMsg(base++, "�û���Ϊ״̬����ȷ�紦��ͣ���������״̬�����µ��û��޷����ܷ���");
		reportMap.put("UNDELIV", report);
		report = new ReportMsg(base++, "��Ϣ���ڱ�����״̬");
		reportMap.put("ACCEPTD", report);
		report = new ReportMsg(base++, "��Ϣ����δ֪״̬");
		reportMap.put("UNKNOWN", report);
		report = new ReportMsg(base++, "��Ϣ��ΪĳЩԭ�򱻾ܾ�");
		reportMap.put("REJECTD", report);
		report = new ReportMsg(base++, "����·��ʧ��");
		reportMap.put("NOROUTE", report);
		report = new ReportMsg(base++, "SCP��������Ӧ��Ϣʱ��״̬����");
		reportMap.put("CA:", report);
		report = new ReportMsg(base++, "��δ��������");
		reportMap.put("CA:0051", report);
		report = new ReportMsg(base++, "��δ�ɹ���¼");
		reportMap.put("CA:0052", report);
		report = new ReportMsg(base++, "������Ϣʧ��");
		reportMap.put("CA:0053", report);
		report = new ReportMsg(base++, "��ʱδ���յ���Ӧ��Ϣ");
		reportMap.put("CA:0054", report);
		report = new ReportMsg(base++, "SCP���ش�����Ӧ��Ϣʱ��״̬����");
		reportMap.put("CB:", report);
		report = new ReportMsg(base++, "��������Ԥ�����û�");
		reportMap.put("CB:0001:", report);
		report = new ReportMsg(base++, "���ݿ����ʧ��");
		reportMap.put("CB:0002", report);
		report = new ReportMsg(base++, "��Ȩʧ��");
		reportMap.put("CB:0003", report);
		report = new ReportMsg(base++, "�������������");
		reportMap.put("CB:0004", report);
		report = new ReportMsg(base++, "�û�״̬�쳣(����δͷ��ʹ�á���ֵ������������ֵ�����뱣���ڡ���ֵ����ʧ)");
		reportMap.put("CB:0005:", report);
		report = new ReportMsg(base++, "����û�м���");
		reportMap.put("CB:0006", report);
		report = new ReportMsg(base++, "�û�����");
		reportMap.put("CB:0007", report);
		report = new ReportMsg(base++, "�������·�������");
		reportMap.put("CB:0008", report);
		report = new ReportMsg(base++, "�������Ƿ�Ѷ�");
		reportMap.put("CB:0009", report);
		report = new ReportMsg(base++, "�û���ע��������");
		reportMap.put("CB:0010", report);
		report = new ReportMsg(base++, "�û�û��ע��������");
		reportMap.put("CB:0011", report);
		report = new ReportMsg(base++, "δ�Ǽǵ�����");
		reportMap.put("CB:0014", report);
		report = new ReportMsg(base++, "���ص�¼ժҪ����");
		reportMap.put("CB:0015", report);
		report = new ReportMsg(base++, "��������");
		reportMap.put("CB:0016", report);
		report = new ReportMsg(base++, "������������δ����");
		reportMap.put("CB:0017", report);
		report = new ReportMsg(base++, "�ظ�������Ϣ���к�msgid��ͬ�ļƷ�������Ϣ");
		reportMap.put("CB:0018", report);
		report = new ReportMsg(base++, "δ֪����");
		reportMap.put("CB:0020", report);
		report = new ReportMsg(base++, "���ݿ����");
		reportMap.put("CB:0021", report);
		report = new ReportMsg(base++, "SCP����ʧ��");
		reportMap.put("CB:0022", report);
		report = new ReportMsg(base++, "��ֵԽ��");
		reportMap.put("CB:0023", report);
		report = new ReportMsg(base++, "�ֶγ���");
		reportMap.put("CB:0024", report);
		report = new ReportMsg(base++, "���������");
		reportMap.put("CB:0025", report);
		report = new ReportMsg(base++, "�����ظ�");
		reportMap.put("CB:0026", report);
		report = new ReportMsg(base++, "δ�Ǽǵ�SP");
		reportMap.put("CB:0040", report);
		report = new ReportMsg(base++, "SP�ʻ�״̬�쳣");
		reportMap.put("CB:0041", report);
		report = new ReportMsg(base++, "SP��Ȩ��");
		reportMap.put("CB:0042", report);
		report = new ReportMsg(base++, "SP�ʻ��Ѵ���");
		reportMap.put("CB:0043", report);
		report = new ReportMsg(base++, "δ�Ǽǵ�SPҵ������");
		reportMap.put("CB:0044", report);
		report = new ReportMsg(base++, "SPҵ�����������쳣");
		reportMap.put("CB:0045", report);
		report = new ReportMsg(base++, "SPҵ�������Ѵ���");
		reportMap.put("CB:0046", report);
		report = new ReportMsg(base++, "�����û��Ѵ���");
		reportMap.put("CB:0052", report);
		report = new ReportMsg(base++, "�����û�������");
		reportMap.put("CB:0053", report);
		report = new ReportMsg(base++, "�����û�״̬�쳣");
		reportMap.put("CB:0054", report);
		report = new ReportMsg(base++, "ǩԼ��Ϣ�Ѵ���");
		reportMap.put("CB:0055", report);
		report = new ReportMsg(base++, "ǩԼ��Ϣ������");
		reportMap.put("CB:0056", report);
		report = new ReportMsg(base++, "ǩԼ�����쳣");
		reportMap.put("CB:0057", report);
		report = new ReportMsg(base++, "�����ѳ���");
		reportMap.put("CB:0061", report);
		report = new ReportMsg(base++, "�������ѳ���");
		reportMap.put("CB:0062", report);
		report = new ReportMsg(base++, "�û��ܾ�");
		reportMap.put("CB:0063", report);
		report = new ReportMsg(base++, "����Ϣ����Ѵ���");
		reportMap.put("CB:0064", report);
		report = new ReportMsg(base++, "��Ӧ�۷����󲻴���");
		reportMap.put("CB:0065", report);
		report = new ReportMsg(base++, "�۷������ѱ�ȷ��");
		reportMap.put("CB:0066", report);
		report = new ReportMsg(base++, "δ����ļƷ�����");
		reportMap.put("CB:0067", report);
		report = new ReportMsg(base++, "δ����ı��뷽ʽ");
		reportMap.put("CB:0068", report);
		report = new ReportMsg(base++, "DSMP��������Ӧ��Ϣʱ��״̬����");
		reportMap.put("DA:", report);
		report = new ReportMsg(base++, "�ȴ�DSMP������Ӧ��ʱ");
		reportMap.put("DA:0054", report);
		report = new ReportMsg(base++, "���͸�DSMPʧ��");
		reportMap.put("DA:0320", report);
		report = new ReportMsg(base++, "����DSMP��Ӧʧ��");
		reportMap.put("DA:0330", report);
		report = new ReportMsg(base++, "��DSMP֮��soap�����쳣");
		reportMap.put("DA:0360", report);
		report = new ReportMsg(base++, "DSMP���ش�����Ӧ��Ϣʱ��״̬����");
		reportMap.put("DB:", report);
		report = new ReportMsg(base++, "�ֻ����벻����");
		reportMap.put("DB:0100", report);
		report = new ReportMsg(base++, "�ֻ��������");
		reportMap.put("DB:0101", report);
		report = new ReportMsg(base++, "�û�ͣ�� �û���ֵ��Ҫ��������һ����Ϣ��SP�����ܼ����û��Ķ��Ž��շ���");
		reportMap.put("DB:0102", report);
		report = new ReportMsg(base++, "�û�Ƿ��");
		reportMap.put("DB:0103", report);
		report = new ReportMsg(base++, "�û�û��ʹ�ø�ҵ���Ȩ��");
		reportMap.put("DB:0104", report);
		report = new ReportMsg(base++, "ҵ��������");
		reportMap.put("DB:0105", report);
		report = new ReportMsg(base++, "����������");
		reportMap.put("DB:0106", report);
		report = new ReportMsg(base++, "ҵ�񲻴���");
		reportMap.put("DB:0107", report);
		report = new ReportMsg(base++, "��ҵ����ͣ����");
		reportMap.put("DB:0108", report);
		report = new ReportMsg(base++, "�÷������಻����");
		reportMap.put("DB:0109", report);
		report = new ReportMsg(base++, "�÷���������δ��ͨ");
		reportMap.put("DB:0110", report);
		report = new ReportMsg(base++, "��ҵ����δ��ͨ");
		reportMap.put("DB:0111", report);
		report = new ReportMsg(base++, "SP�������");
		reportMap.put("DB:0112", report);
		report = new ReportMsg(base++, "SP������");
		reportMap.put("DB:0113", report);
		report = new ReportMsg(base++, "SP��ͣ����");
		reportMap.put("DB:0114", report);
		report = new ReportMsg(base++, "�û�û�ж�����ҵ��");
		reportMap.put("DB:0115", report);
		report = new ReportMsg(base++, "�û���ͣ������ҵ��");
		reportMap.put("DB:0116", report);
		report = new ReportMsg(base++, "��ҵ���ܶԸ��û�����");
		reportMap.put("DB:0117", report);
		report = new ReportMsg(base++, "�û��Ѿ������˸�ҵ��");
		reportMap.put("DB:0118", report);
		report = new ReportMsg(base++, "�û�����ȡ����ҵ��");
		reportMap.put("DB:0119", report);
		report = new ReportMsg(base++, "������ʽ����");
		reportMap.put("DB:0120", report);
		report = new ReportMsg(base++, "û�и���ҵ��");
		reportMap.put("DB:0121", report);
		report = new ReportMsg(base++, "�����쳣");
		reportMap.put("DB:0122", report);
		report = new ReportMsg(base++, "ҵ��۸�Ϊ��");
		reportMap.put("DB:0123", report);
		report = new ReportMsg(base++, "ҵ��۸��ʽ����");
		reportMap.put("DB:0124", report);
		report = new ReportMsg(base++, "ҵ��۸񳬳���Χ");
		reportMap.put("DB:0125", report);
		report = new ReportMsg(base++, "���û������������û�");
		reportMap.put("DB:0126", report);
		report = new ReportMsg(base++, "���û�û���㹻�����");
		reportMap.put("DB:0127", report);
		report = new ReportMsg(base++, "����,����ʧ��");
		reportMap.put("DB:0128", report);
		report = new ReportMsg(base++, "�û��Ѿ��������û�");
		reportMap.put("DB:0129", report);
		report = new ReportMsg(base++, "�û���BOSS��û������û�����");
		reportMap.put("DB:0130", report);
		report = new ReportMsg(base++, "BOSSϵͳ����ͬ������");
		reportMap.put("DB:0131", report);
		report = new ReportMsg(base++, "�����Ϣ������");
		reportMap.put("DB:0132", report);
		report = new ReportMsg(base++, "�û�����ͬ������");
		reportMap.put("DB:0133", report);
		report = new ReportMsg(base++, "SP����ͬ������");
		reportMap.put("DB:0134", report);
		report = new ReportMsg(base++, "ҵ������ͬ������");
		reportMap.put("DB:0135", report);
		report = new ReportMsg(base++, "�û��������");
		reportMap.put("DB:0136", report);
		report = new ReportMsg(base++, "α����Ϣ����");
		reportMap.put("DB:0137", report);
		report = new ReportMsg(base++, "�û������Ϣ������");
		reportMap.put("DB:0138", report);
		report = new ReportMsg(base++, "�û�δ�㲥��ҵ��");
		reportMap.put("DB:0140", report);
		report = new ReportMsg(base++, "�����쳣");
		reportMap.put("DB:9001", report);
		report = new ReportMsg(base++, "ҵ�����س������Ƶ�����");
		reportMap.put("DB:9007", report);
		report = new ReportMsg(base++, "SP��������Ӧ��Ϣʱ��״̬����");
		reportMap.put("SA:", report);
		report = new ReportMsg(base++, "SP���ش�����Ӧ��Ϣʱ��״̬����");
		reportMap.put("SB:", report);
		report = new ReportMsg(base++, "��һ��ISMG��������Ӧ��Ϣʱ��״̬����");
		reportMap.put("IA:", report);
		report = new ReportMsg(base++, "��һ��ISMG���ش�����Ӧ��Ϣʱ��״̬����");
		reportMap.put("IB:", report);
		report = new ReportMsg(base++, "û�д���һ��ISMG�����յ�״̬����ʱ��״̬����");
		reportMap.put("IC:", report);
		report = new ReportMsg(base++, "����ISMG�ڲ���������");
		reportMap.put("ID:", report);
		report = new ReportMsg(base++, "ҵ��������");
		reportMap.put("ID:0007", report);
		report = new ReportMsg(base++, "�ƷѺ���·���жϴ���(�����ز��������μƷѺ���)");
		reportMap.put("ID:0009", report);
		report = new ReportMsg(base++, "SPACE�û���Ȩģ���Ȩ�û�ͣ����Ƿ�Ѵ���");
		reportMap.put("ID:0020", report);
		report = new ReportMsg(base++, "SPACE�û���Ȩģ�飺�û���������");
		reportMap.put("ID:0021:", report);
		report = new ReportMsg(base++, "��Ϣ��ʽ����һ��ָGBתUnicodeʧ��");
		reportMap.put("ID:0100", report);
		report = new ReportMsg(base++, "�Ʒ�����(Fee_Type)����");
		reportMap.put("ID:0101", report);
		report = new ReportMsg(base++, "������(�������汾��MT��ǰתMTʧ��)");
		reportMap.put("ID:0111", report);
		report = new ReportMsg(base++, "�����п۷�����ʧ��");
		reportMap.put("ID:0113", report);
		report = new ReportMsg(base++, "MT������smsAgent�����г�ʱ");
		reportMap.put("ID:0151", report);
		report = new ReportMsg(base++, "ǰת��������أ��������û����Ӧ");
		reportMap.put("ID:0154", report);
		report = new ReportMsg(base++, "SMSC��������Ӧ��Ϣʱ��״̬����");
		reportMap.put("MA:", report);
		report = new ReportMsg(base++, "SMSC���ش�����Ӧ��Ϣʱ��״̬����");
		reportMap.put("MB:", report);
		report = new ReportMsg(base++, "�������û��ֻ�����");
		reportMap.put("MB:0015", report);
		report = new ReportMsg(base++, "��Ϣ���ȴ���");
		reportMap.put("MB:0016", report);
		report = new ReportMsg(base++, "����ȴ���");
		reportMap.put("MB:0017", report);
		report = new ReportMsg(base++, "��ϢID��Ч");
		reportMap.put("MB:0018", report);
		report = new ReportMsg(base++, "û��ִ�д������Ȩ��");
		reportMap.put("MB:0019", report);
		report = new ReportMsg(base++, "��Ч��SYSTEMID");
		reportMap.put("MB:0032", report);
		report = new ReportMsg(base++, "��Ч������");
		reportMap.put("MB:0033", report);
		report = new ReportMsg(base++, "��Ч��SYSTEMTYPE");
		reportMap.put("MB:0034", report);
		report = new ReportMsg(base++, "��ַ����");
		reportMap.put("MB:0064", report);
		report = new ReportMsg(base++, "��������ύ��");
		reportMap.put("MB:0065", report);
		report = new ReportMsg(base++, "��������ʹ���,�����û��ֻ�����");
		reportMap.put("MB:0066", report);
		report = new ReportMsg(base++, "��Ч���û�");
		reportMap.put("MB:0067", report);
		report = new ReportMsg(base++, "��Ч�����ݸ�ʽ");
		reportMap.put("MB:0068", report);
		report = new ReportMsg(base++, "������Ϣʧ��");
		reportMap.put("MB:0069", report);
		report = new ReportMsg(base++, "��Ч�Ķ���ϢID");
		reportMap.put("MB:0070", report);
		report = new ReportMsg(base++, "���ݿ�ʧ��");
		reportMap.put("MB:0071", report);
		report = new ReportMsg(base++, "ȡ����Ϣʧ��");
		reportMap.put("MB:0072", report);
		report = new ReportMsg(base++, "����Ϣ״̬����");
		reportMap.put("MB:0073", report);
		report = new ReportMsg(base++, "�滻��Ϣʧ��");
		reportMap.put("MB:0074", report);
		report = new ReportMsg(base++, "�滻��ϢԴ��ַ����");
		reportMap.put("MB:0075", report);
		report = new ReportMsg(base++, "��Ч��Դ��ַTON");
		reportMap.put("MB:0096", report);
		report = new ReportMsg(base++, "��Ч��Դ��ַNPI");
		reportMap.put("MB:0097", report);
		report = new ReportMsg(base++, "Դ��ַ����");
		reportMap.put("MB:0098", report);
		report = new ReportMsg(base++, "��Ч��Ŀ�ĵ�ַTON");
		reportMap.put("MB:0099", report);
		report = new ReportMsg(base++, "��Ч��Ŀ�ĵ�ַNPI");
		reportMap.put("MB:0100", report);
		report = new ReportMsg(base++, "Ŀ�ĵ�ַ����");
		reportMap.put("MB:0101", report);
		report = new ReportMsg(base++, "��Ч�Ķ�ʱʱ��");
		reportMap.put("MB:0102", report);
		report = new ReportMsg(base++, "��Ч�ĳ�ʱʱ��");
		reportMap.put("MB:0103", report);
		report = new ReportMsg(base++, "��Ч��ESM_CALSS");
		reportMap.put("MB:0104", report);
		report = new ReportMsg(base++, "��Ч��UDLEN");
		reportMap.put("MB:0105", report);
		report = new ReportMsg(base++, "��Ч��PRI");
		reportMap.put("MB:0106", report);
		report = new ReportMsg(base++, "��Ч��Registered_delivery_flag");
		reportMap.put("MB:0107", report);
		report = new ReportMsg(base++, "��Ч��Replace_if_present_flag");
		reportMap.put("MB:0108", report);
		report = new ReportMsg(base++, "ָ���û��Ѿ�����");
		reportMap.put("MB:0128", report);
		report = new ReportMsg(base++, "�����û�ʧ��");
		reportMap.put("MB:0129", report);
		report = new ReportMsg(base++, "�û�ID����");
		reportMap.put("MB:0130", report);
		report = new ReportMsg(base++, "ָ���û�������");
		reportMap.put("MB:0131", report);
		report = new ReportMsg(base++, "ϵͳδ�Ӷ������Ľ��յ�״̬����");
		reportMap.put("MC:", report);
		report = new ReportMsg(base++, "ϵͳδ�Ӷ������Ľ��յ�״̬����");
		reportMap.put("MC:0015", report);
		report = new ReportMsg(base++, "�û���ʱ��ػ����߲��ڷ������ȵ��µĶ���Ϣ��ʱû�еݽ����û��ֻ���");
		reportMap.put("MI:", report);
		report = new ReportMsg(base++, "�û���ʱ��ػ����߲��ڷ������ȵ��µĶ���Ϣ��ʱû�еݽ����û��ֻ���");
		reportMap.put("MI:0000", report);
		report = new ReportMsg(base++, "��Ϣ�Ѿ���ɾ��");
		reportMap.put("MJ:", report);
		report = new ReportMsg(base++, "�û���Ϊ״̬����ȷ�紦��ͣ���������״̬�����µ��û��޷����ܷ���");
		reportMap.put("MK:", report);
		report = new ReportMsg(base++, "�û���Ϊ״̬����ȷ�紦��ͣ���������״̬�����µ��û��޷����ܷ���");
		reportMap.put("MK:0000", report);
		report = new ReportMsg(base++, "��Ϣ���ڱ�����״̬");
		reportMap.put("ML:", report);
		report = new ReportMsg(base++, "��Ϣ����δ֪״̬");
		reportMap.put("MM:", report);
		report = new ReportMsg(base++, "��Ϣ��ΪĳЩԭ�򱻾ܾ�");
		reportMap.put("MN:", report);
		report = new ReportMsg(base++, "����ֵ");
		reportMap.put("MH:", report);
	}

	public static int getCmppReportResult(String stat)
	{

		int ret = 1;
		if (stat.equalsIgnoreCase("DELIVRD"))
		{
			ret = 0;
		} else if (mobileErrorUitl.getCmppList().contains(stat))
		{
			ret = SmsErrCode.USER_MOBILE_ERROR.getValue();
		}

		return ret;
	}

	public static String getSgipDetailInfo(int errorCode)
	{

		String ret = "";

		switch (errorCode)
		{

		case 0:
			ret = "0*DELIVRD:Message is delivered to destination";
			break;
		case 5:
			ret = "5*������ʽ��ָ�����в���ֵ��������Ͳ�������Э��涨�ķ�Χ������";
			break;
		case 6:
			ret = "6*�Ƿ��ֻ����룬Э���������ֻ������ֶγ��ַ�86130������ֻ�����ǰδ�ӡ�86��ʱ��Ӧ����";
			break;
		case 7:
			ret = "7*��ϢID��";
			break;
		case 8:
			ret = "8*��Ϣ���ȴ�";
			break;
		case 9:
			ret = "9*�Ƿ����кţ��������к��ظ������кŸ�ʽ�����";
			break;
		case 10:
			ret = "10*�Ƿ�����GNS";
			break;
		case 11:
			ret = "11*�ڵ�æ��ָ���ڵ�洢������������ԭ����ʱ�����ṩ��������";
			break;
		case 12:
			ret = "12*Fee_terminal_Id����";
			break;
		case 13:
			ret = "13*Dest_terminal_Id����";
			break;

		case 21:
			ret = "21*Ŀ�ĵ�ַ���ɴָ·�ɱ����·������Ϣ·����ȷ����·�ɵĽڵ���ʱ�����ṩ��������";
			break;
		case 22:
			ret = "22*·�ɴ�ָ·�ɱ����·�ɵ���Ϣ·�ɳ�����������ת��SMG��";
			break;
		case 23:
			ret = "23*·�ɲ����ڣ�ָ��Ϣ·�ɵĽڵ���·�ɱ��в�����";
			break;
		case 24:
			ret = "24*�ƷѺ�����Ч����Ȩ���ɹ�ʱ�����Ĵ�����Ϣ";
			break;
		case 25:
			ret = "25*�û�����ͨ�ţ��粻�ڷ�������δ�����������";
			break;
		case 26:
			ret = "26*�ֻ��ڴ治��";
			break;
		case 27:
			ret = "27*�ֻ���֧�ֶ���Ϣ";
			break;
		case 28:
			ret = "28*�ֻ����ն���Ϣ���ִ���";
			break;
		case 29:
			ret = "29*��֪�����û�";
			break;
		case 30:
			ret = "30*���ṩ�˹���";
			break;
		case 31:
			ret = "31*�Ƿ��豸";
			break;
		case 32:
			ret = "32*ϵͳʧ��";
			break;
		case 33:
			ret = "33*�������Ķ�����";
			break;
		default:
			ret = errorCode + "*��������";
			break;
		}
		return ret;

	}

	public static String getContent(byte[] bys, int msg_fmt)
	{

		if (bys == null || bys.length <= 0)
			return "";
		String content = null;
		int offset = 0;
		String appendHead = "";
		if (bys.length >= 6)
		{
			if (bys[0] == 5 && bys[1] == 0 && bys[2] == 3)
			{
				offset = 6;
				// ������ȡ��ͷ�����ϳ�һ�����ظ�Ӧ��
				// appendHead = "(" + bys[5] + "/" + bys[4] + ")";
			}
		}
		try
		{
			if (msg_fmt == 8 || msg_fmt == 25)
			{
				content = new String(bys, offset, bys.length - offset, "UTF-16BE");
			} else if (msg_fmt == 15)
			{
				content = new String(bys, offset, bys.length - offset, "GBK");
			} else
			{
				content = new String(bys, offset, bys.length - offset);
			}

		} catch (Exception e)
		{
			content = "" + e;
		}
		if (offset > 0)
		{
			content = appendHead + content;
		}

		return content;
	}

	public static String getCmppSubmitRespResult(int result)
	{

		if (result < 0)
		{
			result += 256;
		}
		String ret = null;
		switch (result)
		{
		case 0:
			ret = "�ɹ�";
			break;
		case 1:
			ret = "��Ϣ�ṹ��";
			break;
		case 2:
			ret = "�����ִ�";
			break;
		case 3:
			ret = "��Ϣ����ظ�";
			break;
		case 4:
			ret = "��Ϣ���ȴ�";
			break;
		case 5:
			ret = "�ʷѴ����";
			break;
		case 6:
			ret = "���������Ϣ��";
			break;
		case 7:
			ret = "ҵ������";
			break;
		case 8:
			ret = "�������ƴ�";
			break;
		case 9:
			ret = "�����ز��������˼ƷѺ���";
			break;
		case 10:
			ret = "src_id����";
			break;
		case 11:
			ret = "Msg_src����";
			break;
		case 12:
			ret = "�Ʒѵ�ַ��";
			break;
		case 13:
			ret = "Ŀ�ĵ�ַ��";
			break;
		}

		if (ret == null)
		{
			ret = "submitResp��������";
		}

		ret = result + " " + ret;

		return ret;
	}
	public static String getCmppReportDetailResult(String stat)
	{
		String ret = stat + " ";
		ReportMsg report = reportMap.get(stat);
		if (report == null && stat.length() > 3)
		{
			String pre = stat.substring(0, 3);
			report = reportMap.get(pre);
		}
		if (report != null)
		{
			ret += report.detail;
		} else
		{
			ret += "δ֪����";
		}

		return ret;
	}
	public static int getSmgpReportResult(String stat)
	{
		int ret = 1;
		if (stat.equals("DELIVRD")||stat.equals("ELIVRD"))
		{
			ret = 0;
		} else if (mobileErrorUitl.getSmgpList().contains(stat))
		{
			ret = SmsErrCode.USER_MOBILE_ERROR.getValue();
		}
		return ret;
	}
	public static int getGWResponseResult(int stat)
	{
		int ret=stat;
		if(mobileErrorUitl.getGwList().contains(String.valueOf(stat)))
		{
			ret=1000016;
		}
		return ret;
	}

	/**
	 * SMGP��Ӧ���ת��
	 * 
	 * @param result
	 * @return String
	 * @throws
	 */
	public static String getSmgpSubmitRespResult(int result)
	{
		String ret = "";
		switch (result)
		{
		case 0:
			ret = "0*�ɹ�";
			break;
		case 1:
			ret = "1*ϵͳæ";
			break;
		case 2:
			ret = "2*�����������";
			break;
		case 10:
			ret = "10*��Ϣ�ṹ��";
			break;
		case 11:
			ret = "11*�����ִ�";
			break;
		case 12:
			ret = "12*���к��ظ�";
			break;
		case 20:
			ret = "20*IP��ַ��";
			break;
		case 21:
			ret = "21*��֤��";
			break;
		case 22:
			ret = "22*�汾̫��";
			break;
		case 30:
			ret = "30*�Ƿ���Ϣ����(MsgType)";
			break;
		case 31:
			ret = "31*�Ƿ����ȼ�(Priority)";
			break;
		case 32:
			ret = "32*�Ƿ��ʷ�����(FeeType)";
			break;
		case 33:
			ret = "33*�Ƿ��ʷѴ���(FeeCode)";
			break;
		case 34:
			ret = "34*�Ƿ���Ϣ��ʽ(MsgFormat)";
			break;
		case 35:
			ret = "36*�Ƿ�ʱ���ʽ";
			break;
		case 36:
			ret = "�Ƿ����ų���(Msg_Length)";
			break;
		case 37:
			ret = "37*��Ч���ѹ�";
			break;
		case 43:
			ret = "43*�Ƿ��������(ServiceId)";
			break;
		case 44:
			ret = "44*�Ƿ���Ч��(ValidTime)";
			break;
		case 45:
			ret = "45*�Ƿ���ʱ����(AtTime)";
			break;
		case 46:
			ret = "46*�Ƿ������û�����(SrcTermid)";
			break;
		case 47:
			ret = "47*�Ƿ������û�����(DestTermid)";
			break;
		case 48:
			ret = "48*�Ƿ��ƷѺ���(ChargeTermid)";
			break;
		case 49:
			ret = "49*�Ƿ�SP�������(SPCode)";
			break;
		case 69:
			ret = "69*�Ƿ�SP��ҵ����(SPID)";
			break;
		default:
			ret = result + "*δ֪����";
		}
		return ret;
	}

}

class ReportMsg
{

	int result;

	String detail;

	ReportMsg(int result, String detail)
	{

		this.result = result;
		this.detail = detail;
	}
}
