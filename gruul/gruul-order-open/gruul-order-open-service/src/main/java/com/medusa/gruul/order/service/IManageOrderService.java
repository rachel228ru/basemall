package com.medusa.gruul.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.model.OrderVo;
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
public interface IManageOrderService extends IService<Order> {


    /**
     * PC管理端订单列表
     *
     * @param dto the dto
     * @return com.medusa.gruul.common.core.util.PageUtils page utils
     * @author alan
     * @date 2019 /11/20 20:04
     */
    PageUtils searchOrder(ManageSearchOrderDto dto);

    /**
     * 商家批量关闭订单
     *
     * @param orderIds the order ids
     * @return void
     * @author alan
     * @date 2019 /11/23 19:03
     */
    void closeOrder(List<Long> orderIds);

    /**
     * 商家批量备注订单
     *
     * @param orderIds the order ids
     * @param note     the note
     * @param isOver   the is over
     * @return void
     * @author alan
     * @date 2019 /11/23 19:03
     */
    void noteOrder(List<Long> orderIds, String note, Boolean isOver);

    /**
     * 商家移出发货单
     *
     * @param orderIds the order ids
     * @return void
     * @author alan
     * @date 2019 /11/23 19:03
     */
    void removeSendBill(List<Long> orderIds);

    /**
     * 商家查看评价
     *
     * @param dto the dto
     * @return com.medusa.gruul.common.core.util.PageUtils page utils
     * @author alan
     * @date 2019 /12/3 20:06
     */
    PageUtils searchOrderEvaluate(ManageSearchEvaluateDto dto);

    /**
     * 商家设置评价为精选
     *
     * @param ids the ids
     * @return void
     * @author alan
     * @date 2019 /11/23 19:03
     */
    void choiceEvaluate(List<Long> ids);

    /**
     * 商家回复评价
     *
     * @param id    the id
     * @param reply the reply
     * @return void
     * @author alan
     * @date 2019 /11/23 19:03
     */
    void replyEvaluate(Long id, String reply);

    /**
     * 商家查询订单详情
     *
     * @param orderId the order id
     * @return com.medusa.gruul.order.model.OrderVo order vo
     * @author alan
     * @date 2019 /11/26 20:41
     */
    OrderVo orderInfo(Long orderId);

    /**
     * 商家取消设置评价为精选
     *
     * @param asList the as list
     * @return void
     * @author alan
     * @date 2019 /12/2 21:16
     */
    void unChoiceEvaluate(List<Long> asList);

    /**
     * Search logistics order list page utils.
     *
     * @param manageLogisticsOrderDto the manage logistics order dto
     * @return the page utils
     * 物流批量发货订单列表
     * @param:id
     * @return: com.medusa.gruul.order.model.ManageDeliveryOrderVo
     * @throws:
     * @author wangpeng
     * @version : v1.0
     * @date 2020 /3/6 7:33 下午
     */
    PageUtils searchLogisticsOrderList(ManageLogisticsOrderDto manageLogisticsOrderDto);

    /**
     * 控制台订单数据概况
     *
     * @param
     * @return com.medusa.gruul.order.model.ManageOrderOverviewVo overview
     * @author alan
     * @date 2020 /5/6 19:10
     */
    ManageOrderOverviewVo getOverview();

    /**
     * searchLogisticsOrder
     *
     * @param dto the dto
     * @return java.util.List<com.medusa.gruul.order.model.ManageOrderVo> list
     * @author alan
     * @date 2020 /6/17 22:02
     */
    List<ManageOrderVo> searchLogisticsOrder(ManageSearchLogisticsOrderDto dto);

    /**
     * countLogisticsWaitSend
     *
     * @param
     * @return java.lang.Integer integer
     * @author alan
     * @date 2020 /6/17 22:02
     */
    Integer countLogisticsWaitSend();

    /**
     * logisticsSend
     *
     * @param dto the dto
     * @return void
     * @author alan
     * @date 2020 /6/17 22:02
     */
    void logisticsSend(ManageSearchLogisticsOrderDto dto);

    /**
     * removeSendBillByProductIds
     *
     * @param productIds the product ids
     * @return void
     * @author alan
     * @date 2020 /6/17 22:08
     */
    void removeSendBillByProductIds(List<Long> productIds);

}
