package com.medusa.gruul.afs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;
import com.medusa.gruul.afs.api.enums.AfsOrderCloseTypeEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
import com.medusa.gruul.afs.api.model.AfsSimpleVo;
import com.medusa.gruul.afs.mapper.AfsOrderItemMapper;
import com.medusa.gruul.afs.mapper.AfsOrderMapper;
import com.medusa.gruul.afs.model.*;
import com.medusa.gruul.afs.mp.Sender;
import com.medusa.gruul.afs.mp.model.BaseAfsOrderMessage;
import com.medusa.gruul.afs.service.IAfsNegotiateHistoryService;
import com.medusa.gruul.afs.service.IAfsOrderService;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.LocalDateTimeUtils;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.logistics.api.feign.RemoteLogisticsFeginService;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import com.medusa.gruul.order.api.constant.OrderConstant;
import com.medusa.gruul.order.api.entity.OrderItem;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.enums.DeliverTypeEnum;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.order.api.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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
public class AfsOrderServiceImpl extends ServiceImpl<AfsOrderMapper, AfsOrder> implements IAfsOrderService {
    @Resource
    private RemoteOrderService orderService;
    @Resource
    private AfsOrderItemMapper afsOrderItemMapper;
    @Resource
    private Sender sender;
    @Resource
    private IAfsNegotiateHistoryService negotiateHistoryService;
    @Resource
    private RemoteLogisticsFeginService remoteLogisticsFeginService;

    /**
     * 新增售后详情
     *
     * @param dto            the dto
     * @param orderVo        the order vo
     * @param applyOrderItem the apply order item
     * @param afsOrder       the afs order
     * @return the afs order item
     */
    public AfsOrderItem saveAfsOrderItem(UserApplyDto dto, OrderVo orderVo, OrderItem applyOrderItem,
                                         AfsOrder afsOrder) {
        AfsOrderItem afsOrderItem = new AfsOrderItem();
        afsOrderItem.setAfsId(afsOrder.getId());
        afsOrderItem.setProductId(applyOrderItem.getProductId());
        afsOrderItem.setProductSkuId(applyOrderItem.getProductSkuId());
        afsOrderItem.setProductQuantity(dto.getProductQuantity());
        afsOrderItem.setOrderId(orderVo.getId());
        afsOrderItem.setOrderId(orderVo.getId());
        afsOrderItem.setProductPic(applyOrderItem.getProductPic());
        afsOrderItem.setProductName(applyOrderItem.getProductName());
        afsOrderItem.setProductPrice(applyOrderItem.getProductPrice());
        afsOrderItem.setSpecs(applyOrderItem.getSpecs());
        afsOrderItem.setRefundAmount(dto.getRefundAmount());
        afsOrderItemMapper.insert(afsOrderItem);
        return afsOrderItem;
    }

