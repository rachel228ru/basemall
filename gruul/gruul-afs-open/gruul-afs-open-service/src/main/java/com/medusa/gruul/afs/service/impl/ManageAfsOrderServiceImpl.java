package com.medusa.gruul.afs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.account.api.model.AccountInfoDto;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;
import com.medusa.gruul.afs.api.enums.AfsOrderCloseTypeEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
import com.medusa.gruul.afs.mapper.AfsOrderMapper;
import com.medusa.gruul.afs.model.*;
import com.medusa.gruul.afs.mp.Sender;
import com.medusa.gruul.afs.mp.model.AfsRemoveDeliverOrderMessage;
import com.medusa.gruul.afs.mp.model.BaseAfsOrderMessage;
import com.medusa.gruul.afs.service.IAfsNegotiateHistoryService;
import com.medusa.gruul.afs.service.IAfsOrderItemService;
import com.medusa.gruul.afs.service.IManageAfsOrderService;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.order.api.model.ExchangeOrderDto;
import com.medusa.gruul.order.api.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 售后工单 服务实现类
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@Slf4j
@Service
public class ManageAfsOrderServiceImpl extends ServiceImpl<AfsOrderMapper, AfsOrder> implements IManageAfsOrderService {

    @Resource
    private IAfsOrderItemService afsOrderItemService;
    @Resource
    private RemoteOrderService orderService;
    @Resource
    private Sender sender;
    @Resource
    private IAfsNegotiateHistoryService negotiateHistoryService;
    @Resource
    private RemoteMiniAccountService remoteMiniAccountService;


    /**
     * 商家拒绝
     *
     * @param dto
     * @return void
     * @author alan
     * @date 2021/3/17 22:33
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellerRefuse(SellerRefusalDto dto) {
        AfsOrder afsOrder = this.getById(dto.getAfsId());
        if (ObjectUtil.isNull(afsOrder)) {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }
        if (afsOrder.getStatus().waitSellerApprove()) {
            afsOrder.setStatus(AfsOrderStatusEnum.CLOSE);
            afsOrder.setCloseType(AfsOrderCloseTypeEnum.SELLER_REFUSE);
            afsOrder.setCloseTime(LocalDateTime.now());
            afsOrder.setRefusalReason(dto.getRefusalReason());
            afsOrder.setDeadline(null);
            this.updateById(afsOrder);
            negotiateHistoryService.sellerRefuse(afsOrder);
        } else {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }

    }

    /**
     * 商家同意
     *
     * @param afsId
     * @param isSystem
     * @return void
     * @author alan
     * @date 2021/3/17 22:34
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellerApprove(Long afsId, Boolean isSystem) {
        AfsOrder afsOrder = this.getById(afsId);
        if (ObjectUtil.isNull(afsOrder)) {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }
        if (afsOrder.getStatus().waitSellerApprove()) {
            //按照售后类型检查是否满足申请条件
            switch (afsOrder.getType()) {
                case REFUND:
                    //直接退款
                    userApplyRefund(afsOrder, isSystem);
                    break;
                case RETURN_REFUND:
                    //检查订单状态。如果未签收，直退款，已签收需先退货
                    userApplyReturn(afsOrder, isSystem, false);
                    break;
                default:
                    throw new ServiceException("不是有效的售后类型");
            }
            //关闭其他正在进行的售后单和换货单
            closeAfsOrderAndExChangeOrder(afsOrder);
        } else {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }
    }


    /**
     * 关闭订单和换货单
     *
     * @param afsOrder
     * @return void
     * @author alan
     * @date 2021/3/17 22:34
     */
    private void closeAfsOrderAndExChangeOrder(AfsOrder afsOrder) {
        //查询这个售后包含的其他订单
        List<AfsOrderItem> itemList =
                afsOrderItemService.list(new LambdaQueryWrapper<AfsOrderItem>()
                        .eq(AfsOrderItem::getAfsId, afsOrder.getId())
                );
        if (itemList.isEmpty()) {
            return;
        }
        //获得订单ID的集合
        String originalOrderIds =
                itemList.stream().map(AfsOrderItem::getOrderId).map(String::valueOf).collect(Collectors.joining(StrUtil.COMMA));
        //查询这些订单是否有其他正在进行的售后单
        List<AfsOrder> afsOrderList =
                baseMapper.selectProgressByOriginalOrderIdsAndIdNotIn(originalOrderIds,
                        afsOrder.getId());
        if (afsOrderList.isEmpty()) {
            return;
        }
        //如果上一步的售后单不为空
        for (AfsOrder order : afsOrderList) {
            //如果其他售后单的类型和当前售后单类型相同，或者其他售后单的列席不是换货单，关闭其他售后单，原因为重新换货
            if (order.getType().equals(afsOrder.getType())) {
                order.setStatus(AfsOrderStatusEnum.CLOSE);
                order.setCloseType(AfsOrderCloseTypeEnum.RE_APPLY);
                order.setCloseTime(LocalDateTime.now());
                order.setDeadline(null);
                baseMapper.updateById(order);
            }
        }
        itemList =
                afsOrderItemService.list(new LambdaQueryWrapper<AfsOrderItem>()
                        .in(AfsOrderItem::getAfsId, afsOrderList.stream()
                                .map(AfsOrder::getId)
                                .collect(Collectors.toSet())));

        List<Long> orderIds = itemList.stream().map(AfsOrderItem::getOrderId).collect(Collectors.toList());
        //关闭换货订单
        orderService.closeExchangeOrder(orderIds);
        for (Long orderId : orderIds) {
            //关闭签收单
            closeReceipt(orderId, afsOrder.getId(), afsOrder.getTenantId(), afsOrder.getShopId());
        }
    }


