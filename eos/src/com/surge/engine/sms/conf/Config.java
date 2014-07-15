package com.surge.engine.sms.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public abstract class Config
{
	/**
	 * �����ļ�����
	 * 
	 * ���·������cmpp.xml��config/cmpp.xml
	 */
	protected final String file;
	/**
	 * �����ļ�URL
	 */
	private URL url;

	/**
	 * �����ļ�JDOM�ĵ�����
	 */
	protected Document document;

	/**
	 * ��ʼ�������ļ�
	 * 
	 * @param file            �ļ�����ָ��jar�������·������config/cmpp.xml
	 * @throws IOException
	 * @throws JDOMException
	 */
	protected Config(String file)
	{
		this.file = file;
		url = Config.class.getClassLoader().getResource(file);

		if (url == null)
		{
			try
			{
				url = new File(file).toURL();
			}
			catch (MalformedURLException e)
			{
				Logger.getLogger(Config.class).error("", e);
			}
		}

		SAXBuilder builder = new SAXBuilder();
		try
		{
			InputStream is = url.openStream();
			document = builder.build(is);
			this.doGetConfig();
		}
		catch (Exception e)
		{
			Logger.getLogger(Config.class).error("", e);
		}
	}

	/**
	 * ���������ļ��е���Ϣ
	 * 
	 */
	protected abstract void doGetConfig();

}
