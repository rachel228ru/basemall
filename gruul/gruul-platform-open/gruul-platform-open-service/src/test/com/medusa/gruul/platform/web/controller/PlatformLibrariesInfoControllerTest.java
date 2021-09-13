package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

class PlatformLibrariesInfoControllerTest extends BaseTest {

    private static String baseUrl = "/platform-libraries-info";


    @Test
    void getBaseLibraries() {
        String url = baseUrl;
        JSONObject param = new JSONObject();
        param.put("page", 1);
        param.put("size", 10);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

        param.put("categoryType", 2);
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

    }

    @Test
    void getByBaseLibrariesId() {
        String url = baseUrl.concat("/0");
        JSONObject param = new JSONObject();
        param.put("page", 1);
        param.put("size", 10);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result,500);

        url = baseUrl.concat("/34");
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }
}