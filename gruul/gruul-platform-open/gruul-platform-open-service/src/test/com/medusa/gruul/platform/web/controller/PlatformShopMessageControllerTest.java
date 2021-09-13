package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

class PlatformShopMessageControllerTest extends BaseTest {


    private static String baseUrl = "/shop-message";


    @Test
    void msgAll() {
        String url = baseUrl.concat("/buyer/notify");
        JSONObject param = new JSONObject();
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }
}