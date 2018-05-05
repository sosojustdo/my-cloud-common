package com.cloudyoung.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.cloudyoung.common.enums.PlatformNameEnum;

/**
 * Description: 新的HttpUtil方法
 * All Rights Reserved.
 * @version 1.0  2017年7月11日 下午6:53:11  by 代鹏（daipeng.456@gmail.com）创建
 */
public final class HttpUtils {

    private static final Logger logger = LogManager.getLogger(HttpUtils.class);

    /** get请求. **/
    public static final String GET_METHOD = "get";

    /** post请求. **/
    public static final String POST_METHOD = "post";

    public static final String UTF8 = "UTF-8";

    public static final String GBK = "GBK";

    /** Httpclient对应CONNECTION_TIMEOUT对应的值 */
    private static final Integer CONNECTION_TIMEOUT = 5000;

    /** Httpclient对应SO_TIMEOUT对应的值 */
    private static final Integer SO_TIMEOUT = 10000;

    /** Httpclient对应CONNECTION_REQUEST_TIMEOUT对应的值 */
    private static final Integer CONNECTION_REQUEST_TIMEOUT = 5000;

    private HttpUtils() {
    }

    /**
     * Description: Http请求获取返回内容（可设置超时时间） 
     * @Version1.0 2017年7月11日 下午6:52:06 by 代鹏（daipeng.456@gmail.com）创建
     * @param url
     * @param methodStr
     * @param headerMap
     * @param paramsMap
     * @param encode
     * @param connectionTimeout
     * @param soTimeout
     * @return
     */
    public static String getContent(final String url, String methodStr, Map<String, String> headerMap, Map<String, String> paramsMap, String encode, int connectionTimeout, int soTimeout) {
        final HttpClient httpClient = new HttpClient();
        HttpMethodBase method = null;
        if (methodStr.toLowerCase().equals(GET_METHOD)) {
            method = new GetMethod(url);
            if (null != paramsMap && paramsMap.size() > 0) {
                NameValuePair[] params = getParamsFromMap(paramsMap);
                String queryString = EncodingUtil.formUrlEncode(params, encode);
                method.setQueryString(queryString);
            }
        } else {
            method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
            NameValuePair[] params = getParamsFromMap(paramsMap);
            ((PostMethod) method).setRequestBody(params);
        }

        if (null != headerMap && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                method.addRequestHeader(header.getKey(), header.getValue());
            }
        }

        try {
            // 设置连接一个url的连接等待超时时间
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
            // 设置读取数据的超时时间
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);

