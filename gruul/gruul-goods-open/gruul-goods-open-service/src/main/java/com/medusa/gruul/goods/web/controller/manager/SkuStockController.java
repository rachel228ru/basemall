package com.medusa.gruul.goods.web.controller.manager;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.service.manager.ISkuStockService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 产品sku信息 前端控制器
 * </p>
 *
 * @author lcysike
 * @since 2019-10-01
 */
@RestController
@RequestMapping("/manager/sku/stock")
public class SkuStockController {

    @Autowired
    private ISkuStockService skuStockService;


    /**
     * 查询单个库存
     *
     * @param skuId
     * @return com.medusa.gruul.common.core.util.Result<com.medusa.gruul.goods.api.entity.SkuStock>
     * @Author alan
     * @Date 2019/10/4 16:08
     */
    @GetMapping("stock")
    public Result<SkuStock> findById(@ApiParam(value = "产品skuId", required = true) Long skuId) {
        return Result.ok(skuStockService.getById(skuId));
    }

}
