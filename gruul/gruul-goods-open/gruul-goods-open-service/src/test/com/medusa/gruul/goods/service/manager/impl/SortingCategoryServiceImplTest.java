package com.medusa.gruul.goods.service.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({SortingCategoryServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class SortingCategoryServiceImplTest {

    @Resource
    private ISortingCategoryService sortingCategoryService;

    @Test
    public void getSortingCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        SortingCategoryParam sortingCategoryParam = new SortingCategoryParam();
        IPage<SortingCategoryVo> page = sortingCategoryService.getSortingCategoryList(sortingCategoryParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    @Test
    public void getAllSortingCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        SortingCategoryParam sortingCategoryParam = new SortingCategoryParam();
        List<SortingCategoryVo> sortingCategoryVos = sortingCategoryService.getAllSortingCategoryList(sortingCategoryParam);
        Assert.assertNotNull(sortingCategoryVos);
        System.out.println(sortingCategoryVos.toString());
    }

    @Test
    public void getSortingCategoryById() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 395L;
        SortingCategoryVo sortingCategoryVo = sortingCategoryService.getSortingCategoryById(id);
        Assert.assertNotNull(sortingCategoryVo);
        System.out.println(sortingCategoryVo.toString());
    }

    @Test
    public void addSortingCategoryList() {
        List<SortingCategoryDto> sortingCategoryDtos = new ArrayList<>(CommonConstants.NUMBER_TWO);
        SortingCategoryDto sortingCategoryDtoOne = new SortingCategoryDto();
        sortingCategoryDtoOne.setName("二级分类1");
        sortingCategoryDtos.add(sortingCategoryDtoOne);
        SortingCategoryDto sortingCategoryDtoTwo = new SortingCategoryDto();
        sortingCategoryDtoTwo.setName("二级分类2");
        sortingCategoryDtos.add(sortingCategoryDtoTwo);
        try{
            sortingCategoryService.addSortingCategoryList(sortingCategoryDtos);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateSortingCategory() {
        SortingCategoryDto sortingCategoryDtoOne = new SortingCategoryDto();
        sortingCategoryDtoOne.setId(395L);
        sortingCategoryDtoOne.setName("二级分类1");
        try{
            sortingCategoryService.updateSortingCategory(sortingCategoryDtoOne);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void deleteSortingCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {395L, 396L};
        try{
            sortingCategoryService.deleteSortingCategoryList(ids);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateSortingCategorySort() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<SortingCategoryDto> sortingCategoryDtos = new ArrayList<>(CommonConstants.NUMBER_TWO);
        SortingCategoryDto sortingCategoryDtoOne = new SortingCategoryDto();
        sortingCategoryDtoOne.setId(395L);
        sortingCategoryDtoOne.setSort(2);
        sortingCategoryDtos.add(sortingCategoryDtoOne);
        SortingCategoryDto sortingCategoryDtoTwo = new SortingCategoryDto();
        sortingCategoryDtoTwo.setId(396L);
        sortingCategoryDtoTwo.setSort(1);
        sortingCategoryDtos.add(sortingCategoryDtoTwo);
        try{
            sortingCategoryService.updateSortingCategorySort(sortingCategoryDtos);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void getSortingCategoryByProductId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long productId = 375L;
        SortingCategoryVo sortingCategoryVo = sortingCategoryService.getSortingCategoryByProductId(productId);
        Assert.assertNotNull(sortingCategoryVo);
        System.out.println(sortingCategoryVo.toString());
    }
}