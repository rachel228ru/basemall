package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.vo.manager.ItemVo;
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
public class SkuStockMapperTest {

    @Resource
    private SkuStockMapper skuStockMapper;

    @Test
    public void queryItemVoByIds() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> skuIds = new ArrayList<>(CommonConstants.NUMBER_TWO);
        skuIds.add(918L);
        skuIds.add(919L);
        List<ItemVo> skuStockVo = skuStockMapper.queryItemVoByIds(skuIds);
        Assert.assertNotNull(skuStockVo);
        System.out.println(skuStockVo.toString());
    }
}