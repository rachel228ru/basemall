package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.vo.manager.ProductAttributeVo;
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
public class ProductAttributeMapperTest {

    @Resource
    private ProductAttributeMapper productAttributeMapper;

    @Test
    public void queryByProductId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 375L;
        List<ProductAttributeVo> distributionSetVos = productAttributeMapper.queryByProductId(id);
        Assert.assertNotNull(distributionSetVos);
        System.out.println(distributionSetVos.toString());
    }
}