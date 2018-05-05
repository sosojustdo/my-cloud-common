package com.cloudyoung.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Description:加解密工具类 
 * Copyright (c) Department of Research and Development/Beijing
 * All Rights Reserved.
 * @version 1.0  2016年10月15日 下午4:12:39  by 杨雷（yanglei@cloud-young.com）创建
 */
public class EncryptUtil {
	
	private final static String DES = "DES";
    private final static String CODE = "UTF-8";
    
    
    /**
     * @Description: 根据key对json串加密
     * @param:
     * @return: String
     * @throws:
     */
    public static String encrypt(String data, String key) throws Exception {
        return encrypt(data, key, CODE);
    }
    
    /**
     * @Description:加密转BASE64
     * @param:
     * @return: String
     * @throws:
     */
    public static String encrypt(String data, String key,String code) throws Exception {
        byte[] bt = encrypt(data.getBytes(code), key.getBytes(code));
        String strs = Base64.encodeBase64String(bt);
        return strs;
    }
    
    /**
     * @Description: DES加密
     * @param:
     * @return: byte[]
     * @throws:
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
    
    
    
    /**
     * @Description: 根据key对json串解密
     * @param:
     * @return: String
     * @throws:
     */
    public static String decryptString(String data, String key) throws Exception {
        //解密后
       return decryptString(data, key,CODE);
        
    }
    
    /**
     * @Description: 转UTF-8进行解密
     * @param:
     * @return: String
     * @throws:
     */
    public static String decryptString(String data, String key,String code) throws Exception {
       return decrypt(data, key,code);
    }
    
    /**
     * @Description: 转BASE64解密
     * @param:
     * @return: String
     * @throws:
     */
    public static String decrypt(String data, String key,String code) throws IOException,Exception {
		if (data == null)
		    return null;
		byte[] buf = Base64.decodeBase64(data.getBytes(code));
		byte[] bt = decrypt(buf, key.getBytes(code));
		return new String(bt,code);
	}
    
    
    /**
     * @Description: DES解密
     * @param:
     * @return: byte[]
     * @throws:
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
    
	public static String sha512(String originalText) {
		if (originalText == null)
			return null;
		return DigestUtils.sha512Hex(originalText);
	}

	public static String MD5(String originalText) {
		if (originalText == null)
			return null;
		return DigestUtils.md5Hex(originalText);
	}

	public static String MD5(String originalText, String salt) {
		if (originalText == null)
			return null;
		return DigestUtils.md5Hex(mergePasswordAndSalt(originalText, salt, false));
	}

	public static String mergePasswordAndSalt(String password, Object salt, boolean strict) {
		if (password == null) {
			password = "";
		}

		if (strict && (salt != null)) {
			if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
				throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
			}
		}

		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}
	
    /**
     * Description: base64加密
     * @param 
     * @return String
     * @throws 
     */
	public static String base64Encode(String originalText) throws UnsupportedEncodingException {
		if (originalText == null)
			return null;
		return Base64.encodeBase64String(originalText.getBytes(CODE));
	}
	
    /**
     * Description: base64解密
     * @param 
     * @return String
     * @throws 
     */
	public static String base64Decode(String text) throws UnsupportedEncodingException {
		if (text == null)
			return null;
		return new String(Base64.decodeBase64(text), CODE);
	}
	
}
