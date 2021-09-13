package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class ApiShoppingCartMapperTest {

    @Resource
    private ApiShoppingCartMapper apiShoppingCartMapper;

    @Test
    public void queryShoppingCartListByUserId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Map map = new HashMap<>();
        map.put("userId", "1269526293284786176");
        map.put("type", 0);
        List<ApiShoppingCartVo> apiShoppingCartVos = apiShoppingCartMapper.queryShoppingCartListByUserId(map);
        Assert.assertNotNull(apiShoppingCartVos);
        System.out.println(apiShoppingCartVos.toString());
    }



    @Test
    public void selectEffectShoppingCart() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Map map = new HashMap<>();
        map.put("userId", "1269526293284786176");
        map.put("type", 0);
        List<ApiShoppingCartVo> apiShoppingCartVos = apiShoppingCartMapper.selectEffectShoppingCart(map);
        Assert.assertNotNull(apiShoppingCartVos);
        System.out.println(apiShoppingCartVos.toString());
    }
}