    /**
     * 用户申请售后
     *
     * @param dto
     * @return com.medusa.gruul.afs.api.entity.AfsOrder
     * @author alan
     * @date 2021/3/17 22:26
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AfsOrder userApply(UserApplyDto dto) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        OrderVo orderVo = orderService.orderInfo(dto.getOrderId());
        if (ObjectUtil.isNull(orderVo)) {
            throw new ServiceException("订单获取失败，请刷新后重试");
        }
        if (orderVo.getType().equals(OrderTypeEnum.EXCHANGE) || orderVo.getType().equals(OrderTypeEnum.REPLENISH)) {
            Long orderId = baseMapper.selectOriginalOrderByOrderId(dto.getOrderId());
            orderVo = orderService.orderInfo(orderId);
        }
        if (ObjectUtil.isNull(orderVo)) {
            throw new ServiceException("订单获取失败，请刷新后重试");
        }
        //查询之前的申请次数
        Integer userAfterSaleNum = getUserApplyNumber(orderVo.getId());

        //获取订单设置
        OrderSetting orderSetting = orderService.getOrderSetting();
        if (ObjectUtil.isNull(orderSetting)) {
            throw new ServiceException("订单设置获取失败，请管理员检查设置并稍后重试");
        }
        //获取申请的订单详情
        OrderItem applyOrderItem = null;
        for (OrderItem orderItem : orderVo.getOrderItemList()) {
            if (orderItem.getProductSkuId().equals(dto.getProductSkuId())) {
                applyOrderItem = orderItem;
            }
        }
        if (ObjectUtil.isNull(applyOrderItem)) {
            throw new ServiceException("无效的商品信息");
        }
        //检查订单状态
        checkOrderStatus(orderVo, dto, orderSetting, userAfterSaleNum);
        //检查售后入参
        checkAfsOrderParam(orderVo, dto);
        //按照售后类型检查是否满足申请条件
        switch (dto.getType()) {
            //退款
            case REFUND:
                checkRefundParam(dto, orderVo);
                break;
            //退货退款
            case RETURN_REFUND:
                checkRefundParam(dto, orderVo);
                checkReturnParam(dto, orderVo);
                break;
            default:
                throw new ServiceException("不是有效的售后类型");
        }
        if (orderVo.getStatus().equals(OrderStatusEnum.WAIT_FOR_SEND)) {
            dto.setRefundAmount(dto.getRefundAmount().add(orderVo.getFreightAmount()));
        }

        AfsOrder afsOrder = saveAfsOrder(dto, curUserDto, orderVo, userAfterSaleNum, orderSetting, null);

        AfsOrderItem afsOrderItem = saveAfsOrderItem(dto, orderVo, applyOrderItem, afsOrder);
        //初始化协商历史
        negotiateHistoryService.init(Collections.singletonList(afsOrderItem), dto, "买家");
        BaseAfsOrderMessage message = new BaseAfsOrderMessage();
        message.setId(afsOrder.getId());
        sender.sendMerchantAutoConfirmMessage(message,
                orderSetting.getMerchantConfirmOvertime() * TimeConstants.ONE_DAY);
        return afsOrder;

    }


    /**
     * 保存售后单
     *
     * @param dto
     * @param curUserDto
     * @param orderVo
     * @param userAfterSaleNum
     * @param orderSetting
     * @param receiptBillId
     * @return com.medusa.gruul.afs.api.entity.AfsOrder
     * @author alan
     * @date 2021/3/17 22:27
     */
    @Override
    public AfsOrder saveAfsOrder(BaseApplyDto dto, CurUserDto curUserDto, OrderVo orderVo, Integer userAfterSaleNum,
                                 OrderSetting orderSetting, Long receiptBillId) {
        AfsOrder afsOrder = new AfsOrder();
        afsOrder.setShopId(orderVo.getShopId());
        afsOrder.setNo(orderVo.getId().toString().concat(dto.getType().getName()).concat(String.valueOf(userAfterSaleNum + 1)));
        afsOrder.setType(dto.getType());
        afsOrder.setStatus(AfsOrderStatusEnum.WAIT_FOR_BUSINESS_APPROVED);
        afsOrder.setReceiptBillId(receiptBillId);
        afsOrder.setUserId(curUserDto.getUserId());
        afsOrder.setUserName(curUserDto.getNikeName());
        afsOrder.setUserAvatarUrl(curUserDto.getAvatarUrl());
        afsOrder.setProductSkuId(dto.getProductSkuId());
        afsOrder.setTemplateId(dto.getTemplateId());
        afsOrder.setRefundAmount(dto.getRefundAmount());
        if (dto.getRefundAmount().equals(BigDecimal.ZERO)) {
            afsOrder.setRefundAmount(OrderConstant.MIN_PAY_FEE);
        }
        afsOrder.setDeadline(LocalDateTimeUtil.now().plusDays(orderSetting.getMerchantConfirmOvertime()));
        afsOrder.setDescription(dto.getDescription());
        // TODO: 2021/8/3 不确定 申请售后的签收单ID 是什么
        afsOrder.setReceiptBillId(orderVo.getOrderDelivery().getOrderId());
        afsOrder.setImages(dto.getImages());
        afsOrder.setIsLogistics(orderVo.getOrderDelivery().getDeliveryType().equals(DeliverTypeEnum.LOGISTICS));
        this.save(afsOrder);
        return afsOrder;
    }

