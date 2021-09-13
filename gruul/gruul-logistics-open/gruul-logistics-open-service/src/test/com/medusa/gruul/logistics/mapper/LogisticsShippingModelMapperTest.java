package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.api.entity.LogisticsShippingModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class LogisticsShippingModelMapperTest {

    @Resource
    private LogisticsShippingModelMapper logisticsShippingModelMapper;

    @Test
    public void selectByParamMap() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_TWO);
        param.put("region_json", "540000");
        param.put("logistics_id", "194");
        List<LogisticsShippingModel> logisticsShippingModels = logisticsShippingModelMapper.selectByParamMap(param);
        Assert.assertNotNull(logisticsShippingModels);
        System.out.println(logisticsShippingModels.toString());
    }
}