package com.surge.engine;

import java.util.concurrent.atomic.AtomicInteger;

public class SysConst
{
	private static AtomicInteger flag = new AtomicInteger(1);

	/** ɾ�������˲�����ǰ���ͽ����(SMS_SENT)�еļ�¼����λ���졡 **/
	private static int backTime;

	/** ������Чʱ�� ��λ:���� **/
	private static int validTime;


	/**
	 * 
	 * TODO
	 * 
	 * @param f
	 *            void
	 * @throws
	 */

	public synchronized static void setValue(int f)
	{
		// flag.addAndGet(f);
		flag.set(f);
	}
	public synchronized static int getFalg()
	{
		return flag.get();
	}

	public synchronized static void descFalg(int f)
	{
		for (int i = 0; i < f; i++)
		{
			flag.decrementAndGet();
		}

	}
	public static int getBackTime()
	{
		return backTime;
	}

	public static void setBackTime(int back)
	{
		backTime = back;
	}

	public static int getValidTime()
	{
		return validTime;
	}

	public static void setValidTime(int time)
	{
		validTime = time;
	}
}