    /**
     * 检查订单状态
     *
     * @param order
     * @param dto
     * @param orderSetting
     * @param userAfterSaleNum
     * @return void
     * @author alan
     * @date 2021/3/17 22:27
     */
    @Override
    public void checkOrderStatus(OrderVo order, BaseApplyDto dto, OrderSetting orderSetting, Integer userAfterSaleNum) {
        if (ObjectUtil.isNull(order)) {
            throw new ServiceException("订单不存在，请刷新后再试");
        }
        if (userAfterSaleNum >= orderSetting.getAfsApplyNumber()) {
            throw new ServiceException(String.format("订单%s申请售后次数已经超过最大限制，不能继续申请售后", order.getId()));
        }
        if (OrderStatusEnum.isClose(order.getStatus())) {
            throw new ServiceException(String.format("订单%s已经关闭，不能继续申请售后", order.getId()));
        }
        if (ObjectUtil.isNotNull(order.getOrderDelivery().getReceiveTime()) &&
                DateUtil.offsetDay(LocalDateTimeUtils.convertLDTToDate(order.getOrderDelivery().getReceiveTime()),
                        orderSetting.getFinishOvertime()).getTime()
                        < System.currentTimeMillis()) {
            throw new ServiceException(String.format("订单%s已经离确认收货已经超过%s天，不能继续申请售后", order.getId(),
                    orderSetting.getFinishOvertime()));
        }

        AfsOrder afsOrder = this.selectProgressByOrderIdAndProductSkuId(order.getId(), dto.getProductSkuId());
        if (ObjectUtil.isNotNull(afsOrder)) {
            if (afsOrder.getProductSkuId().equals(dto.getProductSkuId())) {

                    //当前换货单等待审核的也不能再次申请
                if (afsOrder.getStatus().equals(AfsOrderStatusEnum.WAIT_FOR_BUSINESS_APPROVED)) {
                    throw new ServiceException(String.format("订单%s正在申请售后，请等待当前售后流程结束", order.getId()));
                }

                if (afsOrder.getType().isRefund() && afsOrder.getStatus().equals(AfsOrderStatusEnum.SUCCESS)) {
                    throw new ServiceException(String.format("订单%s中商品%s,已经成功退款，不能继续申请售后", order.getId(),
                            dto.getProductSkuId()));
                }
            }
        }
    }

    /**
     * 检查售后单参数
     *
     * @param orderVo
     * @param dto
     * @return void
     * @author alan
     * @date 2021/3/17 22:27
     */
    private void checkAfsOrderParam(OrderVo orderVo, UserApplyDto dto) {
        Boolean matchProductSkuId = false;
        for (OrderItem orderItem : orderVo.getOrderItemList()) {
            if (orderItem.getProductSkuId().equals(dto.getProductSkuId())) {
                matchProductSkuId = true;
                BigDecimal rate = NumberUtil.div(dto.getProductQuantity(), orderItem.getProductQuantity());
                log.info("rate is {}", rate);
                BigDecimal refundAmount = NumberUtil.mul(orderItem.getRealAmount(), rate);
                if (ObjectUtil.isNotNull(orderItem.getRealAmount()) || orderItem.getRealAmount().equals(BigDecimal.ZERO)) {
                    refundAmount = NumberUtil.mul(orderVo.getPayAmount(), rate);
                }
                if (dto.getProductQuantity() > orderItem.getProductQuantity()) {
                    log.info("dto.getProductQuantity() is {}", dto.getProductQuantity());
                    log.info("orderItem.getProductQuantity() is {}", orderItem.getProductQuantity());
                    throw new ServiceException("所选的商品数量超过订单的总数量");
                }

                if (NumberUtil.isLess(refundAmount, dto.getRefundAmount())) {
                    log.info("refundAmount is {}", refundAmount);
                    log.info("dto.getRefundAmount() is {}", dto.getRefundAmount());
                    throw new ServiceException("退款金额超出所选商品总金额");

                }
            }
        }
        if (!matchProductSkuId) {
            throw new ServiceException("申请的订单不包含所选商品");
        }
    }

    /**
     * 根据售后单ID获取售后单详情
     *
     * @param afsId
     * @return com.medusa.gruul.afs.model.AfsOrderVo
     * @author alan
     * @date 2021/3/17 22:27
     */
    @Override
    public AfsOrderVo getAfsOrderInfo(Long afsId) {
        AfsOrder afsOrder = baseMapper.selectById(afsId);
        if (ObjectUtil.isNull(afsOrder)) {
            throw new ServiceException("无效的售后信息，请刷新后重试");
        }
        AfsOrderVo vo = new AfsOrderVo();
        BeanUtil.copyProperties(afsOrder, vo);
        AfsOrderItem afsOrderItem = afsOrderItemMapper.selectOne(new LambdaQueryWrapper<AfsOrderItem>()
                .eq(AfsOrderItem::getAfsId, afsId)
                .last("limit 1"));
        vo.setItem(afsOrderItem);
        OrderVo orderVo = orderService.orderInfo(afsOrderItem.getOrderId());
        vo.setOriginalOrder(orderVo);
        return vo;
    }



