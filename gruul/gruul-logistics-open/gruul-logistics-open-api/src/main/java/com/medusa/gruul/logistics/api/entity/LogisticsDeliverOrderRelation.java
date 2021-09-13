package com.medusa.gruul.logistics.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * t_logistics_deliver_order_relation
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_deliver_order_relation")
@ApiModel(value = "LogisticsDeliverOrderRelation对象", description = "物流发货单关联订单信息表")
public class LogisticsDeliverOrderRelation extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 物流发货单id
     */
    private Long logisticsId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单包含商品信息_json格式[{"id":12,"code":"2312"}]
     */
    private String goodObj;

    /**
     * 物流公司名称
     */
    private String logisticsName;

    /**
     * 物流公司单号
     */
    private String logisticsCode;

    /**
     * 是否发货,0=未发货,1=已发货
     */
    private Integer isSend;

    /**
     * 物流单,状态:1=代发货
     */
    private Integer type;
}