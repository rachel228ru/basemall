package com.medusa.gruul.order.controller.remote;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.order.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RemoteOrderControllerTest extends BaseTest {

    @Test
    public void orderInfo() {
        String url = "/remote/info";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderId", super.orderId);
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);

    }

    @Test
    public void productRate() {
        String url = "/remote/product/rate";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("productIds", Arrays.asList(205));
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);

    }

    @Test
    public void getNotShippedOrder() {
        String url = "/remote/get/not-shipped/orders";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("start", DateUtil.beginOfMonth(new DateTime()).toString());
        paramsMap.put("end", DateUtil.endOfMonth(new DateTime()).toString());
        paramsMap.put("tenantId", "100002");
        paramsMap.put("shopId", "100002100001");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);

    }

    @Test
    public void getOrderListByIds() {
        String url = "/remote/get/orders";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderIds", new Long[]{super.orderId});
        paramsMap.put("tenantId", "100002");
        paramsMap.put("shopId", "100002100001");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);

    }

    @Test
    public void getGroupLeaderOrderOverview() {
        String url = "/remote/group_leader/overview";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("groupLeaderId", super.assId);
        paramsMap.put("tenantId", "100002");
        paramsMap.put("shopId", "100002100001");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);

    }

    @Test
    public void getSiteOrderOverview() {
        String url = "/remote/point/overview";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pointId", super.pointId);
        paramsMap.put("tenantId", "100002");
        paramsMap.put("shopId", "100002100001");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);

    }

    @Test
    public void getAssSalesVoByTime() {
        String url = "/remote/ass/time/sales";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("startTime", DateUtil.beginOfMonth(new DateTime()).toString());
        paramsMap.put("endTime", DateUtil.endOfMonth(new DateTime()).toString());
        paramsMap.put("tenantId", "100002");
        paramsMap.put("shopId", "100002100001");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);

    }

    @Test
    public void getAreaAssSalesVoByShop() {
        String url = "/remote/area/ass/sales";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("startTime", DateUtil.beginOfMonth(new DateTime()).toString());
        paramsMap.put("endTime", DateUtil.endOfMonth(new DateTime()).toString());
        paramsMap.put("tenantId", "100002");
        paramsMap.put("shopId", "100002100001");
        MvcResult result = super.getMockTestGet(url, paramsMap);
        super.printResult(result);


    }

    @Test
    public void getAssSalesVoByAreaAssId() {
        String url = "/remote/{}/ass/sales";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("startTime", DateUtil.beginOfMonth(new DateTime()).toString());
        paramsMap.put("endTime", DateUtil.endOfMonth(new DateTime()).toString());
        MvcResult result = super.getMockTestGet(StrUtil.format(url, "102"), paramsMap);
        super.printResult(result);


    }

    @Test
    public void getAssSalesVoByAssIds() {
        String url = "/remote/ass/sales";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("assIds", new Long[]{84L});
        MvcResult result = super.getMockTestGet(StrUtil.format(url, "102"), paramsMap);
        super.printResult(result);

    }


    @Test
    public void activityStatistics() {
        String url = "/remote/activity/statistics";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("activityIds", new Long[]{84L});
        MvcResult result = super.getMockTestGet(StrUtil.format(url, "102"), paramsMap);
        super.printResult(result);

    }


}
