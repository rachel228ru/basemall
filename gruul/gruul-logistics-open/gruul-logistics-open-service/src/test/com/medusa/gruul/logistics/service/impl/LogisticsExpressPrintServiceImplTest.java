package com.medusa.gruul.logistics.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressPrintDto;
import com.medusa.gruul.logistics.model.param.LogisticsAddressParam;
import com.medusa.gruul.logistics.model.param.LogisticsExpressPrintParam;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressPrintVo;
import com.medusa.gruul.logistics.service.ILogisticsExpressPrintService;
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

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({LogisticsExpressPrintServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RabbitAutoConfiguration.class,
        RedisAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class LogisticsExpressPrintServiceImplTest {

    @Resource
    private ILogisticsExpressPrintService logisticsExpressPrintService;

    @Test
    public void getExpressPrintList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsExpressPrintParam logisticsExpressPrintParam = new LogisticsExpressPrintParam();
        IPage<LogisticsExpressPrintVo> logisticsExpressPrintVoIPage = logisticsExpressPrintService.getExpressPrintList(logisticsExpressPrintParam);
        Assert.assertNotNull(logisticsExpressPrintVoIPage);
        System.out.println(logisticsExpressPrintVoIPage.getRecords().toString());
    }

    @Test
    public void getExpressPrintInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 10L;
        LogisticsExpressPrintVo logisticsExpressPrintVo = logisticsExpressPrintService.getExpressPrintInfo(id);
        Assert.assertNotNull(logisticsExpressPrintVo);
        System.out.println(logisticsExpressPrintVo.toString());
    }

    @Test
    public void setExpressPrintInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsExpressPrintDto logisticsExpressPrintDto = new LogisticsExpressPrintDto();
        logisticsExpressPrintDto.setId(10L);
        logisticsExpressPrintDto.setDeviceType("1");
        logisticsExpressPrintDto.setDeviceModel("热敏型");
        logisticsExpressPrintDto.setDeviceName("美杜莎热敏型打印机");
        logisticsExpressPrintDto.setDeviceNo("0001");
        logisticsExpressPrintDto.setDeviceKey("1234");
        logisticsExpressPrintDto.setStatus("1");
        try{
            logisticsExpressPrintService.setExpressPrintInfo(logisticsExpressPrintDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void setExpressPrintStatus() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Integer type = 1;
        Long id = 10L;
        try{
            logisticsExpressPrintService.setExpressPrintStatus(type, id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void delExpressPrintInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 10L;
        try{
            logisticsExpressPrintService.delExpressPrintInfo(id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}