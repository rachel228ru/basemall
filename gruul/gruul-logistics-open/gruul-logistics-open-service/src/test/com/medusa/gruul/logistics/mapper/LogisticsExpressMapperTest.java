package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;
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
public class LogisticsExpressMapperTest {

    @Resource
    private LogisticsExpressMapper logisticsExpressMapper;

    @Test
    public void queryLogisticsExpressList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsExpressParam logisticsExpressParam = new LogisticsExpressParam();
        List<LogisticsExpressVo> logisticsExpressVos = logisticsExpressMapper.queryLogisticsExpressList(logisticsExpressParam);
        Assert.assertNotNull(logisticsExpressVos);
        System.out.println(logisticsExpressVos.toString());
    }

    @Test
    public void queryLogisticsExpressInfo() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 23L;
        LogisticsExpressVo logisticsExpressVo = logisticsExpressMapper.queryLogisticsExpressInfo(id);
        Assert.assertNotNull(logisticsExpressVo);
        System.out.println(logisticsExpressVo.toString());
    }
}