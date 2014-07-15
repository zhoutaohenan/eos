package com.surge.engine.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class DesEncrypter
{
	/**
	 * 默认的加密密钥
	 */
	public static final String DEFAULT_PASS_PHRASE = "C'est la vie.";

	/**
	 * 
	 * @param str待加密的字符串
	 * @param passPhrase密钥
	 *            ,任一字符串
	 * @return
	 * @throws FormatException
	 */
	public static String encrypt(String str) throws Exception
	{
		byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56,
				(byte) 0x35, (byte) 0xE3, (byte) 0x03 };

		int iterationCount = 19;

		try
		{
			KeySpec keySpec = new PBEKeySpec(DEFAULT_PASS_PHRASE.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);
			Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8);
			return BASE64Encoder.encode(enc);
		} catch (BadPaddingException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvalidKeyException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	/**
	 * 
	 * @param str待解密的字符串
	 * @param passPhrase密钥
	 *            ,该字符串必须和加密时候的密钥一致,否则将不会得到正确密码
	 * @return
	 * @throws FormatException
	 */
	public static String decrypt(String str, String passPhrase) throws Exception
	{
		byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56,
				(byte) 0x35, (byte) 0xE3, (byte) 0x03 };

		int iterationCount = 19;

		try
		{
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);
			Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			byte[] dec = BASE64Encoder.decode(str);
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF8");
		} catch (BadPaddingException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvalidKeyException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	public static void main(String[] args) {
	    try {
            System.out.println(DesEncrypter.encrypt("test"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}