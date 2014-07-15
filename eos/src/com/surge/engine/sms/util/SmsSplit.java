/*
 * Title: mas20 @author Administrator Created on 2008-4-8
 */

package com.surge.engine.sms.util;

import java.util.ArrayList;
import java.util.List;

import com.surge.engine.sms.common.MsgFmt;
import com.surge.engine.sms.pojo.SplitMsg;

/**
 * ���Ų��
 * 
 * @author Administrator
 * 
 */
public class SmsSplit
{

	/** ���Ķ���Ҫ�ӵ�ǰ׺���ȣ�1/2�� */
	private static int PERFIX_LEN = 5;

	/** δ��ֵĶ������� */
	private String content;

	/** �Ƿ�֧�ֳ����� */
	private boolean isLongSmSupport = false;

	/** ���Ķ���ǩ�� **/
	private String ismgSignCn;

	/** Ӣ�Ķ���ǩ�� **/
	private String ismgSignEn;

	/** һ�����Ķ��ų��� **/
	private int smsLengthCn;

	/** һ��Ӣ�Ķ��ų��� **/
	private int smsLengthEn;

	/** �Ƿ�ÿ������Ҫ����ǩ�� */
	private boolean isSmsSign;

	/** �û�ǩ�� **/
	private String userSign;

	public SmsSplit(String content, boolean isLongSmSupport, int smsLengthCn, int smsLengthEn,
			boolean isSmsSign, String ismgSignCn, String ismgSignEn, String userSign)
	{
		this.content = content;
		this.isLongSmSupport = isLongSmSupport;
		this.smsLengthCn = smsLengthCn;
		this.smsLengthEn = smsLengthEn;
		this.ismgSignCn = ismgSignCn;
		this.ismgSignEn = ismgSignEn;
		this.isSmsSign = isSmsSign;
		this.userSign = userSign;
		if (this.userSign == null)
		{
			this.userSign = "";
		}
		if (content.length() > 600)
		{
			// ��ʱд��
			PERFIX_LEN = 7;
		}

	}

	/**
	 * ��ͨ��Ӣ�Ķ��Ų��
	 * 
	 * @param content
	 * @param msgFmt
	 * @param smsType
	 * @return
	 */
	public SplitMsg splitSm()
	{
		/** �����Ƿ��Ǵ�Ӣ�ĵ� */
		boolean isEnglish = CharsetTools.isStringValidate(content);
		/** Ӣ�Ķ��ŵ�����ǩ���Ƿ��Ǵ�Ӣ�ĵ� */
		boolean ismgSignEnIsEn = CharsetTools.isStringValidate(ismgSignEn);
		/** �û�ǩ���Ƿ��Ǵ�Ӣ�� **/
		boolean userSignIsEn = CharsetTools.isStringValidate(userSign);

		if (isEnglish && ismgSignEnIsEn	&& ((isLongSmSupport && (content.length() + ismgSignEn.length() <= this.smsLengthEn)) || !isLongSmSupport))
		{
			if (ismgSignEn.length() <= 0)
			{
				if (userSignIsEn)
				{
					return splitSmEn();
				}
			}
			else
			{
				return splitSmEn();
			}
		} 
		return splitSmCn(isEnglish);
	}
	public SplitMsg splitSmEn()
	{
		SplitMsg splitMsg = new SplitMsg();
		List<String> list = new ArrayList<String>();
		splitMsg.setEnglish(true);
		// ������ǩ����һ�����ŵĳ���
		int msgLenAddSign = 0;
		boolean isSpSign = false;
		if (ismgSignEn.length() > 0)
		{
			msgLenAddSign = smsLengthEn - ismgSignEn.length();
			isSpSign = true;
		} else
		{
			msgLenAddSign = smsLengthEn - userSign.length();
		}
		int lastMsgLen = msgLenAddSign;// ���һ�����ŵĳ��ȣ�֧�ֳ����źͲ�֧�ֳ����Ų�ͬ
		int contLen = smsLengthEn; // һ�����Ķ��Ŷ��ŵı�׼����
		int word_count = content.length();// �������ݵ����峤��
		int pk_total = 1;
		if (contLen > word_count)
			contLen = word_count;
		if (word_count > msgLenAddSign) // ����������ݴ��ڼ�����ǩ����һ�����ŵĳ�����Ҫ���
		{
			if (isLongSmSupport) // �������֧�ֳ����ţ��������Ķ��ţ���������Ҫ�������ֽڣ��������ֻ���һ��
			{
				lastMsgLen -= 6;
				contLen -= 6;
				if (isSmsSign && isSpSign)
				{
					contLen = msgLenAddSign - 6;
				}
			} else
			// ������ز�֧�ֳ����ţ���ǰ׺�磺��1/2��
			{

				lastMsgLen -= PERFIX_LEN;
				if (isSmsSign)
				{
					// ������ز�֧���ֶ��ţ�����ÿ����ֶ���Ҫ��ǩ��
					contLen = msgLenAddSign - PERFIX_LEN;
				} else
				{
					contLen -= PERFIX_LEN;
				}
			}
			pk_total = word_count / contLen;
			if (word_count % contLen != 0)
				pk_total++;
		}
		// ��ȡ�����һ�����ŵĳ���
		int lastLen = content.substring((pk_total - 1) * contLen, content.length()).length();
		boolean isSpillage = false;
		// ������һ�����ȳ��������ٷ�һ��
		if (lastLen > lastMsgLen)
		{
			pk_total++;
			isSpillage = true;
		}
		String tmp = content;
		for (int pk_number = 1; pk_number <= pk_total; pk_number++)
		{
			int endindex = Math.min(pk_number * contLen, word_count);
			int beginIndex = (pk_number - 1) * contLen;
			if (pk_total > 1)
			{
				if (isSpillage && pk_number == pk_total - 1)
				{
					endindex = (pk_number - 1) * contLen + lastMsgLen;
				}
				if (isSpillage && pk_number == pk_total)
				{
					beginIndex = (pk_number - 2) * contLen + lastMsgLen;
				}
				String perfix = "(" + pk_number + "/" + pk_total + ")";

				if (isLongSmSupport)
				{
					if (pk_number == pk_total)
					{
						if (isSpSign)// ��������ǩ��
						{
							tmp = content.substring(beginIndex, endindex);
						} else
						// ���û�ǩ�������һ���������ݺ��ǩ��
						{
							tmp = content.substring(beginIndex, endindex) + userSign;
						}

					} else
					{
						tmp = content.substring(beginIndex, endindex);
					}
				} else
				{
					// if (isSmsSign)
					// {
					// tmp = perfix + content.substring(beginIndex, endindex);
					// } else if (pk_number == pk_total)
					// {
					// tmp = perfix + content.substring(beginIndex, endindex);
					// } else
					// {
					// tmp = perfix + content.substring(beginIndex, endindex);
					// }
					if (isSpSign)
					{
						tmp = perfix + content.substring(beginIndex, endindex);
					} else
					{
						tmp = perfix + content.substring(beginIndex, endindex) + userSign;
					}

				}

				list.add(tmp);
			} else
			{
				if (isSpSign)
				{
					list.add(tmp);
				} else
				{
					list.add(tmp + userSign);
				}

			}

		}
		splitMsg.setMsgs(list.toArray(new String[0]));
		if (isLongSmSupport && list.size() > 1)
		{
			splitMsg.setMsgFmt(MsgFmt.UCS2);
			splitMsg.setLongSms(true);
		} else
		{
			splitMsg.setMsgFmt(MsgFmt.ASCII);
		}

		return splitMsg;
	}