    /**
     * 检查退货参数
     *
     * @param dto
     * @param orderVo
     * @return void
     * @author alan
     * @date 2021/3/17 22:28
     */
    private void checkReturnParam(UserApplyDto dto, OrderVo orderVo) {
        if (ObjectUtil.isNull(dto.getProductQuantity()) || dto.getProductQuantity() < 1) {
            throw new ServiceException("退货数量不得小于1");
        }
        for (OrderItem orderItem : orderVo.getOrderItemList()) {
            if (orderItem.getProductSkuId().equals(dto.getProductSkuId())) {
                if (orderItem.getProductQuantity() < dto.getProductQuantity()) {
                    throw new ServiceException("退货数量不得大于总数量");
                }
            }
        }
    }

    /**
     * 检查退款参数
     *
     * @param dto
     * @param orderVo
     * @return void
     * @author alan
     * @date 2021/3/17 22:28
     */
    private void checkRefundParam(UserApplyDto dto, OrderVo orderVo) {
        if (ObjectUtil.isNull(dto.getRefundAmount()) || NumberUtil.isLess(dto.getRefundAmount(), BigDecimal.ZERO)) {
            throw new ServiceException("退款金额不得小于0");
        }
        if (NumberUtil.isLess(orderVo.getPayAmount(), dto.getRefundAmount())) {
            throw new ServiceException("退款金额不得大于实际支付金额");
        }
    }

    /**
     * 根据订单ID和商品ID查询进行中的订单
     *
     * @param orderId
     * @param productSkuId
     * @return com.medusa.gruul.afs.api.entity.AfsOrder
     * @author alan
     * @date 2021/3/17 22:29
     */
    @Override
    public AfsOrder selectProgressByOrderIdAndProductSkuId(Long orderId, Long productSkuId) {
        return baseMapper.selectProgressByOrderIdAndProductSkuId(orderId, productSkuId);
    }


    /**
     * 获取用户申请售后的次数
     *
     * @param orderId
     * @return java.lang.Integer
     * @author alan
     * @date 2021/3/17 22:30
     */
    @Override
    public Integer getUserApplyNumber(Long orderId) {
        Set<Long> originalOrderIds = new HashSet<>();
        Long originalOrderId = baseMapper.selectOriginalOrderByOrderId(orderId);
        if (ObjectUtil.isNotNull(originalOrderId)) {
            originalOrderIds.add(originalOrderId);
        }
        originalOrderIds.add(orderId);
        Integer userAfterSaleNum = baseMapper.getOrderApplyNumber(CollUtil.newArrayList(originalOrderIds),
                Arrays.asList(AfsOrderTypeEnum.REFUND.getCode(),
                        AfsOrderTypeEnum.RETURN_REFUND.getCode()));
        return userAfterSaleNum;
    }

