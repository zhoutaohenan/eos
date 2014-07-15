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
	 * 配置文件名称
	 * 
	 * 相对路径，如cmpp.xml或config/cmpp.xml
	 */
	protected final String file;
	/**
	 * 配置文件URL
	 */
	private URL url;

	/**
	 * 配置文件JDOM文档对象
	 */
	protected Document document;

	/**
	 * 初始化配置文件
	 * 
	 * @param file            文件名。指在jar包的相对路径，如config/cmpp.xml
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
	 * 加载配置文件中的信息
	 * 
	 */
	protected abstract void doGetConfig();

}
