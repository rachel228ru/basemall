package com.medusa.gruul.goods.service.manager.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.assemble.api.feign.AssActAssociationService;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.ResultList;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.feign.RemoteGoodsService;
import com.medusa.gruul.goods.api.model.dto.manager.*;
import com.medusa.gruul.goods.api.model.param.manager.DiscountProductParam;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.DiscountProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.ItemVo;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.SkuStockVo;
import com.medusa.gruul.goods.service.manager.IProductService;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.oss.api.feign.RemoteSysOssService;
import com.medusa.gruul.shipping.api.feign.RemoteShippingFeignService;
import com.medusa.gruul.shipping.model.vo.ShippingLinePointListsVo;
import com.medusa.gruul.shipping.model.vo.ShippingPointInfoVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({ProductServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RedisAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class ProductServiceImplTest {

    @Resource
    private IProductService productService;

    @MockBean
    RemoteShippingFeignService remoteShippingFeignService;

    @MockBean
    AssActAssociationService assActAssociationService;

    @MockBean
    RemoteSysOssService remoteShippingService;

    @MockBean
    RemoteOrderService remoteOrderService;

    @Test
    public void getProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        IPage<ProductVo> page = productService.getProductList(productParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    @Test
    public void getSupplierProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        productParam.setProviderId("84");
        IPage<ProductVo> page = productService.getSupplierProductList(productParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    @Test
    public void setLaunchArea() {
    }

    @Test
    public void setProductFeignInfo() {
    }

    @Test
    public void getProductById() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 374L;
        ProductVo productVo = productService.getProductById(id);
        Assert.assertNotNull(productVo);
        System.out.println(productVo.toString());
    }

    @Test
    public void getAllPointInfoList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ResultList<ShippingLinePointListsVo> resultList = productService.getAllPointInfoList();
        Assert.assertNotNull(resultList.getData());
        System.out.println(resultList.getData().toString());
    }

    @Test
    public void getPointInfoListByIds() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {374L, 375L};
        ResultList<ShippingPointInfoVo> resultList = productService.getPointInfoListByIds(ids);
        Assert.assertNotNull(resultList.getData());
        System.out.println(resultList.getData().toString());
    }

    @Test
    public void getSkuStockAndMemberPriceById() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 375L;
        List<SkuStockVo> skuStockVos = productService.getSkuStockAndMemberPriceById(id);
        Assert.assertNotNull(skuStockVos);
        System.out.println(skuStockVos.toString());
    }

    @Test
    public void issueProduct() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductDto productDto = new ProductDto();
        productDto.setSortingCategoryId(541L);
        productDto.setProviderId(1L);
        productDto.setFreightTemplateId(1L);
        productDto.setLimitType(0);
        productDto.setSaleMode(1L);
        productDto.setName("康师傅红烧牛肉面");
        productDto.setPic("00");
        productDto.setAlbumPics("11");
        productDto.setVideoUrl("22");
        productDto.setUnit("克");
        productDto.setWeight(BigDecimal.valueOf(100));
        productDto.setOpenSpecs(true);
        productDto.setStatus(0);
        List<SkuStockDto> skuStockDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        SkuStockDto skuStockDto = new SkuStockDto();
        skuStockDto.setSkuCode("0003");
        skuStockDto.setSpecs("100*100");
        skuStockDto.setPic("11");
        skuStockDto.setPrice(new BigDecimal(100));
        skuStockDto.setOriginalPrice(new BigDecimal(200));
        skuStockDto.setStock(200);
        skuStockDto.setLowStock(10);
        skuStockDtos.add(skuStockDto);
        productDto.setSkuStocks(skuStockDtos);
        try{
            productService.issueProduct(productDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateProductShowCategory() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<ProductShowCategoryDto> productShowCategoryDtos = new ArrayList<>(CommonConstants.NUMBER_TWO);
        ProductShowCategoryDto productShowCategoryDto = new ProductShowCategoryDto();
        productShowCategoryDto.setParentId(0L);
        productShowCategoryDto.setShowCategoryId(572L);
        productShowCategoryDtos.add(productShowCategoryDto);
        Long[] productIds = {375L};
        try{
            productService.updateProductShowCategory(productShowCategoryDtos, productIds);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateProductSaleMode() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {375L};
        Long saleMode = 0L;
        String type = "0";
        try{
            productService.updateProductSaleMode(ids, saleMode, type);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void setLaunchArea1() {
    }

    @Test
    public void updateProduct() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductDto productDto = new ProductDto();
        productDto.setId(375L);
        productDto.setSortingCategoryId(541L);
        productDto.setProviderId(1L);
        productDto.setFreightTemplateId(1L);
        productDto.setLimitType(0);
        productDto.setSaleMode(1L);
        productDto.setName("康师傅红烧牛肉面");
        productDto.setPic("00");
        productDto.setAlbumPics("11");
        productDto.setVideoUrl("22");
        productDto.setUnit("克");
        productDto.setWeight(BigDecimal.valueOf(100));
        productDto.setOpenSpecs(true);
        productDto.setStatus(0);
        List<SkuStockDto> skuStockDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        SkuStockDto skuStockDto = new SkuStockDto();
        skuStockDto.setSkuCode("0003");
        skuStockDto.setSpecs("100*100");
        skuStockDto.setPic("11");
        skuStockDto.setPrice(new BigDecimal(100));
        skuStockDto.setOriginalPrice(new BigDecimal(200));
        skuStockDto.setStock(200);
        skuStockDto.setLowStock(10);
        skuStockDtos.add(skuStockDto);
        productDto.setSkuStocks(skuStockDtos);
        try{
            productService.updateProduct(productDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateMemberPrice() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 375L;
        List<SkuStockDto> skuStockDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        SkuStockDto skuStockDto = new SkuStockDto();
        skuStockDto.setSkuCode("0003");
        skuStockDto.setSpecs("100*100");
        skuStockDto.setPic("11");
        skuStockDto.setPrice(new BigDecimal(100));
        skuStockDto.setOriginalPrice(new BigDecimal(200));
        skuStockDto.setStock(200);
        skuStockDto.setLowStock(10);
        List<MemberPriceDto> memberPriceDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        MemberPriceDto memberPriceDto = new MemberPriceDto();
        memberPriceDto.setMemberLevelId(1L);
        memberPriceDto.setMemberPrice(new BigDecimal(70));
        MemberPriceDto memberPriceDto1 = new MemberPriceDto();
        memberPriceDtos.add(memberPriceDto);
        memberPriceDto1.setMemberLevelId(2L);
        memberPriceDto1.setMemberPrice(new BigDecimal(80));
        memberPriceDtos.add(memberPriceDto1);
        MemberPriceDto memberPriceDto2 = new MemberPriceDto();
        memberPriceDto2.setMemberLevelId(3L);
        memberPriceDto2.setMemberPrice(new BigDecimal(90));
        memberPriceDtos.add(memberPriceDto2);
        skuStockDto.setMemberPrices(memberPriceDtos);
        skuStockDtos.add(skuStockDto);
        try{
            productService.updateMemberPrice(id, skuStockDtos);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateSkuRebate() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 375L;
        List<SkuStockDto> skuStockDtos = new ArrayList<>(CommonConstants.NUMBER_ONE);
        List<SkuRebateDto> skuRebateDtos = new ArrayList<>(CommonConstants.NUMBER_TWO);
        SkuStockDto skuStockDto = new SkuStockDto();
        skuStockDto.setSkuCode("0003");
        skuStockDto.setSpecs("100*100");
        skuStockDto.setPic("11");
        skuStockDto.setPrice(new BigDecimal(100));
        skuStockDto.setOriginalPrice(new BigDecimal(200));
        skuStockDto.setStock(200);
        skuStockDto.setLowStock(10);
        SkuStockDto skuStockDto1 = new SkuStockDto();
        SkuRebateDto skuRebateDto = new SkuRebateDto();
        skuRebateDto.setSkuId(8297L);
        skuRebateDto.setRebatePrice(new BigDecimal(35.00));
        skuRebateDtos.add(skuRebateDto);
        skuStockDto1.setId(8297L);
        skuStockDto1.setMemberType(1);
        skuStockDto1.setSkuRebates(skuRebateDtos);
        skuStockDtos.add(skuStockDto);
        skuStockDtos.add(skuStockDto1);
        try{
            productService.updateSkuRebate(id, skuStockDtos);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateDominoState() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {375L};
        Integer dominoState = 0;
        try{
            productService.updateDominoState(ids, dominoState);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateProductStatus() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {375L};
        Integer state = 0;
        Integer type = 0;
        try{
            productService.updateProductStatus(ids, state, type);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void deleteProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {375L};
        Integer type = 0;
        try{
            productService.deleteProductList(ids, type);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void getAssembleProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        List<ProductVo> productVos = productService.getAssembleProductList(JSON.toJSONString(productParam));
        Assert.assertNotNull(productVos);
        System.out.println(productVos.toString());
    }

    @Test
    public void getDiscountProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        DiscountProductParam discountProductParam = new DiscountProductParam();
        IPage<DiscountProductVo> page = productService.getDiscountProductList(discountProductParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    @Test
    public void getRemoveProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        DiscountProductParam discountProductParam = new DiscountProductParam();
        List<Long> productIds = new ArrayList<>(CommonConstants.NUMBER_TWO);
        productIds.add(374L);
        productIds.add(375L);
        discountProductParam.setProductIds(productIds);
        IPage<DiscountProductVo> page = productService.getRemoveProductList(discountProductParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    @Test
    public void getAliveProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {374L, 375L};
        List<DiscountProductVo> discountProductVos = productService.getAliveProductList(ids);
        Assert.assertNotNull(discountProductVos);
        System.out.println(discountProductVos.toString());
    }

    @Test
    public void getDiscountProductTypeList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> productIds = new ArrayList<>(CommonConstants.NUMBER_TWO);
        productIds.add(374L);
        productIds.add(375L);
        List<DiscountProductVo> discountProductVos = productService.getDiscountProductTypeList(productIds);
        Assert.assertNotNull(discountProductVos);
        System.out.println(discountProductVos.toString());
    }

    @Test
    public void getProductCount() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Integer count = productService.getProductCount();
        Assert.assertNotNull(count);
        System.out.println(count.toString());
    }

    @Test
    public void getAssembleProductIdList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> ids = productService.getAssembleProductIdList();
        Assert.assertNotNull(ids);
        System.out.println(ids.toString());
    }

    @Test
    public void checkProductByTemplateId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long templateId = 1L;
        Boolean flag = productService.checkProductByTemplateId(templateId);
        Assert.assertNotNull(flag);
        System.out.println(flag.toString());
    }

    @Test
    public void updateLaunchAreaByPointManagerId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long pointManagerId = 2L;
        try{
            productService.updateLaunchAreaByPointManagerId("100002", "100002100001", pointManagerId);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void findItemVoByIds() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> skuIds = new ArrayList<>(CommonConstants.NUMBER_ONE);
        skuIds.add(918L);
        List<ItemVo> itemVos = productService.findItemVoByIds(skuIds);
        Assert.assertNotNull(itemVos);
        System.out.println(itemVos.toString());
    }

    @Test
    public void updateSkuStockByProduct() {
    }

    @Test
    public void addSkuStock() {
    }

    @Test
    public void updateCacheShoppingCartProductStatus() {
    }

    @Test
    public void updateCacheShoppingCartProductSaleMode() {
    }

    @Test
    public void updateCacheShoppingCartProduct() {
    }

    @Test
    public void deleteCacheShoppingCartProduct() {
    }

    @Test
    public void deleteSkuStock() {
    }

    @Test
    public void updateSkuStock() {
    }

    @Test
    public void createDefaultProduct() {
        try{
            productService.createDefaultProduct();
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void getProductSn() {
    }

    @Test
    public void importCsvProduct() {
    }

    @Test
    public void setCsvPic() {
    }

    @Test
    public void setCsvSku() {
    }

    @Test
    public void getCsvProductList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ProductParam productParam = new ProductParam();
        IPage<ProductVo> page = productService.getCsvProductList(productParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }
}