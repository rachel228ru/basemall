package com.medusa.gruul.account.web.controller.manager;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.account.model.dto.SetMemberDto;
import com.medusa.gruul.account.model.dto.UpgradeMemberRankDto;
import com.medusa.gruul.account.web.model.Param.*;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description MemberInfoControllerTest 单元测试类
 * @Author zhaokw
 * @Date 10:05 2020\8\23 0023
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberInfoControllerTest {
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
     * Method: memberList
     */
    @Test
    public void memberList() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberInfoParam memberInfoParam = new MemberInfoParam();
        memberInfoParam.setPage(1);
        memberInfoParam.setSize(20);
        String obj = JSONObject.toJSONString(memberInfoParam);
        MvcResult result = mockMvc.perform(get("/manager/memberList")
                .content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: sendCode
     */
    @Test
    public void sendCode() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        PhoneParam phoneParam = new PhoneParam();
        phoneParam.setPhone("12867564567");
        String obj = JSONObject.toJSONString(phoneParam);
        MvcResult result = mockMvc.perform(get("/manager/sendCode").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: setMember
     */
    @Test
    public void setMember() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        SetMemberDto setMemberDto = new SetMemberDto();
        setMemberDto.setUserId("1")
                .setCode("176472")
                .setPhone("12354546746")
                .setRankCode("1");
        String obj = JSONObject.toJSONString(setMemberDto);
        MvcResult result = mockMvc.perform(post("/manager/setMember").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: getMemberCardRank
     */
    @Test
    public void getMemberCardRank() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MvcResult result = mockMvc.perform(get("/manager/getMemberCardRank"))
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
        UpdateMemberExpireTimeDto updateMemberExpireTimeDto = new UpdateMemberExpireTimeDto();
        updateMemberExpireTimeDto.setMemberNumber("1000022007180430042")
                .setDate(1L)
                .setMemberExpireTime("2020-07-19");
        String obj = JSONObject.toJSONString(updateMemberExpireTimeDto);
        MvcResult result = mockMvc.perform(post("/manager/updateMemberExpireTime").content(obj))
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
        UpgradeMemberRankDto upgradeMemberRankDto = new UpgradeMemberRankDto();
        upgradeMemberRankDto.setMemberNumber("1000022007215654613")
                .setRankCode("2")
                .setRankName("金卡会员");
        String obj = JSONObject.toJSONString(upgradeMemberRankDto);
        MvcResult result = mockMvc.perform(post("/manager/upgradeMemberRank").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送post请求
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
        MvcResult result = mockMvc.perform(get("/manager/getMemberCardUpgrade").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: getMemberRecord
     */
    @Test
    public void getMemberRecord() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberRecordParam memberRecordParam = new MemberRecordParam();
        memberRecordParam.setPage(1);
        memberRecordParam.setSize(20);
        memberRecordParam.setShopUserId("1248795148536647680");
        String obj = JSONObject.toJSONString(memberRecordParam);
        MvcResult result = mockMvc.perform(get("/manager/getMemberRecord")
                .content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

} 
