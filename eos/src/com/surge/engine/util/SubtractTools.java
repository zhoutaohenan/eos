
package com.surge.engine.util;

import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;

public class SubtractTools
{

	private byte[] buf = null;

	private int pos = 0;

	public SubtractTools(byte[] buf, int offset)
	{

		this.buf = buf;
		this.pos = offset;
	}

	public void reset(byte[] buf, int offset)
	{

		this.buf = buf;
		this.pos = offset;
	}

	public byte getByte()
	{

		byte ret = buf[pos];
		pos++;
		return ret;
	}

	public short getShort()
	{

		byte[] bys = getBytes(2);
		short ret = DataTools.bytes2Short(bys, 0);
		return ret;
	}

	public int getInt()
	{

		byte[] bys = getBytes(4);
		int ret = DataTools.bytes2Int(bys, 0);
		return ret;
	}

	public long getLong()
	{

		byte[] bys = getBytes(8);
		long ret = DataTools.bytes2Long(bys, 0);
		return ret;
	}

	public byte[] getBytes(int len)
	{

		if (len <= 0)
			return null;
		byte[] ret = new byte[len];
		System.arraycopy(buf, pos, ret, 0, len);
		pos += len;
		return ret;
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ���Զ�����0Ϊ���������ַ���
	 * 
	 * @return ����byte�������ɵ��ַ���(ȱʡ����)
	 */
	public String getCString()
	{

		String ret = null;

		int i = pos;
		int len = buf.length;
		for (i = pos; i < len; i++)
		{
			if (buf[i] == 0)
				break;
		}
		if (i == len)
			return null;

		len = i - pos;
		pos += len + 1;
		ret = new String(buf, pos - len - 1, len);
		return ret;
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ���Զ�����0Ϊ���������ַ���
	 * 
	 * @param charset
	 *            �ַ�������
	 * 
	 * @return ����byte�������ɵ��ַ���(ȱʡ����)
	 * @throws UnsupportedEncodingException
	 *             ��charset��������Stringʧ�����׳����쳣
	 */
	public String getCString(String charset)
			throws UnsupportedEncodingException
	{

		String ret = null;

		int i = pos;
		int len = buf.length;
		for (i = pos; i < len; i++)
		{
			if (buf[i] == 0)
				break;
		}
		if (i == len)
			return null;

		len = i - pos;
		pos += len + 1;
		ret = new String(buf, pos - len - 1, len, charset);
		return ret;
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ���ַ���
	 * 
	 * @param len
	 *            �ӵ�ǰλ��ת�����ַ����Ķ�����byte����
	 * @return ����byte�������ɵ��ַ���(ȱʡ����)
	 */
	public String getString(int len)
	{

		if (len <= 0)
			return null;
		pos += len;
		String ret = new String(buf, pos - len, len);
		return ret.trim();
	}

	public String getString(int len, String charset)
			throws UnsupportedEncodingException
	{

		if (len <= 0)
			return null;
		pos += len;
		String ret = new String(buf, pos - len, len, charset);
		return ret;
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ��UTF-8�ַ���
	 * 
	 * 
	 * @return UTF-8�ַ���
	 * 
	 * @throws UTFDataFormatException
	 *             �����ȡ���ֽ����鲻��һ����Ч��java UTF-8�ַ������׳����쳣.
	 */
	public String getUTF() throws UTFDataFormatException
	{

		int utflen = getShort();
		if (utflen < 0)
			utflen += 65536;

		if (utflen > 65535)
			throw new UTFDataFormatException("UTF-8�ַ�������̫��(" + utflen + ")");

		byte[] bytearr = getBytes(utflen);

		StringBuffer str = new StringBuffer(utflen);
		int c, char2, char3;
		int count = 0;

		while (count < utflen)
		{
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:

				/* 0xxxxxxx */
				count++;
				str.append((char) c);
				break;
			case 12:
			case 13:

				/* 110x xxxx 10xx xxxx */
				count += 2;
				if (count > utflen)
					throw new UTFDataFormatException("13 count(" + count
							+ ") > utflen(" + utflen + ")");
				char2 = (int) bytearr[count - 1];
				if ((char2 & 0xC0) != 0x80)
					throw new UTFDataFormatException("13 (char2(" + char2
							+ ") & 0xC0) != 0x80");
				str.append((char) (((c & 0x1F) << 6) | (char2 & 0x3F)));
				break;
			case 14:

				/* 1110 xxxx 10xx xxxx 10xx xxxx */
				count += 3;
				if (count > utflen)
					throw new UTFDataFormatException("14 count(" + count
							+ ") > utflen(" + utflen + ")");
				char2 = (int) bytearr[count - 2];
				char3 = (int) bytearr[count - 1];
				if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
					throw new UTFDataFormatException("14 (char2(" + char2
							+ ") & 0xC0) != 0x80 || (char3(" + char3
							+ ") & 0xC0) != 0x80");
				str
						.append((char) (((c & 0x0F) << 12)
								| ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0)));
				break;
			default:

				/* 10xx xxxx, 1111 xxxx */
				throw new UTFDataFormatException("default(" + c + ")");
			}
		}
		// The number of chars produced may be less than utflen
		return new String(str);
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ���ַ�����ȥ���ַ������˿ո�
	 * 
	 * @param len
	 *            �ӵ�ǰλ��ת�����ַ����Ķ�����byte����
	 * @return ����byte�������ɵ��ַ���(ȱʡ����)
	 */
	public String getTrimString(int len)
	{

		String ret = getString(len);
		return ret.trim();
	}

	public String getTrimString(int len, String charset)
			throws UnsupportedEncodingException
	{

		String ret = getString(len, charset);
		return ret.trim();
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ���Զ�����0Ϊ���������ַ�����ȥ���ַ������˿ո�
	 * 
	 * @return ����byte�������ɵ��ַ���(ȱʡ����)
	 */
	public String getTrimCString()
	{

		String ret = getCString();
		return ret.trim();
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ���Զ�����0Ϊ���������ַ�����ȥ���ַ������˿ո�
	 * 
	 * @param charset
	 *            �ַ�������
	 * 
	 * @return ����byte�������ɵ��ַ���(ȱʡ����)
	 * @throws UnsupportedEncodingException
	 *             ��charset��������Stringʧ�����׳����쳣
	 */
	public String getTrimCString(String charset)
			throws UnsupportedEncodingException
	{

		String ret = getCString(charset);
		return ret.trim();
	}

	/**
	 * �ӻ�����������ݶ����еĵ�ǰλ�û��һ��UTF-8�ַ�����ȥ���ַ������˿ո�
	 * 
	 * 
	 * @return UTF-8�ַ���
	 * 
	 * @throws UTFDataFormatException
	 *             �����ȡ���ֽ����鲻��һ����Ч��java UTF-8�ַ������׳����쳣.
	 */
	public String getTrimUTF() throws UTFDataFormatException
	{

		String ret = getUTF();
		return ret.trim();
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
}
