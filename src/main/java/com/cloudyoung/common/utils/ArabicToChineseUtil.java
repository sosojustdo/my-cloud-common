package com.cloudyoung.common.utils;

import org.apache.commons.lang.StringUtils;
/**
 * Description: 数字大小写转换
 * Copyright (c) Department of Research and Development/Beijing
 * All Rights Reserved.
 * @version 1.0  2016年7月22日 下午9:07:42  by 杨雷（yanglei@cloud-young.com）创建
 */
public class ArabicToChineseUtil {
	
	static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿" };
    static char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
    
	public static String foematInteger(int num) {
		
		if(num == 0) return String.valueOf(numArray[0]);
		
        char[] processValue = String.valueOf(num).toCharArray();
        int len = processValue.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = processValue[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == processValue[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        
        String result = sb.toString();
        //处理后面的零
        if(result.endsWith("零"))
        	result = StringUtils.substring(result, 0, result.length()-1);
        //处理10-19 
        if(result.startsWith("一十"))
        	result = StringUtils.substring(result, 1, result.length());
        
        return result;
    }

    public static String formatDecimal(double decimal) {
        String decimals = String.valueOf(decimal);
        int decIndex = decimals.indexOf(".");
        int integ = Integer.valueOf(decimals.substring(0, decIndex));
        int dec = Integer.valueOf(decimals.substring(decIndex + 1));
        String result = foematInteger(integ) + "." + formatFractionalPart(dec);
        return result;
    }

    private static String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }
}
