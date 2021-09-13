package com.medusa.gruul.goods.web.controller.manager;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.vo.manager.ProductAttributeVo;
import com.medusa.gruul.goods.service.manager.IProductAttributeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 产品属性信息
 * </p>
 *
 * @author lcysike
 * @since 2019-10-01
 */
@RestController
@RequestMapping("/manager/product/attribute")
public class ProductAttributeController {

    @Autowired
    private IProductAttributeService productAttributeService;

    /**
     * 根据产品id查询产品属性列表
     */
    @GetMapping("/list/{productId}")
    @ApiOperation(value = "产品属性列表")
    public Result<List<ProductAttributeVo>> selectByProductId(@ApiParam(value = "产品id", required = true) @PathVariable("productId") Long productId) {
        List<ProductAttributeVo> list = productAttributeService.selectByProductId(productId);
        return Result.ok(list);
    }

}
