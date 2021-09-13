package com.medusa.gruul.order.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.model.BaseOrderMessage;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.order.mapper.OrderDeliveryMapper;
import com.medusa.gruul.order.mapper.OrderMapper;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.mq.Sender;
import com.medusa.gruul.order.service.IMiniOrderService;
import com.medusa.gruul.order.service.IOrderSettingService;
import com.medusa.gruul.order.service.IPointOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class PointOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IPointOrderService {


    @Override
    public List<UserOverviewVo> getUserOverviewVos(List<Order> orderList) {
        Map<String, List<Order>> userOrderMap = orderList.stream().collect(Collectors.groupingBy(Order::getUserId));
        List<UserOverviewVo> overviewVoList = new ArrayList<>(userOrderMap.size());
        for (String userId : userOrderMap.keySet()) {
            List<Order> userOrderList = userOrderMap.get(userId);
            UserOverviewVo vo = new UserOverviewVo();
            Order fistOrder = userOrderList.get(0);
            vo.setUserId(fistOrder.getUserId());
            vo.setUserName(fistOrder.getUserName());
            vo.setUserAvatarUrl(fistOrder.getUserAvatarUrl());
            vo.setWaitForSend(userOrderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.WAIT_FOR_SEND).count());
            vo.setShipped(userOrderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.SHIPPED).count());
            vo.setWaitForPickup(userOrderList.stream().filter(o -> o.getStatus() == OrderStatusEnum.WAIT_FOR_PICKUP).count());
            overviewVoList.add(vo);
        }
        return overviewVoList;
    }





}
