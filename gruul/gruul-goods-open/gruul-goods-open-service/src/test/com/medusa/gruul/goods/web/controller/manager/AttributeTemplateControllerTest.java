package com.medusa.gruul.goods.web.controller.manager;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateDto;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateSecondDto;
import com.medusa.gruul.goods.api.model.param.manager.AttributeTemplateParam;
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

/**
 * AttributeTemplateController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>10/15/2019</pre>
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
public class AttributeTemplateControllerTest {
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
     * Method: list(AttributeTemplateParam attributeTemplateParam)
     */
    @Test
    public void testList() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        AttributeTemplateParam param = new AttributeTemplateParam();
        String obj = JSONObject.toJSONString(param);
        MvcResult result = mockMvc.perform(get("/manager/attribute/template/list").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: save(@RequestBody @Validated AttributeTemplateDto attributeTemplateDto)
     */
    @Test
    public void testSave() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        AttributeTemplateDto dto = new AttributeTemplateDto();
        dto.setParentId(0L);
        dto.setName("测试模板");
        dto.setContent("模板内容");

        List<AttributeTemplateSecondDto> secondDtoList = new ArrayList<>();
        AttributeTemplateSecondDto secondDtoOne = new AttributeTemplateSecondDto();
        secondDtoOne.setName("属性1");
        secondDtoOne.setContent("属性内容1");
        secondDtoList.add(secondDtoOne);

        AttributeTemplateSecondDto secondDtoTwo = new AttributeTemplateSecondDto();
        secondDtoTwo.setName("属性1");
        secondDtoTwo.setContent("属性内容1");
        secondDtoList.add(secondDtoTwo);

        dto.setAttributeTemplates(secondDtoList);

        MvcResult ret = mockMvc.perform(
                post("/manager/attribute/template/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(dto)))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }

    /**
     * Method: update(@RequestBody AttributeTemplateDto attributeTemplateDto)
     */
    @Test
    public void testUpdate() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        AttributeTemplateDto dto = new AttributeTemplateDto();
        dto.setId(5L);
        dto.setParentId(0L);
        dto.setName("测试模板");
        dto.setContent("修改模板内容");

        List<AttributeTemplateSecondDto> secondDtoList = new ArrayList<>();
        AttributeTemplateSecondDto secondDtoOne = new AttributeTemplateSecondDto();
        secondDtoOne.setName("属性3");
        secondDtoOne.setContent("属性内容3");
        secondDtoList.add(secondDtoOne);

        AttributeTemplateSecondDto secondDtoTwo = new AttributeTemplateSecondDto();
        secondDtoTwo.setName("属性4");
        secondDtoTwo.setContent("属性内容5");
        secondDtoList.add(secondDtoTwo);

        dto.setAttributeTemplates(secondDtoList);

        MvcResult ret = mockMvc.perform(
                put("/manager/attribute/template/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(dto)))
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
        Long[] ids = {12L, 15L};
        MvcResult ret = mockMvc.perform(
                delete("/manager/attribute/template/delete/" + ArrayUtil.join(ids, ","))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(ret.getResponse().getContentAsString());
    }


} 
