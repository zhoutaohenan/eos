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
	 * char����ת��Ϊbyte����,���ֽ���ǰ
	 * 
	 * 
	 * @param chs
	 *            ��ת����char����
	 * @param buf
	 *            ���洢��byte����
	 * @param offset
	 *            buf��ƫ��ֵ
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
	 * charת��Ϊbyte��,���ֽ���ǰ
	 * 
	 * 
	 * @param c
	 *            ��ת���ĵ��ַ�
	 * 
	 * @param buf
	 *            ���洢��byte����
	 * @param offset
	 *            buf��ƫ��ֵ
	 */
	public static void char2Bytes(char c, byte[] buf, int offset)
	{
		char ch = c;
		buf[offset] = (byte) ch;
		ch >>= 8;
		buf[offset + 1] = (byte) ch;
	}

	/**
	 * byte��ת��Ϊchar,���ֽ���ǰ
	 * 
	 * 
	 * @param buf
	 *            ��ת���ĵ�byte����(�������ٱ���Ϊ2)
	 * @param offset
	 *            buf��ƫ��ֵ
	 * 
	 * @return ת�����char�ַ�
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
	 * shortת��Ϊbyte����,���ֽ���ǰ
	 * 
	 * 
	 * @param i
	 *            ��ת���ĵ�short�ͱ���
	 * 
	 * @param buf
	 *            ���洢��byte����
	 * @param offset
	 *            buf��ƫ��ֵ
	 */
	public static void short2Bytes(short i, byte[] buf, int offset)
	{
		buf[offset + 1] = (byte) i;
		i >>= 8;
		buf[offset] = (byte) i;
	}

	/**
	 * byte����ת��Ϊshor��,���ֽ���ǰ
	 * 
	 * 
	 * @param buf
	 *            ��ת���ĵ�byte����(�������ٱ���Ϊ2)
	 * @param offset
	 *            buf��ƫ��ֵ
	 * 
	 * @return ת�����shortֵ
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
	 * intת��Ϊbyte����,���ֽ���ǰ
	 * 
	 * 
	 * @param i
	 *            ��ת���ĵ�int�ͱ���
	 * 
	 * @param buf
	 *            ���洢��byte����
	 * @param offset
	 *            buf��ƫ��ֵ
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
	 * byte����ת��Ϊint,���ֽ���ǰ
	 * 
	 * 
	 * @param buf
	 *            ��ת���ĵ�byte����(�������ٱ���Ϊ4)
	 * @param offset
	 *            buf��ƫ��ֵ
	 * 
	 * @return ת�����intֵ
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
	 * longת��Ϊbyte����,���ֽ���ǰ
	 * 
	 * 
	 * @param i
	 *            ��ת���ĵ�long�ͱ���
	 * 
	 * @param buf
	 *            ���洢��byte����
	 * @param offset
	 *            buf��ƫ��ֵ
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
	 * byte����ת��Ϊlong,���ֽ���ǰ
	 * 
	 * 
	 * @param buf
	 *            ��ת���ĵ�byte����(�������ٱ���Ϊ8)
	 * @param offset
	 *            buf��ƫ��ֵ
	 * 
	 * @return ת�����longֵ
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
	 * byte����ת��Ϊshort,���ֽ���ǰ
	 * 
	 * 
	 * @param buf
	 *            ��ת���ĵ�byte����(�������ٱ���Ϊ2)
	 * @param offset
	 *            buf��ƫ��ֵ
	 * 
	 * @return ת�����shortֵ
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
	 * byte����ת��Ϊint,���ֽ���ǰ
	 * 
	 * 
	 * @param buf
	 *            ��ת���ĵ�byte����(�������ٱ���Ϊ4)
	 * @param offset
	 *            buf��ƫ��ֵ
	 * 
	 * @return ת�����intֵ
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
	 * byte����ת��Ϊfloat,���ֽ���ǰ
	 * 
	 * 
	 * @param buf
	 *            ��ת���ĵ�byte����
	 * @param offset
	 *            buf��ƫ��ֵ
	 * 
	 * @return ת�����floatֵ
	 */
	public static float bytes2Float(byte[] buf, int offset)
	{
		return Float.intBitsToFloat(bytes2Int(buf, offset));
	}

	/**
	 * ��ʮ�����Ƶ��ַ���ת��Ϊ������
	 * 
	 * <p>
	 * �ַ����д�ŵ�ΪAB089D������������.
	 * ע��:�Ƿ�0x��ͷ����Сд����,�ַ������ȱ���Ϊż��,�����׳�IllegalArgumentException�쳣
	 * </p>
	 * 
	 * @param hex
	 *            ��ת����ʮ�����Ƶ��ַ���
	 * @return ת�����byte����
	 */
	public static byte[] hex2Binary(String hex)
	{
		if (hex == null || hex.length() < 1)
			return null;

		if (hex.startsWith("0x") || hex.startsWith("0X"))
			hex = hex.substring(2);

		int len = hex.length();
		if (len % 2 != 0)
			throw new IllegalArgumentException("��ת����ʮ�������ַ�������Ӧ��Ϊż��");

		byte[] bys = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
		{
			String tmp = hex.substring(i, i + 2);
			bys[i / 2] = (byte) Integer.parseInt(tmp, 16);
		}
		return bys;
	}

	/**
	 * �Ѷ���������ת��Ϊʮ�����Ƶ��ַ���
	 * 
	 * <p>
	 * �ַ����д�ŵ�ΪAB089D������������,����û��0x��ͷ,һ��byte��Ӧ���ַ�������Ϊ2
	 * </p>
	 * 
	 * @param bys
	 *            ��ת���Ķ�����������
	 * @return ת�����ʮ�������ַ���,���bysΪ�ջ��߳���Ϊ0�򷵻�null
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
