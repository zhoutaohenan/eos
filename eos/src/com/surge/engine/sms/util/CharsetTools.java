
package com.surge.engine.sms.util;

public class CharsetTools
{
	/** 半角标点符号开始位置 */

	private static final int SINGLE_BYTE_SYMBOL_START = 0x0020;

	/** 半角标点符号结束位置 */

	private static final int SINGLE_BYTE_SYMBOL_END = 0x007E;

	/** 半角片假名开始位置 */

	private static final int SINGLE_BYTE_KATAKANA_START = 0xFF61;

	/** 半角片假名结束位置 */

	private static final int SINGLE_BYTE_KATAKANA_END = 0xFF9F;

	/** 半角空格 */

	private static final int SINGLE_BYTE_SPACE_END = 0x0020;

	/**

	 * 半角数字判定

	 * @return 判定結果 true:半角数字

	 */

	public static boolean isSingleByteDigit( final char c ) {

	    return ( '0' <= c ) && ( c <= '9' );

	}

	/**

	 * 半角英字判定

	 * @return 判定結果 true:半角英字

	 */

	public static boolean isSingleByteAlpha( final char c ) {

	    return ( ( 'a' <= c ) && ( c <= 'z' ) ) || ( ( 'A' <= c ) && ( c <= 'Z' ) );

	}

	/**

	 * 半角标点符号判定

	 * @return 判定結果 true:半角标点符号

	 */

	public static boolean isSingleByteSymbol( final char c ) {

	    return ( SINGLE_BYTE_SYMBOL_START <= c ) && 

	                ( c <= SINGLE_BYTE_SYMBOL_END ) && 

	                !isSingleByteAlpha( c ) && 

	                !isSingleByteDigit( c );

	}

	/**

	 * 半角片假名判定

	 * @return 判定結果 true:半角片假名

	 */

	public static boolean isSingleByteKatakana( final char c ) {

	    return ( SINGLE_BYTE_KATAKANA_START <= c ) && ( c <= SINGLE_BYTE_KATAKANA_END );

	}

	/**

	 * 半角空格判定

	 * @return 判定結果 true:半角空格

	 */

	public static boolean isSingleByteSpace( final char c ) {   

	    boolean bRet = false;    

	    if ( c == SINGLE_BYTE_SPACE_END ) {        

	        bRet = true;

	    }    

	    return bRet;

	}


	

	/*判定函数同半角

	===========================================

	根据以上各字符判定函数可以写出检验输入文字是否规范的函数

	例如：


	 * 只能输入半角英数和半角"@"符号

	 * @return 判定結果 true:验证合法

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
		System.out.println("结果:"+isStringValidate("(1/8)(1/2)Stopstopstopstopstopstopstopstopstopstopstopstopstopstopstopstopstopst"));
	}
}
