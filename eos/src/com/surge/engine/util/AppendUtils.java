package com.surge.engine.util;

import java.io.UnsupportedEncodingException;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class AppendUtils
{
	private byte[] buf = null;

	private int pos = 0;

	public AppendUtils(int len)
	{
		if (len < 10)
			len = 10;
		this.buf = new byte[len];
	}

	public void appendByte(byte by)
	{
		checkMem(1);
		buf[pos] = by;
		pos++;
	}

	public void appendShort(short i)
	{
		byte[] tmp = new byte[2];
		DataTools.short2Bytes(i, tmp, 0);
		appendBytes(tmp, 2);
	}

	public void appendInt(int i)
	{
		byte[] tmp = new byte[4];
		DataTools.int2Bytes(i, tmp, 0);
		appendBytes(tmp, 4);
	}

	public void appendLong(long i)
	{
		byte[] tmp = new byte[8];
		DataTools.long2Bytes(i, tmp, 0);
		appendBytes(tmp, 8);
	}

	public void appendBytes(byte[] bys)
	{
		if (bys == null)
			return;
		appendBytes(bys, 0, bys.length);
	}

	public void appendBytes(byte[] bys, int len)
	{
		appendBytes(bys, 0, len);
	}

	public void appendBytes(byte[] bys, int offset, int len)
	{
		checkMem(len);
		if (bys != null)
		{
			int min = Math.min(len, bys.length - offset);
			System.arraycopy(bys, offset, buf, pos, min);
		}
		pos += len;
	}

	/**
	 * ���ַ���������ȱʡ���뷽ʽ����Ϊbytes����ʽ�洢������,���彫�ḽ�Ӷ�����0
	 * 
	 * @param str
	 *            ���洢���ַ���
	 */
	public void appendCString(String str)
	{
		appendString(str);
		appendByte((byte) 0);
	}

	/**
	 * ���ַ���������<code>charset</code>��ʽ����Ϊbytes����ʽ�洢������,���彫�ḽ�Ӷ�����0
	 * 
	 * @param str
	 *            ���洢���ַ���
	 * 
	 * @param charset
	 *            �ַ����ı�������
	 * @throws UnsupportedEncodingException
	 *             ��charset���������׳����쳣
	 */
	public void appendCString(String str, String charset) throws UnsupportedEncodingException
	{
		appendString(str, charset);
		appendByte((byte) 0);
	}

	public void appendString(String str)
	{
		if (str == null)
			return;
		appendString(str, str.length());
	}

	public void appendString(String str, int len)
	{
		checkMem(len);
		if (str != null)
		{
			byte[] bys = str.getBytes();
			int min = Math.min(len, bys.length);
			System.arraycopy(bys, 0, buf, pos, min);
		}
		pos += len;
	}

	public void appendString(String str, String charset) throws UnsupportedEncodingException
	{
		if (str == null)
			return;

		byte[] bys = str.getBytes(charset);
		int len = bys.length;
		checkMem(len);
		System.arraycopy(bys, 0, buf, pos, len);
		pos += len;
	}

	/**
	 * ���ַ���������<code>charset</code>��ʽ����Ϊbytes����ʽ�洢������
	 * 
	 * @param str
	 *            ���洢���ַ���
	 * 
	 * @param charset
	 *            �ַ����ı�������
	 * @param len
	 *            ע��,����ĳ���ָ����ת�����<b>�ֽ�����ĳ���</b>
	 * @throws UnsupportedEncodingException
	 *             ��charset���������׳����쳣
	 */
	public void appendString(String str, String charset, int len)
			throws UnsupportedEncodingException
	{
		checkMem(len);
		if (str != null)
		{
			byte[] bys;
			if (charset != null)
				bys = str.getBytes(charset);
			else
				bys = str.getBytes();
			int min = Math.min(len, bys.length);
			System.arraycopy(bys, 0, buf, pos, min);
		}
		pos += len;
	}

	/**
	 * ���ַ�����UTF8����ʽ�洢��������
	 * 
	 * <p/>
	 * First, two bytes are written to out as if by the <code>writeShort</code>
	 * method giving the number of bytes to follow. This value is the number of
	 * bytes actually written out, not the length of the string. Following the
	 * length, each character of the string is output, in sequence, using the
	 * modified UTF-8 encoding for the character. If no exception is thrown, the
	 * counter <code>written</code> is incremented by the total number of bytes
	 * written to the output stream. This will be at least two plus the length
	 * of <code>str</code>, and at most two plus thrice the length of
	 * <code>str</code>.
	 * 
	 * @param str
	 *            a string to be written.
	 * @return The number of bytes written out.
	 */
	public int appendUTF(String str)
	{
		int strlen = str.length();
		int utflen = 0;
		char[] charr = new char[strlen];
		int c, count = 0;

		str.getChars(0, strlen, charr, 0);

		for (int i = 0; i < strlen; i++)
		{
			c = charr[i];
			if ((c >= 0x0001) && (c <= 0x007F))
			{
				utflen++;
			} else if (c > 0x07FF)
			{
				utflen += 3;
			} else
			{
				utflen += 2;
			}
		}

		if (utflen > 65535)
			throw new IllegalArgumentException("UTF-8�ַ�������̫��(" + utflen + ")");

		byte[] bytearr = new byte[utflen + 2];
		bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
		bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);
		for (int i = 0; i < strlen; i++)
		{
			c = charr[i];
			if ((c >= 0x0001) && (c <= 0x007F))
			{
				bytearr[count++] = (byte) c;
			} else if (c > 0x07FF)
			{
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			} else
			{
				bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			}
		}
		appendBytes(bytearr);
		// out.write(bytearr);
		return utflen + 2;
	}

	public byte get(int positon)
	{
		if (positon < 0 || positon > pos - 1)
			throw new ArrayIndexOutOfBoundsException("get(" + positon + ")");
		return buf[positon];
	}

	public byte[] subBytes(int pos0, int pos1)
	{
		if (pos0 < 0 || pos1 > pos || pos1 < pos0)
			throw new ArrayIndexOutOfBoundsException("pos0:" + pos0 + " pos1:" + pos1);

		byte[] tmp = new byte[pos1 - pos0];
		System.arraycopy(buf, pos0, tmp, 0, pos1 - pos0);

		return tmp;
	}

	public byte[] getOutBytes()
	{
		return subBytes(0, pos);
	}

	/** ���õ�ǰ����,�ȵ�ǰ���ݳ���Ϊ0 */
	public void reset()
	{
		pos = 0;
	}

	/**
	 * ��õ�ǰ���ݵĳ���
	 * 
	 * 
	 * @return int
	 */
	public int size()
	{
		return pos;
	}

	private void checkMem(int len)
	{
		if (buf.length < len + pos)
		{
			int newTotalLen = buf.length * 2;
			if (newTotalLen < len + pos)
				newTotalLen = len + pos;
			byte[] tmp = new byte[newTotalLen];
			System.arraycopy(buf, 0, tmp, 0, buf.length);
			buf = tmp;
		}
	}
}
