package com.medusa.gruul.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.entity.MiniAccountAddress;
import com.medusa.gruul.account.api.enums.BlacklistEnum;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.account.api.model.*;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.*;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.goods.api.constant.GoodsSkuStockRedisKey;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.api.feign.RemoteGoodsService;
import com.medusa.gruul.goods.api.model.vo.manager.ItemVo;
import com.medusa.gruul.goods.api.param.OperateStockDto;
import com.medusa.gruul.logistics.api.feign.RemoteLogisticsFeginService;
import com.medusa.gruul.logistics.model.dto.manager.CountCostDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsFreightDto;
import com.medusa.gruul.order.api.constant.OrderCode;
import com.medusa.gruul.order.api.constant.OrderConstant;
import com.medusa.gruul.order.api.constant.OrderFailedRedisKey;
import com.medusa.gruul.order.api.constant.OrderQueueEnum;
import com.medusa.gruul.order.api.entity.*;
import com.medusa.gruul.order.api.enums.DeliverTypeEnum;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import com.medusa.gruul.order.api.enums.PayTypeEnum;
import com.medusa.gruul.order.api.model.*;
import com.medusa.gruul.order.mapper.*;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.mq.Sender;
import com.medusa.gruul.order.service.IMiniOrderService;
import com.medusa.gruul.order.service.IOrderShareSettingService;
import com.medusa.gruul.payment.api.feign.RemotePaymentService;
import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import com.medusa.gruul.payment.api.model.dto.RefundNotifyResultDto;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.dto.ShopConfigDto;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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
public class MiniOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IMiniOrderService {
    @Resource
    private RemoteGoodsService remoteGoodsService;
    @Resource
    private RemotePaymentService remotePaymentService;
    @Resource
    private RemoteMiniAccountService remoteMiniAccountService;
    @Resource
    private RemoteLogisticsFeginService remoteLogisticsFeginService;

    @Resource
    private IOrderShareSettingService orderShareSettingService;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderSettingMapper orderSettingMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private OrderEvaluateMapper orderEvaluateMapper;
    @Resource
    private OrderProductEvaluateMapper productEvaluateMapper;
    @Resource
    private Sender sender;
    @Resource
    private RemoteMiniInfoService remoteMiniInfoService;
    @Resource
    private AfsOrderMapper afsOrderMapper;


    /**
     * 根据用户选择的商品返回结算页需要的数据
     *
     * @param dto
     * @return com.medusa.gruul.order.model.ConfirmOrderVo
     * @author alan
     * @date 2020/7/26 14:19
     */
    @Override
    public ConfirmOrderVo getConfirmOrder(ConfirmOrderDto dto) {
        TimeInterval timer = DateUtil.timer();
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        if (ObjectUtil.isNull(curUserDto)) {
            throw new ServiceException(SystemCode.UNAUTHORIZED);
        }
        log.info("当前用户信息:" + curUserDto.toString());
        ConfirmOrderVo result = new ConfirmOrderVo();
        GoodsSkuStockRedisKey redisStock = new GoodsSkuStockRedisKey();
        //查询商品库存情况
        List<ItemVo> itemVoList = remoteGoodsService.findItemVoByIds(dto.getItemSkuIds());
        if (CollUtil.isEmpty(itemVoList)) {
            throw new ServiceException(OrderCode.DATA_HAS_BEEN_UPDATED);
        }
        Map<Long, ItemVo> itemVoMap = itemVoList.stream().collect(Collectors.toMap(ItemVo::getProductSkuId, v -> v));
        for (ItemDto item : dto.getItems()) {
            ItemVo itemVo = itemVoMap.get(item.getSkuId());
            itemVo.setProductQuantity(item.getNumber());
            String stock = redisStock.get(item.getSkuId().toString());
            if (ObjectUtil.isNull(stock)) {
                throw new ServiceException(OrderCode.THIRD_PARTY_SERVICE_EXCEPTION);
            }
            if (Integer.parseInt(stock) > item.getNumber()) {
                //库存状态：0->有库存
                itemVo.setStatus(0);
            } else {
                //库存状态：1->无库存
                itemVo.setStatus(1);
            }
        }
        result.setItemVoList(itemVoList);
        log.info(ObjectUtil.isNotNull(itemVoList) ? itemVoList.toString() : "");
        log.info("查询商品耗时{}ms", timer.intervalRestart());

        //查询用户持有的积分、收货地址
        AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(curUserDto.getUserId(), Arrays.asList(2,
                3, 5));
        result.setMiniAccountAddress(accountInfoDto.getMiniAccountAddress());
        log.info(ObjectUtil.isNotNull(accountInfoDto) ? accountInfoDto.toString() : "");
        log.info("查询用户耗时{}ms", timer.intervalRestart());
        //Todo 查询用户的会员情况

        //Todo 查询积分使用规则

        //Todo 查询满减活动信息

        //Todo 查询单个客户优惠劵信息

        return result;
    }


    /**
     * 获取运费的参数包装类，将参数包装成需要的格式
     *
     * @param miniAccountAddress
     * @param deliverType
     * @param itemDtoList
     * @return com.medusa.gruul.shipping.model.dto.CountCostDto
     * @author alan
     * @date 2020/7/26 14:32
     */
    private CountCostDto getFreightAmount(MiniAccountAddress miniAccountAddress,
                                          DeliverTypeEnum deliverType,
                                          List<ItemDto> itemDtoList) {
        //查询运费
        log.info("miniAccountAddress is {}", JSONUtil.toJsonStr(miniAccountAddress));
        log.info("deliverType is {}", deliverType.getDesc());
        CountCostDto countCostDto;
        GetCostDto getCostDto = new GetCostDto();
        getCostDto.setType(1);
        getCostDto.setRegion(miniAccountAddress.getPostCode());
        getCostDto.setItems(itemDtoList);
        log.info("getCostDto is {}", JSONUtil.toJsonStr(getCostDto));
        countCostDto = getFreightAmount(getCostDto);
        log.info("CountCostDto is {}", JSONUtil.toJsonStr(countCostDto));
        return countCostDto;
    }

