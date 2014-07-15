package com.surge.engine.protocol.sms.gw.util;


import java.util.*;
//import java.util.regex.*;
import java.util.StringTokenizer;
/**
 * 一些常用的函数
 */
public class DateConvertTools{

    /**
     * 将 byte 数组转化成整数
     * @param b     字节数组
     * @param iFrom 开始转换位置
     * @param iLen  要转换的字节数
     * @return 转换后结果
     */
    public static int b2i(byte[] b ,int iFrom,int iLen)
    {
        int iRet=0;
        int iTep;
        for(int i=iFrom;i<iFrom+iLen && i<b.length;i++){
            iTep=b[i];
            if(iTep<0){
                iTep+=256;
            }
            //iTep = iTep << (iLen-1-(i-iFrom)*8);
            //iRet = iRet | iTep;

            iRet=(iRet<<8)|iTep;
        }
        return iRet;
    }

    /**
     *将 整数转化成字节数组
     * @param iData 要转换的整数
     * @param b     转换后存放结果的字节数组
     * @param iFrom 存放字节起始位置
     * @param iLen  将整数转换成的字节数
     */
    public static void i2b(int iData,byte[] b ,int iFrom,int iLen)
    {
        int id=iData;
        int iOff=iLen-1;
        if(iOff<0) iOff=0;
        for(int i=iFrom;i<iFrom+iLen && i<b.length;i++){
            if(i-iFrom<4){
                //b[i]=(byte)((id >> ((i-iFrom)*8) ) & 0xFF);
                b[i]=(byte)((id >> ((iOff-(i-iFrom))*8) ) & 0xFF);
            }else{
                b[i]=0;
            }
        }
    }

    /**
     *将 字符串转化成字节
     * @param s     要转换的字符串
     * @param b     转换后存放结果的字节数组
     * @param iFrom 存放字节起始位置
     * @param iLen  将字符串转换成的字节数
     * @param bFill 如字符字节数不足，则填充字节
     */
    public static void s2b(String s,byte []b,int iFrom,int iLen,byte bFill)
    {
        byte []t=null;
        if(s!=null && s.length()>0) t=s.getBytes();
        if(t==null){
            for(int i=iFrom;i<iFrom+iLen && i<b.length;i++){
                b[i]=bFill;
            }
        }else{
            for(int i=iFrom;i<iFrom+iLen && i<b.length;i++){
                if(i-iFrom<t.length){
                    b[i]=t[i-iFrom];
                }else{
                    b[i]=bFill;
                }
            }
        }
    }

    /**
     *将 字节转化成字符串
     * @param b     转换后存放结果的字节数组
     * @param iFrom 存放字节起始位置
     * @param iLen  将字符串转换成的字节数
     * @param bCut  字符串的尾部要切除的字符
     */
    public static String b2s(byte []b,int iFrom,int iLen,byte bCut)
    {
        if(b==null || iFrom+iLen>b.length){
            return null;
        }
        if(iLen<1) return "";

        String s=new String(b,iFrom,iLen);
        if(s==null || s.length()==0 ) return "";
        int i=s.length()-1;
        while(i>=0 && s.charAt(i)==(char)bCut){
            i--;
        }
        if(i<0) return "";
        return s.substring(0,i+1);
     }


     /**
     *将 字节转化成字符串,到字节值等于 bEnd 位置
     * @param b     转换后存放结果的字节数组
     * @param iFrom 存放字节起始位置
     * @param bEnd  分界字节
     */
    public static String b2s(byte []b,int iFrom,byte bEnd)
    {
        if(b==null || iFrom>=b.length){
            return null;
        }
        int iTo=b.length;
        for(int i=iFrom;i<b.length;i++){
            if(b[i]==bEnd){
                iTo=i;
                break;
            }
        }
        if(iTo<=iFrom) return "";
        return new String(b,iFrom,iTo-iFrom);
     }

     public static byte[] h2b(String sVal)
     {
        if(sVal==null || sVal.length()==0){
            return null;
        }
        sVal=sVal.toUpperCase();
        int iLen=sVal.length();
        if(iLen%2 == 1){
            sVal="0"+sVal;
        }
        iLen=sVal.length()/2;
        byte []b=new byte[iLen];
        char c1,c2;
        for(int i=0;i<iLen;i++){
            c1=sVal.charAt(2*i);
            c2=sVal.charAt(2*i+1);
            b[i]=c2b(c1,c2);
        }
        return b;
     }

