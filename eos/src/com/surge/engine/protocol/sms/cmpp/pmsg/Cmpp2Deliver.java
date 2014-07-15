package com.surge.engine.protocol.sms.cmpp.pmsg;

import java.io.UnsupportedEncodingException;

import com.surge.engine.protocol.sms.util.SmsTools;
import com.surge.engine.util.AppendUtils;
import com.surge.engine.util.SubtractTools;
import com.surge.engine.util.Tools;

/**
 * Title:�й��ƶ�Deliver�ӿ� Description: Copyright: Copyright (c) 2003-2004 Company:
 * seasee
 * 
 * @author huzi
 * @version 1.0, 07/06/05
 * @since JDK1.5
 */

public class Cmpp2Deliver extends Cmpp2SendObject
{

	private static final long serialVersionUID = -3452122137731734770L;

	private int ismg_id;

	/** ��Ϣ��ʶ */
	private long msg_id;

	/**
	 * Ŀ�ĺ��� SP�ķ�����룬һ��4--6λ��������ǰ׺Ϊ�������ĳ����룻 �ú������ֻ��û�����Ϣ�ı��к���
	 */
	private String dest_id;

	/** ҵ�����ͣ������֡���ĸ�ͷ��ŵ���� */
	private String service_id;

	/** GSMЭ�����͡���ϸ������ο�GSM03.40�е�9.2.3.9 */
	private byte tp_pid;

	/** GSMЭ�����͡���ϸ������ο�GSM03.40�е�9.2.3.23����ʹ��1λ���Ҷ��� */
	private byte tp_udhi;

	/** ��Ϣ��ʽ 0��ASCII�� 3������д������ 4����������Ϣ 8��UCS2���� 15����GB���� */
	private byte msg_fmt;

	/** Դ�ն�MSISDN����(״̬����ʱ��ΪCMPP_SUBMIT��Ϣ��Ŀ���ն˺���) */
	private String src_id;

	/** �Ƿ�Ϊ״̬���� 0����״̬���� 1��״̬���� */
	private byte registered_delivery;

	/** ��Ϣ����(������) */
	private byte[] msg_content = null;

	/** ������ */
	private String reserved;

	// ������

	private int totalCout = 0;

	// ��ǰ����
	private int currCout = 0;

	// ��ǰʱ��
	private long currTime;

	/** ��Ϣ����(�ַ���,��msg_content�õ�) */
	private String content = "";

	private int seq_id;
	
	private String [] longContent;

	public Cmpp2Deliver()
	{

	}

	public Cmpp2Deliver(String src_id, String dest_id, String content)
	{

		AppendUtils t1 = new AppendUtils(100);

		t1.appendLong(Tools.getMsgId());
		t1.appendString(dest_id, 21);
		t1.appendString("", 10);
		t1.appendByte((byte) 0);
		t1.appendByte((byte) 0);
		t1.appendByte((byte) 15);
		t1.appendString(src_id, 21);
		t1.appendByte((byte) 0);
		try
		{
			byte[] bys = content.getBytes("GBK");
			t1.appendByte((byte) bys.length);
			t1.appendBytes(bys);
		}
		catch (UnsupportedEncodingException ex)
		{
		}
		t1.appendLong(0);

		body = t1.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_DELIVER, Tools.getSeqId());
	}

	public Cmpp2Deliver(byte[] bys, int seq_id)
	{

		SubtractTools tool = new SubtractTools(bys, 0);
		this.seq_id = seq_id;
		msg_id = tool.getLong();
		dest_id = tool.getTrimString(21);
		service_id = tool.getTrimString(10);
		tp_pid = tool.getByte();
		tp_udhi = tool.getByte();
		msg_fmt = tool.getByte();

		src_id = tool.getTrimString(21);
		if (src_id.startsWith("+86"))
		{
			src_id = src_id.substring(3);
		}
		else if (src_id.startsWith("86"))
		{
			src_id = src_id.substring(2);
		}

		registered_delivery = tool.getByte();
		int msg_len = tool.getByte();
		if (msg_len < 0)
			msg_len += 256;
		msg_content = tool.getBytes(msg_len);
		reserved = tool.getString(8);

		src_id = Tools.trimMobile(src_id);

		// ����ǰ4���ֽ�Ϊ5��0��3; ����Ϊ6���ֽ� �����Ӳ�֧��
		if (msg_content != null && msg_content.length >= 3)
		{
			if (msg_content[0] == 5 && msg_content[1] == 0 && msg_content[2] == 3)
			{
				totalCout = msg_content[4]; // ������
				currCout = msg_content[5]; // ��ǰ��
			}
		}
		// ��MO����
		if (registered_delivery == 0)
		{
			content = SmsTools.getContent(msg_content, msg_fmt);
		}
	}

	/**
	 * ȷ���Ƿ���״̬����
	 * 
	 * 
	 * @return true:״̬���� false:��״̬����
	 */
	public boolean isReport()
	{

		return (registered_delivery == 1);
	}

	/**
	 * ����״̬����
	 * 
	 * @return �����״̬�����򷵻�CmppReport�����򷵻�null
	 */
	public Cmpp2Report getReport()
	{

		if (registered_delivery != 1 || msg_content == null)
			return null;
		else
		{
			Cmpp2Report StatRpt = new Cmpp2Report(dest_id, msg_content);
			return StatRpt;
		}
	}

	public String getContent()
	{

		return content;
	}

	public String getDestId()
	{

		return dest_id;
	}

	public long getMsgId()
	{

		return msg_id;
	}

	public String getSrcTerminalId()
	{

		return src_id;
	}

	public byte getMsgFmt()
	{

		return msg_fmt;
	}

	public String getServiceId()
	{

		return service_id;
	}

	public int getCurrCout()
	{

		return currCout;
	}

	public int addCurrCout()
	{

		return ++currCout;
	}

	public int getTotalCout()
	{

		return totalCout;
	}

	public void setContent(String content)
	{

		this.content = content;
	}

	public long getCurrTime()
	{

		return currTime;
	}

	public void setCurrTime(long currTime)
	{

		this.currTime = currTime;
	}

	public void setTotalCout(int totalCout)
	{

		this.totalCout = totalCout;
	}

	public int getIsmgId()
	{

		return ismg_id;
	}

	public void setIsmgId(int ismg_id)
	{

		this.ismg_id = ismg_id;
	}
	public String getSeqId()
	{

		return String.valueOf(this.seq_id);
	}
	public String toString()
	{

		StringBuilder sb = new StringBuilder(200);
		sb.append(" CmppDeliver :");
		sb.append(" msg_id:").append(msg_id);
		sb.append(" dest:").append(dest_id);
		sb.append(" src:").append(src_id);
		sb.append(" content:").append(content);

		return sb.toString();
	}

	public String[] getLongContent()
	{
		return longContent;
	}

	public void setLongContent(String[] longContent)
	{
		this.longContent = longContent;
	}

}
