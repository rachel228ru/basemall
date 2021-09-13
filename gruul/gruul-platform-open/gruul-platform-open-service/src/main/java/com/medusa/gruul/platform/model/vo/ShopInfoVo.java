package com.medusa.gruul.platform.model.vo;

import com.medusa.gruul.common.core.constant.enums.TemplateCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/4/18
 */
@Data
public class ShopInfoVo {

    @ApiModelProperty(value = "管理台店铺的shopId,表id")
    private Long platformShopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;


    @ApiModelProperty(value = "店铺log")
    private String logoUrl;


    @ApiModelProperty(value = "营业时间,自行分割")
    private String businessHours;

    @ApiModelProperty(value = "小程序底部打标")
    private String miniBottomLog;

    @ApiModelProperty(value = "店铺电话")
    private String shopPhone;

    @ApiModelProperty(value = "1部署中 2正常 ，3已打烊，4禁用,5不在营业时间 6店铺到期")
    private Integer status;


    @ApiModelProperty(value = "店铺当前使用的模板名称")
    private String templateName;

    @ApiModelProperty(value = "店铺当前使用的模板标识")
    private TemplateCodeEnum templateCode;

    @ApiModelProperty(value = "到期时间")
    private LocalDateTime dueTime;

    @ApiModelProperty(value = "当前套餐等级")
    private Integer level;

    @ApiModelProperty(value = "套餐名称")
    private String packageName;

    @ApiModelProperty(value = "是否到期 0 不是 1是")
    private Integer isDue;


    @ApiModelProperty(value = "店铺当前使用模板的版本，或当前最新的版本")
    private String teamplateVersion;


    @ApiModelProperty(value = "当前支付渠道 1-微信支付 2-环迅支付 3-随行支付")
    private Integer payType;
    /**
     * 订单来源
     */
    @ApiModelProperty(value = "订单来源   0-用户购买 1-管理台购买（店铺列表，购买，续费） 2-平台赠送（用户创建店铺时自动赠送） 3-平台创建（为指定商户直接创建指定套餐店铺）4-代理付费")
    private Integer orderSource;
}