     public static byte c2b(char c1,char c2)
     {
        byte b1,b2;
        if(c1>='A'){
            b1=(byte)(c1-'A'+10);
        }else{
            b1=(byte)(c1-'0');
        }
        if(c2>='A'){
            b2=(byte)(c2-'A'+10);
        }else{
            b2=(byte)(c2-'0');
        }
        return (byte)((b1<<4)|b2);
     }


     /**
     *将 字节转化成HEX字符串
     * @param b     转换后存放结果的字节数组
     * @param iFrom 存放字节起始位置
     * @param iLen  将字符串转换成的字节数
     * @param bCut  字符串的尾部要切除的字符
     */
     public static String b2h(byte b)
     {
        int i=b;

        if(i<0) i+=256;
        String s="";
        int ih=i>>4;
        int il=i&0x0F;
        if(ih>=10){
            ih+='A'-10;
        }else{
            ih+='0';
        }
        if(il>=10){
            il+='A'-10;
        }else{
            il+='0';
        }
        return s=String.valueOf((char)ih)+String.valueOf((char)il);
     }


     /**
     *将 字节转化成HEX字符串
     * @param b     转换后存放结果的字节数组
     * @param iFrom 存放字节起始位置
     * @param iLen  将字符串转换成的字节数
     * @param bCut  字符串的尾部要切除的字符
     */
     public static void showBin(byte []b)
     {
        if(b==null){
            System.out.println("null bytes!");
            return;
        }
        showBin(b,0,b.length);

     }


     public static void showBin(byte []b,int iFrom,int iLen)
     {
        if(b==null){
            System.out.println("null bytes!");
            return;
        }
        System.out.println("bytes length="+b.length);
        for(int i=iFrom;i<b.length && i<iFrom+iLen;i++){
            if(i>0 && i%16==0){
                System.out.println();
            }
            System.out.print(b2h(b[i])+"-");
        }
        System.out.println();
     }

     /**
     *将 字节拷贝
     * @param fb    源字节数组
     * @param tb    目的字节数组
     * @param iFrom 目的存放起始位置
     * @param bCut  要copy 的长度
     * @param bFill 不足时填充字节
     */
     public static void addb(byte []fb,byte []tb,int iFrom,int iLen,byte bFill)
     {
        int iAdd=0;
        if(fb!=null){
            for(int i=iFrom;i<tb.length && i<iFrom+iLen && i<iFrom+fb.length;i++){
                tb[i]=fb[i-iFrom];
                iAdd++;
            }
        }
        while(iAdd<iLen && iFrom+iAdd<tb.length){
            tb[iFrom+iAdd]=bFill;
            iAdd++;
        }
     }

     /**
     * 将 从字节中截取字节
     * @param fb    源字节数组
     * @param iFrom 目的存放起始位置
     * @param bCut  要copy 的长度
     * @param bCut  从截取字节中要截去的字节
     */
     public static byte[] cutb(byte []fb,int iFrom,int iLen,byte bCut)
     {
        int iAdd=0;
        if(fb==null || iFrom>=fb.length){
            return null;
        }
        int it=iFrom+iLen;
        if(it>fb.length){
            it=fb.length;
        }

        it--;
        while(it>=iFrom && fb[it]==bCut){
            it--;
        }

        if(it<iFrom) return null;
        int iLef=it+1-iFrom;
        byte []r=new byte[iLef];
        System.arraycopy(fb,iFrom,r,0,iLef);
        return r;
     }

     /**
      * 将格式为 yyyymmddhhmmss 的串转换成 SQL-Server Data/Time
      */
     public static String toDate(String s)
     {
        if(s==null || s.length()<8) return null;
        String s0=s;
        long l=-1;
        try{
            l=Long.parseLong(s0);
        }catch(Exception e){}
        if(l<=0){
            s0="20000101";
        }
        while(s0.length()<14){
            s0+="0";
        }
        String sy=s0.substring(0,4);
        String sm=s0.substring(4,6);
        String sd=s0.substring(6,8);
        String sh=s0.substring(8,10);
        String mi=s0.substring(10,12);
        String ss=s0.substring(12,14);

        String s1=sy+"-"+sm+"-"+sd+" "+
                  sh+":"+mi+":"+ss;
        String sDate="convert(datetime,'"+s1+"')";
        return sDate;
     }

