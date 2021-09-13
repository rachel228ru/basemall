package com.medusa.gruul.logistics.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.AreaDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsIncludePostageDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsShippingModelDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsTemplateDto;
import com.medusa.gruul.logistics.model.param.LogisticsTemplateParam;
import com.medusa.gruul.logistics.model.vo.LogisticsTemplateVo;
import com.medusa.gruul.logistics.service.ILogisticsTemplateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({LogisticsTemplateServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RabbitAutoConfiguration.class,
        RedisAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class LogisticsTemplateServiceImplTest {

    @Resource
    private ILogisticsTemplateService logisticsTemplateService;

    @Test
    public void getTemplateList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsTemplateParam logisticsTemplateParam = new LogisticsTemplateParam();
        IPage<LogisticsTemplateVo> logisticsTemplateVoIPage = logisticsTemplateService.getTemplateList(logisticsTemplateParam);
        Assert.assertNotNull(logisticsTemplateVoIPage);
        System.out.println(logisticsTemplateVoIPage.getRecords().toString());
    }

    @Test
    public void getTemplateInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 194L;
        LogisticsTemplateVo logisticsTemplateVo = logisticsTemplateService.getTemplateInfo(id);
        Assert.assertNotNull(logisticsTemplateVo);
        System.out.println(logisticsTemplateVo.toString());
    }

    @Test
    public void addOrUpdateTemplate() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsTemplateDto logisticsTemplateDto = new LogisticsTemplateDto();
        logisticsTemplateDto.setId(194L);
        logisticsTemplateDto.setName("全国包邮模版");
        logisticsTemplateDto.setValuationModel(1);
        logisticsTemplateDto.setChoiceConditionPostage(1);
        List<LogisticsShippingModelDto> logisticsShippingModelDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        LogisticsShippingModelDto logisticsShippingModelDto = new LogisticsShippingModelDto();
        logisticsShippingModelDto.setValuationModel(1);
        logisticsShippingModelDto.setFirstPiece(6);
        logisticsShippingModelDto.setSecondPiece(3);
        List<AreaDto> areaDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        AreaDto areaDto = new AreaDto();
        areaDto.setFatherid("110000");
        areaDto.setValue("110100");
        areaDtos.add(areaDto);
        logisticsShippingModelDto.setRegion(areaDtos);
        logisticsShippingModelDtos.add(logisticsShippingModelDto);
        List<LogisticsIncludePostageDto> logisticsIncludePostageDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        LogisticsIncludePostageDto logisticsIncludePostageDto = new LogisticsIncludePostageDto();
        logisticsIncludePostageDto.setType(0);
        logisticsIncludePostageDto.setPieceNum(5);
        List<AreaDto> areaDtos1 = new ArrayList<>(CommonConstants.NUMBER_ONE);
        AreaDto areaDto1 = new AreaDto();
        areaDto.setFatherid("500000");
        areaDto.setValue("500100");
        areaDtos.add(areaDto1);
        logisticsIncludePostageDto.setRegion(areaDtos1);
        logisticsIncludePostageDtos.add(logisticsIncludePostageDto);
        try{
            logisticsTemplateService.addOrUpdateTemplate(logisticsTemplateDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

//    @Test
//    public void addLogisticsShippingModel() {
//    }
//
//    @Test
//    public void addLogisticsIncludePostage() {
//    }

    @Test
    public void removeTemplateById() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 194L;
        try{
            logisticsTemplateService.removeTemplateById(id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void feignGetList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<LogisticsTemplateVo> logisticsTemplateVos = logisticsTemplateService.feignGetList();
        Assert.assertNotNull(logisticsTemplateVos);
        System.out.println(logisticsTemplateVos.toString());
    }

    @Test
    public void feignGetInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 194L;
        LogisticsTemplateVo logisticsTemplateVo = logisticsTemplateService.feignGetInfo(id);
        Assert.assertNotNull(logisticsTemplateVo);
        System.out.println(logisticsTemplateVo.toString());
    }

//    @Test
//    public void freightCalculation() {
//    }
}