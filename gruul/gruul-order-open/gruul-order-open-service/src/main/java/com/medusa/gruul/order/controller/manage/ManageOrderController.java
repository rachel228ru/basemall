package com.medusa.gruul.order.controller.manage;


import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.ResultList;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.api.feign.RemoteLogisticsFeginService;
import com.medusa.gruul.order.api.constant.OrderShareSettingRedisKey;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.entity.OrderShareSetting;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.service.IManageOrderService;
import com.medusa.gruul.order.service.IOrderDeliveryService;
import com.medusa.gruul.order.service.IOrderSettingService;
import com.medusa.gruul.order.service.IOrderShareSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 城市合伙人PC端
 *
 * @author alan
 * @date 2019/11/13 20:20
 */
@Slf4j
@RestController
@RequestMapping("/manage")
@Api(tags = "城市合伙人PC端订单接口")
public class ManageOrderController {
    private static final String sname = "{sname}";
    @Resource
    private IManageOrderService orderService;
    @Resource
    private IOrderSettingService orderSettingService;
    @Resource
    private IOrderShareSettingService orderShareSettingService;
    @Resource
    private IOrderDeliveryService orderDeliveryService;
    @Resource
    private RemoteLogisticsFeginService remoteLogisticsFeginService;

    @ApiOperation(value = "商家查询订单", notes = "商家查询订单")
    @GetMapping("/search")
    public Result searchOrder(@Validated ManageSearchOrderDto dto) {
        PageUtils page = orderService.searchOrder(dto);
        return Result.ok(page);
    }

    @ApiOperation(value = "商家查询订单", notes = "商家查询订单")
    @GetMapping("/overview")
    public Result<ManageOrderOverviewVo> getOverview() {
        ManageOrderOverviewVo vo = orderService.getOverview();
        return Result.ok(vo);
    }

    @ApiOperation(value = "是否开启评论", notes = "是否开启评论")
    @GetMapping("/evaluate/setting")
    public Result getSetting() {
        OrderSetting orderSetting = orderSettingService.getOne(null);
        return Result.ok(orderSetting.getOpenEvaluate());
    }

    @ApiOperation(value = "设置是否开启评论", notes = "设置是否开启评论")
    @PutMapping("/evaluate/setting")
    public Result setting(@RequestParam(value = "openEvaluate") Boolean openEvaluate) {
        OrderSetting orderSetting = orderSettingService.getOne(null);
        orderSetting.setOpenEvaluate(openEvaluate);
        orderSettingService.updateById(orderSetting);
        return Result.ok();
    }


    /**
     * Gets order setting.
     *
     * @return the order setting
     */
    @ApiOperation("查询订单设置")
    @GetMapping("/setting")
    public Result<OrderSetting> getOrderSetting() {
        log.info(TenantContextHolder.getTenantId()+"tenantId");
        log.info(ShopContextHolder.getShopId()+"shopId");
        OrderSetting orderSetting = orderSettingService.getOne(null);
        return Result.ok(orderSetting);
    }

    /**
     * Sets order setting.
     *
     * @param setting the setting
     * @return the order setting
     */
    @ApiOperation("设置订单设置")
    @PostMapping("/setting")
    public Result<OrderSetting> setOrderSetting(@RequestBody @NotNull @Validated OrderSetting setting) {
        OrderSetting orderSetting = orderSettingService.update(setting);
        return Result.ok(orderSetting);
    }


    @ApiOperation(value = "获取当前分享晒单设置", notes = "晒单设置")
    @GetMapping("/share/setting")
    public Result getShareSetting() {
        OrderShareSetting orderShareSetting = orderShareSettingService.getOne(null);
        return Result.ok(orderShareSetting);
    }

    @ApiOperation(value = "设置分享晒单设置", notes = "晒单设置")
    @PutMapping("/share/setting")
    public Result setShareSetting(@RequestBody @Validated OrderShareSettingVo vo) {
        OrderShareSettingRedisKey redisKey = new OrderShareSettingRedisKey();
        //Todo 分享写死了name
        vo.setTitle("{sname}");
        vo.setBackground(" ");
        OrderShareSetting orderShareSetting = orderShareSettingService.getOne(null);
        if (vo.isDefaultValue()) {
            orderShareSetting.setBackground(OrderShareSetting.DEFAULT_BACKGROUND);
            orderShareSetting.setTitle(OrderShareSetting.DEFAULT_TITLE);
        } else {
            if (StrUtil.count(vo.getTitle(), sname) != 1) {
                throw new ServiceException("标题必须且只能包含一个收货人信息");
            }
            orderShareSetting.setBackground(vo.getBackground());
            orderShareSetting.setTitle(vo.getTitle());
        }
        orderShareSettingService.updateById(orderShareSetting);
        redisKey.del(ShopContextHolder.getShopId());
        return Result.ok(orderShareSetting);
    }



