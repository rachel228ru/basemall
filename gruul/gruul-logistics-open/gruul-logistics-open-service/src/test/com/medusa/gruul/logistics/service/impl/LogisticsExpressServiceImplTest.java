package com.medusa.gruul.logistics.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressDto;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;
import com.medusa.gruul.logistics.service.ILogisticsExpressService;
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
@Import({LogisticsExpressServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RabbitAutoConfiguration.class,
        RedisAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class LogisticsExpressServiceImplTest {

    @Resource
    private ILogisticsExpressService logisticsExpressService;

    @Test
    public void getExpressList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsExpressParam logisticsExpressParam = new LogisticsExpressParam();
        List<LogisticsExpressVo> logisticsExpressVos = logisticsExpressService.getExpressList(logisticsExpressParam);
        Assert.assertNotNull(logisticsExpressVos);
        System.out.println(logisticsExpressVos.toString());
    }

    @Test
    public void getExpressInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 23L;
        LogisticsExpressVo logisticsExpressVo = logisticsExpressService.getExpressInfo(id);
        Assert.assertNotNull(logisticsExpressVo);
        System.out.println(logisticsExpressVo.toString());
    }

    @Test
    public void setExpressInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsExpressDto logisticsExpressPrintDto = new LogisticsExpressDto();
        logisticsExpressPrintDto.setId(23L);
        logisticsExpressPrintDto.setName("顺丰");
        logisticsExpressPrintDto.setCode("SF");
        logisticsExpressPrintDto.setPhone("123456");
        logisticsExpressPrintDto.setAddressId(1L);
        logisticsExpressPrintDto.setCustomerName("美杜莎");
        logisticsExpressPrintDto.setCustomerPassword("123456");
        logisticsExpressPrintDto.setLinkName("张三");
        logisticsExpressPrintDto.setLinkTel("123456");
        logisticsExpressPrintDto.setStatus("1");
        try{
            logisticsExpressService.setExpressInfo(logisticsExpressPrintDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void delExpressInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 23L;
        try{
            logisticsExpressService.delExpressInfo(id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void getLogisticsExpressRoute() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        String waybillNo = "123";
        String shipperType = "1";
        Result result = logisticsExpressService.getLogisticsExpressRoute(waybillNo, shipperType);
        Assert.assertNotNull(result.getData());
        System.out.println(result.getData().toString());
    }
}