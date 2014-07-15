
package com.surge.engine.protocol.sms.gw.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;

/**
 * @description 
 * @project: eskprj
 * @Date:2010-8-16
 * @version  1.0
 * @Company: 33e9
 * @author glping
 */
public class GwTools
{
	private static int sequence_id = 0;

	private static String sequenceStr = "";

	private static Random rd = new Random(System.currentTimeMillis());

	public static synchronized int getSequenceID()
	{
		if (sequence_id > 1000000000)
			sequence_id = 0;
		sequence_id += 1;
		return sequence_id;
	}
	public static synchronized String getSequenceStr()
	{
		sequenceStr = String.valueOf(getSequenceID());
		return sequenceStr;
	}

	public static int random(int n)
	{
		int ret = 0;
		while (ret == 0)
		{
			ret = rd.nextInt(n);
		}
		return ret;
	}

	public static boolean isSameDay(int date0, int date1)
	{
		return (date0 == date1);
	}

	public static boolean isSameWeek(int date0, int date1)
	{
		int y0 = date0 / 10000;
		int m0 = date0 % 10000 / 100;
		int d0 = date0 % 100;

		int y1 = date1 / 10000;
		int m1 = date1 % 10000 / 100;
		int d1 = date1 % 100;

		return ((y0 == y1) && (m0 == m1) && (d0 / 7 == d1 / 7));
	}

	public static boolean isSameMonth(int date0, int date1)
	{
		int y0 = date0 / 10000;
		int m0 = date0 % 10000 / 100;

		int y1 = date1 / 10000;
		int m1 = date1 % 10000 / 100;

		return ((y0 == y1) && (m0 == m1));
	}

	public static int getDate()
	{
		Calendar cal = Calendar.getInstance();
		return (cal.get(1) * 10000 + (cal.get(2) + 1) * 100 + cal.get(5));
	}

	public static int getDate(Calendar cal)
	{
		return (cal.get(1) * 10000 + (cal.get(2) + 1) * 100 + cal.get(5));
	}

	public static int getTime()
	{
		Calendar cal = Calendar.getInstance();
		return (cal.get(11) * 3600 + cal.get(12) * 60 + cal.get(13));
	}

	public static int getTime(Calendar cal)
	{
		return (cal.get(11) * 3600 + cal.get(12) * 60 + cal.get(13));
	}

	public static String getTimeStr()
	{
		Calendar cal = Calendar.getInstance();
		int h = cal.get(11);
		int m = cal.get(12);
		int s = cal.get(13);

		StringBuffer sb = new StringBuffer(10);
		if (h < 10)
			sb.append(0);
		sb.append(h);
		sb.append(":");
		if (m < 10)
			sb.append(0);
		sb.append(m);
		sb.append(":");
		if (s < 10)
			sb.append(0);
		sb.append(s);

		return sb.toString();
	}

	public static String getTimeStr(Calendar cal)
	{
		int h = cal.get(11);
		int m = cal.get(12);
		int s = cal.get(13);

		StringBuffer sb = new StringBuffer(10);
		if (h < 10)
			sb.append(0);
		sb.append(h);
		sb.append(":");
		if (m < 10)
			sb.append(0);
		sb.append(m);
		sb.append(":");
		if (s < 10)
			sb.append(0);
		sb.append(s);

		return sb.toString();
	}

	public static String getTimeStr(long seconds)
	{
		long h = seconds / 3600L;
		long m = seconds % 3600L / 60L;
		long s = seconds % 60L;

		StringBuffer sb = new StringBuffer(10);
		if (h < 10L)
			sb.append(0);
		sb.append(h);
		sb.append(":");
		if (m < 10L)
			sb.append(0);
		sb.append(m);
		sb.append(":");
		if (s < 10L)
			sb.append(0);
		sb.append(s);

		return sb.toString();
	}

	public static String getFullDateTimeStr()
	{
		Calendar cal = Calendar.getInstance();
		long ymd = cal.get(1) * 10000 + (cal.get(2) + 1) * 100 + cal.get(5);
		long hms = cal.get(11) * 10000 + cal.get(12) * 100 + cal.get(13);
		StringBuffer sb = new StringBuffer(20);
		sb.append(ymd);
		sb.append(hms);
		return sb.toString();
	}

