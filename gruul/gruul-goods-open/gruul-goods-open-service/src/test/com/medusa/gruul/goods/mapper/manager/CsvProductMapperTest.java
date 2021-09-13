package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
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
public class CsvProductMapperTest {

    @Resource
    private CsvProductMapper csvProductMapper;

    @Test
    public void queryCsvProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        productParam.setPlace(1);
        IPage<ProductVo> page = new Page<>(productParam.getCurrent(), productParam.getSize());
        List<ProductVo> productVos = csvProductMapper.queryCsvProductList(page, productParam);
        Assert.assertNotNull(productVos);
        System.out.println(productVos.toString());
    }
}