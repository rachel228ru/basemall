package com.medusa.gruul.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.model.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface IRemoteOrderService extends IService<Order> {


    /**
     * 订单详情
     *
     * @param orderId the order id
     * @return com.medusa.gruul.order.model.OrderVo order vo
     * @author alan
     * @date 2019 /11/25 22:32
     */
    OrderVo orderInfo(Long orderId);


    /**
     * 获取商品评价
     *
     * @param productIds the product ids
     * @return java.util.List<com.medusa.gruul.order.api.model.ProductRateVo> list
     * @author alan
     * @date 2020 /2/7 20:22
     */
    List<ProductRateVo> productRate(List<Long> productIds);

    /**
     * 获取未生成发货单的订单
     *
     * @param param the param
     * @return java.util.List<com.medusa.gruul.order.api.model.GetOrderListDto> not shipped order
     * @author alan
     * @date 2019 /12/12 21:11
     */
    List<GetOrderListDto> getNotShippedOrder(GetOrderListDtoByTimeScope param);

    /**
     * 根据订单Id获取指定订单详情
     *
     * @param param the param
     * @return java.util.List<com.medusa.gruul.order.api.model.GetOrderListDto> order list by ids
     * @author alan
     * @date 2019 /12/12 21:11
     */
    List<GetOrderListDto> getOrderListByIds(GetOrderListParam param);


    /***
     * @param orderDeliveryDtos the order delivery dtos
     * @return the int
     * 物流订单发货
     * @param:orderDeliveryDto
     * @return: java.util.List<com.medusa.gruul.order.api.model.AreaAssSalesVo>
     * @throws:
     * @author wangpeng
     * @version  : v1.0
     * @date 2020 /3/14 10:16 上午
     */
    int doLogisticsOrderDelivery(List<OrderDeliveryDto> orderDeliveryDtos);






    /**
     * 获取指定商品最后购买人的信息
     *
     * @param productIds the product ids
     * @return java.util.List<com.medusa.gruul.order.api.model.ProductBuyerVo> product last buyers
     * @author alan
     * @date 2020 /6/15 20:56
     */
    List<ProductBuyerVo> getProductLastBuyers(String[] productIds);

    /**
     * 等待发货的商品ID
     *
     * @param sendBillId the send bill id
     * @return java.util.List<java.lang.Long> list
     * @Author alan
     * @Date 2020 /6/20 11:09 AM
     */
    List<Long> waitSendProductList(String sendBillId);


    /**
     * 获取订单设置
     *
     * @return the order setting
     */
    OrderSetting getOrderSetting();

    /**
     * 根据订单ID获取订单详情
     *
     * @param orderIds the order ids
     * @return the list
     */
    List<OrderVo> orderInfoList(List<Long> orderIds);


    /**
     * 创建一个换货单
     *
     * @param dto the dto
     * @return the long
     */
    Long createExchangeOrder(ExchangeOrderDto dto);

    /**
     * 关闭订单
     *
     * @param afsId        the afs id
     * @param refundAmount the refund amount
     * @param type         the type
     * @param orderId      the order id
     */
    void closeOrder(Long afsId, BigDecimal refundAmount, Integer type, Long orderId);

    /**
     * 关闭换货单
     *
     * @param orderIds the order ids
     */
    void closeExchangeOrder(List<Long> orderIds);

    /**
     * 统计满减活动的销售额
     *
     * @param fullScaleIds the full scale ids
     * @return the list
     */
    List<ActivityStatisticsVo> fullScaleStatisticsByActivityId(Long[] fullScaleIds);

    /**
     * 查询是否有提货点的历史订单
     *
     * @param tenantId the tenant id
     * @param shopId   the shop id
     * @param pointId  the point id
     * @return the point order history
     */
    Boolean getPointOrderHistory(String tenantId, String shopId, String pointId);

    /**
     * 根据订单ID和SkuId查询指定商品有没有售后信息
     *
     * @param orderId      the order id
     * @param productSkuId the product sku id
     * @return the afs order
     */
    AfsOrder selectByOrderIdAndProductSkuId(Long orderId, Long productSkuId);

    /**
     * 检查订单是否有真正进行中的售后
     *
     * @param orderIds
     * @return java.lang.Boolean
     * @author alan
     * @date 2021/3/17 21:49
     */
    Boolean checkAfsOrder(List<Long> orderIds);

    /**
     * 更新订单到指定状态
     *
     * @param orderIds
     * @param statusEnum
     * @return java.lang.Boolean
     * @author alan
     * @date 2021/3/17 21:49
     */
    Boolean updateOrderStatus(List<Long> orderIds, OrderStatusEnum statusEnum);
}
