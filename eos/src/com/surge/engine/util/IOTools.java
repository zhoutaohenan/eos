
package com.surge.engine.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.rmi.UnknownHostException;
import java.sql.Connection;


public class IOTools
{

	/**
	 * �ж�ָ���ĵ�ַ���˿��Ƿ�ɵ���,�൱�ڶ˿�ɨ��
	 * 
	 * 
	 * @param host
	 *            ��ɨ��ĵ�ַ
	 * @param port
	 *            ��ɨ��Ķ˿�
	 * @return boolean true:��ʾ���Ե��� false:��ʾ���ɵ���
	 */
	public static boolean isReachable(String host, int port)
	{

		boolean ret = false;
		Socket cs = null;
		try
		{
			cs = new Socket(host, port);
			ret = true;
		} catch (Exception ex)
		{
		} finally
		{
			try
			{
				if (cs != null)
					cs.close();
			} catch (Exception e)
			{
			}
		}
		return ret;
	}

	/**
	 * <p>
	 * ����ϴ��������ж�������,ֱ������len�ֽڳ��Ȼ����쳣,�������ݷ���
	 * </p>
	 * 
	 * @param in
	 *            �����ֽ�������
	 * 
	 * @param len
	 *            Ҫ�������ֽڳ���
	 * @return ������byte����
	 * @throws IOException
	 *             �����δ����len�����ֽ����ݵ�����������ĩβ���׳����쳣,
	 */
	public static byte[] read(InputStream in, int len) throws IOException
	{

		if (len < 0)
			return read(in);
		byte[] bys = new byte[len];
		int a, b = 0;
		while (b < len)
		{
			a = in.read(bys, b, len - b);
			if (a == -1)
				throw new IOException(
						"the length is not enough to read data from in.(" + b
								+ "/" + len + ")");
			b += a;
		}
		return bys;
	}

	/**
	 * <p>
	 * ����ϴ��������ж�������,ֱ��������ĩβ,�������ݷ���
	 * </p>
	 * 
	 * @param in
	 *            �����ֽ�������
	 * 
	 * @return ������byte����
	 * @throws IOException
	 *             ��ȡ���ݹ���Ҳ�����׳����쳣
	 */
	public static byte[] read(InputStream in) throws IOException
	{

		byte[] bys = new byte[8192];

		AppendUtils t1 = new AppendUtils(10240);
		int a = 0;
		while (a >= 0)
		{
			a = in.read(bys, 0, bys.length);
			if (a > 0)
				t1.appendBytes(bys, 0, a);
		}
		bys = t1.getOutBytes();
		return bys;
	}

	/**
	 * <p>
	 * ����ϴ��������ж�������,ֱ������һ��CRLF(�س�����),�������ݷ���
	 * </p>
	 * 
	 * @param in
	 *            �����ֽ�������
	 * 
	 * @return ������byte����
	 * @throws IOException
	 *             ��ȡ���ݹ���Ҳ�����׳����쳣
	 */
	public static byte[] readTillCRLF(InputStream in) throws IOException
	{

		byte[] bys = new byte[2];
		bys[0] = 13;
		bys[1] = 10;

		return readTillDesiredArrays(in, bys);
	}

	/**
	 * <p>
	 * ����ϴ��������ж�������,ֱ����������CRLF(�س�����),�������ݷ���
	 * </p>
	 * 
	 * @param in
	 *            �����ֽ�������
	 * 
	 * @return ������byte����
	 * @throws IOException
	 *             ��ȡ���ݹ���Ҳ�����׳����쳣
	 */
	public static byte[] readTillDoubleCRLF(InputStream in) throws IOException
	{

		byte[] bys = new byte[4];
		bys[0] = 13;
		bys[1] = 10;
		bys[2] = 13;
		bys[3] = 10;

		return readTillDesiredArrays(in, bys);
	}

	/**
	 * <p>
	 * ����ϴ��������ж�������,ֱ����������Ҫ��bysƥ����ֽ�����Ϊֹ,�������ݷ���
	 * </p>
	 * 
	 * @param in
	 *            �����ֽ�������
	 * 
	 * @param bys
	 *            ��ƥ����ֽ�����
	 * @return ������byte����
	 * @throws IOException
	 *             ��ȡ���ݹ���Ҳ�����׳����쳣
	 */
	public static byte[] readTillDesiredArrays(InputStream in, byte[] bys)
			throws IOException
	{

		AppendUtils appendTools = new AppendUtils(1024);
		int len = bys.length;
		int i = 0;
		byte b;
		while (true)
		{
			b = (byte) in.read();
			if (b == -1)
				throw new IOException(
						"the end of the stream is reached after get "
								+ appendTools.size() + " bytes");
			appendTools.appendByte(b);
			int size = appendTools.size();
			if (size < len)
				continue;
			for (i = 0; i < len; i++)
			{
				if (appendTools.get(size - 1 - i) != bys[len - 1 - i])
					break;
			}
			if (i == len)
				break;
		}
		return appendTools.getOutBytes();
	}

	/**
	 * <p>
	 * ����ϴ��������ж�������,ֱ������len�ֽڳ��Ȼ����쳣,�������ݷ���
	 * </p>
	 * 
	 * @param in
	 *            �����ֽ�������
	 * 
	 * @param buf
	 *            ��ŵ����ݻ���
	 * 
	 * @param offset
	 *            ��������ʼ��ַ
	 * @param len
	 *            Ҫ�������ֽڳ���
	 * @throws IOException
	 *             �����δ����len�����ֽ����ݵ�����������ĩβ���׳����쳣, ��ȡ���ݹ���Ҳ�����׳����쳣
	 */
	public static void read(InputStream in, byte[] buf, int offset, int len)
			throws IOException
	{

		int a, b = 0;
		while (b < len)
		{
			a = in.read(buf, b + offset, len - b);
			if (a == -1)
				throw new IOException(
						"the length is not enough to read data from in.(" + b
								+ "/" + len + ")");
			b += a;
		}
	}

