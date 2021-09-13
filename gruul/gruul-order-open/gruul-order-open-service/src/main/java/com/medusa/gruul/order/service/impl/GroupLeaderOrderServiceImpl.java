package com.medusa.gruul.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.mapper.OrderMapper;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.service.IGroupLeaderOrderService;
import com.medusa.gruul.order.service.IPointOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
public class GroupLeaderOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IGroupLeaderOrderService {

    @Resource
    private IPointOrderService pointOrderService;



    @Override
    public List<UserOverviewVo> getUserOverviewPage(String keyword) {
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        if (ObjectUtil.isNull(curUser)) {
            throw new ServiceException(SystemCode.UNAUTHORIZED);
        }
        List<Order> orderList = baseMapper.selectListByAssId(keyword);
        return pointOrderService.getUserOverviewVos(orderList);
    }

    @Override
    public PageUtils<SimpleOrderVo> getOrderPage(QueryUserOrderDto dto) {
        Page<SimpleOrderVo> page = baseMapper.searchSimpleOrderVoForGroupPage(new Page(dto.getCurrent(),
                dto.getSize()), dto.getStatus(), dto.getUserId());
        return new PageUtils<>(page);
    }

}

