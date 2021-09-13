package com.medusa.gruul.account.web.controller.manager;

import com.alibaba.fastjson.JSONArray;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description MemberPayRuleControllerTest 单元测试类
 * @Author zhaokw
 * @Date 10:05 2020\8\23 0023
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberPayRuleControllerTest {
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext applicationContext;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    @After
    public void after() {
    }

    /**
     * Method: getMemberPayRule
     */
    @Test
    public void getMemberPayRule() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MvcResult result = mockMvc.perform(get("/manager/getMemberPayRule"))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: saveMemberPayRule
     */
    @Test
    public void saveMemberPayRule() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberPayRule> memberPayRuleList = new ArrayList<>();
        MemberPayRule memberPayRule = new MemberPayRule();
        memberPayRule.setId(2L)
                .setState(1)
                .setDiscountAmount(new BigDecimal(20))
                .setPayAmount(new BigDecimal(100));
        memberPayRuleList.add(memberPayRule);
        MemberPayRule memberPayRule1 = new MemberPayRule();
        memberPayRule1.setId(3L)
                .setState(1)
                .setDiscountAmount(new BigDecimal(50))
                .setPayAmount(new BigDecimal(200));
        memberPayRuleList.add(memberPayRule1);
        MemberPayRule memberPayRule2 = new MemberPayRule();
        memberPayRule2.setId(6L)
                .setState(1)
                .setDiscountAmount(new BigDecimal(200))
                .setPayAmount(new BigDecimal(500));
        memberPayRuleList.add(memberPayRule2);
        String obj = JSONArray.toJSONString(memberPayRuleList);
        MvcResult result = mockMvc.perform(post("/manager/saveMemberPayRule").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

} 
