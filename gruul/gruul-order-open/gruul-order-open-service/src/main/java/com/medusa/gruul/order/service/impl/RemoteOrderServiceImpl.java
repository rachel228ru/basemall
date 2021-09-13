package com.medusa.gruul.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.account.api.model.AccountInfoDto;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.order.api.constant.OrderQueueNameConstant;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import com.medusa.gruul.order.api.entity.OrderItem;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import com.medusa.gruul.order.api.enums.PayTypeEnum;
import com.medusa.gruul.order.api.model.*;
import com.medusa.gruul.order.mapper.*;
import com.medusa.gruul.order.mq.Sender;
import com.medusa.gruul.order.service.IRemoteOrderService;
import com.medusa.gruul.payment.api.feign.RemotePaymentService;
import com.medusa.gruul.payment.api.model.dto.RefundRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
@Slf4j
@Service
public class RemoteOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IRemoteOrderService {
    @Resource
    private OrderProductEvaluateMapper orderProductEvaluateMapper;
    @Resource
    private OrderEvaluateMapper orderEvaluateMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private AfsOrderMapper afsOrderMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderSettingMapper orderSettingMapper;
    @Resource
    private RemotePaymentService remotePaymentService;
    @Resource
    private RemoteMiniAccountService remoteMiniAccountService;
    @Resource
    private Sender sender;


    @Override
    public OrderVo orderInfo(Long orderId) {
        OrderVo vo = baseMapper.selectOrderVoById(orderId);
        vo.setProductTotalQuantity(vo.getOrderItemList().stream().mapToInt(OrderItem::getProductQuantity).sum());
        return vo;
    }

    @Override
    public List<ProductRateVo> productRate(List<Long> productIds) {
        return orderProductEvaluateMapper.selectProductRateByList(productIds);
    }

