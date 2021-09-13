package com.medusa.gruul.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.model.*;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface IGroupLeaderOrderService extends IService<Order> {

    /**
     * Gets user overview page.
     *
     * @param keyword the keyword
     * @return the user overview page 用户概况列表
     * @author alan
     * @date 2019 /12/14 20:59
     */
    List<UserOverviewVo> getUserOverviewPage(String keyword);


    /**
     * 用户订单列表
     *
     * @param dto the dto
     * @return com.medusa.gruul.common.core.util.PageUtils<com.medusa.gruul.order.model.SimpleOrderVo>  order page
     * @author alan
     * @date 2019 /12/10 21:34
     */
    PageUtils<SimpleOrderVo> getOrderPage(QueryUserOrderDto dto);





}
