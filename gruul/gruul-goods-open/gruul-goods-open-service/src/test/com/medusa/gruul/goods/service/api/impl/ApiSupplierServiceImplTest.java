package com.medusa.gruul.goods.service.api.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.CurUserContextHolder;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.dto.api.ApiSupplierDto;
import com.medusa.gruul.goods.service.api.IApiSupplierService;
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

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({ApiSupplierServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class ApiSupplierServiceImplTest {

    @Resource
    private IApiSupplierService apiSupplierService;

    @Test
    public void addSupplier() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        CurUserContextHolder.setCurUser("account:eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDIzMjYzODV9.jUB3a4UOc8CSaHmWjg2SL3WkkUUVmMC4ehjsPCR94b8");
        ApiSupplierDto apiSupplierDto = new ApiSupplierDto();
        apiSupplierDto.setName("测试供应商");
        apiSupplierDto.setMobile("13188888888");
        apiSupplierDto.setProvince("浙江省");
        apiSupplierDto.setCity("宁波市");
        apiSupplierDto.setCountry("鄞州区");
        apiSupplierDto.setAddress("测试地址");
        apiSupplierDto.setArea("浙江省宁波市鄞州区测试地址");
        apiSupplierDto.setProductInfo("测试产品");
        apiSupplierDto.setComeFrom(1);
        try{
            apiSupplierService.addSupplier(apiSupplierDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}