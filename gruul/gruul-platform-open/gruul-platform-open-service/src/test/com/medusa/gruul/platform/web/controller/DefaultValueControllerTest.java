package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

class DefaultValueControllerTest extends BaseTest {


    private static String baseUrl = "/default-value";


    @Test
    void getValue() {
        String url = baseUrl.concat("/accountCentreDefault/v1.0");
        JSONObject param = new JSONObject();
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

    }
}