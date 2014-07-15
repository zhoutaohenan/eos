package com.surge.engine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description
 * @project: WSurgeEngine
 * @Date:2010-8-4
 * @version 1.0
 * @Company: 33e9
 * @author glping
 */
public class TimeUtil
{
	/**
	 * 判断是否date0和date1表示的日期是否是同年同月同周
	 * 
	 * @param date0
	 *            整数表示的日期,格式yyyymmdd
	 * @param date1
	 *            整数表示的日期,格式yyyymmdd
	 * @return true 表示是同年同月同周
	 */
	public static boolean isSameWeek(int date0, int date1)
	{
		int y0 = date0 / 10000;
		int m0 = (date0 % 10000) / 100;

		int y1 = date1 / 10000;
		int m1 = (date1 % 10000) / 100;

		if (y0 != y1 || m0 != m1)
			return false;

		int d0 = date0 % 100;
		int d1 = date1 % 100;

		Calendar cal = Calendar.getInstance();
		cal.set(y0, m0 - 1, d0);
		int week_of_year0 = cal.get(Calendar.WEEK_OF_YEAR);
		cal.set(y1, m1 - 1, d1);
		int week_of_year1 = cal.get(Calendar.WEEK_OF_YEAR);

		if (week_of_year0 == week_of_year1)
			return true;
		return false;
	}

	/**
	 * 判断是否date0和date1表示的日期是否是同年同月
	 * 
	 * @param date0
	 *            整数表示的日期,格式yyyymmdd
	 * @param date1
	 *            整数表示的日期,格式yyyymmdd
	 * @return true 表示是同年同月同周
	 */
	public static boolean isSameMonth(int date0, int date1)
	{
		int y0 = date0 / 10000;
		int m0 = (date0 % 10000) / 100;

		int y1 = date1 / 10000;
		int m1 = (date1 % 10000) / 100;

		if (y0 != y1 || m0 != m1)
			return false;
		return true;
	}

	/**
	 * 获得Calendar类型的日期实例日期的整数表示
	 * 
	 * @param cal
	 *            Calendar类型的日期实例
	 * 
	 * @return 日期的整数表示,格式yyyymmdd
	 */
	public static int getDate(Calendar cal)
	{
		return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100
				+ cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得当前的日期的整数表示
	 * 
	 * @return 日期的整数表示,格式yyyymmdd
	 */
	public static int getDate()
	{
		Calendar cal = Calendar.getInstance();
		return getDate(cal);
	}

	/**
	 * 获得Calendar类型的日期实例时间的整数表示
	 * 
	 * @param cal
	 *            日历对象
	 * @return 当天经过的秒数
	 */
	public static int getTime(Calendar cal)
	{
		return cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60
				+ cal.get(Calendar.SECOND);
	}

	/**
	 * 获得当前的时间的整数表示
	 * 
	 * @return 当天经过的秒数
	 */
	public static int getTime()
	{
		Calendar cal = Calendar.getInstance();
		return getTime(cal);
	}

	/**
	 * 获得当前的日期的整数表示
	 * 
	 * @param cal
	 *            Calendar类型的日期实例
	 * 
	 * @return 日期的整数表示,格式YYYYMMDDHHMMSS
	 */
	public static long getDateTime(Calendar cal)
	{
		long ymd = cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100
				+ cal.get(Calendar.DAY_OF_MONTH);
		long hms = cal.get(Calendar.HOUR_OF_DAY) * 10000 + cal.get(Calendar.MINUTE) * 100
				+ cal.get(Calendar.SECOND);
		long ret = ymd * 1000000 + hms;
		return ret;
	}

	/**
	 * 获得当前的日期的整数表示
	 * 
	 * @return 日期的整数表示,格式YYYYMMDDHHMMSS
	 */
	public static long getDateTime()
	{
		Calendar cal = Calendar.getInstance();
		return getDateTime(cal);
	}

	/**
	 * 获得当前日期的字符串
	 * 
	 * @param cal
	 *            Calendar类型的日期实例
	 * 
	 * @return 当天的日期字符串"yyyy-mm-dd"
	 */
	public static String getDateStr(Calendar cal)
	{
		String a = "" + getDate(cal);

		StringBuffer sb = new StringBuffer(10);
		sb.append(a.substring(0, 4));
		sb.append("-");
		sb.append(a.substring(4, 6));
		sb.append("-");
		sb.append(a.substring(6, 8));
		return sb.toString();
	}

	/**
	 * 获得当前日期的字符串
	 * 
	 * @return 当天的日期字符串"yyyy-mm-dd"
	 */
	public static String getDateStr()
	{
		Calendar cal = Calendar.getInstance();
		return getDateStr(cal);
	}

	/**
	 * 根据经过的毫秒数获得时间字符串
	 * 
	 * 
	 * @param milliseconds
	 *            经过的毫秒数
	 * @return 时间字符串"hh:mm:ss"
	 */
	public static String getTimeStr(long milliseconds)
	{
		long seconds = milliseconds / 1000;
		long h = seconds / 3600;
		long m = seconds % 3600 / 60;
		long s = seconds % 60;

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

	/**
	 * 获得Calendar类型的时间字符串
	 * 
	 * @param cal
	 *            Calendar类型的日期实例
	 * 
	 * @return 时间字符串"hh:mm:ss"
	 */
	public static String getTimeStr(Calendar cal)
	{
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);

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

	/**
	 * 获得当前时间的字符串
	 * 
	 * @return 时间字符串"hh:mm:ss"
	 */
	public static String getTimeStr()
	{
		Calendar cal = Calendar.getInstance();
		return getTimeStr(cal);
	}

	/**
	 * 获取当天的时期时间字符串
	 * 
	 * @return 日期时间字符串"yyyy-mm-hh hh:mm:ss"
	 */
	public static String getDateTimeStr()
	{
		Calendar cal = Calendar.getInstance();
		return getDateTimeStr(cal);
	}

	/**
	 * 获取cal指定的时期时间字符串
	 * 
	 * @param cal
	 *            Calendar类型的日期实例
	 * 
	 * @return 日期时间字符串"yyyy-mm-hh hh:mm:ss"
	 */
	public static String getDateTimeStr(Calendar cal)
	{
		StringBuffer sb = new StringBuffer(20);
		sb.append(getDateStr(cal));
		sb.append(" ");
		sb.append(getTimeStr(cal));
		return sb.toString();
	}

	/**
	 * 获取当天的时期时间字符串
	 * 
	 * @param milliseconds
	 *            以毫秒为单位的当前时间
	 * @return 日期时间字符串"yyyy-mm-hh hh:mm:ss"
	 */
	public static String getDateTimeStr(long milliseconds)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(new Date(milliseconds));
	}

	/** 字符串转化为日期 */
	public static Date StrToDate(String str)
	{
		Date returnDate = null;
		if (str != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				returnDate = sdf.parse(str);
			} catch (Exception e)
			{
				System.err.println("AppTools [Date StrToDate(String str)] Exception");
				return returnDate;
			}
		}
		return returnDate;
	}

