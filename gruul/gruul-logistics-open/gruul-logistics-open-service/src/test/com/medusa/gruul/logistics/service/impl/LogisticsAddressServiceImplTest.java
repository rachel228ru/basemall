package com.medusa.gruul.logistics.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.CurUserContextHolder;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsAddressDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsBatchDeliverDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsPrintDeliverDto;
import com.medusa.gruul.logistics.model.param.LogisticsAddressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import com.medusa.gruul.logistics.service.ILogisticsAddressService;
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
import java.util.Map;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({LogisticsAddressServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RabbitAutoConfiguration.class,
        RedisAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class LogisticsAddressServiceImplTest {

    @Resource
    private ILogisticsAddressService logisticsAddressService;

    /**
     *
     * Method: getAddressList(LogisticsAddressParam logisticsAddressParam)
     *
     */
    @Test
    public void testGetAddressList() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsAddressParam logisticsAddressParam = new LogisticsAddressParam();
        IPage<LogisticsAddressVo> logisticsAddressVos = logisticsAddressService.getAddressList(logisticsAddressParam);
        Assert.assertNotNull(logisticsAddressVos);
        System.out.println(logisticsAddressVos.getRecords().toString());
    }

    /**
     *
     * Method: getAllAddressList()
     *
     */
    @Test
    public void testGetAllAddressList() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<LogisticsAddressVo> logisticsAddressVos = logisticsAddressService.getAllAddressList();
        Assert.assertNotNull(logisticsAddressVos);
        System.out.println(logisticsAddressVos.toString());
    }

    /**
     *
     * Method: setAddress(LogisticsAddressDto logisticsAddressDto)
     *
     */
    @Test
    public void testSetAddress() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsAddressDto logisticsAddressDto = new LogisticsAddressDto();
        logisticsAddressDto.setName("测试优惠券");
        logisticsAddressDto.setProvince("宁波市");
        logisticsAddressDto.setProvinceId("330000");
        logisticsAddressDto.setCity("宁波市");
        logisticsAddressDto.setCityId("330200");
        logisticsAddressDto.setCountry("江北区");
        logisticsAddressDto.setCountryId("330205");
        logisticsAddressDto.setAddress("长兴路158号");
        logisticsAddressDto.setPhone("18058505737");
        logisticsAddressDto.setZipCode("315000");
        logisticsAddressDto.setDefReceive(0);
        logisticsAddressDto.setDefSend(0);
        try{
            logisticsAddressService.setAddress(logisticsAddressDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: setDefAddress(Integer type, Long id)
     *
     */
    @Test
    public void testSetDefAddress() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Integer type = 1;
        Long id = 115L;
        try{
            logisticsAddressService.setDefAddress(type, id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: delAddress(Long id)
     *
     */
    @Test
    public void testDelAddress() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 115L;
        try{
            logisticsAddressService.delAddress(id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: getDefaultAddress(Integer type)
     *
     */
    @Test
    public void testGetDefaultAddress() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        CurUserContextHolder.setCurUser("1262357585567289344");
        Integer type = 1;
        LogisticsAddressVo logisticsAddressVo = logisticsAddressService.getDefaultAddress(type);
        Assert.assertNotNull(logisticsAddressVo);
        System.out.println(logisticsAddressVo.toString());
    }

    /**
     *
     * Method: listLogisticsCompany(String shopId, String tenantId)
     *
     */
    @Test
    public void testListLogisticsCompany() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        CurUserContextHolder.setCurUser("1262357585567289344");
        Map<String, Object> map = logisticsAddressService.listLogisticsCompany("100002100001", "100002");
        Assert.assertNotNull(map);
        System.out.println(map.toString());
    }

    /**
     *
     * Method: setCompanyDefault(Long logisticsCompanyId)
     *
     */
    @Test
    public void testSetCompanyDefault() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long logisticsCompanyId = 1L;
        try{
            logisticsAddressService.setCompanyDefault(logisticsCompanyId);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: doPrintDeliverGoods(LogisticsPrintDeliverDto logisticsPrintDeliverDto)
     *
     */
    @Test
    public void testDoPrintDeliverGoods() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsPrintDeliverDto logisticsPrintDeliverDto = new LogisticsPrintDeliverDto();
        try{
            logisticsAddressService.doPrintDeliverGoods(logisticsPrintDeliverDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: doPrintDeliverIntegralOrder(LogisticsPrintDeliverDto logisticsPrintDeliverDto)
     *
     */
    @Test
    public void testDoPrintDeliverIntegralOrder() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsPrintDeliverDto logisticsPrintDeliverDto = new LogisticsPrintDeliverDto();
        try{
            logisticsAddressService.doPrintDeliverIntegralOrder(logisticsPrintDeliverDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    /**
     *
     * Method: doBatchDeliver(List<LogisticsBatchDeliverDto> logisticsBatchDeliverDtos, String shopId, String tenantId)
     *
     */
    @Test
    public void testDoBatchDeliver() throws Exception {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<LogisticsBatchDeliverDto> logisticsBatchDeliverDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        LogisticsBatchDeliverDto logisticsBatchDeliverDto = new LogisticsBatchDeliverDto();
        logisticsBatchDeliverDtos.add(logisticsBatchDeliverDto);
        try{
            logisticsAddressService.doBatchDeliver(logisticsBatchDeliverDtos, "100002100001", "100002");
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }


    /**
     *
     * Method: getExpressInfoDto(LogisticsPrintDeliverDto logisticsPrintDeliverDto, String code, Long orderId)
     *
     */
    @Test
    public void testGetExpressInfoDto() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = LogisticsAddressServiceImpl.getClass().getMethod("getExpressInfoDto", LogisticsPrintDeliverDto.class, String.class, Long.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

    /**
     *
     * Method: convertDelivery(String code, Long orderId)
     *
     */
    @Test
    public void testConvertDelivery() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = LogisticsAddressServiceImpl.getClass().getMethod("convertDelivery", String.class, Long.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

    /**
     *
     * Method: convertIntegralDelivery(String code, Long integralOrderId)
     *
     */
    @Test
    public void testConvertIntegralDelivery() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = LogisticsAddressServiceImpl.getClass().getMethod("convertIntegralDelivery", String.class, Long.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

    /**
     *
     * Method: expressPrint(LogisticsPrintDeliverDto logisticsPrintDeliverDto, String code, RoutingInfoVo routingInfoVo, Long orderId)
     *
     */
    @Test
    public void testExpressPrint() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = LogisticsAddressServiceImpl.getClass().getMethod("expressPrint", LogisticsPrintDeliverDto.class, String.class, RoutingInfoVo.class, Long.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

}
