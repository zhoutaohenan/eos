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

	/** 序列号 */
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
	 * 生成唯一序列号
	 * 
	 * @return 序列号
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
	 * 将字符串进行md5加密
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
	 * 将BCD编码的字节数组转成字符串 TODO
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
	 * 将二进制数据进行md5加密
	 * 
	 * @param bys
	 *            待加密的二进制数据流
	 * @return md5加密后的二进制数据流
	 * @throws NoSuchAlgorithmException
	 *             如果java未提供md5加密方法则抛出此异常
	 */
	public static byte[] md5Encode(byte[] bys) throws NoSuchAlgorithmException
	{

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(bys);
		byte[] md5out = md5.digest();
		return md5out;
	}

	/**
	 * 去掉手机号码的以"+86"或者"86"开头的字符串
	 * 
	 * 
	 * @param mobile
	 *            手机号码
	 * @return 简化后的手机号码
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
	 * 将原文件srcFile由字符集转化为字符集为destCharset的目标文件destFile
	 * 
	 * @param srcFile
	 *            原文件名或目录
	 * 
	 * @param srcCharset
	 *            原文件字符集
	 * @param destFile
	 *            目录文件名或者目标目录
	 * 
	 * @param destCharset
	 *            目录文件字符集
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
			System.out.println("第" + i + "次" + getSmsFlag());
		}
	}
}
