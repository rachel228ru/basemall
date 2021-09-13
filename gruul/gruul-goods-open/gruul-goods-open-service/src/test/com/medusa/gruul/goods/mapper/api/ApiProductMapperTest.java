package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class ApiProductMapperTest {

    @Resource
    private ApiProductMapper apiProductMapper;

    @Test
    public void queryByPrimaryKey() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 375L;
        ApiProductVo solitaireActivityVos = apiProductMapper.queryByPrimaryKey(id);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }

    @Test
    public void queryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ApiProductParam apiProductParam = new ApiProductParam();
        IPage<ApiProductVo> page = new Page<>(apiProductParam.getCurrent(), apiProductParam.getSize());
        List<ApiProductVo> solitaireActivityVos = apiProductMapper.queryList(page, apiProductParam);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }

    @Test
    public void queryApiAssembleProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ApiProductParam apiProductParam = new ApiProductParam();
        List<ApiProductVo> solitaireActivityVos = apiProductMapper.queryApiAssembleProductList(apiProductParam);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }

    @Test
    public void querySolitaireActivityProduct() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> ids = new ArrayList<>(CommonConstants.NUMBER_TWO);
        ids.add(374L);
        ids.add(375L);
        Integer productType = 1;
        List<ApiProductVo> solitaireActivityVos = apiProductMapper.querySolitaireActivityProduct(productType, ids);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }
}