    /**
     * 提前订单时候的前置检查
     *
     * @param createOrderDto
     * @return java.lang.String
     * @author alan
     * @date 2020/7/26 14:33
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String preCheckOrder(CreateOrderDto createOrderDto) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        //查询会员持有的积分、收货地址
        AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(curUserDto.getUserId(), Arrays.asList(2,
                3, 5));
        log.info("当前用户信息:" + curUserDto.toString());
        //检查账号
        checkAccount(accountInfoDto);
        //检查缓存中商品库存
        checkStock(createOrderDto);
        //Todo 查询用户的会员情况
        List<ItemVo> itemVoList = remoteGoodsService.findItemVoByIds(createOrderDto.getItemSkuIds());
        if (CollUtil.isEmpty(itemVoList)) {
            throw new ServiceException(OrderCode.DATA_HAS_BEEN_UPDATED);
        }
        //计算订单总金额
        BigDecimal totalAmount = getTotalAmount(itemVoList, createOrderDto.getItemDtoList());
        //检查限购
        checkLimit(itemVoList, curUserDto.getUserId());
        List<OrderItem> orderItemList = getOrderItemList(itemVoList, 1L, createOrderDto.getItemDtoList());
        //Todo 检查优惠券
        //Todo 检查满减活动

        //Todo 检查积分

        //检查收货地址
        MiniAccountAddress accountAddress = checkAddress(accountInfoDto.getMiniAccountAddress(), createOrderDto);
        //计算运费
        BigDecimal freightAmount =
                getFreightAmount(accountAddress, createOrderDto.getDeliverType(),
                        createOrderDto.getItemDtoList()).getCost();

        if (createOrderDto.getDeliverType().equals(DeliverTypeEnum.LOGISTICS)) {
            if (ObjectUtil.isNull(freightAmount) || freightAmount.equals(BigDecimal.valueOf(-1))) {
                throw new ServiceException(OrderCode.NOT_IN_THE_SCOPE_OF_DISTRIBUTION);
            }
        } else {
            if (ObjectUtil.isNull(freightAmount)) {
                freightAmount = BigDecimal.ZERO;
            }
        }
        //检查通过发送创建订单的消息
        CreateOrderMessage message = new CreateOrderMessage();
        message.setOrderVo(createOrderDto);
        //预生成的订单ID
        Long orderId = IdWorker.getId();

        message.setOrderId(orderId);
        message.setCurUser(curUserDto);
        message.setTenantId(TenantContextHolder.getTenantId());
        sender.sendCreateOrderMessage(message);
        return orderId.toString();
    }

    /**
     * 检查是否符合限购要求
     *
     * @param itemVoList
     * @param userId
     * @return void
     * @author alan
     * @date 2020/7/26 14:35
     */
    private void checkLimit(List<ItemVo> itemVoList, String userId) {
        for (ItemVo itemVo : itemVoList) {
            if (ObjectUtil.isNotNull(itemVo.getPerLimit()) && itemVo.getPerLimit() != 0) {
                if (itemVo.getLimitType() != 1) {
                    Integer total = orderItemMapper.countSkuPurchased(itemVo.getProductSkuId(), userId);

                    total = ObjectUtil.isNull(total) ? 0 : total;
                    if (total > 0) {
                        Integer returnNum = afsOrderMapper.countSkuReturn(itemVo.getProductSkuId(), userId);
                        returnNum = ObjectUtil.isNull(returnNum) ? 0 : returnNum;
                        total = total - returnNum;
                    }

                    if (total + itemVo.getProductQuantity() > itemVo.getPerLimit()) {
                        throw new ServiceException(StrUtil.format("商品{}已经超过限购数量", itemVo.getProductName()));
                    }
                } else {
                    List<ItemVo> productItemList =
                            itemVoList.stream().filter(vo -> vo.getProductId().equals(itemVo.getProductId())).collect(Collectors.toList());
                    Integer total = 0;
                    for (ItemVo vo : productItemList) {
                        Integer skuPurchase = orderItemMapper.countSkuPurchased(vo.getProductSkuId(), userId);
                        skuPurchase = ObjectUtil.isNull(skuPurchase) ? 0 : skuPurchase;
                        if (skuPurchase > 0) {
                            Integer returnNum = afsOrderMapper.countSkuReturn(itemVo.getProductSkuId(), userId);
                            returnNum = ObjectUtil.isNull(returnNum) ? 0 : returnNum;
                            skuPurchase = skuPurchase - returnNum;
                        }
                        total = total + skuPurchase;
                    }
                    if (total + itemVo.getProductQuantity() > itemVo.getPerLimit()) {
                        throw new ServiceException(StrUtil.format("商品{}已经超过限购数量", itemVo.getProductName()));
                    }
                }

            }
        }
    }

    /**
     * 检查账户是否在黑名单中
     *
     * @param accountInfoDto
     * @return void
     * @author alan
     * @date 2020/7/26 14:35
     */
    private void checkAccount(AccountInfoDto accountInfoDto) {
        if (ObjectUtil.isNull(accountInfoDto)) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        if (CollUtil.isNotEmpty(accountInfoDto.getRestrictTypes()) && accountInfoDto.getRestrictTypes().contains(BlacklistEnum.REJECT_ORDER.getType())) {
            throw new ServiceException(OrderCode.ACCOUNT_NUMBER_EXCEPTION);
        }
    }


    /**
     * 检查商品库存
     *
     * @param createOrderDto
     * @return void
     * @author alan
     * @date 2020/7/26 14:35
     */
    private void checkStock(CreateOrderDto createOrderDto) {
        GoodsSkuStockRedisKey redisStock = new GoodsSkuStockRedisKey();
        List<ItemDto> itemDtoList = createOrderDto.getItemDtoList();
        for (ItemDto itemDto : itemDtoList) {
            //redis预减库存
            Integer stock = Integer.valueOf(redisStock.get(itemDto.getSkuId().toString()));
            if (stock < itemDto.getNumber()) {
                //读取数据库，刷新缓存，查看商品是否确实售罄
                SkuStock skuStock = remoteGoodsService.findSkuStockById(itemDto.getSkuId());
                if (ObjectUtil.isNotNull(skuStock)) {
                    stock = skuStock.getStock();
                }
                if (stock < 1) {
                    throw new ServiceException(SystemCode.ITEM_SOLD_OUT);
                }
            }
            stock = stock - itemDto.getNumber();
            redisStock.set(itemDto.getSkuId().toString(), stock.toString());
        }
    }


