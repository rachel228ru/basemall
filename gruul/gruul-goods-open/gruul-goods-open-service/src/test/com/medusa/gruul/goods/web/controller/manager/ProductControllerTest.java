package com.medusa.gruul.goods.web.controller.manager;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.model.dto.manager.ProductAttributeDto;
import com.medusa.gruul.goods.api.model.dto.manager.ProductDto;
import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class ProductControllerTest {

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
     * Method: list(ProductParam productParam)
     */
    @Test
    public void testList() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        ProductParam productParam = new ProductParam();
        productParam.setCurrent(1);
        productParam.setSize(5);
        String obj = JSONObject.toJSONString(productParam);
        MvcResult result = mockMvc.perform(get("/manager/product/list")
                .content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: getProductById(@PathVariable("id") Long id)
     */
    @Test
    public void testGetProductById() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long id = 1L;
        MvcResult result = mockMvc.perform(get("/manager/product/get/" + id))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());

    }

    /**
     * Method: getFreighTemplateById(@PathVariable("id") Long id)
     */
    @Test
    public void testGetFreighTemplateById() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long id = 1L;
        MvcResult result = mockMvc.perform(get("/manager/product/get/freight/template/" + id))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: getDistributionById(@PathVariable("id") Long id)
     */
    @Test
    public void testGetDistributionById() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long id = 1L;
        MvcResult result = mockMvc.perform(get("/manager/product/get/distribution/" + id))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: getSkuStockAndMemberPriceById(@ApiParam(value = "产品id", required = true) @PathVariable("id") Long id)
     */
    @Test
    public void testGetSkuStockAndMemberPriceById() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long id = 1L;
        MvcResult result = mockMvc.perform(get("/manager/product/get/sku/stock/" + id))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: issue(@RequestBody @Validated ProductDto productDto)
     */
    @Test
    public void testIssue() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        ProductDto productDto = new ProductDto();
        productDto.setSortingCategoryId(12L);
        productDto.setProviderId(1L);
        productDto.setFreightTemplateId(1L);
        productDto.setLimitType(0);
        productDto.setSaleMode(1L);
        productDto.setName("康师傅红烧牛肉面");
        productDto.setPic("00");
        productDto.setAlbumPics("11");
        productDto.setVideoUrl("22");
        productDto.setUnit("克");
        productDto.setWeight(BigDecimal.valueOf(100));
        productDto.setOpenSpecs(true);
        productDto.setStatus(0);

        List<ProductAttributeDto> productAttributeDtos = new ArrayList<>();
        ProductAttributeDto productAttributeDto = new ProductAttributeDto();
        productAttributeDto.setName("包装");
        productAttributeDto.setValue("塑料");
        productAttributeDtos.add(productAttributeDto);

        ProductAttributeDto productAttributeDto1 = new ProductAttributeDto();
        productAttributeDto1.setName("颜色");
        productAttributeDto1.setValue("红色");
        productAttributeDtos.add(productAttributeDto1);

        List<SkuStockDto> skuStockDtos = new ArrayList<>();

        SkuStockDto skuStockDto = new SkuStockDto();
        List<MemberPriceDto> memberPriceDtos = new ArrayList<>();
        skuStockDto.setSkuCode("0003");
        skuStockDto.setSpecs("100*100");
        skuStockDto.setPic("11");
        skuStockDto.setPrice(new BigDecimal(100));
        skuStockDto.setOriginalPrice(new BigDecimal(200));
        skuStockDto.setStock(200);
        skuStockDto.setLowStock(10);
        MemberPriceDto memberPriceDto = new MemberPriceDto();
        memberPriceDto.setMemberLevelId(1L);
        memberPriceDto.setMemberPrice(new BigDecimal(70));
        MemberPriceDto memberPriceDto1 = new MemberPriceDto();
        memberPriceDtos.add(memberPriceDto);
        memberPriceDto1.setMemberLevelId(2L);
        memberPriceDto1.setMemberPrice(new BigDecimal(80));
        memberPriceDtos.add(memberPriceDto1);
        MemberPriceDto memberPriceDto2 = new MemberPriceDto();
        memberPriceDto2.setMemberLevelId(3L);
        memberPriceDto2.setMemberPrice(new BigDecimal(90));
        memberPriceDtos.add(memberPriceDto2);
        skuStockDto.setMemberPrices(memberPriceDtos);
        skuStockDtos.add(skuStockDto);

        productDto.setProductAttributes(productAttributeDtos);
        productDto.setSkuStocks(skuStockDtos);

        String obj = JSONObject.toJSONString(productDto);

        MvcResult ret = mockMvc.perform(
                post("/manager/product/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: update(@RequestBody @Validated ProductDto productDto)
     */
    @Test
    public void testUpdate() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setSortingCategoryId(12L);
        productDto.setProviderId(1L);
        productDto.setFreightTemplateId(1L);
        productDto.setLimitType(0);
        productDto.setSaleMode(1L);
        productDto.setName("康师傅红烧牛肉面（修改）");
        productDto.setPic("01");
        productDto.setAlbumPics("33");
        productDto.setVideoUrl("44");
        productDto.setUnit("克");
        productDto.setWeight(BigDecimal.valueOf(80));
        productDto.setOpenSpecs(false);

        List<ProductAttributeDto> productAttributeDtos = new ArrayList<>();
        ProductAttributeDto productAttributeDto = new ProductAttributeDto();
        productAttributeDto.setName("包装");
        productAttributeDto.setValue("塑料");
        productAttributeDtos.add(productAttributeDto);

        ProductAttributeDto productAttributeDto1 = new ProductAttributeDto();
        productAttributeDto1.setName("颜色");
        productAttributeDto1.setValue("红色");
        productAttributeDtos.add(productAttributeDto1);

        List<SkuStockDto> skuStockDtos = new ArrayList<>();

        SkuStockDto skuStockDto = new SkuStockDto();
        List<MemberPriceDto> memberPriceDtos = new ArrayList<>();
        skuStockDto.setSkuCode("0003");
        skuStockDto.setSpecs("100*100");
        skuStockDto.setPic("11");
        skuStockDto.setPrice(new BigDecimal(100));
        skuStockDto.setOriginalPrice(new BigDecimal(200));
        skuStockDto.setStock(200);
        skuStockDto.setLowStock(10);
        MemberPriceDto memberPriceDto = new MemberPriceDto();
        memberPriceDto.setMemberLevelId(1L);
        memberPriceDto.setMemberPrice(new BigDecimal(70));
        MemberPriceDto memberPriceDto1 = new MemberPriceDto();
        memberPriceDtos.add(memberPriceDto);
        memberPriceDto1.setMemberLevelId(2L);
        memberPriceDto1.setMemberPrice(new BigDecimal(80));
        memberPriceDtos.add(memberPriceDto1);
        MemberPriceDto memberPriceDto2 = new MemberPriceDto();
        memberPriceDto2.setMemberLevelId(3L);
        memberPriceDto2.setMemberPrice(new BigDecimal(90));
        memberPriceDtos.add(memberPriceDto2);
        skuStockDto.setMemberPrices(memberPriceDtos);
        skuStockDtos.add(skuStockDto);

        productDto.setProductAttributes(productAttributeDtos);
        productDto.setSkuStocks(skuStockDtos);

        MvcResult ret = mockMvc.perform(
                put("/manager/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(productDto)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: updateSaleMode(@RequestBody Long[] ids, @PathVariable("saleMode") Long saleMode)
     */
    @Test
    public void testUpdateSaleMode() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long[] ids = {1L};
        Long saleMode = 1L;
        MvcResult ret = mockMvc.perform(
                put("/manager/product/updateSaleMode/" + saleMode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(ids)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: updateStatus(@RequestBody Long[] ids, @PathVariable("status") Integer status)
     */
    @Test
    public void testUpdateStatus() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Long[] ids = {1L};
        Integer status = 1;
        MvcResult ret = mockMvc.perform(
                put("/manager/product/updateStatus/" + status)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(ids)))
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
        Long[] ids = {1L};
        MvcResult ret = mockMvc.perform(
                delete("/manager/product/delete/" + ArrayUtil.join(ids, ","))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }


}
