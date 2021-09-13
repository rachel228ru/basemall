package com.medusa.gruul.goods.web.controller.manager;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class SortingCategoryControllerTest {

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
     * Method: list()
     */
    @Test
    public void testList() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        SortingCategoryParam sortingCategoryParam = new SortingCategoryParam();
        sortingCategoryParam.setCurrent(1);
        sortingCategoryParam.setSize(5);
        String obj = JSONObject.toJSONString(sortingCategoryParam);
        MvcResult result = mockMvc.perform(get("/manager/sorting/category/list")
                .content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: save(@RequestBody @Validated List<SortingCategoryDto> sortingCategoryDtos)
     */
    @Test
    public void testSave() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<SortingCategoryDto> sortingCategoryDtos = new ArrayList<>();
        SortingCategoryDto sortingCategoryDto = new SortingCategoryDto();
        sortingCategoryDto.setName("厨房用品1");
        sortingCategoryDtos.add(sortingCategoryDto);

        SortingCategoryDto sortingCategoryDto1 = new SortingCategoryDto();
        sortingCategoryDto1.setName("清洁用品1");
        sortingCategoryDtos.add(sortingCategoryDto1);


        MvcResult ret = mockMvc.perform(
                post("/manager/sorting/category/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(sortingCategoryDtos)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: update(@RequestBody @Validated SortingCategoryDto sortingCategoryDto)
     */
    @Test
    public void testUpdate() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        SortingCategoryDto sortingCategoryDto = new SortingCategoryDto();
        sortingCategoryDto.setName("厨房用品3");
        sortingCategoryDto.setId(1L);
        MvcResult ret = mockMvc.perform(
                put("/manager/sorting/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(sortingCategoryDto)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: delete(@RequestBody Long[] ids)
     */
    @Test
    public void testDelete() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long[] ids = {1L, 2L};
        MvcResult ret = mockMvc.perform(
                delete("/manager/sorting/category/delete/" + ArrayUtil.join(ids, ","))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }


}
