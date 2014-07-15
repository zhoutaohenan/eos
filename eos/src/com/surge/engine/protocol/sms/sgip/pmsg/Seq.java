package com.surge.engine.protocol.sms.sgip.pmsg;

import java.util.Calendar;

public class Seq
{
  private static long globalSeq1;
  private static int globalSeq3;
  private static int minSeq = 0;
  private static int maxSeq = 2147483647;

  public static void setNodeId(long nodeId)
  {
    globalSeq1 = nodeId;
  }

  public static long getGlobalSeq1()
  {
    return globalSeq1;
  }

  public static synchronized int getGlobalSeq2() {
    Calendar cal = Calendar.getInstance();
    int month = cal.get(2) + 1;
    int day = cal.get(5);
    int hour = cal.get(11);
    int minute = cal.get(12);
    int second = cal.get(13);
    int time = second + minute * 100 + hour * 10000 + day * 1000000 + month * 
      100000000;

    return time;
  }

  public static synchronized int getGlobalSeq3() {
    if (globalSeq3 == maxSeq)
      globalSeq3 = minSeq;
    else {
      globalSeq3 += 1;
    }

    return globalSeq3;
  }
}