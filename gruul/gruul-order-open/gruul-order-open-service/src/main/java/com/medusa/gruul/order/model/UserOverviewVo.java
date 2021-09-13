package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * The type User overview vo.
 * <p>
 * 用户数据概况
 *
 * @author alan
 * @date 2019 /11/13 20:20
 */
@Data
public class UserOverviewVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", example = "1")
    private String userId;

    /**
     * 用户帐号
     */
    @ApiModelProperty(value = "用户帐号")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatarUrl;

    /**
     * 待发货订单
     */
    @ApiModelProperty(value = "待发货订单")
    private Long waitForSend;

    /**
     * 配送中订单
     */
    @ApiModelProperty(value = "配送中订单")
    private Long shipped;

    /**
     * 待提货订单
     */
    @ApiModelProperty(value = "待提货订单")
    private Long waitForPickup;
}
