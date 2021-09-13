package com.medusa.gruul.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.order.api.entity.OrderItem;
import com.medusa.gruul.order.mapper.OrderItemMapper;
import com.medusa.gruul.order.service.IOrderItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

}