    @Override
    public List<GetOrderListDto> getNotShippedOrder(GetOrderListDtoByTimeScope param) {
        List<GetOrderListDto> dtoList = baseMapper.selectNotShippedOrder(param.getStart(), param.getEnd(),
                param.getTenantId(),
                param.getShopId());
        dtoList = dtoList.stream().filter(dto -> CollUtil.isEmpty(dto.getAfsOrderList())).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<GetOrderListDto> getOrderListByIds(GetOrderListParam param) {
        if (param.getOrderIds().isEmpty()) {
            return new ArrayList<>();
        }
        return baseMapper.selectOrderListByIds(param.getOrderIds(), param.getTenantId(), param.getShopId());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int doLogisticsOrderDelivery(List<OrderDeliveryDto> orderDeliveryDtos) {
        AtomicInteger res = new AtomicInteger(0);
        OrderSetting orderSetting = orderSettingMapper.selectOne(null);
        orderDeliveryDtos.forEach(orderDeliveryDto -> {
            OrderDto orderDto = baseMapper.selectOneById(orderDeliveryDto.getOrderId());
            if (null == orderDto) {
                throw new ServiceException(SystemCode.DATA_NOT_EXIST);
            }
            orderDto.setStatus(OrderStatusEnum.WAIT_FOR_PICKUP.getCode());
            orderDto.setUpdateTime(LocalDateTime.now());
            int i = baseMapper.updateOneById(orderDto);

            OrderDeliveryDto orderDeliveryDb = orderDeliveryMapper.selectOneByOrderId(orderDeliveryDto.getOrderId());
            if (null == orderDeliveryDb) {
                throw new ServiceException(SystemCode.DATA_NOT_EXIST);
            }
            orderDeliveryDto.setDeliveryCompany(orderDeliveryDto.getDeliveryCompany());
            orderDeliveryDto.setDeliverySn(orderDeliveryDto.getDeliverySn());
            orderDeliveryDto.setDeliveryCode(orderDeliveryDto.getDeliveryCode());
            orderDeliveryDto.setUpdateTime(LocalDateTime.now());
            orderDeliveryDto.setDeliveryTime(LocalDateTime.now());
            int i1 = orderDeliveryMapper.updateByParam(orderDeliveryDto);
            //查询会员持有的积分、收货地址
            AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(orderDto.getUserId(),
                    Collections.singletonList(4));
            OrderVo vo = orderMapper.selectOrderVoById(orderDto.getId());
            sender.sendDeliveryMessage(vo, accountInfoDto.getMiniAccountOauths().getOpenId());
            sender.sendShippedOrderMessage(vo);

            BaseOrderMessage baseOrderMessage = new BaseOrderMessage();
            baseOrderMessage.setOrderId(vo.getId());
            baseOrderMessage.setShopId(vo.getShopId());
            baseOrderMessage.setTenantId(vo.getTenantId());
            sender.sendAutoReceiptOrderMessage(baseOrderMessage,
                    orderSetting.getConfirmOvertime() * TimeConstants.ONE_DAY);
            res.set(i + i1);
        });
        return res.get();
    }


    @Override
    public List<ProductBuyerVo> getProductLastBuyers(String[] productIds) {
        List<ProductBuyerVo> vos = new ArrayList<>();
        for (String productId : productIds) {
            ProductBuyerVo vo = new ProductBuyerVo();
            vo.setProductId(productId);
            List<BuyerVo> buyerVos = baseMapper.getProductLastBuyers(productId);
            vo.setBuyerList(buyerVos);
            vos.add(vo);
        }
        return vos;
    }

    /**
     * waitSendProduct
     *
     * @return java.util.List<java.lang.Long>
     * @Author alan
     * @Date 2020/6/20 11:06 AM
     */
    @Override
    public List<Long> waitSendProductList(String sendBillId) {
        return baseMapper.waitSendProductList(sendBillId);
    }

    @Override
    public OrderSetting getOrderSetting() {
        return orderSettingMapper.selectOne(null);
    }


    @Override
    public List<OrderVo> orderInfoList(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return CollUtil.newArrayList();
        }
        List<OrderVo> vos = baseMapper.selectOrderVoListByIds(orderIds);
        return vos;
    }


    @Override
    public Long createExchangeOrder(ExchangeOrderDto dto) {
        OrderVo orderVo = baseMapper.selectOrderVoById(dto.getOrderId());
        //生成订单
        Order exchangeOrder = new Order();

        exchangeOrder.setId(IdWorker.getId());
        exchangeOrder.setUserId(orderVo.getUserId());
        exchangeOrder.setUserName(orderVo.getUserName());
        exchangeOrder.setUserAvatarUrl(orderVo.getUserAvatarUrl());
        exchangeOrder.setUserNote("");
        // Todo 补货单阉割
        exchangeOrder.setType(OrderTypeEnum.EXCHANGE);
        exchangeOrder.setTotalAmount(BigDecimal.ZERO);
        exchangeOrder.setDiscountsAmount(BigDecimal.ZERO);
        exchangeOrder.setPayAmount(BigDecimal.ZERO);
        exchangeOrder.setFreightAmount(BigDecimal.ZERO);
        exchangeOrder.setPromotionAmount(BigDecimal.ZERO);
        exchangeOrder.setCouponId(null);
        exchangeOrder.setCouponAmount(BigDecimal.ZERO);
        exchangeOrder.setFullScaleId(null);
        exchangeOrder.setFullScaleAmount(BigDecimal.ZERO);
        exchangeOrder.setPayType(PayTypeEnum.FREE);
        exchangeOrder.setTransactionId(null);
        exchangeOrder.setPayTime(LocalDateTime.now());
        exchangeOrder.setSourceType(orderVo.getSourceType());
        exchangeOrder.setStatus(OrderStatusEnum.WAIT_FOR_SEND);
        exchangeOrder.setNote("");
        exchangeOrder.setCustomForm(orderVo.getCustomForm());
        exchangeOrder.setRefundAmount(BigDecimal.ZERO);
        baseMapper.insert(exchangeOrder);


        //生成订单明细
        OrderItem orderItem =
                orderVo.getOrderItemList().stream()
                        .filter(o -> o.getProductSkuId().equals(dto.getProductSkuId()))
                        .findFirst().get();
        OrderItem exchangeOrderItem = new OrderItem();

        exchangeOrderItem.setOrderId(exchangeOrder.getId());
        exchangeOrderItem.setProductId(orderItem.getProductId());
        exchangeOrderItem.setProductPic(orderItem.getProductPic());
        exchangeOrderItem.setProductName(orderItem.getProductName());
        exchangeOrderItem.setProductSn(orderItem.getProductSn());
        exchangeOrderItem.setProductPrice(orderItem.getProductPrice());
        exchangeOrderItem.setProductOriginalPrice(orderItem.getProductOriginalPrice());
        exchangeOrderItem.setProductQuantity(dto.getProductQuantity());
        exchangeOrderItem.setProductSkuId(orderItem.getProductSkuId());
        exchangeOrderItem.setProductSkuCode(orderItem.getProductSkuCode());
        exchangeOrderItem.setPromotionAmount(BigDecimal.ZERO);
        exchangeOrderItem.setCouponAmount(BigDecimal.ZERO);
        exchangeOrderItem.setRealAmount(orderItem.getRealAmount());
        exchangeOrderItem.setSpecs(orderItem.getSpecs());
        exchangeOrderItem.setProviderId(orderItem.getProviderId());
        orderItemMapper.insert(exchangeOrderItem);

        //生成订物流信息
        OrderDelivery orderDelivery =
                orderVo.getOrderDelivery();
        OrderDelivery exchangeOrderDelivery = new OrderDelivery();
        exchangeOrderDelivery.setOrderId(exchangeOrder.getId());
        exchangeOrderDelivery.setDeliveryType(orderDelivery.getDeliveryType());
        exchangeOrderDelivery.setReceiverName(orderDelivery.getReceiverName());
        exchangeOrderDelivery.setReceiverPhone(orderDelivery.getReceiverPhone());
        exchangeOrderDelivery.setReceiverPostCode(orderDelivery.getReceiverPostCode());
        exchangeOrderDelivery.setReceiverProvince(orderDelivery.getReceiverProvince());
        exchangeOrderDelivery.setReceiverCity(orderDelivery.getReceiverCity());
        exchangeOrderDelivery.setReceiverRegion(orderDelivery.getReceiverRegion());
        exchangeOrderDelivery.setReceiverDetailAddress(orderDelivery.getReceiverDetailAddress());
        exchangeOrderDelivery.setDeliveryTemplateId(orderDelivery.getDeliveryTemplateId());
        orderDeliveryMapper.insert(exchangeOrderDelivery);
        return exchangeOrder.getId();
    }

    @Override
    public void closeOrder(Long afsId, BigDecimal refundAmount, Integer type, Long orderId) {
        OrderVo order = baseMapper.selectOrderVoById(orderId);
        log.info("订单数据为" + JSON.toJSONString(order));
        AfsOrder applyOrderItem = afsOrderMapper.selectById(afsId);
        order.setRefundAmount(NumberUtil.add(order.getRefundAmount(), refundAmount));
        if (NumberUtil.isLess(order.getPayAmount(), order.getRefundAmount())) {
            throw new ServiceException("退款金额不得大于支付金额");
        }
        Integer refundQuantity = 0;
        for (OrderItemVo orderItemVo : order.getOrderItemList()) {
            log.info("orderItemVo is {}", JSONUtil.toJsonStr(orderItemVo));
            log.info("applyOrderItem is {}", JSONUtil.toJsonStr(applyOrderItem));
            if (orderItemVo.getProductSkuId().equals(applyOrderItem.getProductSkuId())) {
                orderItemVo.setRefundAmount(refundAmount);
                orderItemMapper.updateById(orderItemVo);
                refundQuantity = refundQuantity + orderItemVo.getProductQuantity();
            }
        }
        order.setRefundQuantity(refundQuantity);
        //退费用
        payRefund(refundAmount, order);
        //Todo 退积分
        //针于退款，只要没钱了就关闭订单
        if (type.equals(AfsOrderTypeEnum.REFUND.getCode()) || type.equals(AfsOrderTypeEnum.RETURN_REFUND.getCode())) {
            BigDecimal payAmount = NumberUtil.sub(order.getPayAmount(), order.getFreightAmount());
            if (NumberUtil.equals(refundAmount, payAmount) || NumberUtil.equals(refundAmount,
                    order.getPayAmount()) || NumberUtil.equals(order.getRefundAmount(), order.getPayAmount())) {
                sender.sendCloseOrderMessage(order);
                order.setStatus(OrderStatusEnum.REFUNDED);
                order.setCloseTime(LocalDateTime.now());
            }
            baseMapper.updateById(order);

        }
        sender.sendReturnOrderMessage(order);
        sender.sendRefundSuccess(order);
        //针于换货，没有正在进行的售后就关闭
        // TODO: 2021/8/27 换货？？
//        if (type.equals(AfsOrderTypeEnum.EXCHANGE.getCode())) {
//            List<AfsOrder> afsOrderList = afsOrderMapper.selectProgressByOrderId(order.getId());
//            afsOrderList =
//                    afsOrderList.stream().filter(afsOrder -> !afsOrder.getId().equals(afsId)).collect(Collectors.toList());
//            boolean hasNotApplyOrderItem =
//                    order.getOrderItemList().stream().filter(orderItemVo -> ObjectUtil.isNotNull(orderItemVo.getRefundAmount())).anyMatch(orderItemVo -> NumberUtil.isGreater(orderItemVo.getRefundAmount(), BigDecimal.ZERO));
//            if (afsOrderList.isEmpty() || !hasNotApplyOrderItem) {
//                sender.sendCloseOrderMessage(order);
//                order.setStatus(OrderStatusEnum.EXCHANGE_SUCCESS_CLOSE);
//                order.setCloseTime(LocalDateTime.now());
//            }
//            baseMapper.updateById(order);
//
//        }

    }


    private void payRefund(BigDecimal refundAmount, Order order) {
        if (NumberUtil.isGreater(refundAmount, BigDecimal.ZERO)) {
            switch (order.getPayType()) {
                case WECHAT:
                case FRIEND:
                    wechatPayRefund(refundAmount, order);
                    break;
                default:
                    break;
            }
        }
    }

    private void wechatPayRefund(BigDecimal refundAmount, Order order) {
        RefundRequestDto dto = new RefundRequestDto();
        try {
            dto.setTenantId(order.getTenantId());
            dto.setOrderId(order.getTransactionId());
            dto.setRouteKey(OrderQueueNameConstant.REFUND_NOTIFY);
            //没有发货的订单退掉运费
            if (order.getStatus().equals(OrderStatusEnum.WAIT_FOR_SEND)) {
                refundAmount = refundAmount.add(order.getFreightAmount());
            }
            dto.setTotalFee(NumberUtil.mul(refundAmount, 100).intValue());
            Result result = remotePaymentService.payRefund(dto);
            log.info("微信退款,入参为: {}", JSONUtil.toJsonStr(dto));
            log.info("微信退款,出参为: {}", JSONUtil.toJsonStr(result));
        } catch (Exception e) {
            log.error("调用退款接口失败：入参为：{}", JSONUtil.toJsonStr(dto));
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void closeExchangeOrder(List<Long> orderIds) {
        if (CollUtil.isNotEmpty(orderIds)) {
            baseMapper.closeExchangeOrder(orderIds, OrderStatusEnum.EXCHANGE_CANCEL_CLOSE);
        }
    }


    @Override
    public List<ActivityStatisticsVo> fullScaleStatisticsByActivityId(Long[] fullScaleIds) {
        List<Long> fullScaleIdList = Arrays.asList(fullScaleIds);
        if (fullScaleIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<ActivityStatisticsVo> vos = orderMapper.selectListByFullScaleIds(fullScaleIdList);

        return vos;
    }

    @Override
    public Boolean getPointOrderHistory(String tenantId, String shopId, String pointId) {
        int orderNumber = orderMapper.getPointOrderHistory(tenantId, shopId, pointId);
        return orderNumber > 0;
    }

    @Override
    public AfsOrder selectByOrderIdAndProductSkuId(Long orderId, Long productSkuId) {
        AfsOrder afsOrder = afsOrderMapper.selectByOrderIdAndProductSkuId(orderId, productSkuId);
        if (ObjectUtil.isNotNull(afsOrder)) {
            return afsOrder;
        } else {
            return null;
        }
    }

    @Override
    public Boolean checkAfsOrder(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return Boolean.TRUE;
        }
        Integer size = afsOrderMapper.selectCountProgressByOrderIds(orderIds);
        return size < 1;
    }

    @Override
    public Boolean updateOrderStatus(List<Long> orderIds, OrderStatusEnum statusEnum) {
        if (orderIds.isEmpty()) {
            return Boolean.TRUE;
        }
        Order order = new Order();
        order.setStatus(statusEnum);
        baseMapper.update(order, new LambdaQueryWrapper<Order>().in(Order::getId, orderIds));
        return Boolean.TRUE;
    }
}
