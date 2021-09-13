package com.medusa.gruul.order.controller.manage;

import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.order.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;


public class ManageOrderControllerTest extends BaseTest {

    @Test
    public void searchOrder() {
        String url = "/manage/search";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("current", "1");
        paramsMap.put("deliverType", "102");
        paramsMap.put("orderStatus", "-1");
        paramsMap.put("quicklyDate", "0");
        paramsMap.put("size", "10");
        paramsMap.put("remarkType", "0");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void getSetting() {
        String url = "/manage/setting";
        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void setting() {
        String url = "/manage/setting?openEvaluate=true";
        MvcResult result = super.getMockTestPut(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void line() {
        String url = "/manage/line";
        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void tracesInfo() {
        String url = "/manage/traces";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tracesId", "1");
        paramsMap.put("deliveryCode", "1");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void orderInfo() {
        String url = "/manage/info/{}";
        MvcResult result = super.getMockTestGet(StrUtil.format(url, super.orderId));
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void searchOrderEvaluate() {
        String url = "/manage/evaluate/search";

        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }


    @Test
    public void orderPrompt() {
        String url = "/manage/reply/orderPrompt";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("current", "1");
        paramsMap.put("size", "10");
        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }


    @Test
    public void searchLogisticsOrderList() {
        String url = "/manage/reply/searchDeliveryOrders";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("current", "1");
        paramsMap.put("size", "10");
        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }
}