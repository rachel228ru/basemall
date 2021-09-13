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
public interface IPointOrderService extends IService<Order> {





    /**
     * getUserOverviewVos
     *
     * @param orderList the order list
     * @return java.util.List<com.medusa.gruul.order.model.UserOverviewVo>  user overview vos
     * @author alan
     * @date 2020 /1/7 20:47
     */
    List<UserOverviewVo> getUserOverviewVos(List<Order> orderList);



}
