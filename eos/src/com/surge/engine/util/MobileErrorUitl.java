package com.surge.engine.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class MobileErrorUitl
{
	private static final Logger logger = Logger.getLogger(MobileErrorUitl.class);

	private List<String> cmppList = new ArrayList<String>();

	private List<String> sgipList = new ArrayList<String>();

	private List<String> smgpList = new ArrayList<String>();

	private List<String> gwList = new ArrayList<String>();

	public static MobileErrorUitl mobileErrorUitl = new MobileErrorUitl();

	private MobileErrorUitl()
	{

	}

	/**
	 * ≥ı º≈‰÷√≤Œ ˝
	 * 
	 * @throws Exception
	 */
	public void initConfigParam() throws Exception
	{
		Properties propertyFile = new Properties();

		InputStream istm = new FileInputStream(System.getProperty("user.dir")
				+ "/config/mobile-error.properties");
		propertyFile.load(istm);
		String cmpp[] = propertyFile.getProperty("cmpp").split(",");
		String sgip[] = propertyFile.getProperty("sgip").split(",");
		String smgp[] = propertyFile.getProperty("smgp").split(",");
		String gw[] = propertyFile.getProperty("gw").split(",");
		for (String cm : cmpp)
		{
			cmppList.add(cm);
		}
		for (String sg : sgip)
		{
			sgipList.add(sg);
		}
		for (String sm : smgp)
		{
			smgpList.add(sm);
		}
		for (String gws : gw)
		{
			gwList.add(gws);
		}

	}

	public List<String> getCmppList()
	{
		return cmppList;
	}

	public void setCmppList(List<String> cmppList)
	{
		this.cmppList = cmppList;
	}

	public List<String> getSgipList()
	{
		return sgipList;
	}

	public void setSgipList(List<String> sgipList)
	{
		this.sgipList = sgipList;
	}

	public List<String> getSmgpList()
	{
		return smgpList;
	}

	public void setSmgpList(List<String> smgpList)
	{
		this.smgpList = smgpList;
	}

	public List<String> getGwList()
	{
		return gwList;
	}

	public void setGwList(List<String> gwList)
	{
		this.gwList = gwList;
	}

	public static Logger getLogger()
	{
		return logger;
	}

}
