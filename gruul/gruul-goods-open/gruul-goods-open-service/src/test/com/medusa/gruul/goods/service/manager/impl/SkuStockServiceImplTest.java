package com.medusa.gruul.goods.service.manager.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.param.OperateStockDto;
import com.medusa.gruul.goods.service.manager.ISkuStockService;
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
@Import({SkuStockServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class SkuStockServiceImplTest {

    @Resource
    private ISkuStockService skuStockService;

    @Test
    public void subtractStock() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        OperateStockDto operateStockDto = new OperateStockDto();
        operateStockDto.setSkuId(918L);
        operateStockDto.setNumber(5);
        try{
            Boolean flag = skuStockService.subtractStock(operateStockDto);
            System.out.println(flag);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void batchSubtractStock() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<OperateStockDto> operateStockDtoList = new ArrayList<>(CommonConstants.NUMBER_TWO);
        OperateStockDto operateStockDtoOne = new OperateStockDto();
        operateStockDtoOne.setSkuId(918L);
        operateStockDtoOne.setNumber(5);
        operateStockDtoList.add(operateStockDtoOne);
        OperateStockDto operateStockDtoTwo = new OperateStockDto();
        operateStockDtoTwo.setSkuId(919L);
        operateStockDtoTwo.setNumber(10);
        operateStockDtoList.add(operateStockDtoTwo);
        try{
            Boolean flag = skuStockService.batchSubtractStock(operateStockDtoList);
            System.out.println(flag);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void batchRevertStock() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<OperateStockDto> operateStockDtoList = new ArrayList<>(CommonConstants.NUMBER_TWO);
        OperateStockDto operateStockDtoOne = new OperateStockDto();
        operateStockDtoOne.setSkuId(918L);
        operateStockDtoOne.setNumber(5);
        operateStockDtoList.add(operateStockDtoOne);
        OperateStockDto operateStockDtoTwo = new OperateStockDto();
        operateStockDtoTwo.setSkuId(919L);
        operateStockDtoTwo.setNumber(10);
        operateStockDtoList.add(operateStockDtoTwo);
        try{
            Boolean flag = skuStockService.batchRevertStock(operateStockDtoList);
            System.out.println(flag);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}