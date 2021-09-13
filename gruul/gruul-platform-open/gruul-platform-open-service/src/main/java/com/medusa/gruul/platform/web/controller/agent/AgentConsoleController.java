package com.medusa.gruul.platform.web.controller.agent;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.ShopPackageOrderDto;
import com.medusa.gruul.platform.model.dto.agent.AgentCreateShopDto;
import com.medusa.gruul.platform.model.dto.agent.AgentInfoMotifyDto;
import com.medusa.gruul.platform.model.vo.BuyPackageOrderVo;
import com.medusa.gruul.platform.model.vo.CalculateOrderPriceVo;
import com.medusa.gruul.platform.model.vo.ShopViewListVo;
import com.medusa.gruul.platform.model.vo.agent.*;
import com.medusa.gruul.platform.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/10/31
 */
@RestController
@RequestMapping("/agent/console")
@SaCheckLogin
@Api(tags = "代理后台相关")
public class AgentConsoleController {

    @Autowired
    private IPlatformShopInfoService shopInfoService;
    @Autowired
    private ISysShopPackageOrderService sysShopPackageOrderService;
    @Autowired
    private ISysShopPackageService sysShopPackageService;





    @GetMapping("/merchant/shop")
    @EscapeLogin
    @ApiOperation(value = "代理或渠道商查看名下商户的店铺", tags = "代理后台相关")
    public Result<PageUtils<ShopViewListVo>> merchantShops(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                                                           @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                                                           @ApiParam(value = "开始时间") @RequestParam(required = false) String startTime,
                                                           @ApiParam(value = "结束时间") @RequestParam(required = false) String endTime,
                                                           @ApiParam(value = "店铺名称") @RequestParam(required = false) String search,
                                                           @ApiParam(value = "店铺状态 1部署中 2正常 ，3已打烊，4禁用", required = false)
                                                           @RequestParam(required = false) Integer shopStatus,
                                                           @ApiParam(value = "套餐id", required = false) @RequestParam(required = false) Long packageId,
                                                           @ApiParam(value = "指定代理id", required = false) @RequestParam(required = false) Long agentId,
                                                           @ApiParam(value = "排序方式 1-升级 2-降序 默认降序", defaultValue = "2")
                                                           @RequestParam(required = false, defaultValue = "2") Integer orderBy
    ) {
        //Todo
        return Result.ok();
    }


    @PostMapping("/calculate/package/price")
    @EscapeLogin
    @ApiOperation(value = "计算代理购买套餐操作(续费|购买|升级)价格", tags = "代理后台相关")
    public Result<CalculateOrderPriceVo> calculateOrderPrice(@RequestBody ShopPackageOrderDto shopPackageOrderDto) {
        CalculateOrderPriceVo priceVo = sysShopPackageOrderService.calculateOrderPrice(shopPackageOrderDto);
        return Result.ok(priceVo);
    }

    @PostMapping("/package/buy")
    @EscapeLogin
    @ApiOperation(value = "代理为用户购买套餐操作(续费|购买|升级)", tags = "代理后台相关")
    public Result<BuyPackageOrderVo> optionPackage(@RequestBody @Validated({ShopPackageOrderDto.User.class}) ShopPackageOrderDto shopPackageOrderDto) {
        shopPackageOrderDto.setOrderSource(CommonConstants.NUMBER_FOUR);
        BuyPackageOrderVo orderVo = sysShopPackageOrderService.optionPackage(shopPackageOrderDto);
        return Result.ok(orderVo);
    }

    @GetMapping("/package/{orderId}")
    @EscapeLogin
    @ApiOperation(value = "查询指定订单是否支付成功,支付成功返回data=true", tags = "代理后台相关")
    public Result<Boolean> balanceRecharge(@PathVariable(name = "orderId") Long orderId) {
        Boolean flag = sysShopPackageOrderService.orderPayIfOk(orderId);
        return Result.ok(flag);
    }


    @PostMapping("/create/shop")
    @EscapeLogin
    @ApiOperation(value = "代理为商户创建店铺", tags = "代理后台相关")
    public Result<BuyPackageOrderVo> agentCreateShop(@RequestBody @Validated AgentCreateShopDto agentCreateShopDto) {
        shopInfoService.agentCreateShop(agentCreateShopDto);
        return Result.ok();
    }

    @GetMapping("/package/info")
    @EscapeLogin
    @ApiOperation(value = "代理后台获取套餐相关列表", tags = "代理后台相关")
    public Result<List<PackageInfoListVo>> packageInfoList() {
        List<PackageInfoListVo> list = sysShopPackageService.packageInfo();
        return Result.ok(list);
    }


}