    /**
     * 用户取消售后
     *
     * @param afsId
     * @param isSystem
     * @return void
     * @author alan
     * @date 2021/3/17 22:30
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userCancel(Long afsId, boolean isSystem) {
        AfsOrder afsOrder = this.getById(afsId);
        if (afsOrder.getStatus() == AfsOrderStatusEnum.SUCCESS){
            throw new ServiceException("该售后单已处理");
        }
        if (ObjectUtil.isNull(afsOrder)) {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }
        if (afsOrder.getStatus().canReceipt()) {
            afsOrder.setStatus(AfsOrderStatusEnum.CLOSE);
            afsOrder.setCloseType(AfsOrderCloseTypeEnum.USER_CANCEL);
            afsOrder.setCloseTime(LocalDateTime.now());
            afsOrder.setDeadline(null);
            this.updateById(afsOrder);
            negotiateHistoryService.userCancel(afsOrder, isSystem);
        } else {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }
    }

    /**
     * 用户退货
     *
     * @param afsId
     * @param deliveryCode
     * @param deliveryCompany
     * @param deliverySn
     * @param phone
     * @param reason
     * @return void
     * @author alan
     * @date 2021/3/17 22:30
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userReturn(Long afsId, String deliveryCode, String deliveryCompany, String deliverySn, String phone,
                           String reason) {
        AfsOrder afsOrder = this.getById(afsId);
        if (ObjectUtil.isNull(afsOrder)) {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }
        //获取订单设置
        OrderSetting orderSetting = orderService.getOrderSetting();
        if (afsOrder.getIsLogistics()) {
            if (StrUtil.isBlank(deliveryCompany)) {
                throw new ServiceException("物流公司不能为空");
            }
            if (StrUtil.isBlank(deliverySn)) {
                throw new ServiceException("物流单号不能为空");
            }
            if (StrUtil.isBlank(phone)) {
                throw new ServiceException("联系电话不能为空");
            }
        }
        if (afsOrder.getStatus().equals(AfsOrderStatusEnum.WAIT_FOR_RETURN)) {

            afsOrder.setDeliverySn(deliverySn);
            afsOrder.setDeliveryCode(deliveryCode);
            afsOrder.setDeliveryCompany(deliveryCompany);
            // TODO: 2021/8/3 暂且换成 商家最大审核期限
            afsOrder.setDeadline(LocalDateTimeUtil.now().plusDays(orderSetting.getMerchantConfirmOvertime()));
            afsOrder.setStatus(AfsOrderStatusEnum.WAIT_FOR_BUSINESS_RECEIPT);
            this.updateById(afsOrder);
            //添加协商历史
            negotiateHistoryService.userReturn(afsOrder, afsOrder.getIsLogistics());
            //添加操作倒计时
            BaseAfsOrderMessage message = new BaseAfsOrderMessage();
            message.setId(afsOrder.getId());
        } else {
            throw new ServiceException("没有符合条件的售后单，请刷新后重试");
        }
    }


    /**
     * 根据签收单查询售后
     *
     * @param receiptBillId
     * @return java.util.List<com.medusa.gruul.afs.api.model.AfsSimpleVo>
     * @author alan
     * @date 2021/3/17 22:30
     */
    @Override
    public List<AfsSimpleVo> getAfsOrderByReceiptBillId(Long receiptBillId) {
        return baseMapper.selectAfsOrderByReceiptBillId(receiptBillId);
    }

    /**
     * 订单签收
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2021/3/17 22:30
     */
    @Override
    public void orderReceipt(OrderVo orderVo) {
        if (orderVo.getType().equals(OrderTypeEnum.EXCHANGE)) {
            Long orderId = baseMapper.selectOriginalOrderByOrderId(orderVo.getId());
            orderService.receiptOrder(orderId);
        }

        //生成此售后单的原始订单，在确认收货后自动取消等待商家审批的售后单
        AfsOrder closeAfsOrder = baseMapper.selectByOriginalOrderId(orderVo.getId());
        if (ObjectUtil.isNotNull(closeAfsOrder)) {
            if (closeAfsOrder.getStatus().isBusinessApprove()) {
                closeAfsOrder.setStatus(AfsOrderStatusEnum.CLOSE);
                closeAfsOrder.setCloseType(AfsOrderCloseTypeEnum.USER_CANCEL);
                closeAfsOrder.setCloseTime(LocalDateTime.now());
                this.updateById(closeAfsOrder);
            }
        }
        //此售后单生成的新订单，在确认收货后自动更新此售后单
        AfsOrder completeAfsOrder = baseMapper.selectByOrderId(orderVo.getId());
        if (ObjectUtil.isNotNull(completeAfsOrder)) {
            completeAfsOrder.setStatus(AfsOrderStatusEnum.SUCCESS);
            completeAfsOrder.setSuccessTime(LocalDateTime.now());
            this.updateById(completeAfsOrder);
        }
    }

    /**
     * 订单发货
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2021/3/17 22:31
     */
    @Override
    public void orderShipped(OrderVo orderVo) {
        log.info("执行订单发货逻辑");
        log.info("OrderVo is {}", JSONUtil.toJsonStr(orderVo));
        if (OrderTypeEnum.isExchange(orderVo.getType())) {
            AfsOrder afsOrder = baseMapper.selectByOrderId(orderVo.getId());
            afsOrder.setStatus(AfsOrderStatusEnum.SHIPPED);
            this.updateById(afsOrder);
            negotiateHistoryService.sellerShipped(afsOrder);
        }
    }


