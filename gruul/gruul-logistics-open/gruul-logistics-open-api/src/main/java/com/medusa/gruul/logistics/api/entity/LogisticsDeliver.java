package com.medusa.gruul.logistics.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * t_logistics_deliver
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_deliver")
@ApiModel(value = "LogisticsDeliver对象", description = "物流发货单")
public class LogisticsDeliver extends BaseEntity {
    /**
     * 主键 id
     */
    private Long id;

    /**
     * 物流发货单名称
     */
    private String name;

    /**
     * 物流编号
     */
    private Long code;

    /**
     * 发货单状态	: 1=代发货,2=待提货,3=已完成,4=已关闭
     */
    private Integer type;
}