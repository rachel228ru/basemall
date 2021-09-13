package com.medusa.gruul.order.controller.group;

import com.medusa.gruul.order.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

public class GroupLeaderOrderControllerTest extends BaseTest {

    @Test
    public void getGroupLeaderOrderOverviewVo() {
        String url = "/group/overview";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("assId", super.assId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void getUserOverviewPage() {
        String url = "/group/user/overview";
        MvcResult result = super.getMockTestGet(url);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void getOrderPage() {
        String url = "/group/user/order";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("status", "101");
        paramsMap.put("userId", super.userId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void getSettlementBrokerage() {
        String url = "/group/brokerage/settlement";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("assId", super.assId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }
}
