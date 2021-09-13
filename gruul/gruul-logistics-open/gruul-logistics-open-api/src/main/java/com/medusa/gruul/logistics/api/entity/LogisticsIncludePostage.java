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
 * t_logistics_include_postage
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_include_postage")
@ApiModel(value = "LogisticsIncludePostage对象", description = "运费模板包邮设置表")
public class LogisticsIncludePostage extends BaseEntity {

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
     * 包邮条件类型: 0= 按件数包邮,1=按重量包邮,2=按金额包邮,3=件数+金额 4=重量+金额 包邮
     */
    @ApiModelProperty(value = "包邮条件类型: 0= 按件数包邮,1=按重量包邮,2=按金额包邮,3=件数+金额 4=重量+金额 包邮")
    @TableField("type")
    private Integer type;

    /**
     * 运送地区 json 格式 {"provinceid": ["cityId","cityId2"]}
     */
    @ApiModelProperty(value = "运送地区")
    @TableField("region")
    private String region;

    /**
     * 包邮件数
     */
    @ApiModelProperty(value = "包邮件数")
    @TableField("piece_num")
    private Integer pieceNum;

    /**
     * 包邮重量 单位: 千克(kg)
     */
    @ApiModelProperty("包邮重量 单位: 千克(kg) ")
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 包邮金额
     */
    @ApiModelProperty(value = "包邮金额")
    @TableField("amount_num")
    private BigDecimal amountNum;
}