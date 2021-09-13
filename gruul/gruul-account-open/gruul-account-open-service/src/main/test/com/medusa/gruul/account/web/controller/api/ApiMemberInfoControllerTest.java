package com.medusa.gruul.account.web.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.account.web.model.Param.MemberIdParam;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description ApiMemberInfoControllerTest 单元测试类
 * @Author zhaokw
 * @Date 10:05 2020\8\23 0023
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiMemberInfoControllerTest {
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
     * Method: getMemberInfo
     */
    @Test
    public void getMemberInfo() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MvcResult result = mockMvc.perform(get("/api/getMemberInfo"))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: updateMemberExpireTime
     */
    @Test
    public void updateMemberExpireTime() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        UpdateApiMemberExpireTimeDto updateApiMemberExpireTimeDto = new UpdateApiMemberExpireTimeDto();
        updateApiMemberExpireTimeDto.setAvailableAmount(new BigDecimal(30000))
                .setMemberNumber("1000022007180430042")
                .setOpenType(2)
                .setUseAmount(new BigDecimal("118905.27"))
                .setDate(1L)
                .setMemberExpireTime("2020-07-19");
        String obj = JSONObject.toJSONString(updateApiMemberExpireTimeDto);
        MvcResult result = mockMvc.perform(post("/api/updateMemberExpireTime").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: getMemberCardUpgrade
     */
    @Test
    public void getMemberCardUpgrade() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberIdParam memberIdParam = new MemberIdParam();
        memberIdParam.setMemberId("199");
        String obj = JSONObject.toJSONString(memberIdParam);
        MvcResult result = mockMvc.perform(get("/api/getMemberCardUpgrade").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: upgradeMemberRank
     */
    @Test
    public void upgradeMemberRank() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        UpgradeApiMemberRankDto upgradeApiMemberRankDto = new UpgradeApiMemberRankDto();
        upgradeApiMemberRankDto.setAvailableAmount(new BigDecimal(22))
                .setMemberNumber("1000022007215654613")
                .setOpenType(1)
                .setPaymentType(2)
                .setUseAmount(new BigDecimal(118825.27))
                .setRankCode("2")
                .setRankName("金卡会员");
        String obj = JSONObject.toJSONString(upgradeApiMemberRankDto);
        MvcResult result = mockMvc.perform(post("/api/upgradeMemberRank").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

} 
