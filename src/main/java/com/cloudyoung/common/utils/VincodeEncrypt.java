package com.cloudyoung.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class VincodeEncrypt {
	
	public static final Map<String,String>map=new HashMap<String,String>();
	
	static{
		map.put("A", "K-M");
		map.put("B", "L-N");
		map.put("C", "M-O");
		map.put("D", "N-P");
		map.put("E", "O-Q");
		map.put("F","P-R");
		map.put("G","Q-S");
		map.put("H", "R-T");
		map.put("I", "S-U");
		map.put("J", "T-V");
		map.put("K","U-W");
		map.put("L", "V-X");
		map.put("M","W-Y");
		map.put("N", "X-Z");
		map.put("O", "Y-A");
		map.put("P", "A-B");
		map.put("Q", "A-C");
		map.put("R", "B-D");
		map.put("S", "C-E");
		map.put("T", "D-F");
		map.put("U", "E-G");
		map.put("V", "F-H");
		map.put("W", "G-I");
		map.put("X", "H-J");
		map.put("Y","I-K");
		map.put("Z", "J-L");
		map.put("0", "4-8");
		map.put("1","5-9");
		map.put("2","6-0");
		map.put("3", "7-1");
		map.put("4", "8-2");
		map.put("5","9-3");
		map.put("6", "0-4");
		map.put("7", "1-5");
		map.put("8","2-6");
		map.put("9", "3-7");
	}
	
	/**
	 * Description: 获取激活码
	 * @Version1.0 2017年6月7日 下午1:45:58 by 代鹏（daipeng.456@gmail.com）创建
	 * @param vincode
	 * @return
	 */
	public static String getActivityCode(String vincode){
		if(vincode.trim().length()!=17){
			return null;
		}
		String activityCode=vincode.substring(3).toUpperCase();
		activityCode=activityCode.substring(0, 2)+activityCode.substring(1, 7)+activityCode.substring(6,12)+activityCode.substring(11,14);
		char[]ary=activityCode.toCharArray();
		for(int i=0;i<ary.length;i++){
			String key=ary[i]+"";
			if(i==2||i==8||i==14){
				if(StringUtils.isNotBlank(map.get(key))){
					String verifyStr=getVerifyStr(key);
					ary[i]=verifyStr.charAt(0);
				}
			}else{
				if(StringUtils.isNotBlank(map.get(key))){
					String activityStr=getActivityStr(key);
					ary[i]=activityStr.charAt(0);
				}
			}
		}
		String result=new String(ary);
		return result;
	}
    public static String getVerifyStr(String key){
    	if(map!=null&&StringUtils.isNotBlank(map.get(key))){
    		String value=map.get(key);
    		String[] values=value.split("-");
    		if(values!=null&&values.length==2){
    			return values[1];
    		}
    	}
    	return null;
    }
    
    public static String getActivityStr(String  key){
    	if(map!=null&&StringUtils.isNotBlank(map.get(key))){
    		String value=map.get(key);
    		String[] values=value.split("-");
    		if(values!=null&&values.length==2){
    			return values[0];
    		}
    	}
    	return null;
    }
    
    public static boolean isLegal(String vincode){
    	if(StringUtils.isNotBlank(vincode)){
    		vincode=vincode.trim().toUpperCase();
    		if(vincode.length()==17){
    			if(vincode.indexOf("Q")==-1&&vincode.indexOf("I")==-1&&vincode.indexOf("O")==-1){
    				if(vincode.substring(0, 3).equalsIgnoreCase("LZW")){
    				   if(StringUtils.isNumeric(vincode.substring(11))){
    					  return true;
    				   }
    				   return false;
    			    }
    			   return false;
    			}
    			return false;
    		}
    		return false;
    	}
    	return false;
    }
    
    public static boolean isSimpleLegal(String simpleVinCode){
    	if(StringUtils.isNotBlank(simpleVinCode)){
    		simpleVinCode=simpleVinCode.trim().toUpperCase();
    		if(simpleVinCode.length()==7){
    			if(simpleVinCode.indexOf("Q")==-1&&simpleVinCode.indexOf("I")==-1&&simpleVinCode.indexOf("O")==-1){
					if(StringUtils.isNumeric(simpleVinCode.substring(1))){
						return true;
					}
    				return false;
    			}
    			return false;
    		}
    		return false;
    	}
    	return true;
    }
    public static void main(String[]args){   
 	   // String vi="1234567890dsa";
    	String activityCode="LZWADAGA4HG591026";
    /*	activityCode=activityCode.substring(0, 2)+activityCode.substring(1, 7)+activityCode.substring(6,12)+activityCode.substring(11,14);
    	System.out.println(activityCode);*/
/*    	System.out.println(activityCode.substring(10));  
    	System.out.println(activityCode.indexOf("L"));*/
    	System.out.println(isLegal(activityCode));
    	System.out.println(getActivityCode(activityCode));
    /*	char a[]={'0','1'};    
    	System.out.println(new String (a));  */
      
    }
}
