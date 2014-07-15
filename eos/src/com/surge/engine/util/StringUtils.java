package com.surge.engine.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class StringUtils
{

	// Constants used by escapeHTMLTags
	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();

	private static final char[] AMP_ENCODE = "&amp;".toCharArray();

	private static final char[] LT_ENCODE = "&lt;".toCharArray();

	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	/**
	 * 替换字符串
	 * 
	 * 
	 * @param line
	 *            源字符串
	 * @param oldString
	 *            被替换的子字符串
	 * @param newString
	 *            新子字符串
	 * 
	 * @return String 新字符串
	 */
	public static final String replace(String line, String oldString, String newString)
	{

		if (line == null)
		{
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0)
		{

			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0)
			{
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * 替换字符串
	 * 
	 * 
	 * @param line
	 *            源字符串
	 * @param oldString
	 *            被替换的子字符串
	 * @param newString
	 *            新子字符串
	 * 
	 * @return String 新字符串
	 */
	public static final String replaceFirst(String line, String oldString, String newString)
	{

		if (line == null)
		{
			return null;
		}

		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0)
		{
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;

			buf.append(line2, i, line2.length - i);
			return buf.toString();
		}

		return line;
	}

	/**
	 * 替换字符串，不区分大小写
	 * 
	 * @param line
	 *            源字符串
	 * @param oldString
	 *            被替换的子字符串
	 * @param newString
	 *            新子字符串
	 * 
	 * @return String 新字符串
	 */
	public static final String replaceIgnoreCase(String line, String oldString, String newString)
	{

		if (line == null)
		{
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0)
		{
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0)
			{
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * 替换字符串，不区分大小写，并返回替换的个数
	 * 
	 * 
	 * @param line
	 *            源字符串
	 * @param oldString
	 *            被替换的子字符串
	 * @param newString
	 *            新子字符串
	 * 
	 * @param count
	 *            被替换的个数，在返回时更新这个值
	 * 
	 * @return 新字符串
	 */
	public static final String replaceIgnoreCase(String line, String oldString, String newString,
			int[] count)
	{

		if (line == null)
		{
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0)
		{
			int counter = 0;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0)
			{
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	/**
	 * 替换字符串，并返回替换的个数
	 * 
	 * @param line
	 *            源字符串
	 * @param oldString
	 *            被替换的子字符串
	 * @param newString
	 *            新子字符串
	 * 
	 * @param count
	 *            被替换的个数，在返回时更新这个值
	 * 
	 * @return 新字符串
	 */
	public static final String replace(String line, String oldString, String newString, int[] count)
	{

		if (line == null)
		{
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0)
		{
			int counter = 0;
			counter++;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0)
			{
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	/**
	 * 将带HTML标记的字符串中的“<”“>”标记替换成“&lt;”，“&gt;”
	 * 
	 * 
	 * @param in
	 *            要替换的字符串
	 * 
	 * @return String 替换后的字符串
	 */
	public static final String escapeHTMLTags(String in)
	{

		if (in == null)
		{
			return null;
		}
		char ch;
		int i = 0;
		int last = 0;
		char[] input = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3));
		for (; i < len; i++)
		{
			ch = input[i];
			if (ch > '>')
			{
				continue;
			} else if (ch == '<')
			{
				if (i > last)
				{
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '>')
			{
				if (i > last)
				{
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(GT_ENCODE);
			}
		}
		if (last == 0)
		{
			return in;
		}
		if (i > last)
		{
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	/** Used by the hash method. */
	private static MessageDigest digest = null;

	/**
	 * Hashes a String using the Md5 algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes a
	 * bottleneck in your code, you may wish to maintain a pool of MessageDigest
	 * objects instead of using this method.
	 * <p/>
	 * A hash is a one-way function -- that is, given an input, an output is
	 * easily computed. However, given the output, the input is almost
	 * impossible to compute. This is useful for passwords since we can store
	 * the hash and a hacker will then have a very hard time determining the
	 * original password.
	 * <p/>
	 * In Jive, every time a user logs in, we simply take their plain text
	 * password, compute the hash, and compare the generated hash to the stored
	 * hash. Since it is almost impossible that two passwords will generate the
	 * same hash, we know if the user gave us the correct password or not. The
	 * only negative to this system is that password recovery is basically
	 * impossible. Therefore, a reset password method is used instead.
	 * 
	 * @param data
	 *            the String to compute the hash of.
	 * @return a hashed version of the passed-in String
	 */
	public synchronized static final String md5encoding(String data)
	{

		if (digest == null)
		{
			try
			{
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae)
			{
				nsae.printStackTrace();
			}
		}
		// Now, compute hash.
		digest.update(data.getBytes());
		return encodeHex(digest.digest());
	}

	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p/>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 * 
	 * @param bytes
	 *            an array of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static final String encodeHex(byte[] bytes)
	{

		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;

		for (i = 0; i < bytes.length; i++)
		{
			if (((int) bytes[i] & 0xff) < 0x10)
			{
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * Turns a hex encoded string into a byte array. It is specifically meant to
	 * "reverse" the toHex(byte[]) method.
	 * 
	 * @param hex
	 *            a hex encoded String to transform into a byte array.
	 * @return a byte array representing the hex String[
	 */
	public static final byte[] decodeHex(String hex)
	{

		if (hex == null)
			return new byte[0];

		char[] chars = hex.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2)
		{
			byte newByte = 0x00;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = newByte;
			byteCount++;
		}
		return bytes;
	}

	/**
	 * Returns the the byte value of a hexadecmical char (0-f). It's assumed
	 * that the hexidecimal chars are lower case as appropriate.
	 * 
	 * @param ch
	 *            a hexedicmal character (0-f)
	 * @return the byte value of the character (0x00-0x0F)
	 */
	private static final byte hexCharToByte(char ch)
	{

		switch (ch)
		{
		case '0':
			return 0x00;
		case '1':
			return 0x01;
		case '2':
			return 0x02;
		case '3':
			return 0x03;
		case '4':
			return 0x04;
		case '5':
			return 0x05;
		case '6':
			return 0x06;
		case '7':
			return 0x07;
		case '8':
			return 0x08;
		case '9':
			return 0x09;
		case 'a':
			return 0x0A;
		case 'b':
			return 0x0B;
		case 'c':
			return 0x0C;
		case 'd':
			return 0x0D;
		case 'e':
			return 0x0E;
		case 'f':
			return 0x0F;
		}
		return 0x00;
	}

	// *********************************************************************
	// * Base64 - a simple base64 encoder and decoder.
	// *
	// * Copyright (c) 1999, Bob Withers - bwit@pobox.com
	// *
	// * This code may be freely used for any purpose, either personal
	// * or commercial, provided the authors copyright notice remains
	// * intact.
	// *********************************************************************

	/**
	 * Encodes a String as a base64 String.
	 * 
	 * @param data
	 *            a String to encode.
	 * @return a base64 encoded String.
	 */
	public static String encodeBase64(String data)
	{

		return encodeBase64(data.getBytes());
	}

	/**
	 * Encodes a byte array into a base64 String.
	 * 
	 * @param data
	 *            a byte array to encode.
	 * @return a base64 encode String.
	 */
	public static String encodeBase64(byte[] data)
	{

		if (data == null)
			return "";

		int c;
		int len = data.length;
		StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
		for (int i = 0; i < len; ++i)
		{
			c = (data[i] >> 2) & 0x3f;
			ret.append(cvt.charAt(c));
			c = (data[i] << 4) & 0x3f;
			if (++i < len)
				c |= (data[i] >> 4) & 0x0f;

			ret.append(cvt.charAt(c));
			if (i < len)
			{
				c = (data[i] << 2) & 0x3f;
				if (++i < len)
					c |= (data[i] >> 6) & 0x03;

				ret.append(cvt.charAt(c));
			} else
			{
				++i;
				ret.append((char) fillchar);
			}

			if (i < len)
			{
				c = data[i] & 0x3f;
				ret.append(cvt.charAt(c));
			} else
			{
				ret.append((char) fillchar);
			}
		}
		return ret.toString();
	}

	/**
	 * Decodes a base64 String.
	 * 
	 * @param data
	 *            a base64 encoded String to decode.
	 * @return the decoded String.
	 */
	public static String decodeBase64(String data)
	{

		return decodeBase64(data.getBytes());
	}

	/**
	 * Decodes a base64 aray of bytes.
	 * 
	 * @param data
	 *            a base64 encode byte array to decode.
	 * @return the decoded String.
	 */
	public static String decodeBase64(byte[] data)
	{

		if (data == null)
			return "";

		int c, c1;
		int len = data.length;
		StringBuffer ret = new StringBuffer((len * 3) / 4);
		for (int i = 0; i < len; ++i)
		{
			c = cvt.indexOf(data[i]);
			++i;
			c1 = cvt.indexOf(data[i]);
			c = ((c << 2) | ((c1 >> 4) & 0x3));
			ret.append((char) c);
			if (++i < len)
			{
				c = data[i];
				if (fillchar == c)
					break;

				c = cvt.indexOf((char) c);
				c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
				ret.append((char) c1);
			}

			if (++i < len)
			{
				c1 = data[i];
				if (fillchar == c1)
					break;

				c1 = cvt.indexOf((char) c1);
				c = ((c << 6) & 0xc0) | c1;
				ret.append((char) c);
			}
		}
		return ret.toString();
	}

	private static final int fillchar = '=';

	private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz"
			+ "0123456789+/";

	/**
	 * Converts a line of text into an array of lower case words using a
	 * BreakIterator.wordInstance().
	 * <p>
	 * <p/>
	 * This method is under the Jive Open Source Software License and was
	 * written by Mark Imbriaco.
	 * 
	 * @param text
	 *            a String of text to convert into an array of words
	 * @return text broken up into an array of words.
	 */
	public static final String[] toLowerCaseWordArray(String text)
	{

		if (text == null || text.length() == 0)
		{
			return new String[0];
		}

		ArrayList wordList = new ArrayList();
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
		int start = 0;

		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
				.next())
		{
			String tmp = text.substring(start, end).trim();
			// Remove characters that are not needed.
			tmp = replace(tmp, "+", "");
			tmp = replace(tmp, "/", "");
			tmp = replace(tmp, "\\", "");
			tmp = replace(tmp, "#", "");
			tmp = replace(tmp, "*", "");
			tmp = replace(tmp, ")", "");
			tmp = replace(tmp, "(", "");
			tmp = replace(tmp, "&", "");
			if (tmp.length() > 0)
			{
				wordList.add(tmp);
			}
		}
		return (String[]) wordList.toArray(new String[wordList.size()]);
	}

	/**
	 * Intelligently chops a String at a word boundary (whitespace) that occurs
	 * at the specified index in the argument or before. However, if there is a
	 * newline character before <code>length</code>, the String will be chopped
	 * there. If no newline or whitespace is found in <code>string</code> up to
	 * the index <code>length</code>, the String will chopped at
	 * <code>length</code>.
	 * <p/>
	 * For example, chopAtWord("This is a nice String", 10) will return
	 * "This is a" which is the first word boundary less than or equal to 10
	 * characters into the original String.
	 * 
	 * @param string
	 *            the String to chop.
	 * @param length
	 *            the index in <code>string</code> to start looking for a
	 *            whitespace boundary at.
	 * @return a substring of <code>string</code> whose length is less than or
	 *         equal to <code>length</code>, and that is chopped at whitespace.
	 */
	public static final String chopAtWord(String string, int length)
	{

		if (string == null)
		{
			return string;
		}

		char[] charArray = string.toCharArray();
		int sLength = string.length();
		if (length < sLength)
		{
			sLength = length;
		}

		// First check if there is a newline character before length; if so,
		// chop word there.
		for (int i = 0; i < sLength - 1; i++)
		{
			// Windows
			if (charArray[i] == '\r' && charArray[i + 1] == '\n')
			{
				return string.substring(0, i + 1);
			}
			// Unix
			else if (charArray[i] == '\n')
			{
				return string.substring(0, i);
			}
		}
		// Also check boundary case of Unix newline
		if (charArray[sLength - 1] == '\n')
		{
			return string.substring(0, sLength - 1);
		}

		// Done checking for newline, now see if the total string is less than
		// the specified chop point.
		if (string.length() < length)
		{
			return string;
		}

		// No newline, so chop at the first whitespace.
		for (int i = length - 1; i > 0; i--)
		{
			if (charArray[i] == ' ')
			{
				return string.substring(0, i).trim();
			}
		}

		// Did not find word boundary so return original String chopped at
		// specified length.
		return string.substring(0, length);
	}

	/**
	 * 将字符串中的“<”等字符串替换成“&lt;”等字符串
	 * 
	 * 
	 * @param string
	 *            String 要替换的字符串
	 * 
	 * @return String 替换后的字符串
	 */
	public static final String escapeForXML(String string)
	{

		if (string == null)
		{
			return null;
		}
		char ch;
		int i = 0;
		int last = 0;
		char[] input = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3));
		for (; i < len; i++)
		{
			ch = input[i];
			if (ch > '>')
			{
				continue;
			} else if (ch == '<')
			{
				if (i > last)
				{
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '&')
			{
				if (i > last)
				{
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(AMP_ENCODE);
			} else if (ch == '"')
			{
				if (i > last)
				{
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(QUOTE_ENCODE);
			}
		}
		if (last == 0)
		{
			return string;
		}
		if (i > last)
		{
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	/**
	 * 将字符串中的“&lt;” 等特殊字符串替换成“<”等正常的字符串
	 * 
	 * @param string
	 *            要替换的字符串
	 * 
	 * @return String 替换后的字符串
	 */
	public static final String unescapeFromXML(String string)
	{

		string = replace(string, "&lt;", "<");
		string = replace(string, "&gt;", ">");
		string = replace(string, "&quot;", "\"");
		return replace(string, "&amp;", "&");
	}

	/**
	 * 获得字符串大小：单位k，精确到小数点后2位
	 * 
	 * 
	 * @param string
	 * @return
	 */
	public static final float getStringSize(String string)
	{

		float size = 0f;
		if (string.length() > 0)
		{
			size = string.length() / 1024f;
		}

		size = size * 100;
		if (size > (int) size)
		{
			size = size + 1;
		}
		size = size / 100f;

		DecimalFormat format = new DecimalFormat("##.##");

		return Float.parseFloat(format.format(size));
	}

	private static final char[] zeroArray = "0000000000000000".toCharArray();

	/**
	 * 将字符串的左边补充0
	 * 
	 * @param string
	 *            String 要补充的字符串
	 * 
	 * @param length
	 *            int 补0后的位数，最长16位
	 * 
	 * @return String 补0后的字符串
	 */
	public static final String zeroPadString(String string, int length)
	{

		if (string == null || string.length() > length)
		{
			return string;
		}
		StringBuffer buf = new StringBuffer(length);
		buf.append(zeroArray, 0, length - string.length()).append(string);
		return buf.toString();
	}

	/**
	 * 将日期转换成毫秒
	 * 
	 * @param date
	 *            Date 要转换的日期
	 * @return String 转换后的毫秒数字符串
	 */
	public static final String dateToMillis(Date date)
	{

		return zeroPadString(Long.toString(date.getTime()), 15);
	}

	/**
	 * 清除html标签
	 * 
	 * @return String
	 */
	public static String cleanAndPaste(String html)
	{

		String result = "";
		int strLen = html.split("\\>").length;
		String[] tmpStrOut = new String[strLen];
		String[] tmpStrIn = new String[2];
		tmpStrOut = html.split("\\>");
		for (int i = 0; i < strLen; i++)
		{
			tmpStrIn = tmpStrOut[i].split("\\<");
			result += tmpStrIn[0];
		}
		result = replace(result, "&nbsp;", "");
		return result;
	}

	/**
	 * 递归trim
	 * 
	 * @param baseStr
	 *            目标字符串
	 * @param trimStr
	 *            要替换的字符串
	 * @return
	 * @author Jerry
	 */
	public static String trim(String baseStr, String trimStr)
	{

		if (baseStr == null || baseStr.trim().equals(""))
			return "";
		if (trimStr == null || trimStr.trim().equals(""))
			return baseStr;
		baseStr = baseStr.trim();
		trimStr = trimStr.trim();
		if (baseStr.length() < trimStr.length())
			return baseStr;
		String subStr = baseStr.substring(baseStr.length() - trimStr.length());
		if (subStr.equals(trimStr))
			return trim(baseStr.substring(0, baseStr.length() - trimStr.length()), trimStr);
		return baseStr;
	}

	/**
	 * 确认一个字符串是否为空串
	 * 
	 * @param pStr
	 * @return
	 * @author Jerry
	 */
	public static final boolean emptyOrNull(String pStr)
	{

		if (pStr == null)
			return true;
		return pStr.trim().equals("");

	}

	/**
	 * 将字符串转成指定的编码的内容 TODO
	 * 
	 * @param source
	 *            源字符串
	 * @param code
	 *            编码
	 * @return String
	 * @throws
	 */
	public static final String strConvertEncode(String source, String decode, String encode)
	{
		String result = null;
		if (decode == null || decode.trim().length() < 1)
		{
			decode = "gbk";
		}
		if (encode == null || encode.trim().length() < 1)
		{
			encode = "gbk";
		}
		if (source == null)
		{
			return source;
		}
		try
		{
			byte strs[] = source.getBytes(decode);
			result = new String(strs, encode);
		} catch (UnsupportedEncodingException e)
		{
			result = source;
		}
		return result;
	}

	public static void main(String[] args)
	{

		System.out.println(trim("asdfasdfasdfddd", "dd"));
	}
}
