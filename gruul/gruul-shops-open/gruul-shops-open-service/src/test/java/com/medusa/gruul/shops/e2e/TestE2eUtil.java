package com.medusa.gruul.shops.e2e;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.medusa.gruul.shops.utils.E2eTestGlobalTool;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author create by zq
 * @date created in 2019/11/17
 */
public class TestE2eUtil {

    @Test
    public void ruleTest() throws Exception {
        final String insertRuleUrl = "http://127.0.0.1:9250/assemble/activity/rule/update";
        final String insParam = "{\"assembleBeginTime\":\"-1,16:11\",\"assembleEndTime\":\"0,03:00\",\"assembleName\":\"测试数据A\",\"orderGenerate\":\"0,5\",\"preheatTime\":1573980360304,\"ruleCoverUrl\":\"https://test\"}";

        final String getRuleUrl = "http://127.0.0.1:9250/assemble/activity/rule/get";
        LinkedList linkedList = E2eTestGlobalTool.newE2ePolymerization()
                .addHttp(insertRuleUrl, insParam, true, true, true)
                .addHttp(getRuleUrl, null, false, false, true).startHttp();

        Map map = (Map) JSON.parseObject((String) ((List) linkedList.get(1)).get(1)).get("data");
        map.put("assembleName", "测试数据B");

        final String updateRuleUrl = "http://127.0.0.1:9250/assemble/activity/rule/update";
        linkedList.addAll(E2eTestGlobalTool.newE2ePolymerization()
                .addHttp(updateRuleUrl, map, true, true, true)
                .addHttp(getRuleUrl, null, false, false, true)
                .startHttp());
        linkedList.forEach(System.out::println);
    }

    @Test
    public void getRuleTest() throws Exception {
        final String getRuleUrl = "http://127.0.0.1:9250/assemble/activity/rule/get";
        E2eTestGlobalTool.newE2ePolymerization()
                .addHttp(getRuleUrl, null, false, false, true).startHttp().forEach(System.out::println);
    }

    @Test
    public void assActTest() throws Exception {
        final String saveActivityUrl = "http://127.0.0.1:9250/assemble/activity/save";
        final String value = "{\"ruleId\" : \"25\", \"activityBeginTime\":1574499120000,\"activityEndTime\":1574499120000,\"productsDtoList\" : [{\"productId\" : \"1\"}]}";
        final String listActivityUrl = "http://127.0.0.1:9250/assemble/activity/list?status=0";
        final String deleteActivityUrl = "http://127.0.0.1:9250/assemble/activity/delete?ids=19";
        E2eTestGlobalTool.newE2ePolymerization()
                .addHttp(saveActivityUrl, value, true, true, true)
                .addHttp(listActivityUrl, Maps.newHashMap(), false, false, true)
                .addHttp(deleteActivityUrl, Maps.newHashMap(), true, false, true).startHttp().forEach(System.out::println);



    }

}
