package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/3/9
 */
@Data
public class LoginShopInfoVo {

    @ApiModelProperty(value = "管理台店铺的shopId,表id")
    private Long platformShopId;

    @ApiModelProperty(value = "店铺首页图片")
    private String logoUrl;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "城市合伙人的shopId")
    private String shopId;

    @ApiModelProperty(value = "当前店铺使用的模板跳转地址")
    private String backUrl;


    @ApiModelProperty(value = "小程序信息id")
    private Long miniId;

    @ApiModelProperty(value = "公众号信息id")
    private Long mpId;

    @ApiModelProperty(value = "小程序租户id")
    private String tenantId;

    @ApiModelProperty(value = "到期时间")
    private LocalDateTime dueTime;

    @ApiModelProperty(value = "是否到期 0 不是 1是")
    private Integer isDue;

    /**
     * 待定是否有用
     */
    @ApiModelProperty(value = "用户所在店铺拥有的角色")
    @Deprecated
    private List<RoleInfoVo> roleInfoVo;
}
