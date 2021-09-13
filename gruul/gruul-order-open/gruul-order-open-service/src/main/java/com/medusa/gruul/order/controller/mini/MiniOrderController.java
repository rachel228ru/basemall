package com.medusa.gruul.order.controller.mini;


import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.goods.api.feign.RemoteGoodsService;
import com.medusa.gruul.logistics.model.dto.manager.CountCostDto;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.model.CreateOrderDto;
import com.medusa.gruul.order.api.model.OrderOverviewVo;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.service.IMiniOrderService;
import com.medusa.gruul.order.service.IOrderDeliveryService;
import com.medusa.gruul.order.service.IOrderProductEvaluateService;
import com.medusa.gruul.order.service.IOrderSettingService;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.vo.MiniMsgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 用户小程序端
 * </p>
 *
 * @author alan
 * @since 2019-09-02
 */
@Slf4j
@RestController
@RequestMapping("/mini")
@Api(tags = "用户小程序端订单接口")
public class MiniOrderController {
    @Resource
    private IMiniOrderService miniOrderService;
    @Resource
    private IOrderSettingService orderSettingService;

    @Resource
    private IOrderDeliveryService orderDeliveryService;
    @Resource
    private IOrderProductEvaluateService orderEvaluateService;
    @Resource
    private RemoteMiniInfoService remoteMiniInfoService;


    @ApiOperation("获取结算页信息")
    @PostMapping("/confirm_order")
    public Result<ConfirmOrderVo> getConfirmOrder(@RequestBody @Valid ConfirmOrderDto dto) {
        ConfirmOrderVo confirmOrderVo = miniOrderService.getConfirmOrder(dto);
        return Result.ok(confirmOrderVo);
    }


