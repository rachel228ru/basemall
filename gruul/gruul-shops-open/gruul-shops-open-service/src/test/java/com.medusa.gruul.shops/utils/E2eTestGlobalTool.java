package com.medusa.gruul.shops.utils;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.shops.properties.GlobalConstant;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

/**
 * @author create by zq
 * @date created in 2019/11/09
 */
public class E2eTestGlobalTool {


    private E2eTestGlobalTool(){}


    public static E2ePolymerization newE2ePolymerization() {
        return new E2ePolymerization();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class E2ePolymerization {

        private LinkedList<E2ePolymerization> e2eList = new LinkedList<>();

        private String url;

        private Object parameter;

        private Map map;

        private Boolean postModel;

        private Boolean isJson;

        private Boolean printResponse;

        private Long sleepTime = 0L;


        public E2ePolymerization addHttp(String url, Object parameter, boolean postModel, boolean isJson, boolean printResponse, Long sleepTime) {
            e2eList.add(new E2ePolymerization(null, url, parameter, null, postModel, isJson, printResponse, sleepTime));
            return this;
        }


        public E2ePolymerization addHttp(String url, Object parameter, boolean postModel, boolean isJson, boolean printResponse) {
            e2eList.add(new E2ePolymerization(null, url, parameter, null, postModel, isJson, printResponse, this.sleepTime));
            return this;
        }


        public E2ePolymerization addHttp(String url, Object parameter, Map map, boolean postModel, boolean isJson, boolean printResponse) {
            e2eList.add(new E2ePolymerization(null, url, parameter, map, postModel, isJson, printResponse, sleepTime));
            return this;
        }


        public LinkedList startHttp() throws Exception {
            LinkedList<List> list = new LinkedList<>();
            for (E2ePolymerization e2ePolymerization : this.e2eList) {
                List result = httpClient(e2ePolymerization);
                list.add(result);
                if (500 == (Integer) result.get(0)) {
                    throw new RuntimeException("流程执行异常. 已中断");
                }
            }
            return list;
        }


        private static List httpClient(E2ePolymerization e2ePolymerization) throws Exception {
            return innerHandleHttp(e2ePolymerization.url, e2ePolymerization.parameter, e2ePolymerization.map, e2ePolymerization.postModel, e2ePolymerization.isJson, e2ePolymerization.printResponse);
        }


        private static List httpClient(String url, Object parameter, boolean postModel, boolean isJson, boolean printResponse) throws Exception {
            return innerHandleHttp(url, parameter, null, postModel, isJson, printResponse);
        }


        private static List innerHandleHttp(String url, Object parameter, Map map, Boolean type, Boolean isJson, Boolean printResponse) throws Exception {
            List list;
            if (type) {
                if (isJson) {
                    if (parameter instanceof String) {
                        list = sendPostJson(url, (String) parameter, map);
                    } else {
                        list = sendPostJson(url, JSONObject.toJSONString(parameter), map);
                    }
                } else {
                    list = sendPost(url, parameter, map);
                }
            } else {
                if (isJson) {
                    if (parameter instanceof String) {
                        list = sendGetJson(url, (String) parameter, map);
                    } else {
                        list = sendGetJson(url, JSONObject.toJSONString(parameter), map);
                    }
                } else {
                    list = sendGet(url, parameter, map);
                }
            }
            return innerHandleIO(list, printResponse);
        }


        private static List innerHandleIO(List list, Boolean printResponse) throws Exception {
            CloseableHttpResponse response = (CloseableHttpResponse) list.get(0);
            CloseableHttpClient client = (CloseableHttpClient) list.get(1);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            if (printResponse && 2 == list.size()) {
                System.out.println("<! --------------------- !>");
                System.out.println(String.format("response result -> %s", sb.toString()));
                System.out.println("<! --------------------- !>");
            }

            List<? extends Serializable> resultList = Arrays.asList(response.getStatusLine().getStatusCode(), sb.toString());
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(client);
            return resultList;
        }


        private static List sendPost(String url, Object data, Map map) throws Exception {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            List<NameValuePair> list = new ArrayList<>();
            innerHandleHttpHead(request, map);
            innerHandlePostFormData(list, data);
            request.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            return Arrays.asList(httpClient.execute(request), httpClient);
        }


        private static void innerHandlePostFormData(List<NameValuePair> list, Object data) {
            Map map;
            if (data instanceof Map) {
                map = (Map) data;
            } else {
                if (null == data) {
                    return;
                }
                map = BeanUtil.beanToMap(data);
            }
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                if (null != map.get(key)) {
                    list.add(new BasicNameValuePair(key, map.get(key).toString()));
                }
            }
        }


        private static List sendPostJson(String url, String data, Map map) throws Exception {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            innerHandleHttpHead(request, map);
            request.setHeader("content-type", "application/json");
            StringEntity entity = new StringEntity(data, "UTF-8");
            request.setEntity(entity);
            return Arrays.asList(httpClient.execute(request), httpClient);
        }


        private static List sendGet(String url, Object data, Map map) throws Exception {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            url = innerHandleGetUrlData(url, data);
            HttpGet request = new HttpGet(url);
            innerHandleHttpHead(request, map);
            return Arrays.asList(httpClient.execute(request), httpClient);
        }


        public static String innerHandleGetUrlData(String url, Object data) {
            Map map;
            if (data instanceof Map) {
                map = (Map) data;
            } else {
                if (null == data) {
                    return url;
                }
                map = BeanUtil.beanToMap(data);
            }

            char end = url.charAt(url.length() - 1);
            String em = GlobalConstant.STRING_EMPTY;
            if (end != GlobalConstant.CHAR_ASK && end != GlobalConstant.CHAR_AND) {
                em = url.contains(String.valueOf(GlobalConstant.CHAR_ASK)) ? String.valueOf(GlobalConstant.CHAR_AND) : String.valueOf(GlobalConstant.CHAR_ASK);
            }

            StringBuilder sb = new StringBuilder(url);
            Map.Entry<String, Object> entry;
            for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
                entry = it.next();
                sb.append(em).append(entry.getKey()).append(GlobalConstant.CHAR_EQUAL_TO).append(entry.getValue());
                em = String.valueOf(GlobalConstant.CHAR_AND);
            }
            return sb.toString();
        }


        private static List sendGetJson(String url, String parameter, Map map) {
            return null;
        }


        private static void innerHandleHttpHead(Object request, Map map) {
            if (map == null) {
                return;
            }

            for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
                String key = it.next().toString();
                Object value = map.get(key);
                if (value instanceof String) {
                    if (request instanceof HttpPost) {
                        ((HttpPost) request).setHeader(key, (String) value);
                    }
                    if (request instanceof HttpGet) {
                        ((HttpGet) request).setHeader(key, (String) value);
                    }
                } else {
                    if (request instanceof HttpPost) {
                        ((HttpPost) request).setHeader(key, value.toString());
                    }
                    if (request instanceof HttpGet) {
                        ((HttpGet) request).setHeader(key, value.toString());
                    }
                }
            }
        }
    }

}
