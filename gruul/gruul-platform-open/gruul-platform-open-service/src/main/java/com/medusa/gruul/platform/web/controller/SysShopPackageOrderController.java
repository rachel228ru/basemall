package com.medusa.gruul.platform.web.controller;

import com.ijpay.alipay.AliPayApi;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.OrderOptionDto;
import com.medusa.gruul.platform.model.dto.ShopPackageOrderDto;
import com.medusa.gruul.platform.model.vo.BuyPackageOrderVo;
import com.medusa.gruul.platform.model.vo.CalculateOrderPriceVo;
import com.medusa.gruul.platform.model.vo.PackageOrderConsoleVo;
import com.medusa.gruul.platform.model.vo.PackageOrderVo;
import com.medusa.gruul.platform.service.ISysShopPackageOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * 店铺套餐订单表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/sys-shop-package-order")
@Api(tags = "套餐相关接口")
public class SysShopPackageOrderController {

    @Autowired
    private ISysShopPackageOrderService sysShopPackageOrderService;

    @PostMapping("/calculate/price")
    @EscapeLogin
    @ApiOperation(value = "计算用户套餐操作(续费|购买|升级)价格")
    public Result<CalculateOrderPriceVo> calculateOrderPrice(@RequestBody ShopPackageOrderDto shopPackageOrderDto) {
        CalculateOrderPriceVo priceVo = sysShopPackageOrderService.calculateOrderPrice(shopPackageOrderDto);
        return Result.ok(priceVo);
    }


    @PostMapping("/user/buy")
    @ApiOperation(value = "用户套餐操作(续费|购买|升级)")
    public Result<BuyPackageOrderVo> optionPackage(@RequestBody @Validated({ShopPackageOrderDto.User.class}) ShopPackageOrderDto shopPackageOrderDto) {
        shopPackageOrderDto.setOrderSource(CommonConstants.NUMBER_ZERO);
        BuyPackageOrderVo orderVo = sysShopPackageOrderService.optionPackage(shopPackageOrderDto);
        return Result.ok(orderVo);
    }

    @PostMapping("/admin/buy")
    @EscapeLogin
    @ApiOperation(value = "管理台(续费|购买|升级)")
    public Result adminBuy(@RequestBody @Validated({ShopPackageOrderDto.Admin.class}) ShopPackageOrderDto shopPackageOrderDto) {
        shopPackageOrderDto.setOrderSource(CommonConstants.NUMBER_ONE);
        sysShopPackageOrderService.adminBuy(shopPackageOrderDto);
        return Result.ok();
    }


    @GetMapping("/{orderId}")
    @EscapeLogin
    @ApiOperation(value = "查询指定订单是否支付成功,支付成功返回data=true")
    public Result<Boolean> balanceRecharge(@PathVariable(name = "orderId") Long orderId) {
        Boolean flag = sysShopPackageOrderService.orderPayIfOk(orderId);
        return Result.ok(flag);
    }


    @PostMapping("/notify/wx")
    @EscapeLogin
    @ApiOperation(value = "套餐支付微信回调", hidden = true)
    public String notifyWx(HttpServletRequest request) throws IOException {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        return sysShopPackageOrderService.notifyWx(xmlResult);
    }

    @PostMapping(value = "/notify/alipay")
    @EscapeLogin
    @ApiOperation(value = "套餐支付微信回调", hidden = true)
    public String notifyAlipay(HttpServletRequest request) {
        return sysShopPackageOrderService.notifyAlipay(AliPayApi.toMap(request));
    }


    @GetMapping("/order")
    @EscapeLogin
    @ApiOperation(value = "管理台分页查询订购订单")
    public Result<PageUtils<PackageOrderVo>> orders(
            @ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
            @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
            @ApiParam(value = "支付方式 0-所有 1:余额支付2:微信3:支付宝4:汇款支付") @RequestParam(required = false, defaultValue = "0") Integer payType,
            @ApiParam(value = "充值状态 0-所有 1:处理中2:已经完成 3:关闭") @RequestParam(required = false, defaultValue = "0") Integer status,
            @ApiParam(value = "开始时间") @RequestParam(required = false) String payStartTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) String payEndTime,
            @ApiParam(value = "手机号") @RequestParam(required = false) String phone,
            @ApiParam(value = "订单号") @RequestParam(required = false) String orderNum,
            @ApiParam(value = "订购模板") @RequestParam(required = false) Long templateId,
            @ApiParam(value = "商户昵称") @RequestParam(required = false) String nikeName,
            @ApiParam(value = "用户id") @RequestParam(required = false) Long userId
    ) {
        PageUtils<PackageOrderVo> record = sysShopPackageOrderService.orders(page, size, status, payStartTime, payEndTime, phone,
                orderNum, nikeName, payType, templateId,userId);
        return Result.ok(record);
    }


    @GetMapping("/console/order")
    @ApiOperation(value = "商户查询自己的套餐订单")
    public Result<PageUtils<PackageOrderConsoleVo>> consoleOrders(
            @ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
            @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
            @ApiParam(value = "套餐id", required = true) @RequestParam Long packageId,
            @ApiParam(value = "订单状态 0-所有 1-充值中 2-充值成功  3-订单关闭") @RequestParam(required = false, defaultValue = "0") Integer status,
            @ApiParam(value = "平台类型  1-系统管理员  2-商家") @RequestParam(required = false) Integer platfromType,
            @ApiParam(value = "开始时间") @RequestParam(required = false) String payStartTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) String payEndTime,
            @ApiParam(value = "查询入口  1-控制台  2-店铺商家中心")  Integer selectType
    ) {
        PageUtils<PackageOrderConsoleVo> record = sysShopPackageOrderService.consoleOrders(page, size, status, payStartTime, payEndTime, packageId,platfromType,selectType);
        return Result.ok(record);
    }


    @PutMapping("/order/option")
    @EscapeLogin
    @ApiOperation(value = "管理台套餐订单操作")
    public Result orderOption(@RequestBody OrderOptionDto dto) {
        sysShopPackageOrderService.orderOption(dto);
        return Result.ok();
    }

}
