package com.medusa.gruul.logistics.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * t_logistics_shipping_model
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_shipping_model")
@ApiModel(value = "LogisticsShippingModel对象", description = "物流基础运送方式信息")
public class LogisticsShippingModel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 运费模版id 同 t_logistics_template 主键 id 一致.
     */
    @ApiModelProperty(value = "运费模版id")
    @TableField("logistics_id")
    private Long logisticsId;

    /**
     * 计价方式: 1=按件数,2=按重量
     */
    @ApiModelProperty(value = "计价方式: 1=按件数,2=按重量")
    @TableField("valuation_model")
    private Integer valuationModel;

    /**
     * 首件数
     */
    @ApiModelProperty(value = "首件数")
    @TableField("first_piece")
    private Integer firstPiece;

    /**
     * 首重 单位: 千克/kg
     */
    @ApiModelProperty(value = "首重 单位: 千克/kg")
    @TableField("first_weight")
    private BigDecimal firstWeight;

    /**
     * 首费 单位: 元
     */
    @ApiModelProperty(value = "首费 单位: 元")
    @TableField("first_amount")
    private BigDecimal firstAmount;

    /**
     * 续件数量
     */
    @ApiModelProperty(value = "续件数量")
    @TableField("second_piece")
    private Integer secondPiece;
    /**
     * 续重 单位: 千克/kg
     */
    @ApiModelProperty(value = "续重 单位: 千克/kg")
    @TableField("second_weight")
    private BigDecimal secondWeight;
    /**
     * 续费 单位: 元
     */
    @ApiModelProperty(value = "续费 单位: 元")
    @TableField("second_amount")
    private BigDecimal secondAmount;

    /**
     * 运送地区 json 格式 {"provinceid": ["cityId","cityId2"]}
     */
    @ApiModelProperty(value = "运送地区")
    @TableField("region_json")
    private String regionJson;
}