package com.medusa.gruul.afs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The type Afs user vo.
 *
 * @author alan
 * @description: 提货点申请包含的用户
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "申请包含的用户")
public class AfsUserVo {



    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
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
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    /**
     * 收货人邮编
     */
    @ApiModelProperty(value = "收货人邮编")
    private String receiverPostCode;

    /**
     * 省份/直辖市
     */
    @ApiModelProperty(value = "省份/直辖市")
    private String receiverProvince;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String receiverCity;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String receiverRegion;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;
}
