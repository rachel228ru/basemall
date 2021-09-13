package com.medusa.gruul.goods.web.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.model.dto.api.ApiShoppingCartDto;
import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class ApiShoppingCartControllerTest {

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
     * Method: testGetAllApiShowCategoryList(@ApiParam(value = "用户id", required = true) @PathVariable("userId") String userId)
     */
    @Test
    public void testGetAllApiShowCategoryList() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MvcResult result = mockMvc.perform(get("/api/shopping/cart/getByUserId").header("token", GoodsConstant.TEST_USER_TOKEN))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: addShoppingCart(@RequestBody @Validated ApiShoppingCartDto shoppingCartDto)
     */
    @Test
    public void testAddShoppingCart() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        ApiShoppingCartDto shoppingCartDto = new ApiShoppingCartDto();
        shoppingCartDto.setGoodsNumber(10);
        shoppingCartDto.setSkuId(194L);
        shoppingCartDto.setProductId(11L);
        shoppingCartDto.setProductSn("1829271484059217");
        shoppingCartDto.setStatus(1);
        shoppingCartDto.setProductName("商品多规格");
        List<SkuStockDto> skuStockDtos = new ArrayList<>();
        SkuStockDto skuStockDto1 = new SkuStockDto();
        skuStockDto1.setId(194L);
        skuStockDto1.setPerLimit(50);
        SkuStockDto skuStockDto2 = new SkuStockDto();
        skuStockDto2.setId(195L);
        skuStockDto2.setPerLimit(50);
        skuStockDtos.add(skuStockDto1);
        skuStockDtos.add(skuStockDto2);
        shoppingCartDto.setSkuStocks(skuStockDtos);
        String obj = JSONObject.toJSONString(shoppingCartDto);
        MvcResult ret = mockMvc.perform(
                post("/api/shopping/cart/add").header("token", GoodsConstant.TEST_USER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: updateShoppingCart(@ApiParam(value = "购物车商品新老数据 老-oldApiShoppingCartDto 新-newApiShoppingCartDto", required = true) @RequestBody Map<String, ApiShoppingCartDto> params)
     */
    @Test
    public void testUpdateShoppingCart() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Map<String, ApiShoppingCartDto> params = new HashMap<>();
        ApiShoppingCartDto newShoppingCartDto = new ApiShoppingCartDto();
        newShoppingCartDto.setGoodsNumber(20);
        newShoppingCartDto.setSkuId(195L);
        newShoppingCartDto.setProductId(11L);
        newShoppingCartDto.setProductSn("1829271484059217");
        newShoppingCartDto.setStatus(1);
        newShoppingCartDto.setProductName("商品多规格");

        ApiShoppingCartDto oldShoppingCartDto = new ApiShoppingCartDto();
        oldShoppingCartDto.setGoodsNumber(10);
        oldShoppingCartDto.setSkuId(194L);
        oldShoppingCartDto.setProductId(11L);
        oldShoppingCartDto.setProductSn("1829271484059217");
        oldShoppingCartDto.setStatus(1);
        oldShoppingCartDto.setProductName("商品多规格");

        List<SkuStockDto> skuStockDtos = new ArrayList<>();
        SkuStockDto skuStockDto1 = new SkuStockDto();
        skuStockDto1.setId(194L);
        skuStockDto1.setPerLimit(50);
        SkuStockDto skuStockDto2 = new SkuStockDto();
        skuStockDto2.setId(195L);
        skuStockDto2.setPerLimit(50);
        skuStockDtos.add(skuStockDto1);
        skuStockDtos.add(skuStockDto2);
        newShoppingCartDto.setSkuStocks(skuStockDtos);
        oldShoppingCartDto.setSkuStocks(skuStockDtos);
        params.put("newApiShoppingCartDto", newShoppingCartDto);
        params.put("oldApiShoppingCartDto", oldShoppingCartDto);
        String obj = JSONObject.toJSONString(params);
        MvcResult ret = mockMvc.perform(
                post("/api/shopping/cart/update").header("token", GoodsConstant.TEST_USER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: cleanEffectShoppingCart(@ApiParam(value = "用户id", required = true) @PathVariable("userId") String userId)
     */
    @Test
    public void testCleanEffectShoppingCart() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long[] ids = {1L};
        MvcResult ret = mockMvc.perform(
                delete("/api/shopping/cart/clean/effect").header("token", GoodsConstant.TEST_USER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(ids)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: deleteShoppingCartList(@ApiParam(value = "删除的购物车商品list") @RequestBody List<ApiShoppingCartDto> apiShoppingCartDtos)
     */
    @Test
    public void testDeleteShoppingCartList() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<ApiShoppingCartDto> shoppingCartDtoList = new ArrayList<>();
        ApiShoppingCartDto shoppingCartDto1 = new ApiShoppingCartDto();
        shoppingCartDto1.setProductId(11L);
        shoppingCartDto1.setSkuId(194L);
        shoppingCartDto1.setGoodsNumber(10);
        ApiShoppingCartDto shoppingCartDto2 = new ApiShoppingCartDto();
        shoppingCartDto2.setProductId(11L);
        shoppingCartDto2.setSkuId(195L);
        shoppingCartDto2.setGoodsNumber(10);
        shoppingCartDtoList.add(shoppingCartDto1);
        shoppingCartDtoList.add(shoppingCartDto2);
        String obj = JSONObject.toJSONString(shoppingCartDtoList);
        MvcResult ret = mockMvc.perform(
                post("/api/shopping/cart/delete").header("token", GoodsConstant.TEST_USER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }


}
