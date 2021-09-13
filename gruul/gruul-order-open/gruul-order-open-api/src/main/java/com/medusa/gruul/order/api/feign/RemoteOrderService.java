package com.medusa.gruul.order.api.feign;

import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.model.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


/**
 * The interface Remote order service.
 * <p>
 * RemoteSysOssService.java
 *
 * @author alan
 * @date 2019 /7/17 20:44
 */
@FeignClient(value = "order-open")
@ApiIgnore
public interface RemoteOrderService {


    /**
     * 订单详情,该方法使用Feign默认租户信息，不包含默认查询全部
     *
     * @param orderId the order id
     * @return com.medusa.gruul.common.core.util.Result<com.medusa.gruul.order.api.entity.Order>   order vo
     * @Author alan
     * @Date 2019 /9/9 23:06
     */
    @GetMapping("/remote/info")
    OrderVo orderInfo(@RequestParam(value = "orderId") @NotNull Long orderId);


    /**
     * 获取订单设置
     *
     * @param
     * @return com.medusa.gruul.order.api.entity.OrderSetting order setting
     * @author alan
     * @date 2020 /8/17 22:45
     */
    @GetMapping("/remote/setting")
    OrderSetting getOrderSetting();

    /**
     * Product rate list.该方法使用Feign默认租户信息，不包含默认查询全部
     *
     * @param productIds the product ids
     * @return the list 获取商品评价
     * @author alan
     * @date 2020 /2/7 20:12
     */
    @GetMapping("/remote/product/rate")
    List<ProductRateVo> productRate(@RequestParam(value = "productIds") @NotNull List<Long> productIds);

    /**
     * 获取订单列表
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param tenantId 租户id
     * @param shopId   城市合伙人id
     * @return java.util.List<com.medusa.gruul.order.api.model.GetOrderListDto>   not shipped order
     * @author alan ，zhaozheng
     * @date 2019 /12/19 12:51
     */
    @GetMapping(value = "/remote/get/not-shipped/orders")
    List<GetOrderListDto> getNotShippedOrder(@RequestParam("start") String start, @RequestParam("end") String end,
                                             @RequestParam("tenantId") String tenantId,
                                             @RequestParam("shopId") String shopId);


    /**
     * 根据订单Id获取指定订单详情
     *
     * @param tenantId 租户id
     * @param shopId   城市合伙人id
     * @param orderIds 订单id
     * @return java.util.List<com.medusa.gruul.order.api.model.GetOrderListDto>   order list by ids
     * @author alan ，zhaozheng
     * @date 2019 /12/19 12:00
     */
    @GetMapping(value = "/remote/get/orders")
    List<GetOrderListDto> getOrderListByIds(@RequestParam("tenantId") String tenantId,
                                            @RequestParam("shopId") String shopId,
                                            @RequestParam("orderIds") Long[] orderIds);


    /**
     * 物流订单发货
     *
     * @param orderDeliveryDtoList the order delivery dto list
     * @return the int
     * @param:orderDeliveryDtoList
     * @return: int
     * @throws:
     * @author wangpeng
     * @version : v1.0
     * @date 2020 /3/14 2:10 下午
     */
    @ApiOperation("物流订单发货")
    @GetMapping("/remote/items/delivery")
    int doLogisticsOrderDelivery(@RequestParam(value = "orderDeliveryDtoList") String orderDeliveryDtoList);


    /**
     * 统计满减活动的销售额
     *
     * @param fullScaleIds the full scale ids
     * @return int list
     * @author alan
     * @date 2020 /3/23 22:01
     */
    @ApiOperation("统计满减活动的销售额")
    @GetMapping("/remote/fullScale/statistics")
    List<ActivityStatisticsVo> fullScaleStatistics(@RequestParam(value = "fullScaleIds") Long[] fullScaleIds);



    /**
     * 登陆设置成已播报
     *
     * @param tenantId the tenant id
     * @param shopId   the shop id
     * @return int order prompt by shop id
     * @author alan
     * @date 2020 /3/23 22:01
     */
    @ApiOperation("登陆设置成已播报")
    @GetMapping("/remote/setOrderPromptByShopId")
    int setOrderPromptByShopId(@RequestParam(value = "tenantId") String tenantId,
                               @RequestParam(value = "shopId") String shopId);




