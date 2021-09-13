package com.medusa.gruul.logistics.util;

import com.medusa.gruul.common.core.constant.CommonConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * http请求工具类
 * </p>
 *
 * @author lcysike
 * @since 2020-03-11
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public HttpClientUtil() {
    }

    public String post(String url, StringEntity entity) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = this.postForm(url, entity);
        String body = "";
        body = this.invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var7) {
            logger.error("HttpClientService post error", var7);
        }

        return body;
    }

    public String post(String url, String name, String value) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("content", value));
        HttpPost post = this.postForm(url, new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        String body = "";
        body = this.invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var9) {
            logger.error("HttpClientService post error", var9);
        }

        return body;
    }

    public String postSFAPI(String url, String xml, String verifyCode) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("xml", xml));
        parameters.add(new BasicNameValuePair("verifyCode", verifyCode));
        HttpPost post = this.postForm(url, new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        String body = "";
        body = this.invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var9) {
            logger.error("HttpClientService post error", var9);
        }

        return body;
    }

    public String postYTOAPI(String url, String xml, String verifyCode, String clientCode, String type) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("logistics_interface", xml));
        parameters.add(new BasicNameValuePair("data_digest", verifyCode));
        parameters.add(new BasicNameValuePair("clientId", clientCode));
        parameters.add(new BasicNameValuePair("type", type));
        HttpPost post = this.postForm(url, new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        System.out.println("返回url转码: " + new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        String body = "";
        body = this.invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var9) {
            logger.error("HttpClientService post error", var9);
        }

        return body;
    }

    public String postYTORouteAPI(String routeURL, String sign, String appKey, String format, String method, String timestamp, String userId, String v, String reqXml) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("sign", sign));
        parameters.add(new BasicNameValuePair("app_key", appKey));
        parameters.add(new BasicNameValuePair("format", format));
        parameters.add(new BasicNameValuePair("method", method));
        parameters.add(new BasicNameValuePair("timestamp", timestamp));
        parameters.add(new BasicNameValuePair("user_id", userId));
        parameters.add(new BasicNameValuePair("v", v));
        parameters.add(new BasicNameValuePair("param", reqXml));
        HttpPost post = this.postForm(routeURL, new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        System.out.println("返回url转码: " + new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
        String body = "";
        body = this.invoke(httpClient, post);

        try {
            httpClient.close();
        } catch (IOException var9) {
            logger.error("HttpClientService post error", var9);
        }

        return body;
    }

    public String get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        String body = "";
        body = this.invoke(httpClient, get);

        try {
            httpClient.close();
        } catch (IOException var6) {
            logger.error("HttpClientService get error", var6);
        }

        return body;
    }

    public String delete(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(url);
        String body = "";
        body = this.invoke(httpClient, delete);

        try {
            httpClient.close();
        } catch (IOException var6) {
            logger.error("HttpClientService get error", var6);
        }

        return body;
    }

    public String invoke(CloseableHttpClient httpclient, HttpUriRequest httpost) {
        HttpResponse response = sendRequest(httpclient, httpost);
        String body = "";
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == CommonConstants.SUCCESS) {
            body = parseResponse(response);
        }

        return body;
    }

    private static String parseResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        String body = "";

        try {
            if (entity != null) {
                body = EntityUtils.toString(entity);
            }
        } catch (ParseException var4) {
            logger.error("HttpClientService paseResponse error", var4);
        } catch (IOException var5) {
            logger.error("HttpClientService paseResponse error", var5);
        }

        return body;
    }

    private static HttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost) {
        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException var4) {
            logger.error("HttpClientService sendRequest error", var4);
        } catch (IOException var5) {
            logger.error("HttpClientService sendRequest error", var5);
        }

        return response;
    }

    public HttpPost postForm(String url, StringEntity entity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return httpPost;
    }
}
