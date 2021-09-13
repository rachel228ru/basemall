package com.medusa.gruul.afs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.api.model.AfsSimpleVo;
import com.medusa.gruul.afs.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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
     * 根据订单查询原始订单的信息
     *
     * @param orderId the order id
     * @return java.lang.Long list
     * @author alan
     * @date 2020 /8/27 22:31
     */
    List<Long> selectOriginalOrderListByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * 查询这些订单是否有其他正在进行的售后单
     *
     * @param originalOrderIds the original order ids
     * @param id               the id
     * @return java.util.List<com.medusa.gruul.afs.api.entity.AfsOrder>  list
     * @author alan
     * @date 2020 /9/1 20:09
     */
    List<AfsOrder> selectProgressByOriginalOrderIdsAndIdNotIn(@Param(value = "originalOrderIds") String originalOrderIds,
                                                              @Param(value = "id") Long id);

    /**
     * 控制台售后分页查询
     *
     * @param page       the page
     * @param statusList the status list
     * @param dto        the dto
     * @return the page
     */
    Page<ManageAfsOrderVo> searchManageAfsOrderVoPage(Page page,
                                                      @Param(value = "statusList") List<AfsOrderStatusEnum> statusList, @Param(value = "dto") SearchDto dto);

    /**
     * 根据订单查询原始订单的信息
     *
     * @param orderId the order id
     * @return the long
     */
    Long selectOriginalOrderByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * 根据订单ID和商品ID查询进行中的订单
     *
     * @param orderId      the order id
     * @param productSkuId the product sku id
     * @return the afs order
     */
    AfsOrder selectProgressByOrderIdAndProductSkuId(@Param(value = "orderId") Long orderId, @Param(value =
            "productSkuId") Long productSkuId);

    /**
     * 根据ID查询售后单
     *
     * @param orderId the order id
     * @return the afs order
     */
    AfsOrder selectByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * 获取当前订单的售后次数
     *
     * @param orderId the order id
     * @param types   the types
     * @return the order apply number
     */
    Integer getOrderApplyNumber(@Param(value = "orderIds") List<Long> orderId,
                                @Param(value = "types") List<Integer> types);
    /**
     * 根据签收单查询售后
     *
     * @param receiptBillId the receipt bill id
     * @return the list
     */
    List<AfsSimpleVo> selectAfsOrderByReceiptBillId(@Param(value = "receiptBillId") Long receiptBillId);

    /**
     * 根据订单ID和售后ID查询还在进行中的订单
     *
     * @param orderId the order id
     * @param afsId   the afs id
     * @return the list
     */
    List<AfsOrder> selectProgressByOrderIdAndIdNotIn(@Param(value = "orderId") Long orderId,
                                                     @Param(value = "afsId") Long afsId);

    /**
     * 查询用户售后信息
     *
     * @param page   the page
     * @param userId the user id
     * @return the page
     */
    Page<ApiAfsOrderVo> searchOrder(Page page, @Param(value = "userId") String userId);

    /**
     * 根据原始订单查询对应的售后单
     *
     * @param id
     * @return com.medusa.gruul.afs.api.entity.AfsOrder
     * @author alan
     * @date 2021/3/17 22:06
     */
    AfsOrder selectByOriginalOrderId(@Param(value = "orderId") Long id);
}
