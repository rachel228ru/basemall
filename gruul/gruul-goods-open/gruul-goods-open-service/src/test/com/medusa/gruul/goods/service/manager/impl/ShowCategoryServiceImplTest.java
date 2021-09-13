package com.medusa.gruul.goods.service.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategoryDto;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategorySecondDto;
import com.medusa.gruul.goods.api.model.param.manager.ShowCategoryParam;
import com.medusa.gruul.goods.api.model.vo.manager.ShowCategoryVo;
import com.medusa.gruul.goods.service.manager.IShowCategoryService;
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


@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({ShowCategoryServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class ShowCategoryServiceImplTest {

    @Resource
    private IShowCategoryService showCategoryService;

    @Test
    public void getShowCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ShowCategoryParam showCategoryParam = new ShowCategoryParam();
        IPage<ShowCategoryVo> page = showCategoryService.getShowCategoryList(showCategoryParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    @Test
    public void getAllShowCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ShowCategoryParam showCategoryParam = new ShowCategoryParam();
        List<ShowCategoryVo> showCategoryVos = showCategoryService.getAllShowCategoryList(showCategoryParam);
        Assert.assertNotNull(showCategoryVos);
        System.out.println(showCategoryVos.toString());
    }

    @Test
    public void getShowCategoryById() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        String id = "258";
        ShowCategoryVo showCategoryVo = showCategoryService.getShowCategoryById(id);
        Assert.assertNotNull(showCategoryVo);
        System.out.println(showCategoryVo.toString());
    }

    @Test
    public void addShowCategory() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ShowCategoryDto showCategoryDto = new ShowCategoryDto();
        showCategoryDto.setParentId(0L);
        showCategoryDto.setName("测试分类233");
        List<ShowCategorySecondDto> showCategorySecondDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        ShowCategorySecondDto showCategorySecondDto = new ShowCategorySecondDto();
        showCategorySecondDto.setName("测试二级分类");
        showCategorySecondDtos.add(showCategorySecondDto);
        showCategoryDto.setShowCategorySecondDtos(showCategorySecondDtos);
        try{
            showCategoryService.addShowCategory(showCategoryDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void addSecondList() {
        List<ShowCategorySecondDto> showCategorySecondDtos = new ArrayList<>(CommonConstants.NUMBER_TWO);
        ShowCategorySecondDto showCategorySecondDtoOne = new ShowCategorySecondDto();
        showCategorySecondDtoOne.setName("二级分类1");
        showCategorySecondDtoOne.setParentId(258L);
        showCategorySecondDtos.add(showCategorySecondDtoOne);
        ShowCategorySecondDto showCategorySecondDtoTwo = new ShowCategorySecondDto();
        showCategorySecondDtoTwo.setName("二级分类2");
        showCategorySecondDtoTwo.setParentId(258L);
        showCategorySecondDtos.add(showCategorySecondDtoTwo);

        try{
            showCategoryService.addSecondList(showCategorySecondDtos);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateShowCategory() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ShowCategoryDto showCategoryDto = new ShowCategoryDto();
        showCategoryDto.setParentId(0L);
        showCategoryDto.setName("测试分类233");
        showCategoryDto.setId(258L);
        List<ShowCategorySecondDto> showCategorySecondDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        ShowCategorySecondDto showCategorySecondDto = new ShowCategorySecondDto();
        showCategorySecondDto.setName("测试二级分类");
        showCategorySecondDtos.add(showCategorySecondDto);
        showCategoryDto.setShowCategorySecondDtos(showCategorySecondDtos);
        try{
            showCategoryService.updateShowCategory(showCategoryDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void deleteShowCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 258L;
        try{
            showCategoryService.deleteShowCategoryList(id);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateShowCategorySort() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<ShowCategoryDto> showCategoryDtos = new ArrayList<>(CommonConstants.NUMBER_TWO);
        ShowCategoryDto showCategoryDtoOne = new ShowCategoryDto();
        showCategoryDtoOne.setId(258L);
        showCategoryDtoOne.setSort(2);
        showCategoryDtos.add(showCategoryDtoOne);
        ShowCategoryDto showCategoryDtoTwo = new ShowCategoryDto();
        showCategoryDtoTwo.setId(259L);
        showCategoryDtoTwo.setSort(1);
        showCategoryDtos.add(showCategoryDtoTwo);
        try{
            showCategoryService.updateShowCategorySort(showCategoryDtos);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}