// Copyright (c) 2001 gaohu
package com.surge.engine.protocol.sms.cmpp.pmsg;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.surge.engine.util.AppendUtils;

public class Cmpp2Submit extends Cmpp2SendObject
{
	private static final long serialVersionUID = 7774782422779271110L;

	private String service_id;

	private String sp_id;

	private String src_id;

	private String dest;

	private int count;// ���ط�����

	private long reSendCurrTime;// ���͵����ص�ʱ��

	private int seqId;

	public Cmpp2Submit(int seq_id, int rptIndex, byte pk_total, byte pk_number,
			byte registered_delivery, String service_id, byte fee_user_type,
			String fee_terminal_id, byte tp_pid, byte tp_udhi, byte msg_fmt, String sp_id,
			String fee_type, String fee_code, String src_id, String dest, byte[] msg_content,
			int offset, int len)
	{
		this.service_id = service_id;
		this.sp_id = sp_id;
		this.src_id = src_id;
		this.dest = dest;
		this.seqId = seq_id;
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(dest, ",");
		String tk = null;
		while (st.hasMoreTokens())
		{
			//�������ʱ��ֺ���
			tk = st.nextToken();
			if (tk.length() > 21)
				continue;
			list.add(tk);
			if (list.size() > 100)
				break;
		}
		//�Ʒ�����Ϊ��Чʱ��Ŀ�ĺ���Ʒ�
		if (fee_user_type == 3 && (fee_terminal_id == null || fee_terminal_id.length() == 0))
		{
			fee_terminal_id = dest;
		}
		/*
		 * ����200�����Ǻܾ�ȷ������ԭ����ʵ������������ȫû�д�����Ϊ���淢�͵�ʱ���Ǹ���pos���͵�
		 */
		AppendUtils tool = new AppendUtils(200 + 21 * list.size() + msg_content.length);
		//��Ϣ��ʶ����SP��������ر��������������ա�
		tool.appendLong(0);
		//��ͬMsg_Id����Ϣ����������1��ʼ
		tool.appendByte(pk_total);
		//��ͬMsg_Id����Ϣ��ţ���1��ʼ
		tool.appendByte(pk_number);
		//�Ƿ�Ҫ�󷵻�״̬ȷ�ϱ��棺
		//0������Ҫ
		//1����Ҫ
		//2������SMC����
		//�������Ͷ��Ž������ؼƷ�ʹ�ã������͸�Ŀ���ն�)
		tool.appendByte(registered_delivery);
		//��Ϣ����
		tool.appendByte((byte) 0);// msg_level
		//ҵ�����ͣ������֡���ĸ�ͷ��ŵ����
		tool.appendString(service_id, 10);
		//�Ʒ��û������ֶ�
		//0����Ŀ���ն�MSISDN�Ʒѣ�
		//1����Դ�ն�MSISDN�Ʒѣ�
		//2����SP�Ʒ�;
		//3����ʾ���ֶ���Ч����˭�ƷѲμ�Fee_terminal_Id�ֶΡ�
		tool.appendByte(fee_user_type);
		//���Ʒ��û��ĺ��루�籾�ֽ���գ����ʾ���ֶ���Ч����˭�ƷѲμ�Fee_UserType�ֶΣ����ֶ���Fee_UserType�ֶλ��⣩
		tool.appendString(fee_terminal_id, 21);
		tool.appendByte(tp_pid);
		tool.appendByte(tp_udhi);
		//��Ϣ��ʽ
		//0��ASCII��
		//3������д������
		//4����������Ϣ
		//8��UCS2����
		//15����GB����
		tool.appendByte(msg_fmt);
		tool.appendString(sp_id, 6);// msg_src ͬsp_id
		tool.appendString(fee_type, 2);// fee_type
		tool.appendString(fee_code, 6);// fee_code
		tool.appendString("", 17);// valid_time
		tool.appendString("", 17);// at_time
		//���ն��ŵ�MSISDN����
		tool.appendString(src_id, 21);
		//������Ϣ���û�����(С��100���û�)
		tool.appendByte((byte) list.size());
		for (String mobile : list)
		{
			tool.appendString(mobile.trim(), 21);
		}
		tool.appendByte((byte) len);
		tool.appendBytes(msg_content, offset, len);
		tool.appendString("", 8);

		body = tool.getOutBytes();

		head = new Cmpp2MsgHead(body.length, Common.CMPP_SUBMIT, seq_id);
	}

	public Cmpp2Submit()
	{
	}

	public String getSrcId()
	{
		return src_id;
	}

	public String getDest()
	{
		return dest;
	}

	public String getServiceId()
	{
		return service_id;
	}

	public String getSpId()
	{
		return sp_id;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(1024);
		sb.append(" service_id:");
		sb.append(service_id);
		sb.append(" sp_id:");
		sb.append(sp_id);
		sb.append(" fee_type:");
		sb.append(" src_id:");
		sb.append(src_id);
		sb.append(" dest:");
		sb.append(dest);

		return sb.toString();
	}

	public long getReSendCurrTime()
	{
		return reSendCurrTime;
	}

	public void setReSendCurrTime(long reSendCurrTime)
	{
		this.reSendCurrTime = reSendCurrTime;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getService_id()
	{
		return service_id;
	}

	public void setService_id(String service_id)
	{
		this.service_id = service_id;
	}

	public String getSp_id()
	{
		return sp_id;
	}

	public void setSp_id(String sp_id)
	{
		this.sp_id = sp_id;
	}

	public String getSrc_id()
	{
		return src_id;
	}

	public void setSrc_id(String src_id)
	{
		this.src_id = src_id;
	}

	public void setDest(String dest)
	{
		this.dest = dest;
	}

	public void setSeqId(int seqId)
	{
		this.seqId = seqId;
	}
}