	public static String getFullDateTimeStr(Calendar cal)
	{
		long ymd = cal.get(1) * 10000 + (cal.get(2) + 1) * 100 + cal.get(5);
		long hms = cal.get(11) * 10000 + cal.get(12) * 100 + cal.get(13);
		StringBuffer sb = new StringBuffer(20);
		sb.append(ymd);
		sb.append(hms);
		return sb.toString();
	}

	public static boolean isDigit(String str)
	{
		int len = str.length();
		boolean bRet = true;
		for (int i = 0; i < len; ++i)
		{
			char c = str.charAt(i);
			if (!(Character.isDigit(c)))
			{
				bRet = false;
				break;
			}
		}
		return bRet;
	}

	public static boolean isValidMobile(String str)
	{
		return ((str != null) && (str.length() == 11) && (str.startsWith("13")) && (isDigit(str)));
	}

	public static String getFileExtName(String fileName)
	{
		String ret = null;
		if ((fileName == null) || (fileName.length() < 1))
			return ret;

		int i = fileName.lastIndexOf(".");
		return fileName.substring(i + 1, fileName.length());
	}

	public static void chars2Bytes(char[] chs, byte[] buffer, int offset)
	{
		for (int i = 0; i < chs.length; ++i)
		{
			char c = chs[i];
			buffer[(offset + 2 * i)] = (byte) c;
			c = (char) (c >> '\b');
			buffer[(offset + 2 * i + 1)] = (byte) c;
		}
	}

	public static void char2Bytes(char c, byte[] buffer, int offset)
	{
		char ch = c;
		buffer[offset] = (byte) ch;
		ch = (char) (ch >> '\b');
		buffer[(offset + 1)] = (byte) ch;
	}

	public static void short2Bytes(short i, byte[] buffer, int offset)
	{
		buffer[(offset + 1)] = (byte) i;
		i = (short) (i >> 8);
		buffer[offset] = (byte) i;
	}

	public static short bytes2Short(byte[] buffer, int offset)
	{
		short ret = 0;
		ret = (short) (ret | buffer[offset]);
		ret = (short) (ret << 8);

		ret = (short) (ret | ((buffer[(offset + 1)] < 0) ? buffer[(offset + 1)] + 256
				: buffer[(offset + 1)]));
		return ret;
	}

	public static void int2Bytes(int i, byte[] buffer, int offset)
	{
		buffer[(offset + 3)] = (byte) i;
		i >>= 8;
		buffer[(offset + 2)] = (byte) i;
		i >>= 8;
		buffer[(offset + 1)] = (byte) i;
		i >>= 8;
		buffer[offset] = (byte) i;
	}

