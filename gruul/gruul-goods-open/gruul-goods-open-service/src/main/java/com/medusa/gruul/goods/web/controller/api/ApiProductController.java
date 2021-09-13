package com.medusa.gruul.goods.web.controller.api;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.param.api.ApiProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiAliveProductVo;
import com.medusa.gruul.goods.api.model.vo.api.ApiProductVo;
import com.medusa.gruul.goods.api.model.vo.api.ApiShowCategoryProductVo;
import com.medusa.gruul.goods.service.api.IApiProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序商品信息 前端控制器
 *
 * @author kyl
 * @since 2019-10-06
 */
@RestController
@RequestMapping("/api/product")
public class ApiProductController {

    @Autowired
    private IApiProductService apiProductService;

    /**
     * 商品详情
     */
    @GetMapping("/get/{id}")
    @ApiOperation(value = "商品详情")
    public Result<ApiProductVo> getProductById(@PathVariable("id") Long id) {
        ApiProductVo product = apiProductService.getProductById(id);
        Result<ApiProductVo> result = Result.ok(product);
        return result;
    }

    /**
     * 首页商品列表
     */
    @GetMapping("/index/list")
    @ApiOperation(value = "首页商品列表")
    @EscapeLogin
    public Result<PageUtils<ApiAliveProductVo>> getIndexList(ApiProductParam productParam) {
        PageUtils<ApiAliveProductVo> pageUtils = new PageUtils<>(apiProductService.getPageList(productParam));
        return Result.ok(pageUtils);
    }

    /**
     * 商超系统分类列表
     */
    @GetMapping("/supermarket/list")
    @ApiOperation(value = "商超系统分类")
    @EscapeLogin
    public Result<PageUtils<ApiAliveProductVo>> getSupermarketList(ApiProductParam productParam) {
        PageUtils<ApiAliveProductVo> pageUtils = new PageUtils<>(apiProductService.getSupermarketList(productParam));
        return Result.ok(pageUtils);
    }

    //=============================================商品组件根据商品集合匹配未删除的商品===================================================

    /**
     * 根据商品数组匹配未删除的商品
     *
     * @param ids
     * @param launchArea
     * @param saleMode
     * @return
     */
    @GetMapping("/alive/product/list/{ids}")
    @ApiOperation(value = "根据商品数组匹配未删除的商品")
    @EscapeLogin
    public Result<List<ApiAliveProductVo>> getAliveProductList(@ApiParam(value = "商品ids", required = true) @PathVariable(name = "ids") Long[] ids,
                                                               @ApiParam(value = "投放区域id") @RequestParam("launchArea") String launchArea,
                                                               @ApiParam(value = "商品专区") @RequestParam("saleMode") Long saleMode) {
        List<ApiAliveProductVo> saveList = apiProductService.getAliveProductList(ids, launchArea, saleMode);
        return Result.ok(saveList);
    }

    //=============================================商品组件根据商品集合匹配未删除的商品===================================================

    //=============================================商品分类页组件根据商品分类集合匹配对应分类下的商品===================================================

    /**
     * pc商品分类集合匹配对应分类下的商品 无投放区域与商品专区
     *
     * @param ids
     * @return
     */
    @GetMapping("pc/get/alive/product/list/group/by/category/{ids}")
    @ApiOperation(value = "pc商品分类集合匹配对应分类下的商品")
    @EscapeLogin
    public Result<List<ApiShowCategoryProductVo>> getAliveProductListGroupByCategory(@ApiParam(value = "分类ids", required = true) @PathVariable(name = "ids") Long[] ids) {
        List<ApiShowCategoryProductVo> saveList = apiProductService.getAliveProductListGroupByCategory(ids);
        return Result.ok(saveList);
    }

    /**
     * 商品分类集合匹配对应分类下的商品
     *
     * @param ids
     * @param saleMode
     * @return
     */
    @GetMapping("get/alive/product/list/by/category/{ids}")
    @ApiOperation(value = "商品分类集合匹配对应分类下的商品")
    @EscapeLogin
    public Result<List<ApiShowCategoryProductVo>> getAliveProductListByCategory(@ApiParam(value = "分类ids", required = true) @PathVariable(name = "ids") Long[] ids,
                                                                                @ApiParam(value = "商品专区", required = false) @RequestParam("saleMode") Long saleMode) {
        List<ApiShowCategoryProductVo> saveList = apiProductService.getAliveProductListByCategory(ids,saleMode);
        return Result.ok(saveList);
    }
}
