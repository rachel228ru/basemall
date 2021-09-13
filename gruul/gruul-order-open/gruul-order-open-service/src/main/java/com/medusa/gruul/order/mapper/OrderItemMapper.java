package com.medusa.gruul.order.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.goods.api.param.OperateStockDto;
import com.medusa.gruul.order.api.entity.OrderItem;
import com.medusa.gruul.order.api.model.GoodsBean;
import com.medusa.gruul.order.api.model.OrderItemVo;
import com.medusa.gruul.order.model.SimpleOrderItemVo;
import com.medusa.gruul.order.model.SolitaireOrderItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单中所包含的商品 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * selectListByOrderId
     *
     * @param orderId the order id
     * @return java.util.List<com.medusa.gruul.order.model.SimpleOrderItemVo> list
     * @author alan
     * @date 2019 /11/23 16:05
     */
    List<SimpleOrderItemVo> selectSimpleOrderItemVoByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * selectOrderItemVoListByOrderId
     *
     * @param orderId the order id
     * @return java.util.List<com.medusa.gruul.order.model.SolitaireOrderItemVo> list
     * @author alan
     * @date 2019 /11/23 16:05
     */
    List<SolitaireOrderItemVo> selectSolitaireOrderItemVoByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * selectByOrderId
     *
     * @param orderId the order id
     * @return java.util.List<com.medusa.gruul.order.api.entity.OrderItem> list
     * @author alan
     * @date 2019 /12/1 17:04
     */
    List<OrderItem> selectByOrderId(@Param(value = "orderId") Long orderId);


    /**
     * selectOrderItemVoByOrderId
     *
     * @param orderId the order id
     * @return java.util.List<com.medusa.gruul.order.api.model.OrderItemVo> list
     * @author alan
     * @date 2020 /10/5 09:41
     */
    List<OrderItemVo> selectOrderItemVoByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * 根据订单ID查询订单包含的商品
     *
     * @param orderIds the order ids
     * @return java.util.List<com.medusa.gruul.order.api.model.ItemDto> list
     * @author alan
     * @date 2019 /11/28 21:06
     */
    List<OperateStockDto> selectItemDtoByOrderIds(@Param(value = "orderIds") List<Long> orderIds);


    /**
     * 根据订单ID查询订单包含的商品
     *
     * @param orderId the order id
     * @return java.util.List<com.medusa.gruul.order.api.model.ItemDto> list
     * @author alan
     * @date 2019 /11/28 21:06
     */
    @SqlParser(filter = true)
    List<GoodsBean> selectGoodsBeanByOrderIds(@Param(value = "orderId") Long orderId);

    /**
     * countSkuPurchased
     *
     * @param productSkuId the product sku id
     * @param userId       the user id
     * @return java.lang.Integer integer
     * @author alan
     * @date 2020 /2/13 21:56
     */
    Integer countSkuPurchased(@Param(value = "productSkuId") Long productSkuId, @Param(value = "userId") String userId);

}
