package com.medusa.gruul.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import com.medusa.gruul.order.api.model.OrderDeliveryDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单物流 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface OrderDeliveryMapper extends BaseMapper<OrderDelivery> {


    /**
     * Select one by order id order delivery dto.
     *
     * @param orderId the order id
     * @return the order delivery dto
     * 查询物流订单
     * @param:OrderId
     * @return: com.medusa.gruul.order.api.model.OrderDeliveryDto
     * @throws:
     * @author wangpeng
     * @version : v1.0
     * @date 2020 /3/14 5:22 下午
     */
    OrderDeliveryDto selectOneByOrderId(@Param(value = "orderId") long orderId);

    /**
     * Select by order id order delivery.
     *
     * @param orderId the order id
     * @return the order delivery
     * 查询物流订单
     * @param:OrderId
     * @return: com.medusa.gruul.order.api.model.OrderDeliveryDto
     * @throws:
     * @author wangpeng
     * @version : v1.0
     * @date 2020 /3/14 5:22 下午
     */
    OrderDelivery selectByOrderId(@Param(value = "orderId") long orderId);

    /**
     * Update by param int.
     *
     * @param orderDeliveryDto the order delivery dto
     * @return the int
     * 更新发货时间和状态
     * @param:orderDeliveryDto
     * @return: int
     * @throws:
     * @author wangpeng
     * @version : v1.0
     * @date 2020 /3/14 5:22 下午
     */
    int updateByParam(OrderDeliveryDto orderDeliveryDto);
}
