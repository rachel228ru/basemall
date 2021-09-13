package com.medusa.gruul.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 售后工单 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
public interface AfsOrderMapper extends BaseMapper<AfsOrder> {

    /**
     * Select by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<AfsOrder> selectByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * Select by order id and product sku id afs order.
     *
     * @param orderId      the order id
     * @param productSkuId the product sku id
     * @return the afs order
     */
    AfsOrder selectByOrderIdAndProductSkuId(@Param(value = "orderId") Long orderId,
                                            @Param(value = "productSkuId") Long productSkuId);

    /**
     * Select progress by order id list.
     *
     * @param orderId the order id
     * @return the list
     */
    List<AfsOrder> selectProgressByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * Count progress by point id long.
     *
     * @param pointId the point id
     * @return the long
     */
    Long countProgressByPointId(@Param(value = "pointId") String pointId);

    /**
     * Select original order id by order id long.
     *
     * @param orderId the order id
     * @return the long
     */
    Long selectOriginalOrderIdByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * Select count progress by order ids integer.
     *
     * @param orderIds the order ids
     * @return the integer
     */
    Integer selectCountProgressByOrderIds(@Param(value = "orderIds") List<Long> orderIds);

    /**
     * countSkuReturn
     *
     * @param productSkuId
     * @param userId
     * @return java.lang.Integer
     * @author alan
     * @date 2021/2/6 13:39
     */
    Integer countSkuReturn(@Param(value = "productSkuId") Long productSkuId, @Param(value = "userId") String userId);
}