	/**
	 * 将字符转化成java.sql.Timestamp TODO
	 * 
	 * @param time
	 * @return java.sql.Timestamp
	 * @throws
	 */
	public static java.sql.Timestamp getTimestamp(String time)
	{
		java.sql.Timestamp timestamp = null;
		java.util.Date utilDate;
		int pos = time.lastIndexOf(".");
		String temp = null;
		if (pos > 0)
		{
			temp = time.substring(0, time.lastIndexOf("."));
		} else
		{
			temp = time;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			utilDate = format.parse(temp);
			timestamp = new java.sql.Timestamp(utilDate.getTime());

		} catch (ParseException e)
		{
			timestamp = new java.sql.Timestamp(new java.util.Date().getTime());

		}
		return timestamp;
	}

	/**
	 * 获得当前时间与今日 23:59:59秒相差的毫秒数 TODO
	 * 
	 * @return long
	 * @throws
	 */
	public static long getFixTime()
	{
		Calendar calendar = Calendar.getInstance();
		Calendar now = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		long time = calendar.getTimeInMillis() - now.getTimeInMillis();
		time = time < 0 ? 0 : time;
		if (time != 0)
		{
			time /= 1000;
		}
		return time;
	}

	/**
	 * 得到当前时间 -periodTime时间 TODO
	 * 
	 * @param periodTime
	 *            差距时间
	 * @return String
	 * @throws
	 */
	public static String getFixTime(int periodTime)
	{
		Calendar now = Calendar.getInstance();
		if (periodTime >= 0)
		{
			now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - periodTime);
		}
		String t = getDateTimeStr(now);
		return t;

	}
	/**
	 * 得到前一天的时间 YYYY-MM-DD TODO
	 * 
	 * @return String
	 * @throws
	 */
	public static String getLastDay(int t)
	{
		String lastDay = "";
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -t);
		lastDay = getDateStr(c);
		return lastDay;
	}
	public static void main(String[] args)
	{
		getFixTime();
	}
	/**
	 * 计算字符串时间与当前相距天数 TODO
	 * 
	 * @param str
	 *            yyyy-mm-dd int 计算的天数
	 * @throws
	 */
	public static int calcuateDays(String str)
	{
		int days = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date someDate = null;
		Calendar now = Calendar.getInstance();
		try
		{
			someDate = sdf.parse(str);
		} catch (ParseException e)
		{
			someDate = now.getTime();
		}

		days = Integer.parseInt(String.valueOf((now.getTimeInMillis() - someDate.getTime())
				/ (3600L * 1000 * 24)));

		return days == 0 ? 4 : days;

	}
}