     /**
      * 取得系统日期，格式为 yyyymmdd
      */
     public static String getDate()
     {
        Calendar c = Calendar.getInstance();
        int iYear=c.get(Calendar.YEAR);
        int iMon=c.get(Calendar.MONTH)+1;
        int iDay=c.get(Calendar.DAY_OF_MONTH);
        String sYear=String.valueOf(iYear);
        if(iYear<10) sYear="0"+sYear;

        String sMon=String.valueOf(iMon);
        if(iMon<10) sMon="0"+sMon;

        String sDay=String.valueOf(iDay);
        if (iDay<10) sDay="0"+sDay;

        return sYear+sMon+sDay;
     }

     /**
      * 取得系统日期，格式为 hhmmss
      */
     public static String getTime()
     {
        Calendar c = Calendar.getInstance();
        int iHour=c.get(Calendar.HOUR_OF_DAY);
        int iMin=c.get(Calendar.MINUTE);
        int iSec=c.get(Calendar.SECOND);

        String sHour=String.valueOf(iHour);
        if(iHour<10) sHour="0"+sHour;

        String sMin=String.valueOf(iMin);
        if(iMin<10) sMin="0"+sMin;

        String sSec=String.valueOf(iSec);
        if(iSec<10) sSec="0"+sSec;

        return sHour+sMin+sSec;
     }

     /**
      * 取得系统日期，格式为 MMDDHHMMSS
      */
     public static String getTimeStamp()
     {
        Calendar c = Calendar.getInstance();
        int iMon=c.get(Calendar.MONTH)+1;
        int iDay=c.get(Calendar.DAY_OF_MONTH);
        int iHour=c.get(Calendar.HOUR_OF_DAY);
        int iMin=c.get(Calendar.MINUTE);
        int iSec=c.get(Calendar.SECOND);

        String sMon=String.valueOf(iMon);
        if(iMon<10) sMon="0"+sMon;

        String sDay=String.valueOf(iDay);
        if(iDay<10) sDay="0"+sDay;

        String sHour=String.valueOf(iHour);
        if(iHour<10) sHour="0"+sHour;

        String sMin=String.valueOf(iMin);
        if(iMin<10)  sMin="0"+sMin;

        String sSec=String.valueOf(iSec);
        if(iSec<10) sSec="0"+sSec;

        return sMon+sDay+sHour+sMin+sSec;
     }



     public static boolean haveChina(byte []b)
     {
        if(b==null) return false;
        for(int i=0;i<b.length;i++){
            if(b[i]<0 || b[i]>127){
                return true;
            }
        }
        return false;
     }

     public static boolean isNumber(String s)
     {
        if(s==null || s.length()==0){
            return false;
        }
        char c;
        for(int i=0;i<s.length();i++){
            c=s.charAt(i);
            if(c<'0' || c>'9'){
                return false;
            }
        }
        return true;
     }

