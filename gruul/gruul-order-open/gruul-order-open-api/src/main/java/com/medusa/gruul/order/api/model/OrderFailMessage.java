package com.medusa.gruul.order.api.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 生成订单失败消息体
 *
 * @author alan
 * @date 2019/10/6 14:08
 */
@Data
public class OrderFailMessage implements Serializable {
    private static final long serialVersionUID = 1L;



    /**
     * 优惠券 优惠券可能为空，注意判断
     */
    private Long couponId;

    /**
     * 商铺id
     */
    private String shopId;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 用户id
     */
    private String userId;

    public OrderFailMessage integralFail(Long couponId, String userId, String shopId, String tenantId) {
        OrderFailMessage failMessage=new OrderFailMessage();
        failMessage.setCouponId(couponId);
        failMessage.setUserId(userId);
        failMessage.setShopId(shopId);
        failMessage.setTenantId(tenantId);
        return failMessage;
    }

    public OrderFailMessage stockFail(Long couponId,  String userId, String shopId, String tenantId) {
        OrderFailMessage failMessage=new OrderFailMessage();
        failMessage.setCouponId(couponId);
        failMessage.setUserId(userId);
        failMessage.setShopId(shopId);
        failMessage.setTenantId(tenantId);
        return failMessage;
    }
}
