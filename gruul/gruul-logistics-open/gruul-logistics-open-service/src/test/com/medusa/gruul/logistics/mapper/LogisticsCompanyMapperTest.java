package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.api.entity.LogisticsCompany;
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
public class LogisticsCompanyMapperTest {

    @Resource
    private LogisticsCompanyMapper logisticsCompanyMapper;

    @Test
    public void selectListCompany() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_TWO);
        param.put("shopId", "100002100001");
        param.put("tenantId", "100002");
        List<LogisticsCompany> logisticsCompanies = logisticsCompanyMapper.selectListCompany(param);
        Assert.assertNotNull(logisticsCompanies);
        System.out.println(logisticsCompanies.toString());
    }

    @Test
    public void selectListCompanyByParam() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Map<String, Object> param = new HashMap<>(CommonConstants.NUMBER_TWO);
        param.put("shopId", "100002100001");
        param.put("tenantId", "100002");
        param.put("code", "zt");
        LogisticsCompany logisticsCompany = logisticsCompanyMapper.selectListCompanyByParam(param);
        Assert.assertNotNull(logisticsCompany);
        System.out.println(logisticsCompany.toString());
    }
}