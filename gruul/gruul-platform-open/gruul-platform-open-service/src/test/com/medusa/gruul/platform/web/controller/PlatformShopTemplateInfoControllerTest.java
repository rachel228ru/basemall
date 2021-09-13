package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

class PlatformShopTemplateInfoControllerTest extends BaseTest {

    private static String baseUrl = "/shopTemplate";

    @Test
    void list() {
        String url = baseUrl.concat("/list");
        JSONObject param = new JSONObject();
        param.put("page",1);
        param.put("size",10);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result,100000);

        param.put("type",0);
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

}