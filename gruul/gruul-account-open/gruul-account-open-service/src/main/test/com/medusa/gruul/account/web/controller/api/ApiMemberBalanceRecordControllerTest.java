package com.medusa.gruul.account.web.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.account.web.model.Param.MemberBalanceRecordParam;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description ApiMemberBalanceRecordController 单元测试类
 * @Author zhaokw
 * @Date 10:05 2020\8\23 0023
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiMemberBalanceRecordControllerTest {
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
     * Method: getMemberBalanceRecord(
     * Integer page,
     * Integer size,
     * String yearMonth,
     * String memberInfoId,
     * String userShopId,
     * Integer dealType
     * )
     */
    @Test
    public void getMemberBalanceRecord() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberBalanceRecordParam memberBalanceRecordParam = new MemberBalanceRecordParam();
        memberBalanceRecordParam.setPage(1);
        memberBalanceRecordParam.setSize(20);
        memberBalanceRecordParam.setYearMonth("202007");
        String obj = JSONObject.toJSONString(memberBalanceRecordParam);
        MvcResult result = mockMvc.perform(get("/api/getMemberBalanceRecord")
                .content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }
} 
