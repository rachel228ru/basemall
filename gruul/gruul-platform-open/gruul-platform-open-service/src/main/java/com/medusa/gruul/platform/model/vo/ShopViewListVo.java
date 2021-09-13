package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/3/8
 */
@Data
public class ShopViewListVo {

    @ApiModelProperty(value = "用户id")
    private Long accountId;

    @ApiModelProperty(value = "店铺id")
    private Long id;

    @ApiModelProperty(value = "店铺首页图片")
    private String logoUrl;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;


    @ApiModelProperty(value = "店铺用户账号")
    private String userPhone;


    @ApiModelProperty(value = "0审核中，1部署中 2正常 ，3已打烊，4禁用")
    private Integer status;

    @ApiModelProperty(value = "到期时间")
    private LocalDateTime dueTime;

    @ApiModelProperty(value = "是否到期 0 不是 1是")
    private Integer isDue;


    @ApiModelProperty(value = "使用模板名称")
    private String templateName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否私有化部署（0否 1是）")
    private Integer isPrivatizationDeployment;

    @ApiModelProperty(value = "店铺当前使用的套餐来源   0-用户购买 1-管理台购买（店铺列表，购买，续费） 2-平台赠送（用户创建店铺时自动赠送） 3-平台创建（为指定商户直接创建指定套餐店铺）4-代理付费")
    private Integer orderSource;

    @ApiModelProperty(value = "当前使用的套餐等级")
    private Integer level;

    @ApiModelProperty(value = "当前使用的套餐名称")
    private String packageName;

    @ApiModelProperty(value = "店铺租户id")
    private String tenantId;

    @ApiModelProperty(value = "店铺当前版本")
    private String versionName;
}
