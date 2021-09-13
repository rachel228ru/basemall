package com.medusa.gruul.logistics.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * t_logistics_manage
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_manage")
@ApiModel(value = "LogisticsManage对象", description = "物流设置信息")
public class LogisticsManage extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键 id
     */
    private Long id;

    /**
     * 是否开启物流配送;0=不开启,1=开启
     */
    private Integer state;

    /**
     * 生成订单时间: HH:mm  格式: ["HH:mm","HH:mm","HH:mm","HH:mm"] 最多四个
     */
    private String deliverBuildTime;

}