            final int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                LogUtil.info(logger, PlatformNameEnum.WX, "paramsMap:" + JSON.toJSONString(paramsMap) + "headerMap:" + JSON.toJSONString(headerMap), "HttpUtils_getContent_invoked",
                        "httpStatusCode:" + statusCode);
            } else {
                // return method.getResponseBodyAsString();
                InputStream stream = method.getResponseBodyAsStream();
                byte[] bytes = IOUtils.toByteArray(stream);
                return EncodingUtil.getString(bytes, method.getResponseCharSet());
            }
        } catch (final Exception e) {
            LogUtil.error(logger, e, PlatformNameEnum.WX, "paramsMap:" + JSON.toJSONString(paramsMap) + "headerMap:" + JSON.toJSONString(headerMap), "HttpUtils_getContent_invoked");
        } finally {
            method.releaseConnection();
            // 客户端主动关闭连接
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
        return null;
    }

    /**
     * Description: Http请求获取返回内容（使用默认超时时间） 
     * @Version1.0 2017年7月11日 下午6:52:41 by 代鹏（daipeng.456@gmail.com）创建
     * @param url
     * @param methodStr
     * @param headerMap
     * @param paramsMap
     * @param encode
     * @return
     */
    public static String getContent(final String url, String methodStr, Map<String, String> headerMap, Map<String, String> paramsMap, String encode) {
        return getContent(url, methodStr, headerMap, paramsMap, encode, CONNECTION_TIMEOUT, SO_TIMEOUT);
    }

    /**
     * Description: 普通get请求支持url后拼接参数，urlhurl?a=123&b=456
     * @Version1.0 2017年10月17日 上午10:37:49 by 代鹏（daipeng.456@gmail.com）创建
     * @param urlWithParams
     * @param header
     * @return
     * @throws IOException
     */
    public static String requestGet(String urlWithParams, Map<String, String> header) {
        CloseableHttpClient httpclient = getHttpClient();
        HttpGet httpGet = new HttpGet(urlWithParams);
        // 设置header
        setHttpGetHeader(httpGet, header);
        // 配置请求的超时设置
        httpGet.setConfig(buildRequestConfig());

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LogUtil.info(logger, PlatformNameEnum.WX, urlWithParams, "header params:" + JSON.toJSONString(header), "result:" + EntityUtils.toString(response.getEntity()));
            } else {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                return jsonStr;
            }
        } catch (Exception e) {
            LogUtil.error(logger, e, PlatformNameEnum.WX, urlWithParams, "header params:" + JSON.toJSONString(header));
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Description: post请求，请求body参数传json字符串
     * @Version1.0 2017年10月17日 上午11:36:57 by 代鹏（daipeng.456@gmail.com）创建
     * @param url
     * @param bodyJson
     * @param header
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String requestPost(String url, String bodyJson, Map<String, String> header) {
        CloseableHttpClient httpclient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        // 设置header
        setHttpPostHeader(httpPost, header);
        // 配置请求的超时设置
        httpPost.setConfig(buildRequestConfig());

        StringEntity s = new StringEntity(StringUtils.isNotBlank(bodyJson) ? bodyJson : "", "UTF-8");
        httpPost.setEntity(s);

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                LogUtil.info(logger, PlatformNameEnum.WX, "bodyJson:" + bodyJson + "header params:" + JSON.toJSONString(header), "HttpUtils_requestPost_invoked",
                        "result:" + EntityUtils.toString(response.getEntity()));
            } else {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                return jsonStr;
            }
        } catch (Exception e) {
            LogUtil.error(logger, e, PlatformNameEnum.WX, "bodyJson:" + bodyJson + "header params:" + JSON.toJSONString(header), "HttpUtils_requestPost_invoked");
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * @Descpription: 针对Json格式的Post请求的发送
     * @author    李美根（limg@cloud-young.com）创建
     * @version 1.0  2018/2/23 10:22
     * @param   data 是json 序列号后的数据
     * @return
     */
    public static  String sendJsonPost(String url, String data,PlatformNameEnum platformEnum) {
        String response = null;
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(data,
                        ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httppost.setHeader("Content-Type","application/json");
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils
                        .toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(logger, e, platformEnum.getPlatformName(), "url:"+ url  +",bodyJson:" + data, "sendJsonPost方法");
        }
        return response;
    }



    /**
     * Description:构建RequestConfig
     * @Version1.0 2017年10月17日 上午11:53:34 by 代鹏（daipeng.456@gmail.com）创建
     * @return
     */
    private static RequestConfig buildRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SO_TIMEOUT).build();
        return requestConfig;
    }

    /**
     * Description:设置HttpPost Header
     * @Version1.0 2017年10月17日 下午12:09:28 by 代鹏（daipeng.456@gmail.com）创建
     * @param httpPost
     * @param header
     */
    private static void setHttpPostHeader(HttpPost httpPost, Map<String, String> header) {
        httpPost.addHeader("Content-Type", "text/html;charset=UTF-8");
        if (MapUtils.isNotEmpty(header)) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description:设置HttpGet Header
     * @Version1.0 2017年10月17日 下午12:11:09 by 代鹏（daipeng.456@gmail.com）创建
     * @param httpGet
     * @param header
     */
    private static void setHttpGetHeader(HttpGet httpGet, Map<String, String> header) {
        httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
        if (MapUtils.isNotEmpty(header)) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description:获取HttpClient
     * @Version1.0 2017年10月17日 下午12:12:37 by 代鹏（daipeng.456@gmail.com）创建
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE).build();
    }

    /**
     * 从参数map中构建NameValuePair数组.
     * @param paramsMap
     * @return
     */
    private static NameValuePair[] getParamsFromMap(Map<String, String> paramsMap) {
        if (null != paramsMap && !paramsMap.isEmpty()) {
            NameValuePair[] params = new NameValuePair[paramsMap.size()];
            int pos = 0;
            Iterator<String> iter = paramsMap.keySet().iterator();
            while (iter.hasNext()) {
                String paramName = iter.next();
                String paramValue = paramsMap.get(paramName);

                params[pos++] = new NameValuePair(paramName, paramValue);
            }
            return params;
        }
        return null;
    }

}