     public static String getString(byte []b,int iFrom,int iLen){

        if(b==null || b.length<iFrom+iLen){
            return null;
        }

        char c;
        String s="";
        int b1,b2;

        for(int i=iFrom;i<iFrom+iLen;i++){

            b1=b[i];
            if(b1<0){
                b1+=256;
            }
            i++;
            if(i<iFrom+iLen){
                b2=b[i];
            }else{
                b2=0;
            }
            if(b2<0){
                b2+=256;
            }
            //////////////
            c=(char)((b1<<8 | b2));
            s=s+String.valueOf(c);
            /*
            if(b1==0){
                if(b2!=0){
                    s=s+String.valueOf((char)b2);
                }
            }else{
                if(b2!=0){
                   c=(char)((b1<<8 | b2));
                   s=s+String.valueOf(c);
                }else{
                    s=s+String.valueOf((char)b1);
                }
            }*/
        }
        return s;
     }
     public static String getString1(byte []b,int iFrom,int iLen){
        if(b==null || b.length<iFrom+iLen) return null;
        char c; String s=""; int b1,b2;

        boolean bSub=false;
        for(int i=iFrom;i<iFrom+iLen;i++)
            if(b[i]==0) bSub=true;

        if(!bSub){
            try{
                String st=new String(b,iFrom,iLen);
                if(st!=null && st.length()==iLen) return st;
            }catch(Exception e){}
        }

        for(int i=iFrom;i<iFrom+iLen;i++){
            b1=b[i];
            if(b1<0)  b1+=256;

            i++;
            if(i<iFrom+iLen) b2=b[i];
            else  b2=0;

            if(b2<0)  b2+=256;
            if(b1==0){
                if(b2!=0) s=s+String.valueOf((char)b2);
            }else{
                if(b2!=0){
                   c=(char)((b1<<8 | b2));
                   s=s+String.valueOf(c);
                }else
                   s=s+String.valueOf((char)b1);
            }
        }
        return s;
     }
     	/////截去字符串首尾的字符
     public static String trimChar(String strTemp,String charTemp){
	   if(strTemp==null||strTemp.equals(""))
		   return "";

	   if(charTemp==null||charTemp.equals(""))
		   charTemp=",";
	   //System.out.println(strTemp);
	   if(strTemp.startsWith(charTemp))
		   strTemp=strTemp.substring(charTemp.length(),strTemp.length()-1);

	   if(strTemp.endsWith(charTemp))
		   strTemp=strTemp.substring(0,strTemp.length()-charTemp.length());
	   //System.out.println(strTemp);
	   return strTemp;
    }

	/////按字串切个字串返会字串数组
    public static String[] splite(String str,String charTemp) {
		if(charTemp==null||charTemp.equals(""))
		   charTemp=",";

		StringTokenizer token = new StringTokenizer(str,charTemp);
		String[] result  = new String[token.countTokens()];
		int i = 0;
		while (token.hasMoreTokens()) {
			result[i] = token.nextToken();
			i++;
		}
		return result;
	}

	//替换字符串
	public static String replaceStr(String str,String pattern,String replace){
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		while((e=str.indexOf(pattern,s))>=0){
			result.append(str.substring(s,e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

	//////正则表达式
	/*
	 private static boolean PatternStr(String strBody,String strDeny){
		Pattern p = Pattern.compile(strDeny);
		Matcher m = p.matcher(strBody);
		if (m.find()) {
			return true;
		}else{
			return false;
		}
	}
	*/
	public static String URLEncoding(String strContent){
		strContent = replaceStr(strContent, "%", "%25");
        strContent = replaceStr(strContent, "!", "%21");
        strContent = replaceStr(strContent, "#", "%23");
        strContent = replaceStr(strContent, "$", "%24");
        strContent = replaceStr(strContent, "^", "%5E");
        strContent = replaceStr(strContent, "&", "%26");
        strContent = replaceStr(strContent, "(", "%28");
        strContent = replaceStr(strContent, ")", "%29");
        strContent = replaceStr(strContent, "<", "%3C");
        strContent = replaceStr(strContent, ">", "%3E");
        strContent = replaceStr(strContent, "?", "%3F");
        strContent = replaceStr(strContent, "/", "%2F");
        strContent = replaceStr(strContent, "\\", "%5C");
        strContent = replaceStr(strContent, "=", "%3D");
        strContent = replaceStr(strContent, "+", "%2B");
        strContent = replaceStr(strContent, "|", "%7C");
        strContent = replaceStr(strContent, "[", "%5B");
        strContent = replaceStr(strContent, "]", "%5D");
        strContent = replaceStr(strContent, "{", "%7B");
        strContent = replaceStr(strContent, "}", "%7D");
        strContent = replaceStr(strContent, ",", "%2C");
        strContent = replaceStr(strContent, ";", "%3B");
        strContent = replaceStr(strContent, ":", "%3A");
        strContent = replaceStr(strContent, "'", "%27");
        strContent = replaceStr(strContent, "\"", "%22");
			return strContent;
	}
	//支持中文
	public String getStr(String str)
	{
		try
		{
			String temp_p=str;
			byte[] temp_t=temp_p.getBytes("ISO8859-1");
			String temp=new  String(temp_t);
			return temp;
		}
		catch(Exception e)	{}
		return  "";
	}

     public static void main(String []args)
     {
        byte []b=new byte[16];
        b[0]=0;
        for(int i=1;i<b.length;i++) b[i]=0;
        b=cutb(b,0,16,(byte)0);
        System.out.println(new String(b));
     }
}