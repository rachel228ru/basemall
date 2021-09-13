package com.medusa.gruul.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.account.api.model.AccountInfoDto;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import com.medusa.gruul.order.api.entity.OrderProductEvaluate;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.model.BaseOrderMessage;
import com.medusa.gruul.order.api.model.OrderItemVo;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.order.api.model.RemoveSendBillOrderMessage;
import com.medusa.gruul.order.mapper.*;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.mq.Sender;
import com.medusa.gruul.order.service.IManageOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class ManageOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IManageOrderService {

    @Resource
    private Sender sender;
    @Resource
    private MiniOrderServiceImpl miniOrderService;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderEvaluateMapper orderEvaluateMapper;
    @Resource
    private OrderProductEvaluateMapper orderProductEvaluateMapper;
    @Resource
    private RemoteMiniAccountService remoteMiniAccountService;
    @Resource
    private OrderSettingMapper orderSettingMapper;

    @Override
    public PageUtils searchOrder(ManageSearchOrderDto dto) {
        List<Integer> orderStatusList = new ArrayList<>(4);
        //订单状态 -1：所有订单, 0.待付款（待买家付款）, 1.待发货（买家已付款）, 2.配送中（卖家已发货）, 3.待提货（商家直配已到达提货点或物流订单已发货）, 4.已完成（用户已经签收）, 5.已关闭
        switch (dto.getOrderStatus()) {
            case -1:
                if (ObjectUtil.isNotNull(dto.getSendBillId()) && dto.getSendBillId() == -1) {
                    orderStatusList.add(OrderStatusEnum.WAIT_FOR_SEND.getCode());
                } else {
                    orderStatusList.clear();
                }
                break;
            case 0:
                orderStatusList.add(OrderStatusEnum.WAIT_FOR_PAY.getCode());
                break;
            case 1:
                orderStatusList.add(OrderStatusEnum.WAIT_FOR_SEND.getCode());
                break;
            case 2:
                orderStatusList.add(OrderStatusEnum.SHIPPED.getCode());
                break;
            case 3:
                orderStatusList.add(OrderStatusEnum.WAIT_FOR_PICKUP.getCode());
                break;
            case 4:
                orderStatusList.add(OrderStatusEnum.WAIT_FOR_COMMENT.getCode());
                orderStatusList.add(OrderStatusEnum.COMPLETE.getCode());
                break;
            case 5:
                orderStatusList.add(OrderStatusEnum.BUYER_PAY_TIMEOUT_CLOSE.getCode());
                orderStatusList.add(OrderStatusEnum.BUYER_CANCEL_CLOSE.getCode());
                orderStatusList.add(OrderStatusEnum.SELLER_CANCEL_CLOSE.getCode());
                orderStatusList.add(OrderStatusEnum.EXCHANGE_SUCCESS_CLOSE.getCode());
                break;
            default:
                break;
        }
        String startDate;
        String endDate;
        //近一个月->0; 近三个月->1; 全部->2;
        switch (dto.getQuicklyDate()) {
            case 1:
                startDate = DateUtil.offsetMonth(DateUtil.date(), -3).toDateStr();
                endDate = DateUtil.date().toDateStr();
                break;
            case 2:
                startDate = null;
                endDate = null;
                break;
            case 0:
            default:
                startDate = DateUtil.offsetMonth(DateUtil.date(), -1).toDateStr();
                endDate = DateUtil.date().toDateStr();
                break;
        }
        Page<ManageOrderVo> page = baseMapper.searchManageOrderVoPage(new Page(dto.getCurrent(), dto.getSize()),
                orderStatusList, startDate, endDate, dto);

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeOrder(List<Long> orderIds) {
        List<Order> orders = baseMapper.selectBatchIds(orderIds);
        for (Order order : orders) {
            if (!OrderStatusEnum.canCancel(order.getStatus())) {
                throw new ServiceException("当前状态不能取消此订单");
            }
        }
        for (Order order : orders) {
            miniOrderService.cancelOrder(order.getId(), order, OrderStatusEnum.SELLER_CANCEL_CLOSE);
        }
    }

    @Override
    public void noteOrder(List<Long> orderIds, String note, Boolean isOver) {
        List<Order> orders = baseMapper.selectBatchIds(orderIds);
        for (Order order : orders) {
            StringBuffer sb = new StringBuffer();
            if (StrUtil.isBlank(note)) {
                note = "";
            }
            if (!isOver) {
                if(StrUtil.isNotBlank(order.getNote())){
                    order.setNote(sb.append(order.getNote()).append(StrUtil.CRLF).append(note).toString());
                }else {
                    order.setNote(note);
                }
            } else {
                order.setNote(note);
            }
        }
        this.updateBatchById(orders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSendBill(List<Long> orderIds) {
        List<OrderDelivery> orders = orderDeliveryMapper.selectBatchIds(orderIds);
        for (OrderDelivery order : orders) {
            sender.sendRemoveSendBillOrderMessage(new RemoveSendBillOrderMessage(
                    order.getOrderId(), TenantContextHolder.getTenantId()));
        }
        for (OrderDelivery order : orders) {
            order.setDeliverySn(null);
            order.setDeliveryCompany(null);
            order.setDeliveryTime(null);
            orderDeliveryMapper.updateById(order);
        }

    }

    @Override
    public PageUtils searchOrderEvaluate(ManageSearchEvaluateDto dto) {
        Page<ManageEvaluateVo> page = orderEvaluateMapper.searchOrderEvaluate(new Page(dto.getCurrent(),
                dto.getSize()), dto);
        for (ManageEvaluateVo record : page.getRecords()) {
            record.setRate(record.getShopRate());
        }
        return new PageUtils(page);
    }

    @Override
    public void choiceEvaluate(List<Long> ids) {
        List<OrderProductEvaluate> productEvaluateList = orderProductEvaluateMapper.selectBatchIds(ids);
        for (OrderProductEvaluate productEvaluate : productEvaluateList) {
            productEvaluate.setChoice(true);
            orderProductEvaluateMapper.updateById(productEvaluate);
        }
    }

    @Override
    public void replyEvaluate(Long id, String reply) {
        OrderProductEvaluate productEvaluate = orderProductEvaluateMapper.selectById(id);
        productEvaluate.setReply(reply);
        orderProductEvaluateMapper.updateById(productEvaluate);
    }

    @Override
    public OrderVo orderInfo(Long orderId) {
        return baseMapper.selectOrderVoById(orderId);
    }

    @Override
    public void unChoiceEvaluate(List<Long> ids) {
        List<OrderProductEvaluate> productEvaluateList = orderProductEvaluateMapper.selectBatchIds(ids);
        for (OrderProductEvaluate productEvaluate : productEvaluateList) {
            if (productEvaluate.getChoice()) {
                productEvaluate.setChoice(false);
                orderProductEvaluateMapper.updateById(productEvaluate);
            }
        }
    }

    @Override
    public PageUtils searchLogisticsOrderList(ManageLogisticsOrderDto dto) {
        Page<ManageDeliveryOrderVo> page = baseMapper.searchLogisticsOrderList(new Page(dto.getCurrent(),
                dto.getSize()), dto);
        return new PageUtils(page);

    }

    @Override
    public ManageOrderOverviewVo getOverview() {
        ManageOrderOverviewVo vo = new ManageOrderOverviewVo();
        Integer waitForPay = baseMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus,
                OrderStatusEnum.WAIT_FOR_PAY));
        Integer waitForSend = baseMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus,
                OrderStatusEnum.WAIT_FOR_SEND));
        Integer shipped = baseMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus,
                OrderStatusEnum.SHIPPED));
        Integer waitForPickup = baseMapper.selectCount(new LambdaQueryWrapper<Order>().eq(Order::getStatus,
                OrderStatusEnum.WAIT_FOR_PICKUP));
        vo.setWaitForPay(waitForPay);
        vo.setWaitForSend(waitForSend);
        vo.setShipped(shipped);
        vo.setWaitForPickup(waitForPickup);
        return vo;
    }

    @Override
    public List<ManageOrderVo> searchLogisticsOrder(ManageSearchLogisticsOrderDto dto) {
        List<Long> orderIds = new ArrayList<>();
        if (StrUtil.isNotBlank(dto.getOrderIds())) {
            orderIds =
                    Arrays.stream(dto.getOrderIds().split(StrUtil.COMMA)).map(Long::parseLong).collect(Collectors.toList());
        }

        List<ManageOrderVo> list = baseMapper.searchLogisticsOrder(orderIds);
        return list;
    }

    @Override
    public Integer countLogisticsWaitSend() {
        Integer waitForSend = baseMapper.countLogisticsWaitSend();
        return waitForSend;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logisticsSend(ManageSearchLogisticsOrderDto dto) {
        List<Long> orderIds =
                Arrays.stream(dto.getOrderIds().split(StrUtil.COMMA)).map(Long::parseLong).collect(Collectors.toList());
        OrderSetting orderSetting = orderSettingMapper.selectOne(null);
        OrderDelivery orderDelivery = new OrderDelivery();
        orderDelivery.setDeliveryCompany("无");
        orderDelivery.setDeliverySn("无");
        orderDelivery.setDeliveryTime(LocalDateTime.now());
        orderDeliveryMapper.update(orderDelivery, new LambdaQueryWrapper<OrderDelivery>().in(OrderDelivery::getOrderId,
                orderIds));
        Order order = new Order();
        order.setStatus(OrderStatusEnum.WAIT_FOR_PICKUP);
        baseMapper.update(order, new LambdaQueryWrapper<Order>().in(Order::getId, orderIds));

        for (Long orderId : orderIds) {
            OrderVo vo = baseMapper.selectOrderVoById(orderId);
            //查询会员持有的积分、收货地址
            AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(vo.getUserId(), Arrays.asList(4));
            sender.sendDeliveryMessage(vo, accountInfoDto.getMiniAccountOauths().getOpenId());
            BaseOrderMessage baseOrderMessage = new BaseOrderMessage();
            baseOrderMessage.setOrderId(vo.getId());
            baseOrderMessage.setShopId(vo.getShopId());
            baseOrderMessage.setTenantId(vo.getTenantId());
            sender.sendAutoReceiptOrderMessage(baseOrderMessage,
                    orderSetting.getConfirmOvertime() * TimeConstants.ONE_DAY);
        }
    }

    @Override
    public void removeSendBillByProductIds(List<Long> productIds) {
        if (productIds.isEmpty()) {
            return;
        }
        List<Long> orderIds = baseMapper.selectShippedOrderByProductIds(productIds);
        if (orderIds.isEmpty()) {
            return;
        } else {
            this.removeSendBill(orderIds);
        }
    }


}
