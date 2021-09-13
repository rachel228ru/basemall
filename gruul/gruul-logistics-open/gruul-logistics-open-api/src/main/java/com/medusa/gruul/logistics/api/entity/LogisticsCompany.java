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

import javax.validation.constraints.NotNull;


/**
 * t_logistics_manage
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_company")
@ApiModel(value = "LogisticsCompany", description = "物流公司编码信息")
public class LogisticsCompany extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 快递公司名称
     */
    @ApiModelProperty(value = "快递公司名称")
    @TableField("name")
    private String name;

    /**
     * 快递公司编码
     */
    @ApiModelProperty(value = "快递公司编码")
    @TableField("code")
    private String code;



}