    /**
     * 发货单签收
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2021/3/17 22:31
     */
    @Override
    public void deliverReceipt(OrderVo orderVo) {
        log.info("执行发货单签收逻辑");
        log.info("OrderVo is {}", JSONUtil.toJsonStr(orderVo));
        if (OrderTypeEnum.isExchange(orderVo.getType())) {
            AfsOrder afsOrder = baseMapper.selectByOrderId(orderVo.getId());
            afsOrder.setStatus(AfsOrderStatusEnum.WAIT_FOR_PICKUP);
            this.updateById(afsOrder);
        }
    }

    /**
     * 查询订单列表
     *
     * @param dto
     * @return com.medusa.gruul.common.core.util.PageUtils
     * @author alan
     * @date 2021/3/17 22:31
     */
    @Override
    public PageUtils<ApiAfsOrderVo> searchOrder(SearchOrderDto dto) {
        Page<ApiAfsOrderVo> page = baseMapper.searchOrder(new Page(dto.getCurrent(), dto.getSize()),
                CurUserUtil.getHttpCurUser().getUserId());
        return new PageUtils<ApiAfsOrderVo>(page);

    }

    /**
     * 订单完成
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2021/3/17 22:32
     */
    @Override
    public void orderCompleted(OrderVo orderVo) {
        //生成此售后单的原始订单，在评价订单后自动取消等待商家审批的售后单
        AfsOrder closeAfsOrder = baseMapper.selectByOriginalOrderId(orderVo.getId());
        if (ObjectUtil.isNotNull(closeAfsOrder)) {
            if (closeAfsOrder.getStatus().isBusinessApprove()) {
                closeAfsOrder.setStatus(AfsOrderStatusEnum.CLOSE);
                closeAfsOrder.setCloseType(AfsOrderCloseTypeEnum.USER_CANCEL);
                closeAfsOrder.setCloseTime(LocalDateTime.now());
                this.updateById(closeAfsOrder);
            }
        }
    }

    /**
     * 获取退货地址
     *
     * @param orderId
     * @return com.medusa.gruul.afs.model.ReturnAddressVo
     * @author alan
     * @date 2021/3/17 22:32
     */
    @Override
    public ReturnAddressVo getReturnAddress(Long orderId) {
        ReturnAddressVo returnAddressVo = new ReturnAddressVo();
        OrderVo orderVo = orderService.orderInfo(orderId);
        if (ObjectUtil.isNull(orderVo)) {
            throw new ServiceException("无效的订单信息");
        }

        LogisticsAddressVo logisticsAddressVo = remoteLogisticsFeginService.getFeignDefaultAddress(2);
        if (ObjectUtil.isNotNull(logisticsAddressVo)) {
            log.info(JSONUtil.toJsonStr(logisticsAddressVo));
            returnAddressVo.setAddress(logisticsAddressVo.getProvince() + logisticsAddressVo.getCity() + logisticsAddressVo.getCountry() + logisticsAddressVo.getAddress());
            returnAddressVo.setName(logisticsAddressVo.getName());
            returnAddressVo.setPhone(logisticsAddressVo.getPhone());
        } else {
            throw new ServiceException("获取默认退货地址失败");
        }

        return returnAddressVo;
    }

    /**
     * 获取订单是否过期
     *
     * @param orderId
     * @return java.lang.Boolean
     * @author alan
     * @date 2021/3/17 22:33
     */
    @Override
    public Boolean getAfsExpire(Long orderId) {
        OrderVo orderVo = orderService.orderInfo(orderId);
        if (ObjectUtil.isNull(orderVo)) {
            throw new ServiceException("订单获取失败，请刷新后重试");
        }

        //获取订单设置
        boolean expire = false;
        if (ObjectUtil.isNotNull(orderVo.getOrderDelivery().getReceiveTime())) {
            OrderSetting orderSetting = orderService.getOrderSetting();
            if (ObjectUtil.isNull(orderSetting)) {
                throw new ServiceException("订单设置获取失败，请管理员检查设置并稍后重试");
            }

            expire =
                    DateUtil.offsetDay(LocalDateTimeUtils.convertLDTToDate(orderVo.getOrderDelivery().getReceiveTime()),
                            orderSetting.getFinishOvertime()).getTime()
                            < System.currentTimeMillis();
        }
        return expire;
    }

}