	/**
	 * <p>
	 * ����ϴ��������ж�������,ֱ��buf��������ߵ�����������ĩβ
	 * </p>
	 * 
	 * @param in
	 *            �����ֽ�������
	 * 
	 * @param buf
	 *            ��ŵ����ݻ���
	 * 
	 * @param offset
	 *            ��������ʼ��ַ
	 * @return ������byte����
	 * @throws IOException
	 *             ��ȡ���ݹ��̿����׳����쳣
	 */
	public static int read(InputStream in, byte[] buf, int offset)
			throws IOException
	{

		int len = buf.length - offset;

		int a, b = 0;
		while (b < len)
		{
			a = in.read(buf, b + offset, len - b);
			if (a == -1)
				break;
			b += a;
		}
		return b;
	}

	/**
	 * ���ļ��л�ȡ����
	 * 
	 * @param fileName
	 *            �ļ���
	 * 
	 * @return �ļ����ݵ�byte����
	 * @throws IOException
	 */
	public static byte[] readFile(String fileName) throws IOException
	{

		FileInputStream fin = null;
		byte[] ret = null;
		try
		{
			File f = new File(fileName);
			long len = f.length();
			fin = new FileInputStream(fileName);
			ret = read(fin, (int) len);
		} finally
		{
			try
			{
				if (fin != null)
					fin.close();
			} catch (Exception e)
			{
			}
		}
		return ret;
	}

	/**
	 * ����http�����Ƿ�������
	 * 
	 * 
	 * @param server
	 *            �����Ե�http������ ip��ַ��������
	 * 
	 * @param port
	 *            �����Ե�http�������˿�
	 * 
	 * @return true��ʾhttp������������,�����ʾ������
	 */
	public static boolean isHttpServerOk(String server, int port)
	{

		boolean ret = false;

		HttpURLConnection conn = null;

		String http_url = "http://" + server;
		if (port != 80)
			http_url += ":" + port;
		try
		{
			URL url = new URL(http_url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(false);

			conn.getResponseCode();
			ret = true;
		} catch (Exception e)
		{
		} finally
		{
			try
			{
				if (conn != null)
					conn.disconnect();
			} catch (Exception e)
			{
			}
		}
		return ret;
	}

	/**
	 * ���ֽ�����д�뵽�ļ�
	 * 
	 * @param bys
	 *            ��д���ļ��Ķ���������
	 * 
	 * @param fileName
	 *            �ļ���
	 * 
	 * @param append
	 *            �Ƿ񸽼ӵ��ļ�ĩβ, true��ʾ���ӵ�ԭ�ļ�ĩβ, false��ʾ����ԭ�ļ�
	 * 
	 * @throws IOException
	 */
	public static void write(byte[] bys, String fileName, boolean append)
			throws IOException
	{

		BufferedOutputStream out = null;
		try
		{
			out = new BufferedOutputStream(new FileOutputStream(fileName,
					append));
			out.write(bys);
			out.flush();
		} finally
		{
			if (out != null)
				out.close();
		}
	}

	

	// -----------------------------------------------------------------------
	/**
	 * Unconditionally close an <code>Reader</code>.
	 * <p>
	 * Equivalent to {@link Reader#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param input
	 *            the Reader to close, may be null or already closed
	 */
	public static void closeQuietly(Reader input)
	{

		try
		{
			if (input != null)
			{
				input.close();
			}
		} catch (IOException ioe)
		{
			// ignore
		}
	}

	/**
	 * Unconditionally close a <code>Writer</code>.
	 * <p>
	 * Equivalent to {@link Writer#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param output
	 *            the Writer to close, may be null or already closed
	 */
	public static void closeQuietly(Writer output)
	{

		try
		{
			if (output != null)
			{
				output.close();
			}
		} catch (IOException ioe)
		{
			// ignore
		}
	}

	public static void closeQuietly(Connection connection)
	{

		try
		{
			if (connection != null)
			{
				connection.close();
			}
		} catch (Exception ioe)
		{
			// ignore
		}
	}

	/**
	 * Unconditionally close an <code>InputStream</code>.
	 * <p>
	 * Equivalent to {@link InputStream#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param input
	 *            the InputStream to close, may be null or already closed
	 */
	public static void closeQuietly(InputStream input)
	{

		try
		{
			if (input != null)
			{
				input.close();
			}
		} catch (IOException ioe)
		{
			// ignore
		}
	}

	/**
	 * Unconditionally close an <code>OutputStream</code>.
	 * <p>
	 * Equivalent to {@link OutputStream#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param output
	 *            the OutputStream to close, may be null or already closed
	 */
	public static void closeQuietly(OutputStream output)
	{

		try
		{
			if (output != null)
			{
				output.close();
			}
		} catch (IOException ioe)
		{
			// ignore
		}
	}

	/**
	 * ��ָ���˿�ȷ�������Ƿ�������ӣ�Ŀǰ�ٶ�
	 * 
	 * @author Jerry
	 */
	public static boolean isReachable(String host)
	{

		if (host == null)
			return false;
		try
		{
			InetAddress address = InetAddress.getByName(host);
			return address.isReachable(3000);//
		} catch (UnknownHostException e)
		{
			// e.printStackTrace();
		} catch (IOException e)
		{
			// e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args)
	{

		System.out.println(IOTools.isReachable("192.168.0.124"));
	}
}
