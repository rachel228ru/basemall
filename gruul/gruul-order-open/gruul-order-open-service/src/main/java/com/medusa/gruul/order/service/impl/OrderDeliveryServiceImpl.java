package com.medusa.gruul.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.account.api.model.AccountInfoDto;
import com.medusa.gruul.afs.api.feign.RemoteAfsService;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import com.medusa.gruul.order.api.model.BaseOrderMessage;
import com.medusa.gruul.order.api.model.GenerateSendBillOrderMessage;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.order.api.model.ReceiptSendBillOrderMessage;
import com.medusa.gruul.order.mapper.OrderDeliveryMapper;
import com.medusa.gruul.order.mapper.OrderMapper;
import com.medusa.gruul.order.mapper.OrderSettingMapper;
import com.medusa.gruul.order.model.ReceiverAddressDto;
import com.medusa.gruul.order.mq.Sender;
import com.medusa.gruul.order.service.IOrderDeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单物流信息 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
@Slf4j
@Service
public class OrderDeliveryServiceImpl extends ServiceImpl<OrderDeliveryMapper, OrderDelivery> implements IOrderDeliveryService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderSettingMapper orderSettingMapper;
    @Resource
    private RemoteMiniAccountService remoteMiniAccountService;
    @Resource
    private Sender sender;
    @Resource
    private RemoteAfsService remoteAfsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShipping(GenerateSendBillOrderMessage message) {
        List<OrderDelivery> orderDeliveryList = (List<OrderDelivery>) this.listByIds(message.getOrderIds());
        for (OrderDelivery orderDelivery : orderDeliveryList) {
            String name = message.getName().replace("年", "-").replace("月", "-").replace("日", " ");
            orderDelivery.setDeliveryCompany(message.getDeliveryCompany());
            orderDelivery.setDeliverySn(message.getDeliverySn());
            if (message.getStatus().equals(2)) {
                orderDelivery.setDeliveryTime(LocalDateTime.now());
            }
        }
        //发货单的状态 1->新增；2->发货;
        if (message.getStatus().equals(2)) {
            for (Long orderId : message.getOrderIds()) {
                OrderVo vo = orderMapper.selectOrderVoById(orderId);
                if (vo.getStatus().equals(OrderStatusEnum.WAIT_FOR_SEND)) {
                    //查询会员持有的积分、收货地址
                    //发货单的类型 1->直配发货单；2->物流发货单;
                    log.error("收到物流发货消息{}", message.toString());
                    vo.setStatus(OrderStatusEnum.WAIT_FOR_PICKUP);
                    orderMapper.updateById(vo);
                    AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(vo.getUserId(),
                            Collections.singletonList(4));
                    if (ObjectUtil.isNotNull(accountInfoDto)) {
                        sender.sendDeliveryMessage(vo,
                                accountInfoDto.getMiniAccountOauths().getOpenId());
                    } else {
                        log.error("用户{} OpenId信息为空", vo.getUserId());
                    }
                    sender.sendShippedOrderMessage(vo);
                }
            }
        }
        if (!orderDeliveryList.isEmpty()) {
            this.updateBatchById(orderDeliveryList);
        }
    }


    @Override
    public void receiptSendBill(ReceiptSendBillOrderMessage message) {
        Order order = new Order();
        order.setStatus(OrderStatusEnum.WAIT_FOR_PICKUP);
        orderMapper.update(order,
                new LambdaQueryWrapper<Order>()
                        .in(Order::getId, message.getOrderIds())
                        .eq(Order::getStatus, OrderStatusEnum.SHIPPED));
        //提货点补货单直接设置为完成状态
        orderMapper.update(order,
                new LambdaQueryWrapper<Order>()
                        .in(Order::getId, message.getOrderIds())
                        .eq(Order::getType, OrderTypeEnum.REPLENISH.getCode())
                        .eq(Order::getStatus, OrderStatusEnum.COMPLETE));

        OrderSetting orderSetting = orderSettingMapper.selectOne(null);
        List<OrderVo> orderVos = orderMapper.selectOrderVoListByIds(message.getOrderIds());

        for (OrderVo vo : orderVos) {
            BaseOrderMessage baseOrderMessage = new BaseOrderMessage();
            baseOrderMessage.setOrderId(vo.getId());
            baseOrderMessage.setShopId(vo.getShopId());
            baseOrderMessage.setTenantId(vo.getTenantId());
            sender.sendAutoReceiptOrderMessage(baseOrderMessage,
                    orderSetting.getConfirmOvertime() * TimeConstants.ONE_DAY);
            sender.receiptSendBillOrderMessage(vo);
        }
    }

    @Override
    public void updateReceiverAddress(ReceiverAddressDto dto) {
        OrderDelivery orderDelivery = baseMapper.selectById(dto.getOrderId());
        if (ObjectUtil.isNull(orderDelivery)) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        Order order = orderMapper.selectById(dto.getOrderId());
        if (OrderStatusEnum.canChangeReceiverAddress(order.getStatus())) {
            orderDelivery.setReceiverProvince(dto.getReceiverProvince());
            orderDelivery.setReceiverCity(dto.getReceiverCity());
            orderDelivery.setReceiverRegion(dto.getReceiverRegion());
            orderDelivery.setReceiverDetailAddress(dto.getReceiverDetailAddress());
            orderDelivery.setReceiverName(dto.getReceiverName());
            orderDelivery.setReceiverPhone(dto.getReceiverPhone());
            orderDelivery.setReceiverPostCode(dto.getReceiverPostCode());
            baseMapper.updateById(orderDelivery);
        } else {
            throw new ServiceException("当前状态不允许修改收货地址");

        }
    }
}
