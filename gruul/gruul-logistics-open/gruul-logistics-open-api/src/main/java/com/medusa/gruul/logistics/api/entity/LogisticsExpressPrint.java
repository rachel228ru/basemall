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
 * 电子面单打印机设置
 * t_logistics_express_print
 *
 * @author lcysike
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_logistics_express_print")
@ApiModel(value = "LogisticsExpressPrint对象", description = "电子面单打印机设置")
public class LogisticsExpressPrint extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备类型
     */
    @NotNull
    @ApiModelProperty(value = "设备类型")
    @TableField("device_type")
    private String deviceType;

    /**
     * 设备型号
     */
    @NotNull
    @ApiModelProperty(value = "设备型号")
    @TableField("device_model")
    private String deviceModel;

    /**
     * 设备名称
     */
    @NotNull
    @ApiModelProperty(value = "设备名称")
    @TableField("device_name")
    private String deviceName;

    /**
     * 打印机机身号
     */
    @NotNull
    @ApiModelProperty(value = "打印机机身号")
    @TableField("device_no")
    private String deviceNo;

    /**
     * 打印机KEY
     */
    @NotNull
    @ApiModelProperty(value = "打印机KEY")
    @TableField("device_key")
    private String deviceKey;

    /**
     * 状态 0-停用 1-启用
     */
    @NotNull
    @ApiModelProperty(value = "状态 0-停用 1-启用")
    @TableField("status")
    private String status;
}