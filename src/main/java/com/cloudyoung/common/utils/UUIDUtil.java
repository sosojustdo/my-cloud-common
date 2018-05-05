package com.cloudyoung.common.utils;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class UUIDUtil {
	
	public static final String strGroup = "abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String uuidCharUpper(){
		String ori = uuid();
		if(StringUtils.isNotBlank(ori)){
			return ori.toUpperCase(Locale.CHINA);
		}
		return null;
	}
	
	public static String randomString(int length){
		if(length <= 0){
			throw new IllegalArgumentException("string length must greater than 0!!");
		}
		Random r = new Random();
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<length; i++){
			int index = r.nextInt(strGroup.length());
			buffer.append(strGroup.charAt(index));
		}
		return buffer.toString();		
	}
}