    @ApiOperation("获取运费")
    @PostMapping("/freightAmount")
    public Result<CountCostDto> getFreightAmount(@RequestBody GetCostDto dto) {
        CountCostDto result = miniOrderService.getFreightAmount(dto);
        return Result.ok(result);
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/create")
    public Result<String> createOrder(@RequestBody @Valid CreateOrderDto createOrderDto) {
        String orderId = miniOrderService.preCheckOrder(createOrderDto);
        return Result.ok(orderId);
    }


    @ApiOperation(value = "查询订单创建情况", notes = "-1->已售罄;0->排队中;order->成功;")
    @PostMapping("/result")
    public Result getOrderResult(@RequestBody GroupOrderResultDto dto) {
        Order order = miniOrderService.getById(dto.getOrderId());
        if (order != null) {
            return Result.ok(order);
        } else {
            Integer result = miniOrderService.checkOrder(dto);
            return Result.ok(result);
        }
    }

    @ApiOperation(value = "用户订单概况", notes = "用户订单概况")
    @GetMapping("/overview")
    public Result orderOverview() {
        OrderOverviewVo orderOverviewVo = miniOrderService.orderOverview();
        return Result.ok(orderOverviewVo);
    }


    @ApiOperation(value = "用户查询订单", notes = "用户查询订单")
    @GetMapping("/search")
    public Result<PageUtils<ApiOrderVo>> searchOrder(ApiSearchOrderDto dto) {
        PageUtils<ApiOrderVo> page = miniOrderService.searchOrder(dto);
        return Result.ok(page);
    }


    @ApiOperation(value = "用户查询订单详情", notes = "用户查询订单详情")
    @GetMapping("/info/{orderId}")
    public Result<OrderVo> orderInfo(@PathVariable(value = "orderId") @NotNull Long orderId) {
        OrderVo orderVo = miniOrderService.orderInfo(orderId);
        return Result.ok(orderVo);
    }

    @ApiOperation(value = "小程序查询晒单内容", notes = "晒单设置")
    @GetMapping("/share/{orderId}")
    public Result<OrderShareInfo> orderShareInfo(@PathVariable(value = "orderId") @NotNull Long orderId) {
        OrderShareInfo orderShareInfo = miniOrderService.orderShareInfo(orderId);
        return Result.ok(orderShareInfo);
    }


    @ApiOperation(value = "用户取消订单", notes = "用户取消订单")
    @PutMapping("/cancel/{orderId}")
    public Result cancelOrder(@PathVariable(value = "orderId") @NotNull Long orderId) {
        miniOrderService.cancelOrder(orderId);
        return Result.ok();

    }

    @ApiOperation(value = "用户支付订单", notes = "用户支付订单")
    @PutMapping("/pay/{orderId}")
    public Result payOrder(@PathVariable(value = "orderId") @NotNull Long orderId,
                           @RequestParam(value = "userBalance") Boolean userBalance, HttpServletRequest request) {
        return Result.ok(miniOrderService.payOrder(orderId, userBalance, request));

    }

    @ApiOperation(value = "小程序修改收货地址", notes = "小程序修改收货地址")
    @PutMapping("/receiver/address")
    public Result updateReceiverAddress(@RequestBody @Validated ReceiverAddressDto dto) {
        orderDeliveryService.updateReceiverAddress(dto);
        return Result.ok();
    }


    @ApiOperation(value = "用户确认收货", notes = "用户确认收货")
    @PutMapping("/receipt/{orderId}")
    public Result receiptOrder(@PathVariable(value = "orderId") @NotNull Long orderId) {
        miniOrderService.receiptOrder(orderId, false);
        return Result.ok();
    }


    @EscapeLogin
    @Deprecated
    @ApiOperation(value = "是否开启评论", notes = "是否开启评论")
    @GetMapping("/evaluate/setting")
    public Result evaluateSetting() {
        OrderSetting orderSetting = orderSettingService.getOne(null);
        return Result.ok(orderSetting.getOpenEvaluate());
    }

    @EscapeLogin
    @ApiOperation(value = "是否开启评论", notes = "是否开启评论")
    @GetMapping("/setting")
    public Result<SettingVo> setting() {
        SettingVo settingVo = new SettingVo();
        //获取当前线程中的租户id的店铺信息;getTemplateCodeEnum 获取当前店铺使用模板类型
        CurShopInfoDto tenantIdShopInfo = CurUserUtil.getTenantIdShopInfo();
        OrderSetting orderSetting = orderSettingService.getOne(null);
        settingVo.setOpenEvaluate(orderSetting.getOpenEvaluate());
        settingVo.setSetting(orderSetting);
        //获取当前店铺可使用的小程序订阅模板
        List<MiniMsgVo> miniMsgVoList = remoteMiniInfoService.getCurrentMiniMsg();
        settingVo.setMiniMsgVoList(miniMsgVoList);
        settingVo.setCurShopInfoDto(tenantIdShopInfo);
        return Result.ok(settingVo);
    }

    @ApiOperation(value = "用户评价订单", notes = "用户评价订单")
    @PostMapping("/evaluate")
    public Result evaluateOrder(@RequestBody @Validated ApiOrderEvaluateDto dto) {
        miniOrderService.evaluateOrder(dto);
        return Result.ok();
    }

    @ApiOperation(value = "用户查看评价列表", notes = "用户查看评价列表")
    @GetMapping("/evaluate")
    public Result<PageUtils<ApiOrderEvaluateVo>> evaluateOrder(@RequestHeader(value = "token") String token,
                                                               ApiSearchEvaluateDto dto) {
        log.info(token);
        PageUtils page = miniOrderService.searchOrderEvaluate(dto);
        return Result.ok(page);
    }

    @EscapeLogin
    @ApiOperation(value = "商品详情页评价概况", notes = "商品详情页评价概况")
    @GetMapping("/product/evaluate/overview")
    public Result<UserEvaluateVo> userEvaluateOverview(@RequestParam(value = "productId") @NotBlank String productId) {
        UserEvaluateVo vo = orderEvaluateService.userEvaluateOverview(productId);
        return Result.ok(vo);
    }

    @EscapeLogin
    @ApiOperation(value = "商品详情页评价", notes = "商品详情页评价")
    @GetMapping("/product/evaluate")
    public Result<PageUtils<ProductEvaluateVo>> productEvaluate(ApiSearchProductEvaluateDto dto) {
        PageUtils page = orderEvaluateService.productEvaluate(dto);
        return Result.ok(page);
    }


    @ApiOperation(value = "获取接龙活动最近的买家")
    @GetMapping("/solitaire/lately/buyer")
    public Result<List<LatelyBuyerVo>> solitaireLatelyBuyer(@RequestParam(value = "solitaireActivityId") @NotNull Integer solitaireActivityId) {
        //Todo 接龙阉割
        return Result.ok();
    }


    @ApiOperation(value = "获取接龙活动买家")
    @GetMapping("/solitaire/buyer")
    public Result<PageUtils<SolitaireBuyerVo>> solitaireBuyerPage() {
        //Todo 接龙阉割
        return Result.ok();
    }


    @ApiOperation(value = "获取我的接龙活动订单")
    @GetMapping("/solitaire/my")
    public Result<List<SolitaireBuyerVo>> mySolitaireOrder(@RequestParam(value = "solitaireActivityId") @NotNull Integer solitaireActivityId) {
        //Todo 接龙阉割
        return Result.ok();
    }
}
