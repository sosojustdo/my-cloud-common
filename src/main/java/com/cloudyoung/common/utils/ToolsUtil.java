package com.cloudyoung.common.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Description: 自定义工具类
 * All Rights Reserved.
 * @version 1.0  2016年6月28日 下午6:55:48  by 代鹏（daipeng.456@gmail.com）创建
 */
public class ToolsUtil {
	
	/**
	 * Description: 可变偶数参数转换map
	 * Format:"custId", 123, "status", 2, "domain", "www.baidu.com", "postId", 123456
	 * @Version1.0 2016年6月17日 下午4:38:16 by 代鹏（daipeng.456@gmail.com）创建
	 * @param params
	 * @return
	 */
	public static Map<String, Object> builderMapParams(final Object ...params) {
		if(params.length <= 0){
			new IllegalArgumentException("builderParams params is empty!");
		}
		if(params.length % 2 !=0){
			new IllegalArgumentException("builderParams params length must be even numbers!");
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		List<Object> objectList = Arrays.asList(params);
		int size = objectList.size();
		for(int i=0; i<size; i=i+2){
			Object k = objectList.get(i);
			if(k instanceof String){
				Object v = objectList.get(i+1);
				paramsMap.put((String)k, v);
			}
		}
		return paramsMap;
	}
	
	/**
	 * 隐藏手机号码中间四位
	 * @param phoneNumber
	 * @return
	 */
	public static String hidenMobilePhoneNumer(String phoneNumber) {
		if(checkMobilePhone(phoneNumber)){
			StringBuffer sb = new StringBuffer();
			sb.append(phoneNumber.substring(0, 3));
			sb.append("****");
			sb.append(phoneNumber.substring(7, phoneNumber.length()));
			return sb.toString();
		}
		return phoneNumber;
	}
	
	/**
	 * Description: 正则校验邮箱
	 * @Version1.0 2015年6月12日 下午4:24:28 by 代鹏（daipeng@dangdang.com）创建
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		if(StringUtils.isBlank(email)){
			return false;
		}
		String mail_regex = "^\\w+((\\.\\w+)|(-\\w+))*\\@[a-zA-Z0-9]+((\\.|-|__|_)[a-zA-Z0-9]+)*\\.[a-zA-Z0-9]+$";
		Pattern mail_pattern = Pattern.compile(mail_regex);
		return mail_pattern.matcher(email).matches();
	}
	
	/**
	 * Description: 正则校验手机号
	 * @Version1.0 2015年6月12日 下午4:24:46 by 代鹏（daipeng@dangdang.com）创建
	 * @param phoneNum
	 * @return
	 */
	public static boolean checkMobilePhone(String phoneNum){
		if(StringUtils.isBlank(phoneNum)){
			return false;
		}
		String phone_regex = "^1[234578]\\d{9}$";
		Pattern phone_pattern = Pattern.compile(phone_regex);
		return phone_pattern.matcher(phoneNum).matches();
	}
	
	/**
	 * 过滤内容中字母
	 * @param inputString
	 * @return
	 */
	public static String filterBookReviewContentForAlphabet(String inputString){
		if(inputString == null || inputString.length() == 0){
			return "";
		}
		inputString = inputString.replaceAll("[(A-Za-z)]", "");
		return inputString;
	}
	
	/**
	 * 过滤除了汉字、数字、字母之外所有字符
	 * @param inputString
	 * @return
	 */
	public static String reservedCharacters(String inputString){
		if(inputString == null || inputString.length() == 0){
			return "";
		}
		String reg = "[^\\u4e00-\\u9fa5A-Za-z0-9]";
        return inputString.replaceAll(reg, "");
	}
	
	/**
	 * Description: 判断字符串只包含某些特殊字符
	 * @Version1.0 2016年4月1日 下午5:42:07 by 代鹏（daipeng@dangdang.com）创建
	 * @param source
	 * @param chars
	 * @return
	 */
	public static boolean stringJustIncludeChars(String source, char... chars){
		if(StringUtils.isNotBlank(source) && chars.length > 0 ){
			int cnt = 0;
			int charCount = source.length();
			for(int i = 0; i < charCount; i++){
				char item = source.charAt(i);
				for(int j = 0; j < chars.length; j++){
					if(chars[j] == item){
						cnt++;
						break;
					}
				}
			}
			if(cnt == charCount){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Description: 检查必传参数都不为空
	 * @Version1.0 2016年7月7日 下午1:36:30 by 代鹏（daipeng.456@gmail.com）创建
	 * @param params
	 * @return
	 */
	public static boolean checkParamsNotEmpty(String... params){
		if(null == params || params.length <= 0){
			return false;
		}
		for(String str:params){
			if(StringUtils.isBlank(str)){
				return false;
			}
		}
		return true;
	}
	
}
