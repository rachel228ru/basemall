package com.medusa.gruul.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.order.api.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MiniOrderServiceImplTest {

    private void checkIntegral(BigDecimal totalAmount, BigDecimal integral, BigDecimal allIntegral) {
        //Todo 积分营销阉割
    }


    private BigDecimal handleIntegralAmount(BigDecimal totalAmount, BigDecimal integral, BigDecimal allIntegral) {
        //Todo 积分营销阉割
        return BigDecimal.ZERO;
    }


    @Test
    public void testIntegral() {
        checkIntegral(BigDecimal.valueOf(50), BigDecimal.valueOf(75), BigDecimal.valueOf(2000));
        System.out.println(handleIntegralAmount(BigDecimal.valueOf(50), BigDecimal.valueOf(75),
                BigDecimal.valueOf(2000)));
    }

//    public void checkCoupon(List<OrderItem> orderItemList, MemberCouponVo memberCouponVo, BigDecimal totalAmount) {
//        if (ObjectUtil.isNull(memberCouponVo)) {
//            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
//        }
//        // 0-无门槛折扣劵  1-无门槛现金劵  2-满折劵  3-满减劵
//        switch (memberCouponVo.getCouponType()) {
//            case 0:
//            case 1:
//                break;
//            case 2:
//            case 3:
//                //总金额小于满减金额
//                if (totalAmount.compareTo(memberCouponVo.getConditionAmount()) == -1) {
//                    throw new ServiceException("优惠券不满足使用条件:订单总金额未满足");
//                }
//                break;
//            default:
//                throw new ServiceException(SystemCode.PARAM_VALID_ERROR);
//        }
//        //适用商品：  0-全部商品参与  1-指定商品参与  2-指定商品不参与
//        Set<Long> couponProductIds;
//        Set<Long> productIds = orderItemList.stream().map(OrderItem::getProductId).collect(Collectors.toSet());
//        switch (memberCouponVo.getProductType()) {
//            case 0:
//                break;
//            case 1:
//                couponProductIds =
//                        memberCouponVo.getCouponProducts().stream().map(CouponProductVo::getProductId).collect(Collectors.toSet());
//                if (!CollUtil.containsAny(couponProductIds, productIds)) {
//                    throw new ServiceException("优惠券不满足使用条件:订单未包含指定参与的商品");
//                }
//                break;
//            case 2:
//                couponProductIds =
//                        memberCouponVo.getCouponProducts().stream().map(CouponProductVo::getProductId).collect(Collectors.toSet());
//                if (CollUtil.containsAny(couponProductIds, productIds)) {
//                    throw new ServiceException("优惠券不满足使用条件:订单包含指定不参与的商品");
//                }
//                break;
//            default:
//                throw new ServiceException(SystemCode.PARAM_VALID_ERROR);
//
//        }
//    }

//    private BigDecimal handleCouponAmount(List<OrderItem> orderItemList, MemberCouponVo memberCouponVo) {
//        BigDecimal couponAmount = BigDecimal.ZERO;
//        if (ObjectUtil.isNull(memberCouponVo)) {
//            throw new ServiceException("优惠券不满足使用条件:优惠券不可用");
//        }
//        //适用商品：  0-全部商品参与  1-指定商品参与  2-指定商品不参与
//        Set<Long> couponProductIds;
//        List<OrderItem> canUseCouponOrderItem = null;
//        switch (memberCouponVo.getProductType()) {
//            case 0:
//                canUseCouponOrderItem = orderItemList;
//                break;
//            case 1:
//                couponProductIds =
//                        memberCouponVo.getCouponProducts().stream().map(CouponProductVo::getProductId).collect(Collectors.toSet());
//                canUseCouponOrderItem =
//                        orderItemList.stream().filter(orderItem -> couponProductIds.contains(orderItem.getProductId())).collect(Collectors.toList());
//                break;
//            case 2:
//                couponProductIds =
//                        memberCouponVo.getCouponProducts().stream().map(CouponProductVo::getProductId).collect(Collectors.toSet());
//                canUseCouponOrderItem =
//                        orderItemList.stream().filter(orderItem -> !couponProductIds.contains(orderItem.getProductId())).collect(Collectors.toList());
//                break;
//            default:
//                break;
//        }
//        if (CollUtil.isEmpty(canUseCouponOrderItem)) {
//            throw new ServiceException("优惠券不满足使用条件:无符合条件的商品");
//        }
//        OrderItem useCouponOrderItem = null;
//        //优惠劵类型：  0-无门槛折扣劵  1-无门槛现金劵  2-满折劵  3-满减劵
//        switch (memberCouponVo.getCouponType()) {
//            case 0:
//                useCouponOrderItem = CollUtil.getFirst(canUseCouponOrderItem);
//                couponAmount = NumberUtil.mul(useCouponOrderItem.getRealAmount(),
//                        NumberUtil.div(memberCouponVo.getDiscount(), BigDecimal.TEN));
//                couponAmount = NumberUtil.sub(useCouponOrderItem.getRealAmount(), couponAmount);
//                useCouponOrderItem.setCouponAmount(couponAmount);
//                break;
//            case 1:
//                useCouponOrderItem = CollUtil.getFirst(canUseCouponOrderItem);
//                couponAmount = memberCouponVo.getDiscountAmount();
//                useCouponOrderItem.setCouponAmount(couponAmount);
//                break;
//            case 2:
//                canUseCouponOrderItem =
//                        canUseCouponOrderItem.stream().filter(orderItem -> orderItem.getRealAmount().compareTo(memberCouponVo.getConditionAmount()) != -1).collect(Collectors.toList());
//                useCouponOrderItem = CollUtil.getFirst(canUseCouponOrderItem);
//                couponAmount = NumberUtil.mul(useCouponOrderItem.getRealAmount(),
//                        NumberUtil.div(memberCouponVo.getDiscount(), BigDecimal.TEN));
//                couponAmount = NumberUtil.sub(useCouponOrderItem.getRealAmount(), couponAmount);
//                useCouponOrderItem.setCouponAmount(couponAmount);
//                break;
//            case 3:
//                canUseCouponOrderItem =
//                        canUseCouponOrderItem.stream().filter(orderItem -> orderItem.getRealAmount().compareTo(memberCouponVo.getConditionAmount()) != -1).collect(Collectors.toList());
//                useCouponOrderItem = CollUtil.getFirst(canUseCouponOrderItem);
//                couponAmount = memberCouponVo.getDiscountAmount();
//                useCouponOrderItem.setCouponAmount(couponAmount);
//                break;
//            default:
//                break;
//        }
//        log.info("当前优惠的商品：{},优惠前的价格：{},优惠：{}", memberCouponVo.toString(), useCouponOrderItem.getRealAmount(),
//                couponAmount);
//        return couponAmount;
//
//    }

    @Test
    public void testCoupon() {
//        List<OrderItem> orderItemList = new ArrayList<>();
//        for (Long i = 1L; i < 10; i++) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setProductId(i);
//            orderItem.setRealAmount(NumberUtil.mul(i, BigDecimal.valueOf(100)));
//            orderItemList.add(orderItem);
//        }
//        orderItemList.sort(Comparator.comparing(OrderItem::getRealAmount).reversed());
//
//        List<CouponProductVo> canUseVos1 = new ArrayList<>();
//        for (Long i = 4L; i < 7; i++) {
//            CouponProductVo vo = new CouponProductVo();
//            vo.setProductId(i);
//            canUseVos1.add(vo);
//        }
//
//        List<CouponProductVo> cantUseVos1 = new ArrayList<>();
//        for (Long i = 1L; i < 3; i++) {
//            CouponProductVo vo = new CouponProductVo();
//            vo.setProductId(i);
//            cantUseVos1.add(vo);
//        }
//        List<CouponProductVo> cantUseVos2 = new ArrayList<>();
//        CouponProductVo vo = new CouponProductVo();
//        vo.setProductId(11L);
//        cantUseVos2.add(vo);
//

        //MemberCouponVo couponVo00 = new MemberCouponVo();
        //couponVo00.setCouponType(0);
        //couponVo00.setProductType(0);
        //couponVo00.setDiscount(BigDecimal.valueOf(4));
        //checkCoupon(orderItemList, couponVo00, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo00);

        //MemberCouponVo couponVo01 = new MemberCouponVo();
        //couponVo01.setCouponType(0);
        //couponVo01.setProductType(1);
        //couponVo01.setDiscount(BigDecimal.valueOf(8));
        //couponVo01.setCouponProducts(canUseVos1);
        //checkCoupon(orderItemList, couponVo01, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo01);

        //MemberCouponVo couponVo02 = new MemberCouponVo();
        //couponVo02.setCouponType(0);
        //couponVo02.setProductType(2);
        //couponVo02.setDiscount(BigDecimal.valueOf(4));
        //couponVo02.setCouponProducts(cantUseVos1);
        //try {
        //    checkCoupon(orderItemList, couponVo02, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        //    (BigDecimal.ZERO, BigDecimal::add));
        //    handleCouponAmount(orderItemList, couponVo02);
        //} catch (ServiceException e) {
        //    Assert.assertEquals(e.getMessage(), "优惠券不满足使用条件:订单包含指定不参与的商品");
        //}
        //couponVo02.setCouponProducts(cantUseVos2);
        //checkCoupon(orderItemList, couponVo02, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo02);
        //
        //
//        MemberCouponVo couponVo10 = new MemberCouponVo();
//        couponVo10.setCouponType(1);
//        couponVo10.setProductType(0);
//        couponVo10.setDiscountAmount(BigDecimal.valueOf(200));
//        checkCoupon(orderItemList, couponVo10,
//                orderItemList.stream().map(OrderItem::getRealAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
//        handleCouponAmount(orderItemList, couponVo10);
        //
        //MemberCouponVo couponVo11 = new MemberCouponVo();
        //couponVo11.setCouponType(1);
        //couponVo11.setProductType(1);
        //couponVo11.setDiscountAmount(BigDecimal.valueOf(200));
        //couponVo11.setCouponProducts(canUseVos1);
        //checkCoupon(orderItemList, couponVo11, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo11);
        //
        //MemberCouponVo couponVo12 = new MemberCouponVo();
        //couponVo12.setCouponType(1);
        //couponVo12.setProductType(2);
        //couponVo12.setDiscountAmount(BigDecimal.valueOf(200));
        //couponVo12.setCouponProducts(cantUseVos1);
        //try {
        //    checkCoupon(orderItemList, couponVo12, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        //    (BigDecimal.ZERO, BigDecimal::add));
        //    handleCouponAmount(orderItemList, couponVo12);
        //} catch (ServiceException e) {
        //    Assert.assertEquals(e.getMessage(), "优惠券不满足使用条件:订单包含指定不参与的商品");
        //}
        //couponVo12.setCouponProducts(cantUseVos2);
        //checkCoupon(orderItemList, couponVo12, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo12);
        //
        //
        //MemberCouponVo couponVo20 = new MemberCouponVo();
        //couponVo20.setCouponType(2);
        //couponVo20.setProductType(0);
        //couponVo20.setDiscount(BigDecimal.valueOf(4));
        //couponVo20.setConditionAmount(BigDecimal.valueOf(200));
        //checkCoupon(orderItemList, couponVo20, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo20);
        //
        //
        //MemberCouponVo couponVo21 = new MemberCouponVo();
        //couponVo21.setCouponType(2);
        //couponVo21.setProductType(1);
        //couponVo21.setDiscount(BigDecimal.valueOf(4));
        //couponVo21.setConditionAmount(BigDecimal.valueOf(200));
        //couponVo21.setCouponProducts(canUseVos1);
        //checkCoupon(orderItemList, couponVo21, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo21);
        //
        //MemberCouponVo couponVo22 = new MemberCouponVo();
        //couponVo22.setCouponType(2);
        //couponVo22.setProductType(2);
        //couponVo22.setDiscount(BigDecimal.valueOf(4));
        //couponVo22.setConditionAmount(BigDecimal.valueOf(200));
        //couponVo22.setCouponProducts(cantUseVos1);
        //try {
        //    checkCoupon(orderItemList, couponVo22, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        //    (BigDecimal.ZERO, BigDecimal::add));
        //    handleCouponAmount(orderItemList, couponVo22);
        //} catch (ServiceException e) {
        //    Assert.assertEquals(e.getMessage(), "优惠券不满足使用条件:订单包含指定不参与的商品");
        //}
        //couponVo22.setCouponProducts(cantUseVos2);
        //checkCoupon(orderItemList, couponVo22, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo22);
        //
        //
        //MemberCouponVo couponVo30 = new MemberCouponVo();
        //couponVo30.setCouponType(3);
        //couponVo30.setProductType(0);
        //couponVo30.setDiscountAmount(BigDecimal.valueOf(200));
        //couponVo30.setConditionAmount(BigDecimal.valueOf(200));
        //checkCoupon(orderItemList, couponVo30, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo30);
        //
        //MemberCouponVo couponVo31 = new MemberCouponVo();
        //couponVo31.setCouponType(3);
        //couponVo31.setProductType(1);
        //couponVo31.setDiscountAmount(BigDecimal.valueOf(200));
        //couponVo31.setConditionAmount(BigDecimal.valueOf(200));
        //couponVo31.setCouponProducts(canUseVos1);
        //checkCoupon(orderItemList, couponVo31, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo31);
        //
        //MemberCouponVo couponVo32 = new MemberCouponVo();
        //couponVo32.setCouponType(3);
        //couponVo32.setProductType(2);
        //couponVo32.setDiscountAmount(BigDecimal.valueOf(200));
        //couponVo32.setConditionAmount(BigDecimal.valueOf(200));
        //couponVo32.setCouponProducts(cantUseVos1);
        //try {
        //    checkCoupon(orderItemList, couponVo32, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        //    (BigDecimal.ZERO, BigDecimal::add));
        //    handleCouponAmount(orderItemList, couponVo32);
        //} catch (ServiceException e) {
        //    Assert.assertEquals(e.getMessage(), "优惠券不满足使用条件:订单包含指定不参与的商品");
        //}
        //couponVo32.setCouponProducts(cantUseVos2);
        //checkCoupon(orderItemList, couponVo32, orderItemList.stream().map(OrderItem::getRealAmount).reduce
        // (BigDecimal.ZERO, BigDecimal::add));
        //handleCouponAmount(orderItemList, couponVo32);
    }

//    private BigDecimal handleFullScaleAmount(List<OrderItem> orderItemList, FullScaleVo fullScaleVo) {
//        BigDecimal fullScaleAmount = BigDecimal.ZERO;
//        if (ObjectUtil.isNull(fullScaleVo)) {
//            throw new ServiceException("活动不满足使用条件:活动不可用");
//        }
//        //适用商品：  0-全部商品参与  1-指定商品参与  2-指定商品不参与
//        Set<Long> couponProductIds;
//        List<OrderItem> canUseFullScaleOrderItem = null;
//        switch (fullScaleVo.getProductType()) {
//            case 0:
//                canUseFullScaleOrderItem = orderItemList;
//                break;
//            case 1:
//                couponProductIds =
//                        fullScaleVo.getFullScaleProducts().stream().map(FullScaleProductVo::getProductId).collect(Collectors.toSet());
//                canUseFullScaleOrderItem =
//                        orderItemList.stream().filter(orderItem -> couponProductIds.contains(orderItem.getProductId())).collect(Collectors.toList());
//                break;
//            case 2:
//                couponProductIds =
//                        fullScaleVo.getFullScaleProducts().stream().map(FullScaleProductVo::getProductId).collect(Collectors.toSet());
//                canUseFullScaleOrderItem =
//                        orderItemList.stream().filter(orderItem -> !couponProductIds.contains(orderItem.getProductId())).collect(Collectors.toList());
//                break;
//            default:
//                break;
//        }
//        BigDecimal canUseFullScaleAmount =
//                canUseFullScaleOrderItem.stream().map(OrderItem::getRealAmount).reduce(BigDecimal.ZERO,
//                        BigDecimal::add);
//        List<FullScaleRuleVo> fullScaleRulesList = fullScaleVo.getFullScaleRules();
//        fullScaleRulesList.sort(Comparator.comparing(FullScaleRuleVo::getConditionAmount));
//        FullScaleRuleVo canUseFullScaleRule = null;
//        for (FullScaleRuleVo ruleVo : fullScaleRulesList) {
//            if (canUseFullScaleAmount.compareTo(ruleVo.getConditionAmount()) != -1) {
//                BigDecimal currFullScaleAmount = BigDecimal.ZERO;
//                //满减条件：  0-满减劵  1-满折劵
//                switch (ruleVo.getRuleType()) {
//                    case 0:
//                        currFullScaleAmount = ruleVo.getDiscountAmount();
//                        break;
//                    case 1:
//                        currFullScaleAmount = NumberUtil.mul(canUseFullScaleAmount,
//                                NumberUtil.div(ruleVo.getDiscount(), BigDecimal.TEN));
//                        currFullScaleAmount = NumberUtil.sub(canUseFullScaleAmount, currFullScaleAmount);
//                        break;
//                    default:
//                        break;
//                }
//                if (currFullScaleAmount.compareTo(fullScaleAmount) != -1) {
//                    fullScaleAmount = currFullScaleAmount;
//                    canUseFullScaleRule = ruleVo;
//                }
//            }
//        }
//        log.info("当前优惠等级：{},优惠前的价格：{},优惠金额：{}", canUseFullScaleRule.toString(), canUseFullScaleAmount, fullScaleAmount);
//
//        return fullScaleAmount;
//    }
//
//    @Test
//    public void testFullScale1() {
//        System.out.println(BigDecimal.valueOf(4500).compareTo(BigDecimal.valueOf(200)));
//
//    }
//
//    @Test
//    public void testFullScale() {
//        List<OrderItem> orderItemList = new ArrayList<>();
//        for (Long i = 1L; i < 10; i++) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setProductId(i);
//            orderItem.setRealAmount(NumberUtil.mul(i, BigDecimal.valueOf(40)));
//            orderItemList.add(orderItem);
//        }
//        orderItemList.sort(Comparator.comparing(OrderItem::getRealAmount).reversed());
//
//        List<FullScaleProductVo> canUseVos1 = new ArrayList<>();
//        for (Long i = 4L; i < 7; i++) {
//            FullScaleProductVo vo = new FullScaleProductVo();
//            vo.setProductId(i);
//            canUseVos1.add(vo);
//        }
//
//        List<FullScaleProductVo> cantUseVos1 = new ArrayList<>();
//        for (Long i = 1L; i < 3; i++) {
//            FullScaleProductVo vo = new FullScaleProductVo();
//            vo.setProductId(i);
//            cantUseVos1.add(vo);
//        }
//        List<FullScaleProductVo> cantUseVos2 = new ArrayList<>();
//        FullScaleProductVo vo = new FullScaleProductVo();
//        vo.setProductId(11L);
//        cantUseVos2.add(vo);
//
//        FullScaleVo fullScaleVo = new FullScaleVo();
//        fullScaleVo.setProductType(0);
//        List<FullScaleRuleVo> fullScaleRuleVoList1 = new ArrayList<>();
//        FullScaleRuleVo fullScaleRuleVo = new FullScaleRuleVo();
//        fullScaleRuleVo.setRuleType(1);
//        fullScaleRuleVo.setDiscount(BigDecimal.valueOf(1));
//        fullScaleRuleVo.setConditionAmount(BigDecimal.valueOf(200));
//        fullScaleRuleVoList1.add(fullScaleRuleVo);
//
//        for (int i = 1; i < 5; i++) {
//            fullScaleRuleVo = new FullScaleRuleVo();
//            fullScaleRuleVo.setRuleType(0);
//            fullScaleRuleVo.setDiscountAmount(BigDecimal.valueOf(100 * i));
//            fullScaleRuleVo.setConditionAmount(BigDecimal.valueOf(200 * i));
//            fullScaleRuleVoList1.add(fullScaleRuleVo);
//        }
//
//        fullScaleVo.setFullScaleRules(fullScaleRuleVoList1);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//        fullScaleVo.setProductType(1);
//        fullScaleVo.setFullScaleProducts(canUseVos1);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//        fullScaleVo.setProductType(2);
//        fullScaleVo.setFullScaleProducts(cantUseVos1);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//        fullScaleVo.setFullScaleProducts(cantUseVos2);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//
//
//        List<FullScaleRuleVo> fullScaleRuleVoList2 = new ArrayList<>();
//        for (int i = 1; i < 5; i++) {
//            fullScaleRuleVo = new FullScaleRuleVo();
//            fullScaleRuleVo.setRuleType(1);
//            fullScaleRuleVo.setDiscount(BigDecimal.valueOf(9 - i));
//            fullScaleRuleVo.setConditionAmount(BigDecimal.valueOf(200 * i));
//            fullScaleRuleVoList2.add(fullScaleRuleVo);
//        }
//        fullScaleVo.setFullScaleRules(fullScaleRuleVoList2);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//        fullScaleVo.setProductType(1);
//        fullScaleVo.setFullScaleProducts(canUseVos1);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//        fullScaleVo.setProductType(2);
//        fullScaleVo.setFullScaleProducts(cantUseVos1);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//        fullScaleVo.setFullScaleProducts(cantUseVos2);
//        handleFullScaleAmount(orderItemList, fullScaleVo);
//
//    }


}