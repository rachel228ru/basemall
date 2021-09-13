package com.medusa.gruul.afs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提货点申请的详情 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2020 -08-21
 */
public interface AfsOrderItemMapper extends BaseMapper<AfsOrderItem> {



    /**
     * 根据原始订单ID查询换货单的ID
     *
     * @param orderId the order id
     * @return the list
     */
    List<Long> selectExchangeOrderIdsByOriginalOrderId(@Param(value = "orderId") Long orderId);

    /**
     * 用户售后
     * @param orderId
     * @return  用户售后信息
     */
    List<Map<String,String>> userApplyItem(@Param("orderId") Long orderId);
}
                                            