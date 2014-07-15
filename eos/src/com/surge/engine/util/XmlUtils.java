package com.surge.engine.util;

import org.jdom.Element;

/**
 * @description
 * @project: eskprj
 * @Date:2010-8-9
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class XmlUtils
{
	public static int getInt(Element element, String name)
	{
		return Integer.parseInt(element.getChild(name).getTextTrim());
	}

	public static long getLong(Element element, String name)
	{
		return Long.parseLong(element.getChild(name).getTextTrim());
	}

	public static String getString(Element element, String name)
	{
		return element.getChild(name).getTextTrim();
	}

	public static boolean getBoolean(Element element, String name)
	{
		return Boolean.parseBoolean(element.getChild(name).getTextTrim());
	}

	public static byte getByte(Element element, String name)
	{
		String tmp = element.getChild(name).getTextTrim().toLowerCase();
		if (tmp.startsWith("0x"))
		{
			tmp = tmp.substring(2, tmp.length());
		}
		return Byte.parseByte(tmp, 16);
	}

	public static double getDouble(Element element, String name)
	{
		return Double.parseDouble(element.getChild(name).getTextTrim());
	}

	public static void setInt(Element element, String name, int value)
	{
		element.getChild(name).setText(String.valueOf(value));
	}

	public static void setString(Element element, String name, String value)
	{
		element.getChild(name).setText(value);
	}

	public static void setBoolean(Element element, String name, boolean value)
	{
		element.getChild(name).setText(String.valueOf(value));
	}

	public static void setByte(Element element, String name, byte value)
	{
		element.getChild(name).setText(String.valueOf(value));
	}

	public static void setDouble(Element element, String name, double value)
	{
		element.getChild(name).setText(String.valueOf(value));
	}
}