	public static void int2Bytes(long i, byte[] buffer, int offset)
	{
		buffer[(offset + 3)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 2)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 1)] = (byte) (int) i;
		i >>= 8;
		buffer[offset] = (byte) (int) i;
	}

	public static long bytes2Int2(byte[] buffer, int offset)
	{
		long ret = 0L;

		ret = ret | ((buffer[offset] < 0) ? buffer[offset] + 256 : buffer[offset]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 1)] < 0) ? buffer[(offset + 1)] + 256 : buffer[(offset + 1)]);

		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 2)] < 0) ? buffer[(offset + 2)] + 256 : buffer[(offset + 2)]);

		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 3)] < 0) ? buffer[(offset + 3)] + 256 : buffer[(offset + 3)]);

		return ret;
	}

	public static int bytes2Int(byte[] buffer, int offset)
	{
		int ret = 0;
		ret |= buffer[offset];
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 1)] < 0) ? buffer[(offset + 1)] + 256 : buffer[(offset + 1)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 2)] < 0) ? buffer[(offset + 2)] + 256 : buffer[(offset + 2)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 3)] < 0) ? buffer[(offset + 3)] + 256 : buffer[(offset + 3)]);
		return ret;
	}

	public static void long2Bytes(long i, byte[] buffer, int offset)
	{
		buffer[(offset + 7)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 6)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 5)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 4)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 3)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 2)] = (byte) (int) i;
		i >>= 8;
		buffer[(offset + 1)] = (byte) (int) i;
		i >>= 8;
		buffer[offset] = (byte) (int) i;
	}

	public static long bytes2Long(byte[] buffer, int offset)
	{
		long ret = 0L;
		ret |= buffer[offset];
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 1)] < 0) ? buffer[(offset + 1)] + 256 : buffer[(offset + 1)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 2)] < 0) ? buffer[(offset + 2)] + 256 : buffer[(offset + 2)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 3)] < 0) ? buffer[(offset + 3)] + 256 : buffer[(offset + 3)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 4)] < 0) ? buffer[(offset + 4)] + 256 : buffer[(offset + 4)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 5)] < 0) ? buffer[(offset + 5)] + 256 : buffer[(offset + 5)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 6)] < 0) ? buffer[(offset + 6)] + 256 : buffer[(offset + 6)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 7)] < 0) ? buffer[(offset + 7)] + 256 : buffer[(offset + 7)]);
		return ret;
	}

	public static short sBytes2Short(byte[] buffer, int offset)
	{
		short ret = 0;
		ret = (short) (ret | buffer[(offset + 1)]);
		ret = (short) (ret << 8);
		ret = (short) (ret | ((buffer[offset] < 0) ? buffer[offset] + 256 : buffer[offset]));
		return ret;
	}

	public static int sBytes2Int(byte[] buffer, int offset)
	{
		int ret = 0;
		ret |= buffer[(offset + 3)];
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 2)] < 0) ? buffer[(offset + 2)] + 256 : buffer[(offset + 2)]);
		ret <<= 8;

		ret = ret
				| ((buffer[(offset + 1)] < 0) ? buffer[(offset + 1)] + 256 : buffer[(offset + 1)]);
		ret <<= 8;
		ret |= ((buffer[offset] < 0) ? buffer[offset] + 256 : buffer[offset]);
		return ret;
	}

	public static float bytes2Float(byte[] buffer, int offset)
	{
		return Float.intBitsToFloat(bytes2Int(buffer, offset));
	}

	public static byte[] hex2Binary(String hex)
	{
		if ((hex == null) || (hex.length() < 1))
			return null;

		if ((hex.startsWith("0x")) || (hex.startsWith("0X")))
			hex = hex.substring(2);

		byte[] bys = (byte[]) null;
		int len = hex.length();
		if (len % 2 != 0)
		{
			hex = hex + "0";
			++len;
		}
		bys = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
		{
			String tmp = hex.substring(i, i + 2);
			int v = Integer.parseInt(tmp, 16);
			bys[(i / 2)] = (byte) v;
		}
		return bys;
	}

	public static String binary2Hex(byte[] bys)
	{
		if ((bys == null) || (bys.length < 1))
			return null;

		StringBuffer sb = new StringBuffer(100);

		for (int i = 0; i < bys.length; ++i)
			if (bys[i] >= 16)
				sb.append(Integer.toHexString(bys[i]));
			else if (bys[i] >= 0)
				sb.append("0" + Integer.toHexString(bys[i]));
			else
				sb.append(Integer.toHexString(bys[i]).substring(6, 8));

		return sb.toString();
	}

	public static byte[] read(InputStream in, int size) throws IOException
	{
		byte[] bys = new byte[size];
		int b = 0;
		while (b < size)
		{
			int a = in.read(bys, b, size - b);
			if (a == -1)
				throw new IOException("the length is not enough to read data from in");
			b += a;
		}
		return bys;
	}

	public static byte[] readFile(String fileName) throws IOException
	{
		FileInputStream fin = null;
		byte[] ret = (byte[]) null;
		try
		{
			fin = new FileInputStream(fileName);
			int size = fin.available();
			ret = read(fin, size);
		} finally
		{
			if (fin != null)
				fin.close();
		}
		return ret;
	}

	public static void convert(String srcFileName, String srcCharset, String destFileName,
			String destCharset) throws IOException
	{
		byte[] bys = readFile(srcFileName);
		String str = new String(bys, srcCharset);
		bys = str.getBytes(destCharset);
		FileOutputStream out = new FileOutputStream(destFileName, false);
		out.write(bys);
		out.close();
	}
}