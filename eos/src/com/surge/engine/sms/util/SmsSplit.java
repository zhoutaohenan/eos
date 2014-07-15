/*
 * Title: mas20 @author Administrator Created on 2008-4-8
 */

package com.surge.engine.sms.util;

import java.util.ArrayList;
import java.util.List;

import com.surge.engine.sms.common.MsgFmt;
import com.surge.engine.sms.pojo.SplitMsg;

/**
 * 短信拆分
 * 
 * @author Administrator
 * 
 */
public class SmsSplit
{

	/** 中文短信要加的前缀长度（1/2） */
	private static int PERFIX_LEN = 5;

	/** 未拆分的短信内容 */
	private String content;

	/** 是否支持长短信 */
	private boolean isLongSmSupport = false;

	/** 中文短信签名 **/
	private String ismgSignCn;

	/** 英文短信签名 **/
	private String ismgSignEn;

	/** 一条中文短信长度 **/
	private int smsLengthCn;

	/** 一条英文短信长度 **/
	private int smsLengthEn;

	/** 是否每条都需要短信签名 */
	private boolean isSmsSign;

	/** 用户签名 **/
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
			// 临时写法
			PERFIX_LEN = 7;
		}

	}

	/**
	 * 普通中英文短信拆分
	 * 
	 * @param content
	 * @param msgFmt
	 * @param smsType
	 * @return
	 */
	public SplitMsg splitSm()
	{
		/** 内容是否是纯英文的 */
		boolean isEnglish = CharsetTools.isStringValidate(content);
		/** 英文短信的网关签名是否是纯英文的 */
		boolean ismgSignEnIsEn = CharsetTools.isStringValidate(ismgSignEn);
		/** 用户签名是否是纯英文 **/
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
		// 加网关签名后一条短信的长度
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
		int lastMsgLen = msgLenAddSign;// 最后一条短信的长度，支持长短信和不支持长短信不同
		int contLen = smsLengthEn; // 一条中文短信短信的标准长度
		int word_count = content.length();// 短信内容的总体长度
		int pk_total = 1;
		if (contLen > word_count)
			contLen = word_count;
		if (word_count > msgLenAddSign) // 如果短信内容大于加网关签名后一条短信的长度需要拆分
		{
			if (isLongSmSupport) // 如果网关支持长短信（合起来的短信），内容需要加特殊字节，此种情况只最后一条
			{
				lastMsgLen -= 6;
				contLen -= 6;
				if (isSmsSign && isSpSign)
				{
					contLen = msgLenAddSign - 6;
				}
			} else
			// 如果网关不支持长短信，加前缀如：（1/2）
			{

				lastMsgLen -= PERFIX_LEN;
				if (isSmsSign)
				{
					// 如果网关不支长持短信，并且每条拆分都需要加签名
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
		// 截取后最后一条短信的长度
		int lastLen = content.substring((pk_total - 1) * contLen, content.length()).length();
		boolean isSpillage = false;
		// 如果最后一条长度超长，则再分一条
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
						if (isSpSign)// 若是网关签名
						{
							tmp = content.substring(beginIndex, endindex);
						} else
						// 若用户签名，最后一条短信内容后加签名
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

		// 加网关签名后一条短信的长度
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
		int lastMsgLen = msgLenAddSign;// 最后一条短信的长度，支持长短信和不支持长短信不同
		int contLen = smsLengthCn; // 一条中文短信短信的标准长度70个字符
		int word_count = content.length();// 短信内容的总体长度
		int pk_total = 1;
		if (contLen > word_count)
			contLen = word_count;
		if (word_count > msgLenAddSign) // 如果短信内容大于加网关签名后一条短信的长度需要拆分
		{
			if (isLongSmSupport) // 如果网关支持长短信（合起来的短信），内容需要加特殊字节，此种情况只最后一条
			{

				lastMsgLen -= 3;
				contLen -= 3;
				if (isSmsSign && isSpSign)
				{
					contLen = msgLenAddSign - 3;
				}

			} else
			// 如果网关不支持长短信，加前缀如：（1/2）
			{

				lastMsgLen -= PERFIX_LEN;
				if (isSpSign)
				{
					if (isSmsSign)
					{
						// 如果网关不支长持短信，并且每条拆分都需要加签名
						contLen = msgLenAddSign - PERFIX_LEN;
					} else
					{
						contLen -= PERFIX_LEN;
					}
				} else
				{
					// 如果网关不支长持短信，并且是用户签名
					contLen = msgLenAddSign - PERFIX_LEN;
				}

			}
			pk_total = word_count / contLen;
			if (word_count % contLen != 0)
				pk_total++;
		}
		// 截取后最后一条短信的长度
		int lastLen = content.substring((pk_total - 1) * contLen, content.length()).length();
		boolean isSpillage = false;
		// 如果最后一条长度超长，则再分一条
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
	 * 短信猫拆分长短信方法
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
		// "九九八九十一二三四五六七八九十一二三四一二三四五六九十一一二三四五六九十一一二三四五六九十一一二三四五六九十一一二三四五六九十一";//"九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四";

		// String content =
		// "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

		System.out.println(content.length());

		/* SmsSplit smsSplit = new SmsSplit(content, true, 10, 140, true, "你好","(1/2)");
		 SplitMsg splitMsg = smsSplit.splitSm();
		 String[] msgs = splitMsg.getMsgs();
		 for (int i = 0; i < msgs.length; i++)
		{
		 System.out.println((i + 1) + ": " + msgs[i].length() + " " +
		 msgs[i]);
		 }*/
		 SmsSplit smsSplit = new SmsSplit(content, true, 10, 140, true, "你好","(1/2)","");
		 SplitMsg splitMsg = smsSplit.splitSm();
	}
}
