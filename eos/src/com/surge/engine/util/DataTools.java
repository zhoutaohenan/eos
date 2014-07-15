package com.surge.engine.util;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class DataTools
{
	/**
	 * char数组转化为byte数组,高字节在前
	 * 
	 * 
	 * @param chs
	 *            待转化的char数组
	 * @param buf
	 *            待存储的byte数组
	 * @param offset
	 *            buf的偏移值
	 */
	public static void chars2Bytes(char[] chs, byte[] buf, int offset)
	{
		for (int i = 0; i < chs.length; i++)
		{
			char c = chs[i];
			buf[offset + 2 * i] = (byte) c;
			c >>= 8;
			buf[offset + 2 * i + 1] = (byte) c;
		}
	}

	/**
	 * char转化为byte型,高字节在前
	 * 
	 * 
	 * @param c
	 *            待转化的的字符
	 * 
	 * @param buf
	 *            待存储的byte数组
	 * @param offset
	 *            buf的偏移值
	 */
	public static void char2Bytes(char c, byte[] buf, int offset)
	{
		char ch = c;
		buf[offset] = (byte) ch;
		ch >>= 8;
		buf[offset + 1] = (byte) ch;
	}

	/**
	 * byte型转化为char,高字节在前
	 * 
	 * 
	 * @param buf
	 *            待转化的的byte数组(长度至少必须为2)
	 * @param offset
	 *            buf的偏移值
	 * 
	 * @return 转化后的char字符
	 */
	public static char bytes2Char(byte[] buf, int offset)
	{
		char ret = 0;
		ret |= buf[offset + 1];
		ret <<= 8;
		byte b = buf[offset];
		ret |= b < 0 ? b + 256 : b;
		return ret;
	}

	/**
	 * short转化为byte数组,高字节在前
	 * 
	 * 
	 * @param i
	 *            待转化的的short型变量
	 * 
	 * @param buf
	 *            待存储的byte数组
	 * @param offset
	 *            buf的偏移值
	 */
	public static void short2Bytes(short i, byte[] buf, int offset)
	{
		buf[offset + 1] = (byte) i;
		i >>= 8;
		buf[offset] = (byte) i;
	}

	/**
	 * byte数组转化为shor型,高字节在前
	 * 
	 * 
	 * @param buf
	 *            待转化的的byte数组(长度至少必须为2)
	 * @param offset
	 *            buf的偏移值
	 * 
	 * @return 转化后的short值
	 */
	public static short bytes2Short(byte[] buf, int offset)
	{
		short ret = 0;
		ret |= buf[offset];
		ret <<= 8;
		ret |= buf[offset + 1] < 0 ? buf[offset + 1] + 256 : buf[offset + 1];
		return ret;
	}

	/**
	 * int转化为byte数组,高字节在前
	 * 
	 * 
	 * @param i
	 *            待转化的的int型变量
	 * 
	 * @param buf
	 *            待存储的byte数组
	 * @param offset
	 *            buf的偏移值
	 */
	public static void int2Bytes(int i, byte[] buf, int offset)
	{
		buf[offset + 3] = (byte) i;
		i >>= 8;
		buf[offset + 2] = (byte) i;
		i >>= 8;
		buf[offset + 1] = (byte) i;
		i >>= 8;
		buf[offset] = (byte) i;
	}

	/**
	 * byte数组转化为int,高字节在前
	 * 
	 * 
	 * @param buf
	 *            待转化的的byte数组(长度至少必须为4)
	 * @param offset
	 *            buf的偏移值
	 * 
	 * @return 转化后的int值
	 */
	public static int bytes2Int(byte[] buf, int offset)
	{
		int ret = 0;
		ret |= buf[offset];
		ret <<= 8;
		ret |= buf[offset + 1] < 0 ? buf[offset + 1] + 256 : buf[offset + 1];
		ret <<= 8;
		ret |= buf[offset + 2] < 0 ? buf[offset + 2] + 256 : buf[offset + 2];
		ret <<= 8;
		ret |= buf[offset + 3] < 0 ? buf[offset + 3] + 256 : buf[offset + 3];
		return ret;
	}

	/**
	 * long转化为byte数组,高字节在前
	 * 
	 * 
	 * @param i
	 *            待转化的的long型变量
	 * 
	 * @param buf
	 *            待存储的byte数组
	 * @param offset
	 *            buf的偏移值
	 */
	public static void long2Bytes(long i, byte[] buf, int offset)
	{
		buf[offset + 7] = (byte) i;
		i >>= 8;
		buf[offset + 6] = (byte) i;
		i >>= 8;
		buf[offset + 5] = (byte) i;
		i >>= 8;
		buf[offset + 4] = (byte) i;
		i >>= 8;
		buf[offset + 3] = (byte) i;
		i >>= 8;
		buf[offset + 2] = (byte) i;
		i >>= 8;
		buf[offset + 1] = (byte) i;
		i >>= 8;
		buf[offset] = (byte) i;
	}

	/**
	 * byte数组转化为long,高字节在前
	 * 
	 * 
	 * @param buf
	 *            待转化的的byte数组(长度至少必须为8)
	 * @param offset
	 *            buf的偏移值
	 * 
	 * @return 转化后的long值
	 */
	public static long bytes2Long(byte[] buf, int offset)
	{
		long ret = 0;
		ret |= buf[offset];
		ret <<= 8;
		ret |= buf[offset + 1] < 0 ? buf[offset + 1] + 256 : buf[offset + 1];
		ret <<= 8;
		ret |= buf[offset + 2] < 0 ? buf[offset + 2] + 256 : buf[offset + 2];
		ret <<= 8;
		ret |= buf[offset + 3] < 0 ? buf[offset + 3] + 256 : buf[offset + 3];
		ret <<= 8;
		ret |= buf[offset + 4] < 0 ? buf[offset + 4] + 256 : buf[offset + 4];
		ret <<= 8;
		ret |= buf[offset + 5] < 0 ? buf[offset + 5] + 256 : buf[offset + 5];
		ret <<= 8;
		ret |= buf[offset + 6] < 0 ? buf[offset + 6] + 256 : buf[offset + 6];
		ret <<= 8;
		ret |= buf[offset + 7] < 0 ? buf[offset + 7] + 256 : buf[offset + 7];
		return ret;
	}

	/**
	 * byte数组转化为short,低字节在前
	 * 
	 * 
	 * @param buf
	 *            待转化的的byte数组(长度至少必须为2)
	 * @param offset
	 *            buf的偏移值
	 * 
	 * @return 转化后的short值
	 */
	public static short sBytes2Short(byte[] buf, int offset)
	{
		short ret = 0;
		ret |= buf[offset + 1];
		ret <<= 8;
		ret |= buf[offset] < 0 ? buf[offset] + 256 : buf[offset];
		return ret;
	}

	/**
	 * byte数组转化为int,低字节在前
	 * 
	 * 
	 * @param buf
	 *            待转化的的byte数组(长度至少必须为4)
	 * @param offset
	 *            buf的偏移值
	 * 
	 * @return 转化后的int值
	 */
	public static int sBytes2Int(byte[] buf, int offset)
	{
		int ret = 0;
		ret |= buf[offset + 3];
		ret <<= 8;
		ret |= buf[offset + 2] < 0 ? buf[offset + 2] + 256 : buf[offset + 2];
		ret <<= 8;
		ret |= buf[offset + 1] < 0 ? buf[offset + 1] + 256 : buf[offset + 1];
		ret <<= 8;
		ret |= buf[offset] < 0 ? buf[offset] + 256 : buf[offset];
		return ret;
	}

	/**
	 * byte数组转化为float,高字节在前
	 * 
	 * 
	 * @param buf
	 *            待转化的的byte数组
	 * @param offset
	 *            buf的偏移值
	 * 
	 * @return 转化后的float值
	 */
	public static float bytes2Float(byte[] buf, int offset)
	{
		return Float.intBitsToFloat(bytes2Int(buf, offset));
	}

	/**
	 * 把十六进制的字符串转化为二进制
	 * 
	 * <p>
	 * 字符串中存放的为AB089D类似这种数据.
	 * 注意:是否0x开头及大小写均可,字符串长度必须为偶数,否则抛出IllegalArgumentException异常
	 * </p>
	 * 
	 * @param hex
	 *            待转化的十六进制的字符串
	 * @return 转化后的byte数组
	 */
	public static byte[] hex2Binary(String hex)
	{
		if (hex == null || hex.length() < 1)
			return null;

		if (hex.startsWith("0x") || hex.startsWith("0X"))
			hex = hex.substring(2);

		int len = hex.length();
		if (len % 2 != 0)
			throw new IllegalArgumentException("待转化的十六进制字符串长度应该为偶数");

		byte[] bys = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
		{
			String tmp = hex.substring(i, i + 2);
			bys[i / 2] = (byte) Integer.parseInt(tmp, 16);
		}
		return bys;
	}

	/**
	 * 把二进制数据转化为十六进制的字符串
	 * 
	 * <p>
	 * 字符串中存放的为AB089D类似这种数据,但是没有0x开头,一个byte对应的字符串长度为2
	 * </p>
	 * 
	 * @param bys
	 *            待转化的二六进制数据
	 * @return 转化后的十六进制字符串,如果bys为空或者长度为0则返回null
	 */
	public static String binary2Hex(byte[] bys)
	{
		if (bys == null || bys.length < 1)
			return null;

		StringBuffer sb = new StringBuffer(100);

		for (byte b : bys)
		{
			if (b >= 16)
				sb.append(Integer.toHexString(b));
			else if (b >= 0)
				sb.append("0" + Integer.toHexString(b));
			else
				sb.append(Integer.toHexString(b).substring(6, 8));
		}

		return sb.toString();
	}
}
