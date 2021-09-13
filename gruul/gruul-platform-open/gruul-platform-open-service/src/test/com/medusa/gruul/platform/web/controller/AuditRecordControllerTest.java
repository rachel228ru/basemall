package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

class AuditRecordControllerTest extends BaseTest {


    private static String baseUrl = "/audit-record";


    @Test
    void getMiniAuditRecordList() {
        String url = baseUrl.concat("/audit-record");
        //查询不存在数据
        JSONObject param = new JSONObject();
        param.put("page", 1);
        param.put("size", 10);
        param.put("appId", 124567);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        cn.hutool.json.JSONObject jsonObject = super.assertCodeSuccess(result);
        Integer total = jsonObject.getJSONObject("data").getInt("total");
        assert total.equals(0);


        param.put("appId", "wx4c0a3c2e450c2c7d");
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        jsonObject = super.assertCodeSuccess(result);
        total = jsonObject.getJSONObject("data").getInt("total");
        assert total > 0;
    }
}