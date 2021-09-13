package com.medusa.gruul.goods.service.api.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.CurUserContextHolder;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartVo;
import com.medusa.gruul.goods.service.api.IApiShoppingCartService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({ApiShoppingCartServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RedisAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class ApiShoppingCartServiceImplTest {

    @Resource
    private IApiShoppingCartService apiShoppingCartService;

    @Test
    public void getShoppingCartListByUserId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        CurUserContextHolder.setCurUser("account:eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDIzMjYzODV9.jUB3a4UOc8CSaHmWjg2SL3WkkUUVmMC4ehjsPCR94b8");
        List<ApiShoppingCartVo> fullScaleVos = apiShoppingCartService.getShoppingCartListByUserId();
        Assert.assertNotNull(fullScaleVos);
        System.out.println(fullScaleVos.toString());
    }

    @Test
    public void addShoppingCart() {
    }

    @Test
    public void cleanEffectShoppingCart() {
    }

    @Test
    public void changeSelectStatus() {
    }

    @Test
    public void updateShoppingCart() {
    }

    @Test
    public void updateShoppingCartCache() {
    }

    @Test
    public void updateShoppingCartDatabase() {
    }

    @Test
    public void deleteShoppingCartList() {
    }

    @Test
    public void deleteShoppingCartByOrder() {
    }
}