package com.medusa.gruul.goods.web.controller.manager;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.dto.manager.ProductDto;
import com.medusa.gruul.goods.api.model.dto.manager.ProductShowCategoryDto;
import com.medusa.gruul.goods.api.model.param.manager.DiscountProductParam;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.DiscountProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.SkuStockVo;
import com.medusa.gruul.goods.service.manager.IProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@RestController
@RequestMapping("/manager/product")
public class ProductController {

    @Autowired
    private IProductService productService;


    /**
     * 商品信息列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "商品信息列表")
    @EscapeLogin
    public Result<PageUtils<ProductVo>> getProductList(ProductParam productParam) {
        PageUtils<ProductVo> pageUtils = new PageUtils(productService.getProductList(productParam));
        return Result.ok(pageUtils);
    }

    /**
     * 获取单个商品基础信息
     */
    @GetMapping("/get/{id}")
    @ApiOperation(value = "单个商品基础信息")
    public Result<ProductVo> getProductById(@ApiParam(value = "商品id", required = true) @PathVariable("id") Long id) {
        ProductVo productVo = productService.getProductById(id);
        return Result.ok(productVo);
    }


    /**
     * 获取单个商品sku信息
     */
    @GetMapping("/get/sku/stock/{id}")
    @ApiOperation(value = "单个商品sku信息")
    public Result<List<SkuStockVo>> getSkuStockAndMemberPriceById(@ApiParam(value = "商品id", required = true) @PathVariable("id") Long id) {
        List<SkuStockVo> skuStockVos = productService.getSkuStockAndMemberPriceById(id);
        return Result.ok(skuStockVos);
    }

    /**
     * 商品发布
     */
    @PostMapping("/issue")
    @ApiOperation(value = "商品发布")
    public Result issueProduct(@RequestBody @Validated ProductDto productDto) {
        productService.issueProduct(productDto);
        return Result.ok();
    }

    /**
     * 商品信息修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "商品信息修改")
    public Result updateProduct(@RequestBody @Validated ProductDto productDto) {
        productService.updateProduct(productDto);
        return Result.ok();
    }

    /**
     * 商品展示分类批量设置
     */
    @PostMapping("/updateProductShowCategory/{productIds}")
    @ApiOperation(value = "商品展示分类批量设置")
    public Result updateProductShowCategory(@ApiParam(value = "商品ids", required = true) @PathVariable(name = "productIds") Long[] productIds,
                                            @RequestBody List<ProductShowCategoryDto> productShowCategoryDtos) {
        productService.updateProductShowCategory(productShowCategoryDtos, productIds);
        return Result.ok();
    }

    /**
     * 商品专区变更
     */
    @PutMapping("/updateSaleMode/{saleMode}/{type}")
    @ApiOperation(value = "商品专区变更(0--商超系统，2--限时秒杀)")
    public Result updateProductSaleMode(@ApiParam(value = "商品ids", required = true) @RequestBody Long[] ids,
                                        @ApiParam(value = "商品专区值", required = true) @PathVariable("saleMode") Long saleMode) {
        productService.updateProductSaleMode(ids, saleMode);
        return Result.ok();
    }


    /**
     * 商品上下架
     */
    @PutMapping("/updateStatus/{status}/{type}")
    @ApiOperation(value = "商品上下架")
    public Result updateProductStatus(@ApiParam(value = "商品ids", required = true) @RequestBody Long[] ids,
                                      @ApiParam(value = "商品状态值", required = true) @PathVariable("status") Integer status) {
        productService.updateProductStatus(ids, status);
        return Result.ok();
    }

    /**
     * 商品信息删除
     */
    @DeleteMapping("/delete/{ids}/{type}")
    @ApiOperation(value = "商品信息删除")
    public Result deleteProductList(@ApiParam(value = "商品ids", required = true) @PathVariable(name = "ids") Long[] ids) {
        productService.deleteProductList(ids);
        return Result.ok();
    }

    //=============================================商品组件根据商品集合匹配未删除的商品===================================================

    /**
     * 根据商品数组匹配未删除的商品
     *
     * @param ids
     * @return
     */
    @GetMapping("/alive/product/list/{ids}")
    @ApiOperation(value = "根据商品数组匹配未删除的商品")
    public Result<List<DiscountProductVo>> getAliveProductList(@ApiParam(value = "商品ids", required = true) @PathVariable(name = "ids") Long[] ids) {
        List<DiscountProductVo> saveIds = productService.getAliveProductList(ids);
        return Result.ok(saveIds);
    }

    /**
     * 淘宝商品csv文件导入
     *
     * @param file 商品csv文件
     * @return Result
     */
    @PostMapping("/import/csv/product")
    @ApiOperation(value = "淘宝商品csv文件导入")
    public Result importCsvProduct(MultipartFile file) {
        return productService.importCsvProduct(file);
    }

    /**
     * 素材库商品信息列表
     */
    @GetMapping("/csv/list")
    @ApiOperation(value = "商品信息列表")
    public Result<PageUtils<ProductVo>> getCsvProductList(ProductParam productParam) {
        PageUtils<ProductVo> pageUtils = new PageUtils(productService.getCsvProductList(productParam));
        return Result.ok(pageUtils);
    }

    //=============================================订单移除发货单商品展示列表===================================================

    /**
     * 已发货的订单商品移除列表
     */
    @GetMapping("/remove/product/list")
    @ApiOperation(value = "已发货的订单商品移除列表")
    public Result<PageUtils<DiscountProductVo>> getRemoveProductList(DiscountProductParam discountProductParam) {
        PageUtils<DiscountProductVo> pageUtils = new PageUtils(productService.getRemoveProductList(discountProductParam));
        return Result.ok(pageUtils);
    }


}
