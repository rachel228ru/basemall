package com.medusa.gruul.order.mapper;

//import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.model.*;
import com.medusa.gruul.order.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface OrderMapper extends BaseMapper<Order> {


    /**
     * 小程序端订单列表
     *
     * @param page             the page
     * @param orderStatusList  the order status list
     * @param searchAfterOrder the search after order
     * @param userId           the user id
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.medusa.gruul.order.model.ApiOrderVo>
     * page
     * @author alan
     * @date 2019 /11/23 19:19
     */
    Page<ApiOrderVo> searchApiOrderVoPage(Page page,
                                          @Param(value = "statusList") List<Integer> orderStatusList,
                                          @Param(value = "searchAfterOrder") Boolean searchAfterOrder,
                                          @Param(value = "userId") String userId
    );

    /**
     * PC管理端订单列表
     *
     * @param page            the page
     * @param orderStatusList the order status list
     * @param startDate       the start date
     * @param endDate         the end date
     * @param dto             the dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.medusa.gruul.order.model.ManageOrderVo>
     * page
     * @author alan
     * @date 2019 /11/23 19:20
     */
    Page<ManageOrderVo> searchManageOrderVoPage(Page page,
                                                @Param(value = "statusList") List<Integer> orderStatusList,
                                                @Param(value = "startDate") String startDate,
                                                @Param(value = "endDate") String endDate,
                                                @Param(value = "dto") ManageSearchOrderDto dto);

    /**
     * selectOrderVoById
     *
     * @param orderId the order id
     * @return com.medusa.gruul.order.model.OrderVo order vo
     * @author alan
     * @date 2019 /11/25 21:54
     */
    OrderVo selectOrderVoById(@Param(value = "orderId") Long orderId);

    /**
     * selectOrderVoById
     *
     * @param orderIds the order id
     * @return com.medusa.gruul.order.model.OrderVo order vo
     * @author alan
     * @date 2019 /11/25 21:54
     */
    List<OrderVo> selectOrderVoListByIds(@Param(value = "orderIds") List<Long> orderIds);


    /**
     * searchSimpleOrderVoForGroupPage
     *
     * @param page   the page
     * @param status the status
     * @param userId the user id
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.medusa.gruul.order.model.SimpleOrderVo>
     * page
     * @author alan
     * @date 2020 /1/8 21:19
     */
    Page<SimpleOrderVo> searchSimpleOrderVoForGroupPage(Page page,
                                                        @Param(value = "status") Integer status,
                                                        @Param(value = "userId") String userId

    );

    /**
     * selectOrderListByIds
     *
     * @param orderIdList the order id list
     * @param tenantId    the tenant id
     * @param shopId      the shop id
     * @return java.util.List<com.medusa.gruul.order.api.model.GetOrderListDto>     list
     * @author alan
     * @date 2019 /12/12 22:10
     */
    @SqlParser(filter = true)
    List<GetOrderListDto> selectOrderListByIds(@Param(value = "orderIdList") List<Long> orderIdList,
                                               @Param(value = "tenantId") String tenantId,
                                               @Param(value = "shopId") String shopId
    );

    /**
     * 查询没有生成发货单的订单
     *
     * @param start    the start
     * @param end      the end
     * @param tenantId the tenant id
     * @param shopId   the shop id
     * @return java.util.List<com.medusa.gruul.order.api.model.GetOrderListDto>     list
     * @author alan
     * @date 2019 /12/17 20:52
     */
    @SqlParser(filter = true)
    List<GetOrderListDto> selectNotShippedOrder(@Param(value = "start") String start,
                                                @Param(value = "end") String end,
                                                @Param(value = "tenantId") String tenantId,
                                                @Param(value = "shopId") String shopId);

    /**
     * 查询指定提货点的时间范围中所有已支付的订单
     *
     * @param start    the start
     * @param end      the end
     * @param tenantId the tenant id
     * @param shopId   the shop id
     * @param pointId  the point id
     * @return java.util.List<com.medusa.gruul.order.api.entity.Order>     list
     * @author alan
     * @date 2019 /12/17 20:52
     */
    @SqlParser(filter = true)
    List<Order> selectListByPointIdAndDate(@Param(value = "start") Date start,
                                           @Param(value = "end") Date end,
                                           @Param(value = "tenantId") String tenantId,
                                           @Param(value = "shopId") String shopId,
                                           @Param(value = "pointId") String pointId);

    /**
     * 查询指定提货点的时间范围中所有已支付的订单
     *
     * @param start   the start
     * @param end     the end
     * @param pointId the point id
     * @return java.util.List<com.medusa.gruul.order.api.entity.Order>     list
     * @author alan
     * @date 2019 /12/17 20:52
     */
    List<Order> selectListByPointIdAndDateBetween(@Param(value = "start") Date start,
                                                  @Param(value = "end") Date end,
                                                  @Param(value = "pointId") String pointId);




    /**
     * searchOrderBrokerageVoForGroupPage
     *
     * @param id the id
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.medusa.gruul.order.model.OrderBrokerageVo>   order dto
     * @author wangpeng
     * @date 2020 /3/14 10:47
     */
    @SqlParser(filter = true)
    OrderDto selectOneById(long id);

    /**
     * Update one by id int.
     *
     * @param orderDto the order dto
     * @return the int 更新
     * @param:orderDto
     * @return: int
     * @throws:
     * @author wangpeng
     * @version : v1.0
     * @date 2020 /3/14 5:13 下午
     */
    int updateOneById(OrderDto orderDto);


    /**
     * Search logistics order list page.
     *
     * @param page the page
     * @param dto  the dto
     * @return the page 搜索快递订单
     * @param:orderDto
     * @return: com.medusa.gruul.order.model.ManageOrderVo
     * @throws:
     * @author wangpeng
     * @version : v1.0
     * @date 2020 /3/14 5:13 下午
     */
    Page<ManageDeliveryOrderVo> searchLogisticsOrderList(Page page, @Param(value = "dto") ManageLogisticsOrderDto dto);


    /**
     * Select list by point id and status list.
     *
     * @param tenantId            the tenant id
     * @param shopId              the shop id
     * @param pointId             the point id
     * @param orderStatusEnumList the order status enum list
     * @return the list
     */
    @SqlParser(filter = true)
    List<Order> selectListByPointIdAndStatus(@Param(value = "tenantId") String tenantId,
                                             @Param(value = "shopId") String shopId,
                                             @Param(value = "pointId") String pointId,
                                             @Param(value = "orderStatusList") List<OrderStatusEnum> orderStatusEnumList);


    /**
     * Gets product last buyers.
     *
     * @param productId the product id
     * @return the product last buyers
     */
    List<BuyerVo> getProductLastBuyers(@Param(value = "productId") String productId);

    /**
     * Search logistics order list.
     *
     * @param orderIds the order ids
     * @return the list
     */
    List<ManageOrderVo> searchLogisticsOrder(@Param(value = "orderIds") List<Long> orderIds);

    /**
     * Count logistics wait send integer.
     *
     * @return the integer
     */
    Integer countLogisticsWaitSend();

    /**
     * Select list by ass id list.
     *
     * @param keyword the keyword
     * @return the list
     */
    List<Order> selectListByAssId(@Param(value = "keyword") String keyword);

    /**
     * Select shipped order by product ids list.
     *
     * @param productIds the product ids
     * @return the list
     */
    List<Long> selectShippedOrderByProductIds(@Param(value = "productIds") List<Long> productIds);

    /**
     * Search incentive detail page page.
     *
     * @param page        the page
     * @param beginTime   the begin time
     * @param endTime     the end time
     * @param accountType the account type
     * @param userId      the user id
     * @param deliverType the deliver type
     * @return the page
     */
    Page<ManageOrderVo> searchIncentiveDetailPage(Page page, @Param(value = "beginTime") String beginTime,
                                                  @Param(value = "endTime") String endTime,
                                                  @Param(value = "accountType") String accountType,
                                                  @Param(value = "userId") String userId,
                                                  @Param(value = "deliverType") Integer deliverType);

    /**
     * Wait send product list list.
     *
     * @param sendBillId the send bill id
     * @return the list
     */
    List<Long> waitSendProductList(@Param(value = "sendBillId") String sendBillId);

    /**
     * Gets solitaire lately buyer.
     *
     * @param solitaireActivityId the solitaire activity id
     * @return the solitaire lately buyer
     */
    List<LatelyBuyerVo> getSolitaireLatelyBuyer(@Param(value = "solitaireActivityId") Integer solitaireActivityId);

    /**
     * Gets solitaire buyer page.
     *
     * @param page                the page
     * @param solitaireActivityId the solitaire activity id
     * @return the solitaire buyer page
     */
    Page<SolitaireBuyerVo> getSolitaireBuyerPage(Page page,
                                                 @Param(value = "solitaireActivityId") Integer solitaireActivityId);

    /**
     * Gets my solitaire order.
     *
     * @param solitaireActivityId the solitaire activity id
     * @param userId              the user id
     * @return the my solitaire order
     */
    List<SolitaireBuyerVo> getMySolitaireOrder(@Param(value = "solitaireActivityId") Integer solitaireActivityId,
                                               @Param(value = "userId") String userId);

    /**
     * Close exchange order.
     *
     * @param orderIds the order ids
     * @param status   the status
     */
    void closeExchangeOrder(@Param(value = "orderIds") List<Long> orderIds,
                            @Param(value = "status") OrderStatusEnum status);

    /**
     * Select list by full scale ids list.
     *
     * @param fullScaleIdList the full scale id list
     * @return the list
     */
    List<ActivityStatisticsVo> selectListByFullScaleIds(@Param(value = "fullScaleIdList") List<Long> fullScaleIdList);

    /**
     * Gets point order history.
     *
     * @param tenantId the tenant id
     * @param shopId   the shop id
     * @param pointId  the point id
     * @return the point order history
     */
    @SqlParser(filter = true)
    int getPointOrderHistory(@Param(value = "tenantId") String tenantId,
                             @Param(value = "shopId") String shopId,
                             @Param(value = "pointId") String pointId);

}
