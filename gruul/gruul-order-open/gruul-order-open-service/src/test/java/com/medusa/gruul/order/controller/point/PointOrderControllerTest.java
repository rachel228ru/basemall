package com.medusa.gruul.order.controller.point;

import com.medusa.gruul.order.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

public class PointOrderControllerTest extends BaseTest {


    @Test
    public void getPointOrderOverviewVo() {
        String url = "/point/overview";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pointId", super.pointId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void getUserOverviewList() {
        String url = "/point/user/overview";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pointId", super.pointId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void getOrderPage() {
        String url = "/point/user/order";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("status", "101");
        paramsMap.put("userId", super.userId);
        paramsMap.put("pointId", super.pointId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }

    @Test
    public void getSettlementBrokerage() {
        String url = "/point/brokerage/settlement";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pointId", super.pointId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }
}
