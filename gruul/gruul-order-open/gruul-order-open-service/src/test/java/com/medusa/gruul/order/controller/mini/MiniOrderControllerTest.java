package com.medusa.gruul.order.controller.mini;

import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.order.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

public class MiniOrderControllerTest extends BaseTest {


    @Test
    public void orderOverview() {
        String url = "/mini/overview";
        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void searchOrder() {
        String url = "/mini/search";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("current", "1");
        paramsMap.put("size", "10");
        paramsMap.put("orderStatus", "-1");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void orderInfo() {
        String url = "/mini/info/{}";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("current", "1");
        paramsMap.put("size", "10");
        paramsMap.put("orderStatus", "-1");
        MvcResult result = super.getMockTestGet(StrUtil.format(url, super.orderId));
        super.printResult(result);
        super.assertCodeSuccess(result);
    }


    @Test
    public void setting() {
        String url = "/mini/evaluate/setting";
        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }


    @Test
    public void testEvaluateOrder() {
        String url = "/mini/evaluate";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("current", "1");
        paramsMap.put("size", "10");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void userEvaluateOverview() {
        String url = "/mini/product/evaluate/overview";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("productId", super.productId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void productEvaluate() {
        String url = "/mini/product/evaluate";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("current", "1");
        paramsMap.put("size", "10");
        paramsMap.put("productId", super.productId);
        paramsMap.put("type", 0);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

}
