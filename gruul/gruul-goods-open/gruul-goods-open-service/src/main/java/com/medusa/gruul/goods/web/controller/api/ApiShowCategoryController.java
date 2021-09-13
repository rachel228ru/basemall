package com.medusa.gruul.goods.web.controller.api;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.vo.api.ApiShowCategoryVo;
import com.medusa.gruul.goods.service.api.IApiShowCategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序商品展示分类 前端控制器
 *
 * @author kyl
 * @since 2019-11-05
 */
@RestController
@RequestMapping("/api/show/category")
public class ApiShowCategoryController {

    @Autowired
    private IApiShowCategoryService showCategoryService;

    /**
     * 获取所有含有商品的展示分类
     */
    @GetMapping("/get/all")
    @ApiOperation(value = "所有含有商品的展示分类")
    @EscapeLogin
    public Result<List<ApiShowCategoryVo>> getAllApiShowCategoryList() {
        List<ApiShowCategoryVo> attributeTemplateVos = showCategoryService.getAllApiShowCategoryList();
        return Result.ok(attributeTemplateVos);
    }

    /**
     * 获取对应分区含有商品的展示分类
     */
    @GetMapping("/get/{saleMode}")
    @ApiOperation(value = "获取对应分区含有商品的展示分类")
    @EscapeLogin
    public Result<List<ApiShowCategoryVo>> getApiShowCategoryListBySaleMode(@ApiParam(value = "商品分区 0-商超", required = true) @PathVariable("saleMode") Long saleMode) {
        List<ApiShowCategoryVo> attributeTemplateVos = showCategoryService.getApiShowCategoryListBySaleMode(saleMode);
        return Result.ok(attributeTemplateVos);
    }
}
