package com.medusa.gruul.afs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * The type User apply dto.
 *
 * @author alan
 * @description: 用户申请售后的参数
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "用户申请售后的参数")
public class UserApplyDto extends BaseApplyDto {

    /**
     * 申请售后的订单ID
     */
    @NotNull(message = "申请售后的订单不能为空")
    @ApiModelProperty(value = "申请售后的订单ID")
    private Long orderId;

}