    /**
     * 备注售后单
     *
     * @param dto
     * @return void
     * @author alan
     * @date 2021/3/17 22:34
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void note(SellerNoteDto dto) {
        String note = "";
        List<AfsOrder> records = baseMapper.selectBatchIds(dto.getAfsIds());
        for (AfsOrder record : records) {
            if (StrUtil.isBlank(dto.getNote())) {
                note = "";
            } else {
                note = dto.getNote();
            }
            if (!dto.getOver() && StrUtil.isNotBlank(record.getNote())) {
                record.setNote(record.getNote() + StrUtil.CRLF + note);
            } else {
                record.setNote(note);
            }
            baseMapper.updateById(record);
        }
    }

    /**
     * 用户申请退款
     *
     * @param afsOrder
     * @param isSystem
     * @return void
     * @author alan
     * @date 2021/3/17 22:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userApplyRefund(AfsOrder afsOrder, boolean isSystem) {
        //获取退款金额和原订单信息
        List<AfsOrderItem> afsOrderItemList = afsOrderItemService.list(new LambdaQueryWrapper<AfsOrderItem>()
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));


        for (AfsOrderItem orderItem : afsOrderItemList) {
            //调用退款流程
            orderService.closeOrder(orderItem.getAfsId(), orderItem.getRefundAmount(), afsOrder.getType().getCode(),
                    orderItem.getOrderId());

            List<Long> orderIds = afsOrderItemService.getExchangeOrder(orderItem.getOrderId());
            orderService.closeExchangeOrder(orderIds);
            //添加协商历史
            negotiateHistoryService.refund(afsOrder, isSystem);
            //更新退款单的的签收单状态
            OrderVo orderVo = orderService.orderInfo(orderItem.getOrderId());
            log.info("OrderVo is {}", JSONUtil.toJsonStr(orderVo));
            if (OrderStatusEnum.isClose(orderVo.getStatus())) {
                closeReceipt(orderItem.getOrderId(), orderItem.getAfsId(), orderItem.getTenantId(),
                        orderItem.getShopId());
            }
        }
        //更改售后单状态
        afsOrder.setStatus(AfsOrderStatusEnum.SUCCESS);
        afsOrder.setSuccessTime(LocalDateTime.now());
        afsOrder.setDeadline(null);
        this.updateById(afsOrder);
        //发送微信退款消息
        if (afsOrder.getType().equals(AfsOrderTypeEnum.REFUND) || afsOrder.getType().equals(AfsOrderTypeEnum.RETURN_REFUND)) {
            AfsOrderVo afsOrderVo = new AfsOrderVo();
            BeanUtil.copyProperties(afsOrder, afsOrderVo);
            afsOrderVo.setItem(CollUtil.getFirst(afsOrderItemList));
            AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(afsOrder.getUserId(),
                    Collections.singletonList(4));
            sender.sendWechatRefundMessage(afsOrderVo, accountInfoDto.getMiniAccountOauths().getOpenId());
        }

    }


    /**
     * 关闭签收单
     *
     * @param orderId
     * @param afsId
     * @param tenantId
     * @param shopId
     * @return void
     * @author alan
     * @date 2021/3/17 22:35
     */
    private void closeReceipt(Long orderId, Long afsId, String tenantId, String shopId) {
        List<AfsOrder> afsOrderList = baseMapper.selectProgressByOrderIdAndIdNotIn(orderId, afsId);
        if (afsOrderList.isEmpty()) {
            AfsRemoveDeliverOrderMessage message = new AfsRemoveDeliverOrderMessage();
            message.setOrderId(orderId);
            message.setTenantId(tenantId);
            message.setShopId(shopId);
            sender.sendRemoveSendBillOrderMessage(message);
        }

    }


