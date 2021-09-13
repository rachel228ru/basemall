package com.medusa.gruul.goods.web.controller.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategoryDto;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategorySecondDto;
import com.medusa.gruul.goods.api.model.param.manager.ShowCategoryParam;
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
public class ShowCategoryControllerTest {

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
        ShowCategoryParam showCategoryParam = new ShowCategoryParam();
        showCategoryParam.setCurrent(1);
        showCategoryParam.setSize(5);
        String obj = JSONObject.toJSONString(showCategoryParam);
        MvcResult result = mockMvc.perform(get("/manager/show/category/list")
                .content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: save(@RequestBody @Validated ShowCategoryDto showCategoryDto)
     */
    @Test
    public void testSave() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        ShowCategoryDto showCategoryDto = new ShowCategoryDto();
        showCategoryDto.setLevel(0);
        showCategoryDto.setName("水果1");
        showCategoryDto.setParentId(0L);

        String obj = JSONObject.toJSONString(showCategoryDto);

        MvcResult ret = mockMvc.perform(
                post("/manager/show/category/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());

    }

    /**
     * Method: saveSecond(@RequestBody List<ShowCategorySecondDto> showCategorySecondDtos)
     */
    @Test
    public void testSaveSecond() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<ShowCategorySecondDto> showCategoryDtos = new ArrayList<>();

        ShowCategorySecondDto showCategorySecondDto = new ShowCategorySecondDto();
        showCategorySecondDto.setLevel(1);
        showCategorySecondDto.setName("香蕉");
        showCategorySecondDto.setParentId(41L);
        showCategoryDtos.add(showCategorySecondDto);

        ShowCategorySecondDto showCategorySecondDto1 = new ShowCategorySecondDto();
        showCategorySecondDto1.setLevel(1);
        showCategorySecondDto1.setName("橘子");
        showCategorySecondDto1.setParentId(41L);
        showCategoryDtos.add(showCategorySecondDto1);


        String obj = JSONObject.toJSONString(showCategoryDtos);

        MvcResult ret = mockMvc.perform(
                post("/manager/show/category/save/second")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());

    }

    /**
     * Method: update(@RequestBody @Validated ShowCategoryDto showCategoryDto)
     */
    @Test
    public void testUpdate() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        ShowCategoryDto showCategoryDto = new ShowCategoryDto();
        showCategoryDto.setId(40L);
        showCategoryDto.setLevel(0);
        showCategoryDto.setName("蔬菜");
        showCategoryDto.setParentId(0L);

        MvcResult ret = mockMvc.perform(
                put("/manager/show/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(showCategoryDto)))
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
        Long id = 40L;
        MvcResult ret = mockMvc.perform(
                delete("/manager/show/category/delete/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }


}
