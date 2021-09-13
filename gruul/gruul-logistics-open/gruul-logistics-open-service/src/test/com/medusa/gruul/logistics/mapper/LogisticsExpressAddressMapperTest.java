package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressAddressVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class LogisticsExpressAddressMapperTest {

    @Resource
    private LogisticsExpressAddressMapper logisticsExpressAddressMapper;

    @Test
    public void queryByExpressId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long expressId = 23L;
        LogisticsExpressAddressVo logisticsExpressAddressVo = logisticsExpressAddressMapper.queryByExpressId(expressId);
        Assert.assertNotNull(logisticsExpressAddressVo);
        System.out.println(logisticsExpressAddressVo.toString());
    }

    @Test
    public void queryByExpressCode() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        String expressCode = "zt";
        List<LogisticsExpressAddressVo> logisticsExpressAddressVos = logisticsExpressAddressMapper.queryByExpressCode(expressCode);
        Assert.assertNotNull(logisticsExpressAddressVos);
        System.out.println(logisticsExpressAddressVos.toString());
    }
}