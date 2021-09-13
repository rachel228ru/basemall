package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.param.api.ApiProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiAliveProductVo;
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
public class ApiAliveProductMapperTest {

    @Resource
    private ApiAliveProductMapper apiAliveProductMapper;

    @Test
    public void querySaveProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> ids = new ArrayList<>(CommonConstants.NUMBER_TWO);
        ids.add(374L);
        ids.add(375L);
        Long saleMode = 0L;
        List<ApiAliveProductVo> solitaireActivityVos = apiAliveProductMapper.querySaveProductList(ids, saleMode);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }

    @Test
    public void queryProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ApiProductParam apiProductParam = new ApiProductParam();
        IPage<ApiAliveProductVo> page = new Page<>(apiProductParam.getCurrent(), apiProductParam.getSize());
        List<Long> ids = new ArrayList<>(CommonConstants.NUMBER_TWO);
        ids.add(374L);
        ids.add(375L);
        Integer productType = 1;
        List<ApiAliveProductVo> solitaireActivityVos = apiAliveProductMapper.queryProductList(page, apiProductParam, productType, ids);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }

    @Test
    public void querySuperMarketProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ApiProductParam apiProductParam = new ApiProductParam();
        IPage<ApiAliveProductVo> page = new Page<>(apiProductParam.getCurrent(), apiProductParam.getSize());
        List<ApiAliveProductVo> solitaireActivityVos = apiAliveProductMapper.querySuperMarketProductList(page, apiProductParam);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }

    @Test
    public void queryAssembleProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ApiProductParam apiProductParam = new ApiProductParam();
        IPage<ApiAliveProductVo> page = new Page<>(apiProductParam.getCurrent(), apiProductParam.getSize());
        List<Long> ids = new ArrayList<>(CommonConstants.NUMBER_TWO);
        ids.add(374L);
        ids.add(375L);
        Integer productType = 1;
        List<ApiAliveProductVo> solitaireActivityVos = apiAliveProductMapper.queryAssembleProductList(page, apiProductParam, productType, ids);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }

    @Test
    public void queryShowCategoryProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long secondShowCategoryId = 572L;
        List<ApiAliveProductVo> solitaireActivityVos = apiAliveProductMapper.queryShowCategoryProductList(secondShowCategoryId);
        Assert.assertNotNull(solitaireActivityVos);
        System.out.println(solitaireActivityVos.toString());
    }
}