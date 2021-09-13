package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

class PlatformServiceInfoControllerTest extends BaseTest {

    @Value("${base.url}")
    private String url;

    private static String baseUrl = "/platform-service-info";


    @Test
    void heartbeatUrl() {
        String url = baseUrl.concat("/heartbeat-url");
        JSONObject param = new JSONObject();
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        cn.hutool.json.JSONObject jsonObject = super.assertCodeSuccess(result);
        assert this.url.equals(jsonObject.getStr("value"));

    }
}