	public SplitMsg splitSmCn(boolean isEnglish)
	{
		SplitMsg splitMsg = new SplitMsg();
		List<String> list = new ArrayList<String>();

		// ������ǩ����һ�����ŵĳ���
		int msgLenAddSign = 0;
		boolean isSpSign = false;
		if(!isEnglish) {
			if (ismgSignCn.length() > 0)
			{
				msgLenAddSign = smsLengthCn - ismgSignCn.length();
				isSpSign = true;
			} 
			else
			{
				msgLenAddSign = smsLengthCn - userSign.length();
			}
		} else {
			if (ismgSignEn.length() > 0)
			{
				msgLenAddSign = smsLengthCn - ismgSignEn.length();
				isSpSign = true;
			} 
			else
			{
				msgLenAddSign = smsLengthCn - userSign.length();
			}
		}
		int lastMsgLen = msgLenAddSign;// ���һ�����ŵĳ��ȣ�֧�ֳ����źͲ�֧�ֳ����Ų�ͬ
		int contLen = smsLengthCn; // һ�����Ķ��Ŷ��ŵı�׼����70���ַ�
		int word_count = content.length();// �������ݵ����峤��
		int pk_total = 1;
		if (contLen > word_count)
			contLen = word_count;
		if (word_count > msgLenAddSign) // ����������ݴ��ڼ�����ǩ����һ�����ŵĳ�����Ҫ���
		{
			if (isLongSmSupport) // �������֧�ֳ����ţ��������Ķ��ţ���������Ҫ�������ֽڣ��������ֻ���һ��
			{

				lastMsgLen -= 3;
				contLen -= 3;
				if (isSmsSign && isSpSign)
				{
					contLen = msgLenAddSign - 3;
				}

			} else
			// ������ز�֧�ֳ����ţ���ǰ׺�磺��1/2��
			{

				lastMsgLen -= PERFIX_LEN;
				if (isSpSign)
				{
					if (isSmsSign)
					{
						// ������ز�֧���ֶ��ţ�����ÿ����ֶ���Ҫ��ǩ��
						contLen = msgLenAddSign - PERFIX_LEN;
					} else
					{
						contLen -= PERFIX_LEN;
					}
				} else
				{
					// ������ز�֧���ֶ��ţ��������û�ǩ��
					contLen = msgLenAddSign - PERFIX_LEN;
				}

			}
			pk_total = word_count / contLen;
			if (word_count % contLen != 0)
				pk_total++;
		}
		// ��ȡ�����һ�����ŵĳ���
		int lastLen = content.substring((pk_total - 1) * contLen, content.length()).length();
		boolean isSpillage = false;
		// ������һ�����ȳ��������ٷ�һ��
		if (lastLen > lastMsgLen)
		{
			pk_total++;
			isSpillage = true;
		}
		String tmp = content;
		for (int pk_number = 1; pk_number <= pk_total; pk_number++)
		{
			int endindex = Math.min(pk_number * contLen, word_count);
			int beginIndex = (pk_number - 1) * contLen;
			if (pk_total > 1)
			{
				if (isSpillage && pk_number == pk_total - 1)
				{
					endindex = (pk_number - 1) * contLen + lastMsgLen;
				}
				if (isSpillage && pk_number == pk_total)
				{
					beginIndex = (pk_number - 2) * contLen + lastMsgLen;
				}
				String perfix = "(" + pk_number + "/" + pk_total + ")";

				if (isLongSmSupport)
				{
					// if (pk_number == pk_total)
					// {
					// tmp = content.substring(beginIndex, endindex);
					// } else
					// {
					// tmp = content.substring(beginIndex, endindex);
					// }
					if (isSpSign)
					{
						tmp = content.substring(beginIndex, endindex);
					} else
					{
						if (pk_number == pk_total)
						{
							tmp = content.substring(beginIndex, endindex)+userSign;
						} else
						{
							tmp = content.substring(beginIndex, endindex);
						}
					}
				} else
				{
					if (isSmsSign)
					{
						tmp = perfix + content.substring(beginIndex, endindex);
					} else if (pk_number == pk_total)
					{
						tmp = perfix + content.substring(beginIndex, endindex);
					} else
					{
						tmp = perfix + content.substring(beginIndex, endindex);
					}
					if(isSpSign)
					{
						tmp = perfix + content.substring(beginIndex, endindex);
					}
					else
					{
						tmp = perfix + content.substring(beginIndex, endindex)+userSign;
					}

				}

				list.add(tmp);
			} else
			{
				if(isSpSign)
				{
					list.add(tmp);
				}
				else
				{
					list.add(tmp+userSign);
				}
				
			}

		}
		splitMsg.setMsgs(list.toArray(new String[0]));
		if (isLongSmSupport && list.size() > 1)
		{
			splitMsg.setMsgFmt(MsgFmt.UCS2);
			splitMsg.setLongSms(true);
		} else
		{
			splitMsg.setMsgFmt(MsgFmt.GBK);
		}
		return splitMsg;
	}
	/**
	 * ����è��ֳ����ŷ���
	 * 
	 * @param content
	 *            String
	 * @return ArrayList
	 */
	public static String[] splitLongSm(String content)
	{
		List<String> ret = new ArrayList<String>();
		if (content.length() <= 70)
		{
			ret.add(content);
		} else
		{
			int word_count = content.length();
			int pk_total = 1;
			if (word_count > 70)
			{
				pk_total = word_count / 65;
				if (word_count % 65 != 0)
					pk_total++;
			}

			for (int pk_number = 1; pk_number <= pk_total; pk_number++)
			{
				int endindex = Math.min(pk_number * 65, word_count);

				String tmp = content;
				if (pk_total > 1)
					tmp = "(" + pk_number + "/" + pk_total + ")"
							+ content.substring((pk_number - 1) * 65, endindex);
				ret.add(tmp);

			}

		}
		return ret.toArray(new String[0]);
	}

	public static void main(String[] args)
	{

		String content = "AsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkAsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkAsdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasdfkasd";

		// String content =
		// "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

		// String content =
		// "�žŰ˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһһ������������ʮһһ������������ʮһһ������������ʮһһ������������ʮһ";//"��ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ������������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ���������ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������һ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ�����������߰˾�ʮһ������";

		// String content =
		// "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

		System.out.println(content.length());

		/* SmsSplit smsSplit = new SmsSplit(content, true, 10, 140, true, "���","(1/2)");
		 SplitMsg splitMsg = smsSplit.splitSm();
		 String[] msgs = splitMsg.getMsgs();
		 for (int i = 0; i < msgs.length; i++)
		{
		 System.out.println((i + 1) + ": " + msgs[i].length() + " " +
		 msgs[i]);
		 }*/
		 SmsSplit smsSplit = new SmsSplit(content, true, 10, 140, true, "���","(1/2)","");
		 SplitMsg splitMsg = smsSplit.splitSm();
	}
}
