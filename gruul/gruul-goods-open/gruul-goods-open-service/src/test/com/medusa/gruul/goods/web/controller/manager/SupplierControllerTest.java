package com.medusa.gruul.goods.web.controller.manager;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.model.dto.manager.SupplierDto;
import com.medusa.gruul.goods.api.model.param.manager.SupplierParam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SupplierController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>10/15/2019</pre>
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class SupplierControllerTest {
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
     * Method: list(SupplierParam supplierParam)
     */
    @Test
    public void testList() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        SupplierParam param = new SupplierParam();

        String obj = JSONObject.toJSONString(param);
        MvcResult result = mockMvc.perform(get("/manager/supplier/list").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: save(@RequestBody @Validated SupplierDto supplierDto)
     */
    @Test
    public void testSave() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        SupplierDto dto = new SupplierDto();
        dto.setName("测试供应商2");
        dto.setMobile("13888888888");
        dto.setProvince("110000");
        dto.setCity("110100");
        dto.setCountry("110101");
        dto.setAddress("213");
        dto.setArea("北京市市辖区东城区");
        dto.setProductInfo("产品信息");

        MvcResult ret = mockMvc.perform(
                post("/manager/supplier/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(dto)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: update(@RequestBody SupplierDto supplierDto)
     */
    @Test
    public void testUpdate() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        SupplierDto dto = new SupplierDto();
        dto.setId(1L);
        dto.setName("测试修改供应商");
        dto.setMobile("13666666666");
        dto.setProvince("110000");
        dto.setCity("110100");
        dto.setCountry("110101");
        dto.setAddress("213");
        dto.setArea("北京市市辖区东城区");
        dto.setProductInfo("产品信息");
        dto.setStatus(2);
        dto.setScore(new BigDecimal(4.8));

        MvcResult ret = mockMvc.perform(
                put("/manager/supplier/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(dto)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: delete(@RequestBody String[] ids)
     */
    @Test
    public void testDelete() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long[] ids = {1L, 2L};
        MvcResult ret = mockMvc.perform(
                delete("/manager/supplier/delete/" + ArrayUtil.join(ids, ","))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }


} 
