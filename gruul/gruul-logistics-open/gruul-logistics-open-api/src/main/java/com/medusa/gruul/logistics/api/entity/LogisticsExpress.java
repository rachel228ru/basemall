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
 * 物流公司发货地址设置
 * t_logistics_express
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_express")
@ApiModel(value = "LogisticsExpress对象", description = "物流公司发货地址设置")
public class LogisticsExpress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)

    private Long id;

    /**
     * 快递公司名称
     */
    @NotNull
    @ApiModelProperty(value = "快递公司名称")
    @TableField("name")
    private String name;

    /**
     * 快递公司code
     */
    @NotNull
    @ApiModelProperty(value = "快递公司code")
    @TableField("code")
    private String code;

    /**
     * 快递公司客服电话
     */
    @NotNull
    @ApiModelProperty(value = "快递公司客服电话")
    @TableField("phone")
    private String phone;

    /**
     * 地址id 关联物流发货地址表id
     */
    @NotNull
    @ApiModelProperty(value = "地址id 关联物流发货地址表id")
    @TableField("address_id")
    private Long addressId;

    /**
     * 客户id
     */
    @NotNull
    @ApiModelProperty(value = "客户id")
    @TableField("customer_name")
    private String customerName;

    /**
     * 客户密匙
     */
    @NotNull
    @ApiModelProperty(value = "客户密匙")
    @TableField("customer_password")
    private String customerPassword;

    /**
     * 负责人
     */
    @NotNull
    @ApiModelProperty(value = "负责人")
    @TableField("link_name")
    private String linkName;

    /**
     * 负责人电话
     */
    @NotNull
    @ApiModelProperty(value = "负责人电话")
    @TableField("link_tel")
    private String linkTel;

    /**
     * 状态
     */
    @NotNull
    @ApiModelProperty(value = "状态")
    @TableField("status")
    private String status;
}