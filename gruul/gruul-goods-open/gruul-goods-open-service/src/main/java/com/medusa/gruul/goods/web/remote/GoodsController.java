package com.medusa.gruul.goods.web.remote;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.api.model.vo.api.ApiAliveProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.*;
import com.medusa.gruul.goods.api.param.OperateStockDto;
import com.medusa.gruul.goods.service.api.IApiProductService;
import com.medusa.gruul.goods.service.api.IApiShoppingCartService;
import com.medusa.gruul.goods.service.manager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * sku的库存 前端控制器
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@RestController(value = "remoteGoodsController")
@RequestMapping("/")
public class GoodsController {
    @Autowired
    private ISkuStockService skuStockService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IApiProductService apiProductService;
    @Autowired
    private IApiShoppingCartService apiShoppingCartService;
    @Autowired
    private ISupplierService supplierService;


    //=============================================库存部分===================================================

    /**
     * 查询单个库存
     *
     * @param skuId
     * @return com.medusa.gruul.common.core.util.Result<com.medusa.gruul.goods.api.entity.SkuStock>
     * @Author alan
     * @Date 2019/10/4 16:08
     */
    @GetMapping("stock")
    @EscapeLogin
    public SkuStock findSkuStockById(@RequestParam(value = "skuId", required = true) Long skuId) {
        return skuStockService.getById(skuId);
    }

    /**
     * 批量查询库存
     *
     * @param skuIds
     * @return com.medusa.gruul.common.core.util.Result<java.util.Collection               <               com.medusa.gruul.goods.api.entity.SkuStock>>
     * @Author alan
     * @Date 2019/10/4 16:03
     */
    @GetMapping("stock/batch")
    @EscapeLogin
    public Collection<SkuStock> findSkuStockListByIds(@RequestParam(value = "skuIds", required = true) Set<Long> skuIds) {
        return skuStockService.listByIds(skuIds);
    }

    /**
     * 根据Product Id批量查询库存
     *
     * @param productIds
     * @return com.medusa.gruul.common.core.util.Result<java.util.Collection               <               com.medusa.gruul.goods.api.entity.SkuStock>>
     * @Author alan
     * @Date 2019/10/4 16:03
     */
    @GetMapping("product/stock/batch/")
    @EscapeLogin
    public Collection<SkuStock> findSkuStockListByProductIds(@RequestParam(value = "productIds", required = true) Set<Long> productIds) {
        return skuStockService.list(new QueryWrapper<SkuStock>().in("product_id", productIds));
    }

    /**
     * 减少库存
     *
     * @param operateStockDto
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     * @Author alan
     * @Date 2019/8/10 15:37
     */
    @PutMapping("stock/subtract")
    @EscapeLogin
    public Boolean subtractStock(@RequestBody OperateStockDto operateStockDto) {
        return skuStockService.subtractStock(operateStockDto);
    }


    /**
     * 批量减少库存（商城使用）
     *
     * @param operateStockDtoList
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     */
    @PutMapping("stock/subtract/batch")
    @EscapeLogin
    public Boolean batchSubtractStock(@RequestBody List<OperateStockDto> operateStockDtoList) {
        return skuStockService.batchSubtractStock(operateStockDtoList);
    }


    /**
     * 批量归还库存
     *
     * @param operateStockDtoList
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     */
    @PutMapping("stock/revert/batch")
    @EscapeLogin
    public Boolean batchRevertStock(@RequestBody List<OperateStockDto> operateStockDtoList) {
        return skuStockService.batchRevertStock(operateStockDtoList);
    }

    /**
     * 订单结算删除结算的购物车商品数据
     *
     * @param skuIds
     * @param userId
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     */
    @PutMapping("delete/shopping/cart")
    @EscapeLogin
    public Boolean deleteShoppingCartByOrder(@RequestBody List<Long> skuIds, @RequestParam("userId") String userId) {
        return apiShoppingCartService.deleteShoppingCartByOrder(skuIds, userId);
    }

    //=============================================商品部分===================================================


    /**
     * 查询单个商品详情
     *
     * @param productId
     * @return com.medusa.gruul.goods.api.entity.Product
     * @Author alan
     * @Date 2019/10/5 10:32
     */
    @GetMapping("product")
    @EscapeLogin
    public Product findProductById(@RequestParam(value = "productId", required = true) Long productId) {
        return productService.getById(productId);
    }

    /**
     * 批量查询多个商品详情
     *
     * @param productIds
     * @return java.util.Collection<com.medusa.gruul.goods.api.entity.Product>
     * @Author alan
     * @Date 2019/10/5 10:33
     */
    @GetMapping("product/batch")
    @EscapeLogin
    public Collection<Product> findProductListByIds(@RequestParam(value = "productIds", required = true) Set<Long> productIds) {
        return productService.listByIds(productIds);
    }

    /**
     * 获取单个sku关联商品
     *
     * @param skuIds
     * @return ItemVo
     * @Author lcysike
     * @Date 2019/12/27
     */
    @GetMapping("itemVo")
    @EscapeLogin
    public List<ItemVo> findItemVoByIds(@RequestParam(value = "skuIds", required = true) List<Long> skuIds) {
        return productService.findItemVoByIds(skuIds);
    }

    //=============================================物流部分===================================================


    /**
     * 查询运费模版id是否被商品使用--物流服务调用
     *
     * @param templateId
     * @return Boolean
     * @Author lcysike
     * @Date 2020/03/14 10:33
     */
    @GetMapping("get/logistics/check/product")
    @EscapeLogin
    public Boolean checkProductByTemplateId(@RequestParam(value = "templateId", required = true) Long templateId) {
        return productService.checkProductByTemplateId(templateId);
    }

    /**
     * 批量查询多个商品详情
     *
     * @param productIds
     * @return List<DiscountProductVo>
     */
    @GetMapping("get/discount/product/type/list")
    @EscapeLogin
    public List<DiscountProductVo> findDiscountProductTypeList(@RequestParam(value = "productIds", required = true) List<Long> productIds) {
        return productService.getDiscountProductTypeList(productIds);
    }

    /**
     * 查询上架商品的数量
     *
     * @return Integer
     */
    @GetMapping("get/product/count")
    @EscapeLogin
    public Integer findProductCount() {
        return productService.getProductCount();
    }


    //=============================================数据服务接口===================================================

    /**
     * 根据供应商id数组查询供应商信息
     *
     * @param supplierIds
     * @return List<SupplierVo>
     */
    @GetMapping("get/data/supplier/list")
    @EscapeLogin
    public List<SupplierVo> findDataSetSupplierList(@RequestParam(value = "supplierIds", required = true) List<Long> supplierIds){
        return supplierService.getDataSetSupplierList(supplierIds);
    }

    //=============================================数据服务接口===================================================
}
