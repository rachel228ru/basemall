package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.SkuStockVo;
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
public class ProductMapperTest {

    @Resource
    private ProductMapper productMapper;

    @Test
    public void queryProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        IPage<ProductVo> page = new Page<>(productParam.getCurrent(), productParam.getSize());
        List<ProductVo> productVos = productMapper.queryProductList(page, productParam);
        Assert.assertNotNull(productVos);
        System.out.println(productVos.toString());
    }

    @Test
    public void querySupplierProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        productParam.setProviderId("84");
        IPage<ProductVo> page = new Page<>(productParam.getCurrent(), productParam.getSize());
        List<ProductVo> productVos = productMapper.querySupplierProductList(page, productParam);
        Assert.assertNotNull(productVos);
        System.out.println(productVos.toString());
    }

    @Test
    public void queryAllCount() {
        Integer count = productMapper.queryAllCount();
        Assert.assertNotNull(count);
        System.out.println(count.toString());
    }

    @Test
    public void getProductById() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 375L;
        ProductVo productVo = productMapper.getProductById(id);
        Assert.assertNotNull(productVo);
        System.out.println(productVo.toString());
    }

    @Test
    public void querySkuStock() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 375L;
        List<SkuStockVo> skuStockVos = productMapper.querySkuStock(id);
        Assert.assertNotNull(skuStockVos);
        System.out.println(skuStockVos.toString());
    }

    @Test
    public void appendSale() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long productId = 375L;
        Integer number = 2;
        try{
            productMapper.appendSale(productId, number);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void queryAssembleProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        List<ProductVo> productVos = productMapper.queryAssembleProductList(productParam);
        Assert.assertNotNull(productVos);
        System.out.println(productVos.toString());
    }

    @Test
    public void queryDefaultProduct() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<ProductVo> productVos = productMapper.queryDefaultProduct();
        Assert.assertNotNull(productVos);
        System.out.println(productVos.toString());
    }
}