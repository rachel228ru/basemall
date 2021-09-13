package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

class PlatformShopInfoConsoleControllerTest extends BaseTest {

    private static String baseUrl = "/shop";


    @Test
    void amdinList() {
        String url = baseUrl.concat("/amdin/list");
        JSONObject param = new JSONObject();
        param.put("page",1);
        param.put("size",10);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

    }


    @Test
    void consoleList() {
        String url = baseUrl.concat("/console/list");
        JSONObject param = new JSONObject();
        param.put("page",1);
        param.put("size",10);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    void shopInfo() {
        String url = baseUrl.concat("/info");
        JSONObject param = new JSONObject();
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

    }

    @Test
    void payInfo() {
        String url = baseUrl.concat("/pay/info");
        JSONObject param = new JSONObject();
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }
}