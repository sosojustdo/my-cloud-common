package com.cloudyoung.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;

/**
 * Description: Http异步请求Util工具类(requires JDK8)
 * All Rights Reserved.
 * @version 1.0  2016年12月13日 下午3:32:45  by 代鹏（daipeng.456@gmail.com）创建
 */
@Deprecated
public class HttpAsyncUtil {
	
	//默认字符编码
	private static final String DEFAULT_CHARSET = "UTF-8";
    
	//连接超时时间5秒
    private static final int CONNECT_TIME_OUT = 5000;
    //读取数据超时时间5秒
    private static final int SO_TIME_OUT = 5000;
    
    public static String get(String url){
    	return execute(url, "GET", null, null);
    }
    
    public static String get(String url, Map<String, String> params){
    	return execute(url, "GET", params, null);
    }
    
    public static String get(String url, Map<String, String> params, Map<String, String> headers){
    	return execute(url, "GET", params, headers);
    }
    
    public static String post(String url){
    	return execute(url, "POST", null, null);
    }
    
    public static String post(String url, Map<String, String> params){
    	return execute(url, "POST", params, null);
    }
    
    public static String post(String url, Map<String, String> params, Map<String, String> headers){
    	return execute(url, "POST", params, headers);
    }
    
    public static String put(String url){
    	return execute(url, "PUT", null, null);
    }
    
    public static String put(String url, Map<String, String> params){
    	return execute(url, "PUT", params, null);
    }
    
    public static String put(String url, Map<String, String> params, Map<String, String> headers){
    	return execute(url, "PUT", params, headers);
    }
    
    public static String delete(String url){
    	return execute(url, "DELETE", null, null);
    }
    
    public static String delete(String url, Map<String, String> params){
    	return execute(url, "DELETE", params, null);
    }
    
    public static String delete(String url, Map<String, String> params, Map<String, String> headers){
    	return execute(url, "DELETE", params, headers);
    }
    
    private static String execute(String url, String method, Map<String, String> params, Map<String, String> headers){
    	if(StringUtils.isBlank(url) || StringUtils.isBlank(method)){
    		throw new IllegalArgumentException("HttpAsyncUtil_execute_method_invoke_error, params url and method can not be empty!");
    	}
    	
    	AsyncHttpClient http = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder().setConnectTimeout(CONNECT_TIME_OUT).setReadTimeout(SO_TIME_OUT).setKeepAlive(true).build());
        try {
        	BoundRequestBuilder builder = null;
        	if("GET".equalsIgnoreCase(method)){
        		builder = http.prepareGet(url);
        	}else if("POST".equalsIgnoreCase(method)){
        		builder = http.preparePost(url);
        	}else if("PUT".equalsIgnoreCase(method)){
        		builder = http.preparePut(url);
        	}else if("DELETE".equalsIgnoreCase(method)){
        		builder = http.prepareDelete(url);
        	}else if("PATCH".equalsIgnoreCase(method)){
        		builder = http.preparePatch(url);
        	}else if("TRACE".equalsIgnoreCase(method)){
        		builder = http.prepareTrace(url);
        	}else if("HEAD".equalsIgnoreCase(method)){
        		builder = http.prepareHead(url);
        	}else if("OPTIONS".equalsIgnoreCase(method)){
        		builder = http.prepareOptions(url);
        	}else{
        		throw new IllegalArgumentException("HttpAsyncUtil_execute_method_invoke_error, http method unsupport!");
        	}
        	
    		if (params != null && !params.isEmpty()) {
    			Set<String> keys = params.keySet();
    			for (String key : keys) {
    				builder.addQueryParam(key, params.get(key));
    			}
    		}

    		if (headers != null && !headers.isEmpty()) {
    			Set<String> keys = headers.keySet();
    			for (String key : keys) {
    				builder.addHeader(key, headers.get(key));
    			}
    		}
    		
    		Future<Response> f = builder.execute();  
        	Response response = f.get();
        	if(null != response){
        		return response.getResponseBody(Charset.forName(DEFAULT_CHARSET));  
        	}
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	try {
				http.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
        return null; 
    }
    
}
