package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

class MiniInfoControllerTest extends BaseTest {

    private static String baseUrl = "/mini-info";

    @Test
    void baseInfo() {

        String url = "/base/info";
        JSONObject param = new JSONObject();
        param.put("type", 1);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

        param.put("type", 2);
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

    }

    @Test
    void wxaGetwxacode() {
        String url = baseUrl.concat("/wxa/getwxacode");
        JSONObject param = new JSONObject();
        param.put("path", "");
        //未填写路径
        MvcResult result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result, 100002);

        param.put("path", "/pages/index/index");
        result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result, 61004);
    }

}