package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

class MiniExperienceControllerTest extends BaseTest {


    private static String baseUrl = "/mini-experience";


    @Test
    void wxaGetQrcode() {

        String url = baseUrl.concat("/wxa/get_qrcode");
        JSONObject param = new JSONObject();
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    void wxaMemberauth() {
        String url = baseUrl.concat("/wxa/memberauth");
        JSONObject param = new JSONObject();
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }
}