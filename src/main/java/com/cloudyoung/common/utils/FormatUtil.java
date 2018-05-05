package com.cloudyoung.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Description: 格式化工具类
 * All Rights Reserved.
 * @version 1.0  2015年7月2日 上午10:15:39  by 代鹏（daipeng@dangdang.com）创建
 */
public abstract class FormatUtil {
	
	// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>// }
	public static final String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
	
	// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>// }
	public static final String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
	
	// 定义HTML标签的正则表达式
	public static final String regEx_html = "[\\w]*<[^>]+>[\\w]*";
	
	private FormatUtil() {
	}
	
	public static String formatPrice(String priceStr) {
		double price = Double.valueOf(priceStr);
		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance();
		df.applyPattern("0.00");
		return df.format(price);
	}
	
	/**
	 * 过滤掉HTML标签获取纯文本内容
	 * @param inputString
	 * @return
	 */
	public static String Html2Text(String inputString) {
		if(StringUtils.isBlank(inputString)){
			return "";
		}
		
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			
			//替换“&nbsp;”
			textStr = htmlStr.replaceAll("&nbsp;"," ");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr.trim();
	}
	
	/**
	 * Description:替换转义字符
	 * @Version1.0 2018年1月13日 下午3:16:27 by 代鹏（daipeng.456@gmail.com）创建
	 * @param originString
	 * @return
	 */
	public static String replaceEscapeString(String originString) {
	    if(StringUtils.isNotBlank(originString)) {
	        if(originString.contains("\\")) {
	            return originString.replace("\\", "");
	        }
	        return originString;
	    }
	    return null;
	}
		
}