    /**
     * 根据商品的销售价格获取总价
     *
     * @param itemVoList
     * @param itemDtoList
     * @return java.math.BigDecimal
     * @author alan
     * @date 2020/7/26 14:36
     */
    private BigDecimal getTotalAmount(List<ItemVo> itemVoList, List<ItemDto> itemDtoList) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Long, ItemVo> itemVoMap = itemVoList.stream().collect(Collectors.toMap(ItemVo::getProductSkuId, v -> v));
        for (ItemDto itemDto : itemDtoList) {
            ItemVo itemVo = itemVoMap.get(itemDto.getSkuId());
            itemVo.setProductQuantity(itemDto.getNumber());
            BigDecimal itemTotal = itemVo.getProductPrice().multiply(BigDecimal.valueOf(itemVo.getProductQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        return totalAmount;
    }


    /**
     * 检查用户收货地址信息
     *
     * @param addressList
     * @param dto
     * @return com.medusa.gruul.account.api.entity.MiniAccountAddress
     * @author alan
     * @date 2020/7/26 14:37
     */
    private MiniAccountAddress checkAddress(List<MiniAccountAddress> addressList, CreateOrderDto dto) {
        MiniAccountAddress accountAddress = new MiniAccountAddress();
        if (CollUtil.isEmpty(addressList)) {
            throw new ServiceException("收货地址数据已更新,请刷新后重试");
        }
        for (MiniAccountAddress address : addressList) {
            if (address.getId().equals(dto.getMiniAccountAddressId())) {
                accountAddress = address;
            }
        }
        if (ObjectUtil.isNull(accountAddress)) {
            throw new ServiceException(OrderCode.NO_VALID_SHIP_TO_ADDRESS);
        }
        return accountAddress;
    }

    /**
     * 创建订单
     *
     * @param dto
     * @param orderId
     * @param skuStockList
     * @param curUser
     * @return java.lang.Boolean
     * @author alan
     * @date 2020/7/26 14:37
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createOrder(CreateOrderDto dto, Long orderId, List<SkuStock> skuStockList, CurUserDto curUser) {
        Boolean done = false;
        OrderFailedRedisKey orderFailed = new OrderFailedRedisKey();
        log.info("当前用户信息:" + curUser.toString());
        try {
            //查询会员持有的积分、收货地址
            AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(curUser.getUserId(), Arrays.asList(2
                    , 3, 5));

            //Todo 查询用户的会员情况

            //查询商品描述信息
            List<ItemVo> itemVoList = remoteGoodsService.findItemVoByIds(dto.getItemSkuIds());
            //获得当前订单的总金额
            BigDecimal totalAmount = getTotalAmount(itemVoList, dto.getItemDtoList());
            //创建订单详情并且获得每个商品的总价
            List<OrderItem> orderItemList = getOrderItemList(itemVoList, orderId, dto.getItemDtoList());
            //Todo 计算优惠券

            // Todo 计算满减

            //Todo 计算积分抵扣

            // TODO 并不是每个商品都参与了优惠 平摊存在不合理
            //将订单的总优惠平摊到商品上
            shareDiscount(orderItemList);
            //获取用户收货地址
            MiniAccountAddress accountAddress = checkAddress(accountInfoDto.getMiniAccountAddress(), dto);
            //计算运费
            CountCostDto costDto = getFreightAmount(accountAddress, dto.getDeliverType(),
                    dto.getItemDtoList());
            BigDecimal freightAmount = costDto.getCost();
            if (ObjectUtil.isNull(freightAmount) || freightAmount.equals(BigDecimal.valueOf(-1))) {
                freightAmount = BigDecimal.ZERO;
            }
            BigDecimal promotionAmount = BigDecimal.ZERO;
            //Todo 尝试使用会员权益中的包邮

            //扣除库存
            List<ItemDto> itemDtoList = dto.getItemDtoList();
            Set<OperateStockDto> skuSet = new HashSet<>();
            for (ItemDto itemDto : itemDtoList) {
                skuSet.addAll(skuStockList.stream()
                        .map(vo -> new OperateStockDto(itemDto.getSkuId(), itemDto.getNumber()))
                        .collect(Collectors.toSet()));
            }
            //Todo 扣除优惠券


            //Todo 扣除积分

            //删除购物车数据
            remoteGoodsService.deleteShoppingCartByOrder(dto.getItemSkuIds(), curUser.getUserId());

            //扣除库存
            List<OperateStockDto> operateStockDtoList = new ArrayList<>(skuSet);
            boolean goodsSuccess = remoteGoodsService.batchSubtractStock(operateStockDtoList);

            if (goodsSuccess) {
                //创建订单
                Order order = new Order();
                order.setId(orderId);
                order.setUserId(curUser.getUserId());
                order.setUserName(curUser.getNikeName());
                order.setUserAvatarUrl(curUser.getAvatarUrl());
                order.setUserNote(dto.getUserNote());
                order.setType(dto.getOrderType());
                order.setTotalAmount(totalAmount.add(freightAmount));

                //Todo 合计返利金额

                //Todo 合计会员优惠

                //Todo 促销优化金额=积分抵扣+优惠券+满减+会员价+会员免运费

                // 促销优化金额=优惠券+满减
                promotionAmount =
                        NumberUtil.add(promotionAmount, 0, 0);
                order.setPromotionAmount(promotionAmount);
                //应付金额（实际支付金额）=订单总金额-促销优化金额+运费
                BigDecimal payAmount = NumberUtil.sub(totalAmount, promotionAmount);
                if (payAmount.compareTo(BigDecimal.ZERO) == -1) {
                    payAmount = BigDecimal.ZERO.add(freightAmount);
                } else {
                    payAmount = payAmount.add(freightAmount);
                }
                //支付金额异常

                if (NumberUtil.isLess(payAmount, OrderConstant.MIN_PAY_FEE)) {
                    log.error("订单{},支付金额:{},总金额:{},优惠券优惠:{},满减优惠:{},运费:{}", dto.toString(), payAmount,
                            totalAmount, 0, 0, freightAmount);
                    payAmount = OrderConstant.MIN_PAY_FEE;
                }
                log.info("订单{},支付金额:{},总金额:{},优惠券优惠:{},满减优惠:{},运费:{}", dto.toString(), payAmount,
                        totalAmount, 0, 0, freightAmount);
                //Todo 是否参与活动

                //实际金额=应付金额-退款金额
                order.setDiscountsAmount(payAmount.setScale(2, BigDecimal.ROUND_DOWN));
                order.setPayAmount(payAmount.setScale(2, BigDecimal.ROUND_DOWN));
                order.setFreightAmount(freightAmount);
                order.setCouponId(dto.getCouponId());
                order.setFullScaleId(dto.getFullScaleId());
                order.setPayType(dto.getPayType());
                order.setSourceType(dto.getSourceType());
                order.setStatus(OrderStatusEnum.WAIT_FOR_PAY);

                //Todo 赠送积分

                order.setCustomForm(dto.getCustomForm());

                //Todo 会员权益
                //创建订单收货
                installOrderDelivery(dto, orderId, accountAddress);
                baseMapper.insert(order);
                for (OrderItem orderItem : orderItemList) {
                    orderItemMapper.insert(orderItem);
                }
                done = true;
            } else {
                OrderFailMessage failMessage = new OrderFailMessage().stockFail(dto.getCouponId(),
                        curUser.getUserId(), curUser.getShopId(), TenantContextHolder.getTenantId());
                sender.sendCreateOrderFailMessage(failMessage);
                orderFailed.setNxPx(orderId.toString(), "库存扣除失败", TimeConstants.ONE_DAY);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            orderFailed.setNxPx(orderId.toString(), e.getMessage(), TimeConstants.ONE_DAY);
        }
        return done;
    }

    private void shareDiscount(List<OrderItem> orderItemList) {
        BigDecimal totalRealAmount = orderItemList.stream()
                .map(OrderItem::getRealAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        for (OrderItem orderItem : orderItemList) {
            log.info("优惠分解之前的数据：" + JSONUtil.toJsonStr(orderItem));
            // Todo 优惠券

            //Todo 满减

            //Todo 会员+积分 分解阉割

            //Todo 向下舍入会出现差1分的情况

            // orderItem.setRealAmount(realAmount.setScale(2, BigDecimal.ROUND_DOWN));
            BigDecimal realAmount = NumberUtil.sub(orderItem.getRealAmount(), orderItem.getCouponAmount(),
                    orderItem.getPromotionAmount());
            orderItem.setRealAmount(realAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            if (NumberUtil.isLessOrEqual(orderItem.getRealAmount(), BigDecimal.ZERO)) {
                orderItem.setRealAmount(BigDecimal.ZERO);
            }
            log.info("优惠分解之后的数据：" + JSONUtil.toJsonStr(orderItem));
        }
    }

    private void installOrderDelivery(CreateOrderDto dto, Long orderId,
                                      MiniAccountAddress accountAddress) {
        OrderDelivery orderDelivery = new OrderDelivery();
        orderDelivery.setOrderId(orderId);
        orderDelivery.setDeliveryType(dto.getDeliverType());
        orderDelivery.setDeliveryTemplateId(dto.getDeliveryTemplateId());
        orderDelivery.setReceived(false);
        if (ObjectUtil.isNotNull(accountAddress)) {
            orderDelivery.setReceiverName(accountAddress.getUserName());
            orderDelivery.setReceiverPhone(accountAddress.getPhone());
            orderDelivery.setReceiverPostCode(accountAddress.getPostCode());
            orderDelivery.setReceiverProvince(accountAddress.getProvince());
            orderDelivery.setReceiverCity(accountAddress.getCity());
            orderDelivery.setReceiverRegion(accountAddress.getCounty());
            orderDelivery.setReceiverDetailAddress(accountAddress.getDetailInfo());
        }
        orderDeliveryMapper.insert(orderDelivery);
    }

    /**
     * 组装订单详情表
     *
     * @param itemVoList
     * @param orderId
     * @param itemDtoList
     * @return
     */
    private List<OrderItem> getOrderItemList(List<ItemVo> itemVoList, Long orderId, List<ItemDto> itemDtoList) {
        List<OrderItem> itemList = new LinkedList<>();
        Map<Long, ItemDto> itemDtoMap = itemDtoList.stream().collect(Collectors.toMap(ItemDto::getSkuId, v -> v));
        for (ItemVo itemVo : itemVoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(itemVo.getProductId());
            if (StrUtil.isNotBlank(itemVo.getProductSkuPic())) {
                orderItem.setProductPic(itemVo.getProductSkuPic());
            } else {
                orderItem.setProductPic(itemVo.getProductPic());
            }
            orderItem.setProductName(itemVo.getProductName());
            orderItem.setProductSn(itemVo.getProductSn());
            orderItem.setProductPrice(itemVo.getProductPrice());
            orderItem.setProductOriginalPrice(itemVo.getProductOriginalPrice());
            orderItem.setProductQuantity(itemVo.getProductQuantity());
            orderItem.setProductSkuId(itemVo.getProductSkuId());
            orderItem.setProductSkuCode(itemVo.getProductSkuCode());
            orderItem.setPromotionAmount(BigDecimal.ZERO);
            orderItem.setCouponAmount(BigDecimal.ZERO);
            //Todo  处理会员价逻辑

            //Todo 设置商品返利金额

            //该商品经过优惠后的最终金额
            orderItem.setRealAmount(orderItem.getProductPrice().multiply(BigDecimal.valueOf(itemVo.getProductQuantity())));

            //Todo 计算商品赠送积分

            orderItem.setSpecs(itemVo.getSpecs());
            orderItem.setProviderId(itemVo.getProviderId());
            //Todo 活动商品记录

            //Todo 计算佣金


            itemList.add(orderItem);
        }
        itemList.sort(Comparator.comparing(OrderItem::getRealAmount).reversed());
        log.info("return orderItem {}", JSONUtil.toJsonStr(itemList));
        return itemList;
    }



    /**
     * 取最高商品单价得商品信息
     *
     * @param canUseCouponOrderItem 可以使用优惠券得商品
     */
    private OrderItem useCouponGoods(List<OrderItem> canUseCouponOrderItem) {
        OrderItem orderItem = new OrderItem();
        BigDecimal maxGoodsPrice = canUseCouponOrderItem.get(0).getProductPrice();
        for (int i = 0; i < canUseCouponOrderItem.size(); i++) {
            if (maxGoodsPrice.compareTo(canUseCouponOrderItem.get(i).getProductPrice()) < 1) {
                orderItem = canUseCouponOrderItem.get(i);
            }
        }
        return orderItem;
    }



    /**
     * 根据订单类型获取订单过期时间
     *
     * @param type
     * @return long
     * @author alan
     * @date 2020/7/26 14:39
     */
    private long getExTime(OrderTypeEnum type) {
        OrderSetting orderSetting = orderSettingMapper.selectOne(null);
        if (type.equals(OrderTypeEnum.SEC_KILL)) {
            return TimeConstants.ONE_MINUTES * orderSetting.getFlashOrderOvertime();
        } else {
            return TimeConstants.ONE_MINUTES * orderSetting.getNormalOrderOvertime();
        }
    }


    /**
     * 取消订单
     *
     * @param orderId
     * @return void
     * @author alan
     * @date 2020/7/26 14:39
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        if (ObjectUtil.isNull(order)) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        if (!order.getUserId().equals(CurUserUtil.getHttpCurUser().getUserId())) {
            throw new ServiceException("您无此操作的权限");
        }
        if (!OrderStatusEnum.canCancel(order.getStatus())) {
            throw new ServiceException(OrderCode.ABNORMAL_DATA_STATUS);
        }
        cancelOrder(orderId, order, OrderStatusEnum.BUYER_CANCEL_CLOSE);
    }


    /**
     * 过期自动取消订单
     *
     * @param orderId
     * @return void
     * @author alan
     * @date 2020/7/26 14:39
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoCancelOrder(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        if (ObjectUtil.isNull(order)) {
            return;
        }
        if (!OrderStatusEnum.canCancel(order.getStatus())) {
            return;
        }
        cancelOrder(orderId, order, OrderStatusEnum.BUYER_PAY_TIMEOUT_CLOSE);
    }

    /**
     * Cancel order.
     *
     * @param orderId the order id
     * @param order   the order
     * @param status  the status
     */
    public void cancelOrder(Long orderId, Order order, OrderStatusEnum status) {
        //Todo 归还优惠券

        //Todo 归还积分
        order.setStatus(status);
        order.setCloseTime(LocalDateTime.now());
        List<OperateStockDto> operateStockDtoList =
                orderItemMapper.selectItemDtoByOrderIds(Collections.singletonList(orderId));
        log.info("归还库存参数:{}", operateStockDtoList.toString());
        Boolean success = remoteGoodsService.batchRevertStock(operateStockDtoList);
        if (!success) {
            OrderFailMessage failMessage = new OrderFailMessage().stockFail(order.getCouponId()
                    , order.getUserId(), order.getShopId(), order.getTenantId());
            sender.sendCancelFailOrderMessage(failMessage);
            throw new ServiceException("库存归还失败，请稍后重试");
        }
        baseMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayResultDto payOrder(Long orderId, Boolean userBalance, HttpServletRequest request) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        //查询会员持有的积分、收货地址
        AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(curUserDto.getUserId(), Arrays.asList(2,
                3));
        log.info("当前用户信息:" + curUserDto.toString());
        //检查账号
        checkAccount(accountInfoDto);
        OrderVo orderVo = baseMapper.selectOrderVoById(orderId);
        if (ObjectUtil.isNull(orderVo)) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        if (!OrderStatusEnum.canPay(orderVo.getStatus())) {
            throw new ServiceException("当前状态不能支付此订单");
        }
        if (NumberUtil.isLess(orderVo.getPayAmount(), OrderConstant.MIN_PAY_FEE)) {
            throw new ServiceException("支付金额有误");
        }
        log.info("进入支付订单===================>");
        //发送到期自动取消消息
        BaseOrderMessage message =
                new BaseOrderMessage().setOrderId(orderId).setTenantId(TenantContextHolder.getTenantId()).setShopId(ShopContextHolder.getShopId());
        sender.sendAutoCancelOrderMessage(message, getExTime(orderVo.getType()));
        log.info("超时取消订单成功===================>");
        orderVo.setExpireTime(orderVo.getCreateTime().plusSeconds(getExTime(orderVo.getType()) / 1000));
        updateById(orderVo);
        if (orderVo.getPayType().equals(PayTypeEnum.WECHAT) || orderVo.getPayType().equals(PayTypeEnum.WECHAT_H5)) {
            return userWechat(request, curUserDto, orderVo);
        }
        //Todo 余额支付
        return null;

    }

    /**
     * 使用微信支付
     *
     * @param request
     * @param curUserDto
     * @param orderVo
     * @return com.medusa.gruul.payment.api.model.dto.PayResultDto
     * @author alan
     * @date 2020/8/12 21:15
     */
    private PayResultDto userWechat(HttpServletRequest request, CurUserDto curUserDto, OrderVo orderVo) {
        ShopConfigDto shopConfig = remoteMiniInfoService.getShopConfig(orderVo.getTenantId());
        if (shopConfig == null) {
            throw new ServiceException("商户配置不存在");
        }
        PayInfoVo payInfo = shopConfig.getPayInfo();
        if (payInfo == null) {
            throw new ServiceException("支付配置不存在");
        }
        Integer payType = payInfo.getPayType();
        PayRequestDto dto = new PayRequestDto();
        dto.setTenantId(orderVo.getTenantId());
        dto.setPayChannel(payType);
        if (payType.equals(1)) {
            if (orderVo.getPayType().equals(PayTypeEnum.WECHAT)) {
                dto.setTradeType(1);
            }
            if (orderVo.getPayType().equals(PayTypeEnum.WECHAT_H5)) {
                dto.setTradeType(4);
            }
        } else if (payType.equals(2)) {
            dto.setTradeType(101);
        } else if (payType.equals(3)) {
            dto.setTradeType(201);
        } else if (payType.equals(4)) {
            dto.setTradeType(301);
        }
        dto.setOutTradeNo(orderVo.getId().toString());
        dto.setRouteKey(OrderQueueEnum.QUEUE_ORDER_PAYMENT_NOTIFY.getRouteKey());
        dto.setOpenId(curUserDto.getOpenId());
        dto.setTotalFee(orderVo.getPayAmount());
        dto.setTerminalIp(IPUtils.getIpAddr(request));
        dto.setFeeType("CNY");
        if (orderVo.getType() == OrderTypeEnum.SEC_KILL) {
            dto.setTimeoutExpress("30m");
        } else {
            dto.setTimeoutExpress("6h");
        }
        dto.setBody("小程序购物");
        dto.setSubject("");
        PayResultDto resultDto = remotePaymentService.payRequest(dto);
        if (ObjectUtil.isNull(resultDto)
                || ObjectUtil.isNull(resultDto.getWxResult())
                || ObjectUtil.isNull(resultDto.getWxResult().getTransactionId())) {
            log.error("支付创建失败,入参为: {}", dto.toString());
            log.error("支付创建失败,出参为: {}", resultDto.toString());

        }
        orderVo.setTransactionId(resultDto.getWxResult().getTransactionId());
        this.updateById(orderVo);
        return resultDto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentNotify(Long orderId, String tenantId) {
        TenantContextHolder.setTenantId(tenantId);
        ShopContextHolder.setShopId(null);
        Order order = baseMapper.selectOne(new QueryWrapper<Order>().lambda().eq(Order::getId, orderId).last("limit " +
                "1"));
        TenantContextHolder.setTenantId(order.getTenantId());
        ShopContextHolder.setShopId(order.getShopId());
        order.setStatus(OrderStatusEnum.WAIT_FOR_SEND);
        order.setPayTime(LocalDateTime.now());
        order.setPayType(PayTypeEnum.WECHAT);
        baseMapper.updateById(order);
        OrderVo vo = baseMapper.selectOrderVoById(order.getId());
        sender.sendPayedOrderMessage(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiptOrder(Long orderId, boolean isSystem) {
        Order order = baseMapper.selectById(orderId);
        OrderSetting orderSetting = orderSettingMapper.selectOne(null);
        if (ObjectUtil.isNull(order)) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        if (!isSystem) {
            if (!order.getUserId().equals(CurUserUtil.getHttpCurUser().getUserId())) {
                throw new ServiceException("您无此操作的权限");
            }
        }
        if (!OrderStatusEnum.canReceipt(order.getStatus())) {
            throw new ServiceException("当前状态不能确认此订单");
        }
        List<AfsOrder> afsOrderList = afsOrderMapper.selectProgressByOrderId(orderId);
        for (AfsOrder afsOrder : afsOrderList) {
            if (!afsOrder.getStatus().equals(AfsOrderStatusEnum.WAIT_FOR_BUSINESS_APPROVED) && !isSystem) {
                throw new ServiceException("当前订单有正在进行中的售后订单");
            }
        }
        order.setStatus(OrderStatusEnum.WAIT_FOR_COMMENT);
        baseMapper.updateById(order);
        OrderDelivery orderDelivery = orderDeliveryMapper.selectById(orderId);
        orderDelivery.setReceived(true);
        orderDelivery.setReceiveTime(LocalDateTime.now());
        orderDeliveryMapper.updateById(orderDelivery);
        //到期未评价自动完成订单
        BaseOrderMessage message =
                new BaseOrderMessage().setOrderId(orderId).setTenantId(order.getTenantId()).setShopId(order.getShopId());
        sender.sendAutoCompletedOrderMessage(message, orderSetting.getFinishOvertime() * TimeConstants.ONE_DAY);
        OrderVo vo = orderInfo(order.getId());
        sender.sendReceiptOrderMessage(vo);
    }

    @Override
    public Integer checkOrder(GroupOrderResultDto dto) {
        //先查询已经失败的订单里面有没有
        OrderFailedRedisKey orderFailed = new OrderFailedRedisKey();
        String failMsg = orderFailed.get(dto.getOrderId().toString());
        if (StrUtil.isNotBlank(failMsg)) {
            throw new ServiceException(failMsg);
        }
        //再查询库存是否已经售罄
        Collection<SkuStock> skuStockList = remoteGoodsService.findSkuStockListByIds(dto.getSkuIdSet());
        Map<Long, SkuStock> skuStockMap = null;
        if (CollUtil.isNotEmpty(skuStockList)) {
            skuStockMap = skuStockList.stream().collect(Collectors.toMap(SkuStock::getId, v -> v));
        }
        for (ItemDto itemDto : dto.getItems()) {
            SkuStock skuStock = skuStockMap.get(itemDto.getSkuId());
            if (ObjectUtil.isNotNull(skuStock)) {
                boolean hasStock = skuStock.getStock() > itemDto.getNumber();
                if (!hasStock) {
                    return -1;
                }
            } else {
                return -1;

            }

        }

        return 0;
    }

    @Override
    public PageUtils<ApiOrderVo> searchOrder(ApiSearchOrderDto dto) {
        List<Integer> orderStatusList = new ArrayList<>(4);
        boolean searchAfterOrder = false;
        //订单状态 -1：所有订单, 0.待付款（待买家付款）, 1.待发货（买家已付款）, 2.配送中（卖家已发货）, 3.待提货（商家直配已到达提货点或物流订单已发货）, 4.已完成（用户已经签收）, 5.待评价, 6
        // .售后订单, 7.已关闭
        switch (dto.getOrderStatus()) {
            case -1:
                orderStatusList.clear();
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
                orderStatusList.add(OrderStatusEnum.COMPLETE.getCode());
                break;
            case 5:
                orderStatusList.add(OrderStatusEnum.WAIT_FOR_COMMENT.getCode());
                break;
            case 6:
                searchAfterOrder = true;
                break;
            case 7:
                orderStatusList.add(OrderStatusEnum.REFUNDED.getCode());
                orderStatusList.add(OrderStatusEnum.PART_REFUNDED.getCode());
                orderStatusList.add(OrderStatusEnum.BUYER_PAY_TIMEOUT_CLOSE.getCode());
                orderStatusList.add(OrderStatusEnum.BUYER_CANCEL_CLOSE.getCode());
                orderStatusList.add(OrderStatusEnum.SELLER_CANCEL_CLOSE.getCode());
                orderStatusList.add(OrderStatusEnum.EXCHANGE_SUCCESS_CLOSE.getCode());
                orderStatusList.add(OrderStatusEnum.EXCHANGE_CANCEL_CLOSE.getCode());
                break;
            default:
                break;
        }

        Page<ApiOrderVo> page = baseMapper.searchApiOrderVoPage(new Page(dto.getCurrent(), dto.getSize()),
                orderStatusList, searchAfterOrder, CurUserUtil.getHttpCurUser().getUserId());
        for (ApiOrderVo record : page.getRecords()) {
            if (ObjectUtil.isNull(record.getExpireTime())) {
                record.setExpireTime(record.getCreateTime().plusSeconds(getExTime(record.getType()) / 1000));
            }
            if (record.getType().equals(OrderTypeEnum.EXCHANGE)) {
                Long originalOrderId = afsOrderMapper.selectOriginalOrderIdByOrderId(record.getOrderId());
                record.setOriginalOrderId(originalOrderId);
            }
        }
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void evaluateOrder(ApiOrderEvaluateDto dto) {
        OrderVo orderVo = baseMapper.selectOrderVoById(dto.getOrderId());
        if (ObjectUtil.isNull(orderVo)) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        if (!orderVo.getUserId().equals(CurUserUtil.getHttpCurUser().getUserId())) {
            throw new ServiceException("您无此操作的权限");
        }
        if (!OrderStatusEnum.canEvaluate(orderVo.getStatus())) {
            throw new ServiceException("当前状态不能评价此订单");
        }
        AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(CurUserUtil.getHttpCurUser().getUserId()
                , Arrays.asList(2, 3));
        if (CollUtil.isNotEmpty(accountInfoDto.getRestrictTypes()) && accountInfoDto.getRestrictTypes().contains(BlacklistEnum.REJECT_COMMENT.getType())) {
            throw new ServiceException("账号异常请联系客服");
        }
        String defaultComment = "此用户没有填写评价";
        OrderEvaluate orderEvaluate = new OrderEvaluate();
        orderEvaluate.setOrderId(orderVo.getId());
        orderEvaluate.setUserId(orderVo.getUserId());
        orderEvaluate.setUserName(orderVo.getUserName());
        orderEvaluate.setUserAvatarUrl(orderVo.getUserAvatarUrl());
        orderEvaluate.setShopRate(dto.getShopRate());
        orderEvaluateMapper.insert(orderEvaluate);
        Map<Long, ApiOrderProductEvaluateDto> productEvaluateMap =
                dto.getProductEvaluateDtoList().stream().collect(Collectors.toMap(ApiOrderProductEvaluateDto::getProductSkuId, v -> v));
        for (OrderItem orderItem : orderVo.getOrderItemList()) {
            ApiOrderProductEvaluateDto productEvaluateDto = productEvaluateMap.get(orderItem.getProductSkuId());
            OrderProductEvaluate productEvaluate = new OrderProductEvaluate();
            productEvaluate.setOrderId(orderVo.getId());
            productEvaluate.setProductId(orderItem.getProductId());
            productEvaluate.setProductSkuId(orderItem.getProductSkuId());
            productEvaluate.setProductPic(orderItem.getProductPic());
            productEvaluate.setProductQuantity(orderItem.getProductQuantity());
            productEvaluate.setProductPrice(orderItem.getProductPrice());
            productEvaluate.setProductName(orderItem.getProductName());
            productEvaluate.setSpecs(orderItem.getSpecs());
            if (StrUtil.isNotBlank(productEvaluateDto.getComment())) {
                productEvaluate.setComment(productEvaluateDto.getComment());
            } else {
                productEvaluate.setComment(defaultComment);
            }
            productEvaluate.setPicture(productEvaluateDto.getPicture());
            productEvaluate.setRate(productEvaluateDto.getRate());
            productEvaluateMapper.insert(productEvaluate);
        }
        completeOrder(orderVo.getId());
    }

    @Override
    public void evaluateOrder(Long orderId) {
        OrderVo orderVo = baseMapper.selectOrderVoById(orderId);
        OrderEvaluate orderEvaluate =
                orderEvaluateMapper.selectOne(new LambdaQueryWrapper<OrderEvaluate>().eq(OrderEvaluate::getOrderId,
                        orderVo.getId()));
        if (ObjectUtil.isNull(orderEvaluate)) {
            String defaultComment = "系统自动好评";
            orderEvaluate = new OrderEvaluate();
            orderEvaluate.setOrderId(orderVo.getId());
            orderEvaluate.setUserId(orderVo.getUserId());
            orderEvaluate.setUserName(orderVo.getUserName());
            orderEvaluate.setUserAvatarUrl(orderVo.getUserAvatarUrl());
            orderEvaluate.setShopRate(5);
            orderEvaluateMapper.insert(orderEvaluate);
            for (OrderItem orderItem : orderVo.getOrderItemList()) {
                OrderProductEvaluate productEvaluate = new OrderProductEvaluate();
                productEvaluate.setOrderId(orderVo.getId());
                productEvaluate.setProductId(orderItem.getProductId());
                productEvaluate.setProductSkuId(orderItem.getProductSkuId());
                productEvaluate.setProductPic(orderItem.getProductPic());
                productEvaluate.setProductQuantity(orderItem.getProductQuantity());
                productEvaluate.setProductPrice(orderItem.getProductPrice());
                productEvaluate.setProductName(orderItem.getProductName());
                productEvaluate.setSpecs(orderItem.getSpecs());
                productEvaluate.setComment(defaultComment);
                productEvaluate.setRate(5);
                productEvaluateMapper.insert(productEvaluate);
            }
            completeOrder(orderVo.getId());
        }

    }

    @Override
    public OrderVo orderInfo(Long orderId) {
        OrderVo vo = baseMapper.selectOrderVoById(orderId);
        if (vo.getStatus().equals(OrderStatusEnum.WAIT_FOR_PAY) && ObjectUtil.isNull(vo.getExpireTime())) {
            vo.setExpireTime(vo.getCreateTime().plusSeconds(getExTime(vo.getType()) / 1000));
        }
        vo.setProductTotalQuantity(vo.getOrderItemList().stream().mapToInt(OrderItem::getProductQuantity).sum());
        return vo;
    }

    @Override
    public PageUtils searchOrderEvaluate(ApiSearchEvaluateDto dto) {
        Page<ManageEvaluateVo> page = orderEvaluateMapper.userSearchOrderEvaluate(new Page(dto.getCurrent(),
                dto.getSize()), CurUserUtil.getHttpCurUser().getUserId());
        return new PageUtils(page);
    }

    @Override
    public void completeOrder(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        if (ObjectUtil.isNull(order)) {
            return;
        }
        if (!OrderStatusEnum.canCompleted(order.getStatus())) {
            return;
        }
        order.setStatus(OrderStatusEnum.COMPLETE);
        order.setCompleteTime(LocalDateTime.now());
        baseMapper.updateById(order);
        sender.sendCompletedOrderMessage(orderInfo(orderId));

    }

    @Override
    public CountCostDto getFreightAmount(GetCostDto dto) {
        log.info("GetCostDto is {}", ObjectUtil.isNotNull(dto) ? dto.toString() : "");
        CountCostDto dtoResult = new CountCostDto();
        TimeInterval timer = DateUtil.timer();
        if (dto.getType().equals(1)) {
            List<LogisticsFreightDto> freightDtoList = new ArrayList<>();
            List<ItemVo> itemVos = remoteGoodsService.findItemVoByIds(dto.getItemSkuIds());
            Map<Long, Integer> numberMap = dto.getItems().stream().collect(Collectors.toMap(ItemDto::getSkuId,
                    ItemDto::getNumber));
            for (ItemVo itemVo : itemVos) {
                // 商品包邮 跳出当前add追加
                if (itemVo.getFreightTemplateId() == 0) {
                    continue;
                }
                LogisticsFreightDto freightDto = new LogisticsFreightDto();
                freightDto.setFreightTemplateId(itemVo.getFreightTemplateId());
                freightDto.setPrice(itemVo.getProductPrice());
                freightDto.setWeight(itemVo.getWeight());
                freightDto.setNum(numberMap.get(itemVo.getProductSkuId()));

                freightDtoList.add(freightDto);
            }

            log.info("getLogisticsFreightCalculation param is {} {}", JSONUtil.toJsonStr(freightDtoList),
                    dto.getRegion());
            BigDecimal cost = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(freightDtoList)) {
                cost = remoteLogisticsFeginService.getLogisticsFreightCalculation(JSONUtil.toJsonStr(freightDtoList),
                        dto.getRegion());
            }

            log.info("getLogisticsFreightCalculation result is {}", cost);
            log.info("查询快递运费耗时{}ms", timer.intervalRestart());
            dtoResult = new CountCostDto();
            dtoResult.setCost(cost);
        }
        return dtoResult;

    }

    @Override
    public OrderOverviewVo orderOverview() {
        List<OrderStatusEnum> orderStatusEnumList = Arrays.asList(OrderStatusEnum.WAIT_FOR_PAY,
                OrderStatusEnum.SHIPPED, OrderStatusEnum.WAIT_FOR_PICKUP);
        List<Order> orderList = baseMapper.selectList(new QueryWrapper<Order>().lambda()
                .in(Order::getStatus, orderStatusEnumList)
                .eq(Order::getUserId, CurUserUtil.getHttpCurUser().getUserId())
        );
        OrderOverviewVo vo = new OrderOverviewVo();
        vo.setWaitForPay(orderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.WAIT_FOR_PAY).count());
        vo.setShipped(orderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.SHIPPED).count());
        vo.setWaitForPickup(orderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.WAIT_FOR_PICKUP).count());
        return vo;
    }

    @Override
    public OrderOverviewVo orderOverview(String userId) {
        List<OrderStatusEnum> orderStatusEnumList = Arrays.asList(OrderStatusEnum.WAIT_FOR_PAY,
                OrderStatusEnum.SHIPPED, OrderStatusEnum.WAIT_FOR_PICKUP, OrderStatusEnum.WAIT_FOR_SEND);
        List<Order> orderList = baseMapper.selectList(new QueryWrapper<Order>().lambda()
                .in(Order::getStatus, orderStatusEnumList)
                .eq(Order::getUserId, userId)
                .notIn(Order::getType, OrderTypeEnum.REPLENISH)
        );
        OrderOverviewVo vo = new OrderOverviewVo();
        List<AfsOrder> afsOrders = afsOrderMapper.selectList(new LambdaQueryWrapper<AfsOrder>()
                .eq(AfsOrder::getUserId, userId));
        long afsOrdersNumber = 0;
        if (CollectionUtil.isNotEmpty(afsOrders)) {
            //计算售后订单未处理个数
            long count = afsOrders.stream().filter(afsOrder -> afsOrder.getStatus() != AfsOrderStatusEnum.SUCCESS && afsOrder.getStatus() != AfsOrderStatusEnum.CLOSE).count();
            afsOrdersNumber = count;
        }

        vo.setWaitForPay(orderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.WAIT_FOR_PAY).count());
        vo.setShipped(orderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.SHIPPED).count());
        vo.setWaitForPickup(orderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.WAIT_FOR_PICKUP).count());
        vo.setWithDelivery(orderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.WAIT_FOR_SEND).count());
        vo.setAfsOrder(afsOrdersNumber);
        return vo;
    }

    @Override
    public OrderShareInfo orderShareInfo(Long orderId) {
        OrderShareSetting setting = orderShareSettingService.getSetting();
        OrderDelivery orderDelivery = orderDeliveryMapper.selectByOrderId(orderId);
        List<SimpleOrderItemVo> orderItemVos = orderItemMapper.selectSimpleOrderItemVoByOrderId(orderId);
        OrderShareInfo orderShareInfo = new OrderShareInfo();
        if (ObjectUtil.isNull(setting)) {
            throw new ServiceException("获取分享设置失败，请联系商家检查分享设置");
        }
        orderShareInfo.setTitle(setting.getTitle().replace("{sname}", orderDelivery.getReceiverName()));
        orderShareInfo.setBackground(setting.getBackground());
        List<ShareItemVo> shareItemVos = new ArrayList<>();
        for (SimpleOrderItemVo orderItemVo : orderItemVos) {
            ShareItemVo shareItemVo = new ShareItemVo();
            shareItemVo.setProductName(orderItemVo.getProductName());
            shareItemVo.setProductPic(orderItemVo.getProductPic());
            shareItemVo.setProductQuantity(orderItemVo.getProductQuantity());
            shareItemVos.add(shareItemVo);
        }
        orderShareInfo.setItemList(shareItemVos);
        return orderShareInfo;
    }


    @Override
    public void refundNotify(RefundNotifyResultDto message) {
        log.info(message.toString());
        TenantContextHolder.setTenantId(message.getTenantId());
//        Order order = baseMapper.selectById(message.getOutTradeNo());  查询错误
        Order order = baseMapper.selectOne(new QueryWrapper<Order>().lambda().eq(Order::getTransactionId, message.getOutTradeNo()));
        order.setRefundTransactionId(message.getRefundId());
        baseMapper.updateById(order);
        OrderVo vo = baseMapper.selectOrderVoById(order.getId());
        sender.sendRefundSuccess(vo);
    }


}
