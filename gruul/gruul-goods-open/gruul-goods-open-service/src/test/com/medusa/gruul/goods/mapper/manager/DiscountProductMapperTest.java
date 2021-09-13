package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.param.manager.DiscountProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.DiscountProductVo;
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
public class DiscountProductMapperTest {

    @Resource
    private DiscountProductMapper discountProductMapper;

    @Test
    public void queryDiscountProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        DiscountProductParam discountProductParam = new DiscountProductParam();
        IPage<DiscountProductVo> page = new Page<>(discountProductParam.getCurrent(), discountProductParam.getSize());
        List<DiscountProductVo> discountProductVos = discountProductMapper.queryDiscountProductList(page, discountProductParam);
        Assert.assertNotNull(discountProductVos);
        System.out.println(discountProductVos.toString());
    }

    @Test
    public void queryRemoveProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        DiscountProductParam discountProductParam = new DiscountProductParam();
        IPage<DiscountProductVo> page = new Page<>(discountProductParam.getCurrent(), discountProductParam.getSize());
        List<DiscountProductVo> discountProductVos = discountProductMapper.queryRemoveProductList(page, discountProductParam);
        Assert.assertNotNull(discountProductVos);
        System.out.println(discountProductVos.toString());
    }

    @Test
    public void querySaveProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> ids = new ArrayList<>(CommonConstants.NUMBER_ONE);
        ids.add(375L);
        List<DiscountProductVo> discountProductVos = discountProductMapper.querySaveProductList(ids);
        Assert.assertNotNull(discountProductVos);
        System.out.println(discountProductVos.toString());
    }

    @Test
    public void queryDiscountProductTypeList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> productIds = new ArrayList<>(CommonConstants.NUMBER_ONE);
        productIds.add(375L);
        List<DiscountProductVo> discountProductVos = discountProductMapper.queryDiscountProductTypeList(productIds);
        Assert.assertNotNull(discountProductVos);
        System.out.println(discountProductVos.toString());
    }
}