    /**
     * 用户申请退货
     *
     * @param afsOrder
     * @param isSystem
     * @param fromLeaderApprove
     * @return void
     * @author alan
     * @date 2021/3/17 22:36
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userApplyReturn(AfsOrder afsOrder, Boolean isSystem, boolean fromLeaderApprove) {
        //获取订单设置
        OrderSetting orderSetting = orderService.getOrderSetting();
        //获取原订单信息
        List<AfsOrderItem> afsOrderItemList = afsOrderItemService.list(new LambdaQueryWrapper<AfsOrderItem>()
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));
        OrderVo orderVo = orderService.orderInfo(CollUtil.getFirst(afsOrderItemList).getOrderId());
        //不需要经过退货
        boolean unwantedReturn;
        if (afsOrder.getIsLogistics()) {
            //待商家确认收货的不需要退货
            unwantedReturn = afsOrder.getStatus().isBusinessReceipted();
        } else {
            unwantedReturn = fromLeaderApprove;
        }
        //如果不符合这些条件则需要退货vc
        if (!unwantedReturn) {
            afsOrder.setDeadline(LocalDateTimeUtil.now().plusDays(orderSetting.getUserReturnOvertime()));
            afsOrder.setStatus(AfsOrderStatusEnum.WAIT_FOR_RETURN);
            this.updateById(afsOrder);
            //添加协商历史
            negotiateHistoryService.sellerApproveNeedReturn(afsOrder, isSystem,
                    null,
                    afsOrder.getIsLogistics());
            //添加用户操作倒计时
            BaseAfsOrderMessage message = new BaseAfsOrderMessage();
            message.setId(afsOrder.getId());
            if (afsOrder.getType().equals(AfsOrderTypeEnum.REFUND) || afsOrder.getType().equals(AfsOrderTypeEnum.RETURN_REFUND)) {
                AfsOrderVo afsOrderVo = new AfsOrderVo();
                BeanUtil.copyProperties(afsOrder, afsOrderVo);
                afsOrderVo.setItem(CollUtil.getFirst(afsOrderItemList));
                AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(afsOrder.getUserId(),
                        Collections.singletonList(4));
                sender.sendWechatReturnMessage(afsOrderVo, accountInfoDto.getMiniAccountOauths().getOpenId());
            }
        } else {
            userApplyRefund(afsOrder, isSystem);
        }
    }

    /**
     * 分页查询售后单
     *
     * @param dto
     * @return com.medusa.gruul.common.core.util.PageUtils<com.medusa.gruul.afs.model.ManageAfsOrderVo>
     * @author alan
     * @date 2021/3/17 22:36
     */
    @Override
    public PageUtils<ManageAfsOrderVo> searchManageAfsOrderVoPage(SearchDto dto) {
        List<AfsOrderStatusEnum> statusList = new ArrayList<>();
        //订单状态 -1：所有订单, 0.待处理, 1.处理中, 2.已完成, 3.已关闭
        switch (dto.getStatus()) {
            case -1:
                statusList.clear();
                break;
            case 0:
                statusList.add(AfsOrderStatusEnum.WAIT_FOR_BUSINESS_APPROVED);
                break;
            case 1:
                statusList.add(AfsOrderStatusEnum.WAIT_FOR_RETURN);
                statusList.add(AfsOrderStatusEnum.WAIT_FOR_SEND);
                statusList.add(AfsOrderStatusEnum.SHIPPED);
                statusList.add(AfsOrderStatusEnum.WAIT_FOR_BUSINESS_RECEIPT);
                break;
            case 2:
                statusList.add(AfsOrderStatusEnum.SUCCESS);
                break;
            case 3:
                statusList.add(AfsOrderStatusEnum.CLOSE);
                break;
            default:
                break;
        }
        setDefaultTime(dto);

        Page<ManageAfsOrderVo> page = baseMapper.searchManageAfsOrderVoPage(new Page(dto.getCurrent(), dto.getSize()),
                statusList, dto);

        return new PageUtils(page);
    }

    private void setDefaultTime(SearchDto dto) {
        if (StrUtil.isBlank(dto.getStartTime())) {
            dto.setStartTime(DateUtil.beginOfMonth(new Date()).toString());
        } else {
            dto.setStartTime(dto.getStartTime() + " 00:00:00");
        }
        if (StrUtil.isBlank(dto.getEndTime())) {
            dto.setEndTime(DateUtil.endOfMonth(new Date()).toString());
        } else {
            dto.setEndTime(dto.getEndTime() + " 23:59:59");
        }
    }
}
