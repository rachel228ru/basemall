

package com.medusa.gruul.goods.api.feign;

import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.api.model.vo.api.ApiAliveProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.*;
import com.medusa.gruul.goods.api.param.OperateStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @Description: RemoteGoodsService.java
 * @Author: alan
 * @Date: 2019/7/17 20:44
 */
@FeignClient(value = "goods-open")
@ApiIgnore
public interface RemoteGoodsService {

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
    SkuStock findSkuStockById(@RequestParam(value = "skuId", required = true) Long skuId);

    /**
     * 查询ItemVo
     *
     * @param skuIds
     * @return com.medusa.gruul.goods.api.model.vo.manager.ItemVo
     * @author alan
     * @date 2019/12/26 22:14
     */
    @GetMapping("itemVo")
    List<ItemVo> findItemVoByIds(@RequestParam(value = "skuIds", required = true) List<Long> skuIds);

    /**
     * 根据SKU ID批量查询库存
     *
     * @param skuIds
     * @return com.medusa.gruul.common.core.util.Result<java.util.Collection               <               com.medusa.gruul.goods.api.entity.SkuStock>>
     * @Author alan
     * @Date 2019/10/4 16:03
     */
    @GetMapping("stock/batch")
    Collection<SkuStock> findSkuStockListByIds(@RequestParam(value = "skuIds", required = true) Set<Long> skuIds);

    /**
     * 根据Product Id批量查询库存
     *
     * @param productIds
     * @return com.medusa.gruul.common.core.util.Result<java.util.Collection               <               com.medusa.gruul.goods.api.entity.SkuStock>>
     * @Author alan
     * @Date 2019/10/4 16:03
     */
    @GetMapping("product/stock/batch/")
    Collection<SkuStock> findSkuStockListByProductIds(@RequestParam(value = "productIds", required = true) Set<Long> productIds);

    /**
     * 减少库存
     *
     * @param operateStockDto
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     * @Author alan
     * @Date 2019/8/10 15:37
     */
    @PutMapping("stock/subtract")
    Boolean subtractStock(@RequestBody OperateStockDto operateStockDto);

    /**
     * 批量减少库存
     *
     * @param operateStockDtoList
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     */
    @PutMapping("stock/subtract/batch")
    Boolean batchSubtractStock(@RequestBody List<OperateStockDto> operateStockDtoList);

    /**
     * 批量归还库存
     *
     * @param operateStockDtoList
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     */
    @PutMapping("stock/revert/batch")
    Boolean batchRevertStock(@RequestBody List<OperateStockDto> operateStockDtoList);

    /**
     * 订单结算删除结算的购物车商品数据
     *
     * @param skuIds
     * @param userId
     * @return com.medusa.gruul.common.core.util.Result<java.lang.Boolean>
     */
    @PutMapping("delete/shopping/cart")
    Boolean deleteShoppingCartByOrder(@RequestBody List<Long> skuIds, @RequestParam("userId") String userId);


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
    Product findProductById(@RequestParam(value = "productId", required = true) Long productId);

    /**
     * 批量查询多个商品详情
     *
     * @param productIds
     * @return java.util.Collection<com.medusa.gruul.goods.api.entity.Product>
     * @Author alan
     * @Date 2019/10/5 10:33
     */
    @GetMapping("product/batch")
    Collection<Product> findProductListByIds(@RequestParam(value = "productIds", required = true) Set<Long> productIds);




    //=============================================物流服务接口===================================================


    /**
     * 查询运费模版id是否被商品使用
     *
     * @param templateId
     * @return Boolean
     * @Author lcysike
     * @Date 2020/03/14
     */
    @GetMapping("get/logistics/check/product")
    Boolean checkProductByTemplateId(@RequestParam(value = "templateId", required = true) Long templateId);


    /**
     * 批量查询多个商品详情
     *
     * @param productIds
     * @return List<DiscountProductVo>
     * @Author alan
     * @Date 2019/10/5 10:33
     */
    @GetMapping("get/discount/product/type/list")
    List<DiscountProductVo> findDiscountProductTypeList(@RequestParam(value = "productIds", required = true) List<Long> productIds);

    /**
     * 查询上架商品的数量
     *
     * @return Integer
     * @Author lcysike
     * @Date 2020/06/10
     */
    @GetMapping("get/product/count")
    Integer findProductCount();


    //=============================================数据服务接口===================================================

    /**
     * 根据供应商id数组查询供应商信息
     *
     * @param supplierIds
     * @return List<SupplierVo>
     * @Author lcysike
     * @Date 2020/5/8 12:33
     */
    @GetMapping("get/data/supplier/list")
    List<SupplierVo> findDataSetSupplierList(@RequestParam(value = "supplierIds", required = true) List<Long> supplierIds);
}
