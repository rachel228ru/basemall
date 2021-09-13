package com.medusa.gruul.afs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 申请的详情 服务类
 * </p>
 *
 * @author alan
 * @since 2020 -08-21
 */
public interface IAfsOrderItemService extends IService<AfsOrderItem> {

    /**
     * Gets exchange order.
     *
     * @param orderId the order id
     * @return the exchange order
     */
    List<Long> getExchangeOrder(Long orderId);

    /**
     * 获取用户退款次数 -- 针对商品
     * @param orderId
     * @return  用户当前订单退款情况
     */
    HashMap<String, Object> userApply(Long orderId);
}
