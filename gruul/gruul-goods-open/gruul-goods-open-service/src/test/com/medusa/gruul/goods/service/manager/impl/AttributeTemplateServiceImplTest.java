package com.medusa.gruul.goods.service.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateDto;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateSecondDto;
import com.medusa.gruul.goods.api.model.param.manager.AttributeTemplateParam;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateVo;
import com.medusa.gruul.goods.service.manager.IAttributeTemplateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({AttributeTemplateServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class AttributeTemplateServiceImplTest {

    @Resource
    private IAttributeTemplateService attributeTemplateService;


    /**
     *
     * Method: getAllAttributeTemplateList()
     *
     */
    @Test
    public void testGetAllAttributeTemplateList() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<AttributeTemplateVo> attributeTemplateVos = attributeTemplateService.getAllAttributeTemplateList();
        Assert.assertNotNull(attributeTemplateVos);
        System.out.println(attributeTemplateVos.toString());
    }

    /**
     *
     * Method: getAttributeTemplateList(AttributeTemplateParam attributeTemplateParam)
     *
     */
    @Test
    public void testGetAttributeTemplateList() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        AttributeTemplateParam attributeTemplateParam = new AttributeTemplateParam();
        IPage<AttributeTemplateVo> page = attributeTemplateService.getAttributeTemplateList(attributeTemplateParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    /**
     *
     * Method: addAttributeTemplate(AttributeTemplateDto attributeTemplateDto)
     *
     */
    @Test
    public void testAddAttributeTemplate() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        AttributeTemplateDto attributeTemplateDto = new AttributeTemplateDto();
        attributeTemplateDto.setParentId(0L);
        attributeTemplateDto.setName("测试模版233");
        attributeTemplateDto.setContent("这是一个测试模版");
        List<AttributeTemplateSecondDto> attributeTemplates = new ArrayList<>(CommonConstants.NUMBER_ONE);
        AttributeTemplateSecondDto attributeTemplateSecondDto = new AttributeTemplateSecondDto();
        attributeTemplateSecondDto.setName("测试二级模版");
        attributeTemplateDto.setContent("这是一个测试二级模版");
        attributeTemplates.add(attributeTemplateSecondDto);
        attributeTemplateDto.setAttributeTemplates(attributeTemplates);
        try{
            attributeTemplateService.addAttributeTemplate(attributeTemplateDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: updateAttributeTemplate(AttributeTemplateDto attributeTemplateDto)
     *
     */
    @Test
    public void testUpdateAttributeTemplate() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        AttributeTemplateDto attributeTemplateDto = new AttributeTemplateDto();
        attributeTemplateDto.setId(541L);
        attributeTemplateDto.setName("测试模版233");
        attributeTemplateDto.setParentId(0L);
        attributeTemplateDto.setName("测试接龙活动");
        attributeTemplateDto.setContent("这是一个测试模版");
        List<AttributeTemplateSecondDto> attributeTemplates = new ArrayList<>(CommonConstants.NUMBER_ONE);
        AttributeTemplateSecondDto attributeTemplateSecondDto = new AttributeTemplateSecondDto();
        attributeTemplateSecondDto.setName("测试二级模版");
        attributeTemplateDto.setContent("这是一个测试二级模版");
        attributeTemplates.add(attributeTemplateSecondDto);
        attributeTemplateDto.setAttributeTemplates(attributeTemplates);
        try{
            attributeTemplateService.updateAttributeTemplate(attributeTemplateDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: deleteAttributeTemplateList(Long[] ids)
     *
     */
    @Test
    public void testDeleteAttributeTemplateList() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = new Long[1];
        ids[0] = 541L;
        try{
            attributeTemplateService.deleteAttributeTemplateList(ids);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: deleteAttributeTemplateChild(Long id)
     *
     */
    @Test
    public void testDeleteAttributeTemplateChild() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 542L;
        try{
            attributeTemplateService.deleteAttributeTemplateChild(id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