    @ApiOperation(value = "根据运单号查询物流轨迹", notes = "根据运单号查询物流轨迹")
    @GetMapping("/traces")
    public Result tracesInfo(@RequestParam(value = "tracesId") @NotNull String tracesId,
                             @RequestParam(value = "deliveryCode") @NotNull String deliveryCode) {
        Result logisticsExpressInfo = remoteLogisticsFeginService.getLogisticsExpressInfo(tracesId, deliveryCode);
        return Result.ok(logisticsExpressInfo.getData());
    }


    @ApiOperation(value = "商家查询订单详情", notes = "用户查询订单详情")
    @GetMapping("/info/{orderId}")
    public Result<OrderVo> orderInfo(@PathVariable(value = "orderId") @NotNull Long orderId) {
        OrderVo orderVo = orderService.orderInfo(orderId);
        return Result.ok(orderVo);
    }


    @ApiOperation(value = "商家批量关闭订单", notes = "商家批量关闭订单")
    @PutMapping("/close")
    public Result closeOrder(@RequestBody Long[] orderIds) {
        orderService.closeOrder(Arrays.asList(orderIds));
        return Result.ok();
    }

    @ApiOperation(value = "商家批量备注订单", notes = "商家批量备注订单")
    @PutMapping("/note")
    public Result noteOrder(@RequestBody Long[] orderIds, @RequestParam(value = "over") Boolean over,
                            @RequestParam(value = "note", required = false) String note) {
        orderService.noteOrder(Arrays.asList(orderIds), note, over);
        return Result.ok();
    }

    @ApiOperation(value = "商家后台修改收货地址", notes = "商家后台修改收货地址")
    @PutMapping("/receiver/address")
    public Result updateReceiverAddress(@RequestBody ReceiverAddressDto dto) {
        orderDeliveryService.updateReceiverAddress(dto);
        return Result.ok();
    }

    @ApiOperation(value = "商家移出发货单", notes = "商家移出发货单")
    @PutMapping("/remove/send_bill")
    public Result removeSendBill(@RequestBody Long[] orderIds) {
        orderService.removeSendBill(Arrays.asList(orderIds));
        return Result.ok();
    }

    @ApiOperation(value = "商家移出发货单", notes = "商家移出发货单")
    @PutMapping("/remove/send_bill/product")
    public Result removeSendBillByProductIds(@RequestBody Long[] productIds) {
        orderService.removeSendBillByProductIds(Arrays.asList(productIds));
        return Result.ok();
    }

    @ApiOperation(value = "商家查看评价列表", notes = "商家查看评价列表")
    @GetMapping("/evaluate/search")
    public Result searchOrderEvaluate(@Validated ManageSearchEvaluateDto dto) {
        PageUtils page = orderService.searchOrderEvaluate(dto);
        return Result.ok(page);
    }

    @ApiOperation(value = "商家设置评价为精选", notes = "商家设置评价为精选")
    @PutMapping("/choice/evaluate")
    public Result choiceEvaluate(@RequestBody Long[] ids) {
        orderService.choiceEvaluate(Arrays.asList(ids));
        return Result.ok();
    }

    @ApiOperation(value = "商家取消设置评价为精选", notes = "商家取消设置评价为精选")
    @PutMapping("/unChoice/evaluate")
    public Result unChoiceEvaluate(@RequestBody Long[] ids) {
        orderService.unChoiceEvaluate(Arrays.asList(ids));
        return Result.ok();
    }

    @ApiOperation(value = "商家回复评价", notes = "商家回复评价")
    @PutMapping("/reply/evaluate")
    public Result replyEvaluate(@RequestParam(value = "id") Long id, @RequestParam(value = "reply") String reply) {
        orderService.replyEvaluate(id, reply);
        return Result.ok();
    }

    @Deprecated
    @ApiOperation(value = "快递订单搜索", notes = "快递订单搜索")
    @GetMapping("/reply/searchDeliveryOrders")
    public Result searchLogisticsOrderList(ManageLogisticsOrderDto manageLogisticsOrderDto) {
        PageUtils pageUtils = orderService.searchLogisticsOrderList(manageLogisticsOrderDto);
        return Result.ok(pageUtils);
    }

    @ApiOperation(value = "快递订单搜索", notes = "快递订单")
    @GetMapping("/search/logistics")
    public Result searchLogisticsOrder(ManageSearchLogisticsOrderDto dto) {
        List<ManageOrderVo> list = orderService.searchLogisticsOrder(dto);
        return Result.ok(list);
    }

    @ApiOperation(value = "查询是否有待发货的快递订单", notes = "快递订单")
    @GetMapping("/logistics/wait/send")
    public Result hasWaitSend() {
        Integer hasWaitSend = orderService.countLogisticsWaitSend();
        return Result.ok(hasWaitSend);
    }

    @ApiOperation(value = "无需快递的物流发货", notes = "快递订单")
    @PutMapping("/logistics/send")
    public Result logisticsSend(@RequestBody ManageSearchLogisticsOrderDto dto) {
        orderService.logisticsSend(dto);
        return Result.ok();
    }
}
