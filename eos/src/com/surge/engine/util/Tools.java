package com.surge.engine.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-12
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class Tools
{

	/** ���к� */
	private static int seq_id = 0;

	private static long msg_id = 0;

	private static String lineSeparator = System.getProperty("line.separator");

	private static Random rd = new Random(System.currentTimeMillis());

	private static final long start_time = System.currentTimeMillis();

	private static byte sms_flag = 0;

	public static synchronized byte getSmsFlag()
	{
		if (sms_flag == Byte.MAX_VALUE)
		{
			sms_flag = 0;
		}
		sms_flag++;
		return sms_flag;
	}

	/**
	 * ����Ψһ���к�
	 * 
	 * @return ���к�
	 */
	public static synchronized int getSeqId()
	{

		if (seq_id == Integer.MAX_VALUE)
			seq_id = 0;
		seq_id++;
		return seq_id;
	}

	public static synchronized long getMsgId()
	{

		msg_id++;
		return msg_id;
	}

	public static void csleep(long timeout) throws InterruptedException
	{

		byte[] lock = new byte[0];

		synchronized (lock)
		{
			lock.wait(timeout);
		}

		lock = null;
	}

	/**
	 * ���ַ�������md5����
	 */
	public static String md5Encode(String str)
	{

		String reStr = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte ss[] = md.digest();
			reStr = bytes2String(ss);
		} catch (NoSuchAlgorithmException e)
		{

		}
		return reStr;

	}

	private static String bytes2String(byte[] aa)
	{

		String hash = "";

		for (int i = 0; i < aa.length; i++)
		{
			int temp;
			if (aa[i] < 0)
				temp = 256 + aa[i];
			else
				temp = aa[i];
			if (temp < 16)
				hash += "0";
			hash += Integer.toString(temp, 16);
		}
		hash = hash.toUpperCase();
		// System.out.println("hash result1 is:" + hash);

		return hash;
	}

	/**
	 * ��BCD������ֽ�����ת���ַ��� TODO
	 * 
	 * @param bytes
	 * @return String
	 * @throws
	 */
	public static String bcd2Str(byte[] bytes)
	{
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++)
		{
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		// return temp.toString();
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1)
				: temp.toString();
	}
	/**
	 * �����������ݽ���md5����
	 * 
	 * @param bys
	 *            �����ܵĶ�����������
	 * @return md5���ܺ�Ķ�����������
	 * @throws NoSuchAlgorithmException
	 *             ���javaδ�ṩmd5���ܷ������׳����쳣
	 */
	public static byte[] md5Encode(byte[] bys) throws NoSuchAlgorithmException
	{

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(bys);
		byte[] md5out = md5.digest();
		return md5out;
	}

	/**
	 * ȥ���ֻ��������"+86"����"86"��ͷ���ַ���
	 * 
	 * 
	 * @param mobile
	 *            �ֻ�����
	 * @return �򻯺���ֻ�����
	 */
	public static String trimMobile(String mobile)
	{

		String ret = mobile.trim();

		if (ret.startsWith("+86"))
			ret = ret.substring(3);
		else if (ret.startsWith("86"))
			ret = ret.substring(2);
		return ret;
	}

	/**
	 * ��ԭ�ļ�srcFile���ַ���ת��Ϊ�ַ���ΪdestCharset��Ŀ���ļ�destFile
	 * 
	 * @param srcFile
	 *            ԭ�ļ�����Ŀ¼
	 * 
	 * @param srcCharset
	 *            ԭ�ļ��ַ���
	 * @param destFile
	 *            Ŀ¼�ļ�������Ŀ��Ŀ¼
	 * 
	 * @param destCharset
	 *            Ŀ¼�ļ��ַ���
	 * 
	 * @throws IOException
	 */
	public static void convert(String srcFile, String srcCharset, String destFile,
			String destCharset) throws IOException
	{

		if (srcFile == null || destFile == null)
			return;

		File f = new File(srcFile);
		if (f.isDirectory())
		{
			srcFile = srcFile.replaceAll("\\\\", "/");
			if (!srcFile.endsWith("/"))
				srcFile += "/";
			destFile = destFile.replaceAll("\\\\", "/");
			if (!destFile.endsWith("/"))
				destFile += "/";
			File dir = new File(destFile);
			dir.mkdirs();

			String[] files = f.list();
			for (String file : files)
			{
				convert(srcFile + file, srcCharset, destFile + file, destCharset);
			}
		} else if (f.isFile())
		{
			byte[] bys = IOTools.readFile(srcFile);
			String str = new String(bys, srcCharset);
			str = str.replaceAll("gb2312", "utf-8");
			bys = str.getBytes(destCharset);

			BufferedOutputStream out = null;
			try
			{
				out = new BufferedOutputStream(new FileOutputStream(destFile, false));
				out.write(bys);
				out.flush();
				System.out.println("conver file " + srcFile + " with charset " + srcCharset
						+ " to file " + destFile + " with charset " + destCharset);
			} finally
			{
				try
				{
					if (out != null)
						out.close();
				} catch (Exception e)
				{
				}
			}
		}
	}

	public static void main(String[] args)
	{
		for (int i = 0; i < 129; i++)
		{
			System.out.println("��" + i + "��" + getSmsFlag());
		}
	}
}
