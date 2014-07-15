
package com.surge.engine.sms.util;

public class CharsetTools
{
	/** ��Ǳ����ſ�ʼλ�� */

	private static final int SINGLE_BYTE_SYMBOL_START = 0x0020;

	/** ��Ǳ����Ž���λ�� */

	private static final int SINGLE_BYTE_SYMBOL_END = 0x007E;

	/** ���Ƭ������ʼλ�� */

	private static final int SINGLE_BYTE_KATAKANA_START = 0xFF61;

	/** ���Ƭ��������λ�� */

	private static final int SINGLE_BYTE_KATAKANA_END = 0xFF9F;

	/** ��ǿո� */

	private static final int SINGLE_BYTE_SPACE_END = 0x0020;

	/**

	 * ��������ж�

	 * @return �ж��Y�� true:�������

	 */

	public static boolean isSingleByteDigit( final char c ) {

	    return ( '0' <= c ) && ( c <= '9' );

	}

	/**

	 * ���Ӣ���ж�

	 * @return �ж��Y�� true:���Ӣ��

	 */

	public static boolean isSingleByteAlpha( final char c ) {

	    return ( ( 'a' <= c ) && ( c <= 'z' ) ) || ( ( 'A' <= c ) && ( c <= 'Z' ) );

	}

	/**

	 * ��Ǳ������ж�

	 * @return �ж��Y�� true:��Ǳ�����

	 */

	public static boolean isSingleByteSymbol( final char c ) {

	    return ( SINGLE_BYTE_SYMBOL_START <= c ) && 

	                ( c <= SINGLE_BYTE_SYMBOL_END ) && 

	                !isSingleByteAlpha( c ) && 

	                !isSingleByteDigit( c );

	}

	/**

	 * ���Ƭ�����ж�

	 * @return �ж��Y�� true:���Ƭ����

	 */

	public static boolean isSingleByteKatakana( final char c ) {

	    return ( SINGLE_BYTE_KATAKANA_START <= c ) && ( c <= SINGLE_BYTE_KATAKANA_END );

	}

	/**

	 * ��ǿո��ж�

	 * @return �ж��Y�� true:��ǿո�

	 */

	public static boolean isSingleByteSpace( final char c ) {   

	    boolean bRet = false;    

	    if ( c == SINGLE_BYTE_SPACE_END ) {        

	        bRet = true;

	    }    

	    return bRet;

	}


	

	/*�ж�����ͬ���

	===========================================

	�������ϸ��ַ��ж���������д���������������Ƿ�淶�ĺ���

	���磺


	 * ֻ��������Ӣ���Ͱ��"@"����

	 * @return �ж��Y�� true:��֤�Ϸ�

	 */

	public static boolean isStringValidate(final String str)
	{

		for (int i = str.length() - 1; 0 <= i; i--)
		{
			if (!isSingleByteDigit(str.charAt(i)) &&

			!isSingleByteAlpha(str.charAt(i)) && !isSingleByteSymbol(str.charAt(i))
					&& !isSingleByteKatakana(str.charAt(i)))
			{

				if (str.charAt(i) != "@".charAt(0))
				{
					return false;

				}
			}
		}

		return true;
	}
	public static void main(String [] args)
	{
		System.out.println("���:"+isStringValidate("(1/8)(1/2)Stopstopstopstopstopstopstopstopstopstopstopstopstopstopstopstopstopst"));
	}
}
