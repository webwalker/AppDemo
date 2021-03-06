package com.webwalker.framework.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 3DES加密工具类
 * 
 * @author xu.jian
 * 
 */
public class Encrypter3DES {
	// 密钥
	private static String secretKey = "liuyunqiang@lx100$#365#$";
	// 向量
	private static String iv = "01234567";
	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";

	public static String encode(String plainText, String key) throws Exception {
		secretKey = key;
		return encode(plainText);
	}

	/**
	 * 3DES加密
	 * 
	 * @param plainText
	 *            普通文本
	 * @return
	 * @throws Exception
	 */
	public static String encode(String plainText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return EncrypterBase64.encode(encryptData);
	}

	public static String decode(String encryptText, String key)
			throws Exception {
		secretKey = key;
		return decode(encryptText);
	}

	/**
	 * 3DES解密
	 * 
	 * @param encryptText
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encryptText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher
				.doFinal(EncrypterBase64.decode(encryptText));

		return new String(decryptData, encoding);
	}

	public static String getSecretKey() {
		return secretKey;
	}

	public static void setSecretKey(String secretKey) {
		Encrypter3DES.secretKey = secretKey;
	}

	public static String getIv() {
		return iv;
	}

	public static void setIv(String iv) {
		Encrypter3DES.iv = iv;
	}
}
