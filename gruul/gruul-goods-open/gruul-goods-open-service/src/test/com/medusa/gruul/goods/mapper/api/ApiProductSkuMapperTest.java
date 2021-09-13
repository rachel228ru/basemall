package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.param.api.ApiProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiProductVo;
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
public class ApiProductSkuMapperTest {

    @Resource
    private ApiProductSkuMapper apiProductSkuMapper;

    @Test
    public void querySolitaireActivityProductByParam() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ApiProductParam apiProductParam = new ApiProductParam();
        IPage<ApiProductVo> page = new Page<>(apiProductParam.getCurrent(), apiProductParam.getSize());
        Long[] productIds = {374L, 375L};
        Integer productType = 1;
        Integer distributionMode = 0;
        List<ApiProductVo> apiProductVos = apiProductSkuMapper.querySolitaireActivityProductByParam(page, productType, productIds, distributionMode);
        Assert.assertNotNull(apiProductVos);
        System.out.println(apiProductVos.toString());
    }
}