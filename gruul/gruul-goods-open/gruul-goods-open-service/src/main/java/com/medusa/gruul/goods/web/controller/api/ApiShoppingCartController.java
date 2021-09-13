package com.medusa.gruul.goods.web.controller.api;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.dto.api.ApiShoppingCartDto;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartVo;
import com.medusa.gruul.goods.service.api.IApiShoppingCartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 小程序购物车 前端控制器
 *
 * @author lcy
 * @since 2019-11-15
 */
@RestController
@RequestMapping("/api/shopping/cart")
public class ApiShoppingCartController {

    @Autowired
    private IApiShoppingCartService shoppingCartService;

    /**
     * 获取用户购物车商品信息
     */
    @GetMapping("/getByUserId")
    @ApiOperation(value = "获取用户购物车商品信息")
    public Result<List<ApiShoppingCartVo>> getShoppingCartListByUserId() {
        List<ApiShoppingCartVo> apiShoppingCartVos = shoppingCartService.getShoppingCartListByUserId();
        return Result.ok(apiShoppingCartVos);
    }

    /**
     * 加入购物车
     */
    @PostMapping("/add")
    @ApiOperation(value = "加入购物车")
    public Result addShoppingCart(@RequestBody @Validated ApiShoppingCartDto shoppingCartDto) {
        shoppingCartService.addShoppingCart(shoppingCartDto);
        return Result.ok();
    }

    /**
     * 更新购物车
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改购物车")
    public Result updateShoppingCart(@ApiParam(value = "购物车商品新老数据 老-oldApiShoppingCartDto 新-newApiShoppingCartDto", required = true) @RequestBody Map<String, ApiShoppingCartDto> params) {
        shoppingCartService.updateShoppingCart(params);
        return Result.ok();
    }

    /**
     * 清空购物车失效商品
     * @param ids
     */
    @DeleteMapping("/clean/effect")
    @ApiOperation(value = "清空购物车失效商品")
    public Result cleanEffectShoppingCart(@ApiParam(value = "清空的商品ids", required = true) @RequestBody Long[] ids) {
        shoppingCartService.cleanEffectShoppingCart(ids);
        return Result.ok();
    }

    /**
     * 切换购物车商品选中状态
     * @param ids
     * @param selectStatus
     */
    @DeleteMapping("/change/select/status/{selectStatus}")
    @ApiOperation(value = "切换购物车商品选中状态")
    public Result changeSelectStatus(@ApiParam(value = "清空的商品ids", required = true) @RequestBody Long[] ids,
                                     @ApiParam(value = "选中状态(0--未选中，1--已选中)", required = true) @PathVariable("selectStatus") Integer selectStatus) {
        shoppingCartService.changeSelectStatus(ids, selectStatus);
        return Result.ok();
    }

    /**
     * 购物车商品删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "购物车删除")
    public Result deleteShoppingCartList(@ApiParam(value = "删除的购物车商品list") @RequestBody List<ApiShoppingCartDto> apiShoppingCartDtos) {
        shoppingCartService.deleteShoppingCartList(apiShoppingCartDtos);
        return Result.ok();
    }

}