    /**
     * 批量获取商品的最后购买人信息
     *
     * @param productIds the product ids
     * @return java.util.List<com.medusa.gruul.order.api.model.ProductBuyerVo> product last buyers
     * @author alan
     * @date 2020 /5/7 23:15
     */
    @ApiOperation("批量获取商品的最后购买人信息")
    @GetMapping("/remote/product/last/buyers")
    List<ProductBuyerVo> getProductLastBuyers(@RequestParam(value = "productIds") String[] productIds);


    /**
     * 用户订单概况，需要在header带上租户信息、商铺信息
     *
     * @param userId the user id
     * @return com.medusa.gruul.order.api.model.OrderOverviewVo order overview vo
     * @author alan
     * @date 2020 /5/13 20:54
     */
    @GetMapping("/remote/overview")
    OrderOverviewVo orderOverview(@RequestParam(value = "userId") String userId);


    /**
     * 查询待发货订单包含的商品ID
     *
     * @param sendBillId the send bill id
     * @return java.util.List<java.lang.Long> list
     * @Author alan
     * @Date 2020 /6/20 11:32 AM
     */
    @ApiOperation(value = "查询待发货订单包含的商品ID")
    @GetMapping("/remote/product/wait/send")
    List<Long> waitSendProduct(@RequestParam(value = "sendBillId") String sendBillId);


    /**
     * 批量查询多个订单详情
     *
     * @param orderIds the order ids
     * @return com.medusa.gruul.common.core.util.Result<com.medusa.gruul.order.api.entity.Order> list
     * @Author alan
     * @Date 2019 /9/9 23:06
     */
    @GetMapping("/remote/info/list")
    List<OrderVo> orderInfoList(@RequestParam(value = "orderIds") List<Long> orderIds);

    /**
     * 创建换货单订单
     *
     * @param dto the dto
     * @return java.lang.Long long
     * @author alan
     * @date 2020 /8/23 12:56
     */
    @PostMapping("/remote/exchange/order")
    Long createExchangeOrder(@RequestBody ExchangeOrderDto dto);

    /**
     * 订单退款
     *
     * @param afsId        the afs id
     * @param refundAmount the refund amount
     * @param type         the type
     * @param orderId      the order id
     * @return java.lang.Long
     * @author alan
     * @date 2020 /8/23 12:56
     */
    @PutMapping("/remote/close")
    void closeOrder(
            @RequestParam(value = "afsId") Long afsId,
            @RequestParam(value = "refundAmount") BigDecimal refundAmount,
            @RequestParam(value = "type") Integer type,
            @RequestParam(value = "orderId") Long orderId);

    /**
     * 签收订单
     *
     * @param orderId the order id
     * @return java.lang.Long
     * @author alan
     * @date 2020 /8/23 12:56
     */
    @PutMapping("/remote/receipt")
    void receiptOrder(@RequestParam(value = "orderId") Long orderId);

    /**
     * 申请售后成功关闭之前的换货单
     *
     * @param orderIds the order ids
     * @return void
     * @author alan
     * @date 2020 /9/1 20:28
     */
    @PutMapping("/remote/close/exchange")
    void closeExchangeOrder(@RequestBody List<Long> orderIds);


    /**
     * 根据订单ID和SkuId查询指定商品有没有售后信息
     *
     * @param orderId      the order id
     * @param productSkuId the product sku id
     * @return void afs order
     * @author alan
     * @date 2020 /10/5 10:02
     */
    @GetMapping("/remote/{orderId}/{productSkuId}/afsOrder/")
    AfsOrder selectByOrderIdAndProductSkuId(@PathVariable(value = "orderId") Long orderId,
                                            @PathVariable(value = "productSkuId") Long productSkuId);


    /**
     * 检查订单是否有真正进行中的售后
     *
     * @param orderIds the order ids
     * @return the boolean
     */
    @GetMapping("/remote/checkAfsOrder")
    Boolean checkAfsOrder(@RequestParam(value = "orderIds") List<Long> orderIds);

    /**
     * Update order status boolean.
     *
     * @param orderIds   the order ids
     * @param statusEnum the status enum
     * @return the boolean
     */
    @EscapeLogin
    @PutMapping("/remote/order/status")
    Boolean updateOrderStatus(@RequestParam(value = "orderIds") List<Long> orderIds,
                              @RequestParam(value = "statusEnum") OrderStatusEnum statusEnum);
}
