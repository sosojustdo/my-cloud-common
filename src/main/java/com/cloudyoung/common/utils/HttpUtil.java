package com.cloudyoung.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Deprecated
public final class HttpUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(HttpUtil.class);
	
    private static HttpClient httpClient = new HttpClient();
    
    public static String get(String url) {
		return execute(url, null, null, null, HttpGet.METHOD_NAME, false);
	}

	public static String get(String url, boolean isSSL) {
		return execute(url, null, null, null, HttpGet.METHOD_NAME, isSSL);
	}

	public static String get(String url, Map<String, String> params) {
		return execute(url, params, null, null, HttpGet.METHOD_NAME, false);
	}

	public static String get(String url, Map<String, String> params, boolean isSSL) {
		return execute(url, params, null, null, HttpGet.METHOD_NAME, isSSL);
	}

	public static String post(String url, boolean isSSL) {
		return execute(url, null, null, null, HttpPost.METHOD_NAME, isSSL);
	}

	public static String post(String url) {
		return execute(url, null, null, null, HttpPost.METHOD_NAME, false);
	}

	public static String post(String url, Map<String, String> params) {
		return execute(url, params, null, null, HttpPost.METHOD_NAME, false);
	}

	public static String post(String url, Map<String, String> params, boolean isSSL) {
		return execute(url, params, null, null, HttpPost.METHOD_NAME, isSSL);
	}

	public static String post(String url, Map<String, String> params, Map<String, String> headers) {
		return execute(url, params, headers, null, HttpPost.METHOD_NAME, false);
	}

	public static String post(String url, Map<String, String> params, Map<String, String> headers, boolean isSSL) {
		return execute(url, params, headers, null, HttpPost.METHOD_NAME, isSSL);
	}

	public static String post(String url, Map<String, String> params, Map<String, String> headers, String body) {
		return execute(url, params, headers, body, HttpPost.METHOD_NAME, false);
	}

	public static String get(String url, Map<String, String> params, Map<String, String> headers, String body, boolean isSSL) {
		return execute(url, params, headers, body, HttpGet.METHOD_NAME, isSSL);
	}

	public static String post(String url, Map<String, String> params, Map<String, String> headers, String body, boolean isSSL) {
		return execute(url, params, headers, body, HttpPost.METHOD_NAME, isSSL);
	}

	public static String delete(String url, Map<String, String> params, Map<String, String> headers, String body, boolean isSSL) {
		return execute(url, params, headers, body, HttpDelete.METHOD_NAME, isSSL);
	}

	public static String put(String url, Map<String, String> params, Map<String, String> headers, String body, boolean isSSL) {
		return execute(url, params, headers, body, HttpPut.METHOD_NAME, isSSL);
	}

	private static String execute(String url, Map<String, String> params, Map<String, String> headers, String body, String method, boolean isSSL) {
		CloseableHttpClient httpClient = null;
		if (isSSL) {
			httpClient = getSslClient();
		} else {
			httpClient = HttpClients.createDefault();
//			SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setSoTimeout(1).build();
//			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1).setConnectTimeout(1).setConnectionRequestTimeout(1).build();
//			httpClient = HttpClients.custom().setDefaultSocketConfig(socketConfig).setDefaultRequestConfig(requestConfig).build();
		}
		try {
			RequestBuilder builder = null;
			if (HttpGet.METHOD_NAME.equals(method)) {
				builder = RequestBuilder.get();
			} else if (HttpPost.METHOD_NAME.equals(method)) {
				builder = RequestBuilder.post();
			} else if (HttpDelete.METHOD_NAME.equals(method)) {
				builder = RequestBuilder.delete();
			} else if (HttpPut.METHOD_NAME.equals(method)) {
				builder = RequestBuilder.put();
			}
			builder.setUri(url);
			if (null != headers && !headers.isEmpty()) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					builder.addHeader(entry.getKey(), entry.getValue());
				}
			}
			if (null != params && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					builder.addParameter(entry.getKey(), entry.getValue());
				}
			}
			if (null != body) {
				builder.setEntity(new StringEntity(body, "UTF-8"));
			}
			
			CloseableHttpResponse response = httpClient.execute(builder.build());
			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == statusCode) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("response:{}", result);
				}
				return result;
			} else {
				LOGGER.warn("execute url:{}, 出现问题：{}", url, response.getStatusLine().toString());
			}
		} catch (Exception e) {
			LOGGER.error("execute 出现异常 ", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	public static CloseableHttpClient getSslClient() {
		CloseableHttpClient httpClient = null;
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{xtm}, null);
			SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(ctx);
			httpClient = HttpClients.custom().setSSLSocketFactory(sslFactory).build();
		} catch (Exception e) {
			LOGGER.error("HttpUtil getSslClient出现异常 ", e);
		}
		return httpClient;
	}
  
    /** 
     * @Title: getDataFromURL 
     * @Description: 根据URL跨域获取输出结果，支持http 
     * @param strURL 
     *            要访问的URL地址 
     * @param param 
     *            参数 
     * @return 结果字符串 
     * @throws Exception 
     */
    public static String getDataFromURL(String strURL, Map<String, String> param) throws Exception {  
        URL url = new URL(strURL);  
        URLConnection conn = url.openConnection();  
        conn.setDoOutput(true);  
  
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());  
        final StringBuilder sb = new StringBuilder(param.size() << 4); // 4次方  
        final Set<String> keys = param.keySet();  
        for (final String key : keys) {  
            final String value = param.get(key);  
            sb.append(key); // 不能包含特殊字符  
            sb.append('=');  
            sb.append(value);  
            sb.append('&');  
        }  
        // 将最后的 '&' 去掉  
        sb.deleteCharAt(sb.length() - 1);  
        writer.write(sb.toString());  
        writer.flush();  
        writer.close();  
  
        InputStreamReader reder = new InputStreamReader(conn.getInputStream(), "utf-8");  
        BufferedReader breader = new BufferedReader(reder);  
        // BufferedWriter w = new BufferedWriter(new FileWriter("d:/1.txt"));  
        String content = null;  
        String result = null;  
        while ((content = breader.readLine()) != null) {  
            result += content;  
        }  
  
        return result;  
    }  
  
   /** 
     * @Title: postMethod 
     * @Description: 根据URL跨域获取输出结果，支持https 
     * @param url 
     *            要访问的URL地址(http://www.xxx.com?) 
     * @param urlParm 
     *            参数(id=1212&pwd=2332) 
     * @return 结果字符串 
     */  
    public static String postMethod(String url, String urlParm) {  
        if (null == url || "".equals(url)) {  
            // url = "http://www.baidu.com";  
            return null;  
        }  
        PostMethod post = new PostMethod(url); // new UTF8PostMethod(url);  
        if (null != urlParm && !"".equals(urlParm)) {  
            String[] arr = urlParm.split("&");  
            NameValuePair[] data = new NameValuePair[arr.length];  
            for (int i = 0; i < arr.length; i++) {  
                String name = arr[i].substring(0, arr[i].lastIndexOf("="));  
                String value = arr[i].substring(arr[i].lastIndexOf("=") + 1);  
                data[i] = new NameValuePair(name, value);  
            }  
            post.setRequestBody(data);  
        }  
        int statusCode = 0;  
        String pageContent = "";  
        try {  
            statusCode = httpClient.executeMethod(post);  
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {  
                pageContent = post.getResponseBodyAsString();  
                return pageContent;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
            post.releaseConnection();  
        }  
        return null;  
    }  
  
    public static String doPost(String url, String json) throws Exception {  
        PostMethod postMethod = new PostMethod(url);  
        StringRequestEntity requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");  
        postMethod.setRequestEntity(requestEntity);  
         //发送请求，并获取响应对象   
        int statusCode = httpClient.executeMethod(postMethod);  
        String result = null;  
        if (statusCode == HttpStatus.SC_OK) {  
            result = postMethod.getResponseBodyAsString();  
            LOGGER.debug("response:{}", result);
        } else {  
            System.out.println("Method failed: " + postMethod.getStatusLine());  
        }  
        return result;  
    }  
  
    @SuppressWarnings("unused")
	private static HttpPost postForm(String url, Map<String, String> params) {  
        HttpPost httpost = new HttpPost(url);  
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();  
        Set<String> keySet = params.keySet();  
        for (String key : keySet) {  
            BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, params.get(key));  
            nvps.add(basicNameValuePair);  
        }  
        try {  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return httpost;  
    }  
  
    @SuppressWarnings("unused")
	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {  
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = paseResponse(response);  
        return body;  
    }  
  
    private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {  
        HttpResponse response = null;  
        try {  
            response = httpclient.execute(httpost);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
  
    private static String paseResponse(HttpResponse response) {  
        HttpEntity entity = response.getEntity();  
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return body;  
    }  
  
}