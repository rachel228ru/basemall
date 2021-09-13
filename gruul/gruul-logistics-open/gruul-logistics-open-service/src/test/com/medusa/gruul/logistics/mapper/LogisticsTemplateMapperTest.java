package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.model.param.LogisticsTemplateParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressPrintVo;
import com.medusa.gruul.logistics.model.vo.LogisticsTemplateVo;
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
public class LogisticsTemplateMapperTest {

    @Resource
    private LogisticsTemplateMapper logisticsTemplateMapper;

    @Test
    public void queryTemplateList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        LogisticsTemplateParam logisticsTemplateParam = new LogisticsTemplateParam();
        IPage<LogisticsExpressPrintVo> page = new Page<>(logisticsTemplateParam.getCurrent(), logisticsTemplateParam.getSize());
        List<LogisticsTemplateVo> logisticsTemplateVos = logisticsTemplateMapper.queryTemplateList(page, logisticsTemplateParam);
        Assert.assertNotNull(logisticsTemplateVos);
        System.out.println(logisticsTemplateVos.toString());
    }

    @Test
    public void selectByPrimaryKey() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 194L;
        LogisticsTemplateVo logisticsTemplateVo = logisticsTemplateMapper.selectByPrimaryKey(id);
        Assert.assertNotNull(logisticsTemplateVo);
        System.out.println(logisticsTemplateVo.toString());
    }
}