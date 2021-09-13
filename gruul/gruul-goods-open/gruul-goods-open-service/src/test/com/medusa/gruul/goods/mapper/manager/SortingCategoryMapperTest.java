package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
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
public class SortingCategoryMapperTest {

    @Resource
    private SortingCategoryMapper sortingCategoryMapper;

    @Test
    public void querySortingCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        SortingCategoryParam sortingCategoryParam = new SortingCategoryParam();
        IPage<SortingCategoryVo> page = new Page<>(sortingCategoryParam.getCurrent(), sortingCategoryParam.getSize());
        List<SortingCategoryVo> sortingCategoryVos = sortingCategoryMapper.querySortingCategoryList(page, sortingCategoryParam);
        Assert.assertNotNull(sortingCategoryVos);
        System.out.println(sortingCategoryVos.toString());
    }

    @Test
    public void queryAllSortingCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        SortingCategoryParam sortingCategoryParam = new SortingCategoryParam();
        List<SortingCategoryVo> sortingCategoryVos = sortingCategoryMapper.queryAllSortingCategoryList(sortingCategoryParam);
        Assert.assertNotNull(sortingCategoryVos);
        System.out.println(sortingCategoryVos.toString());
    }

    @Test
    public void querySortingCategoryByProductId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long productId = 375L;
        SortingCategoryVo productVos = sortingCategoryMapper.querySortingCategoryByProductId(productId);
        Assert.assertNotNull(productVos);
        System.out.println(productVos.toString());
    }

    @Test
    public void queryDefaultSortingCategory() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        SortingCategoryVo sortingCategoryVo = sortingCategoryMapper.queryDefaultSortingCategory();
        Assert.assertNotNull(sortingCategoryVo);
        System.out.println(sortingCategoryVo.toString());
    }
}