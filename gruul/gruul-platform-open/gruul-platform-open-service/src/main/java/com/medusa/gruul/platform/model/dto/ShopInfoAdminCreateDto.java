package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 系统后台创建时使用
 *
 * @author whh
 * @description
 * @data: 2020/3/8
 */
@Data
public class ShopInfoAdminCreateDto {


    @ApiModelProperty(value = "店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String shopName;

    @ApiModelProperty(value = "是否私有化部署（0否 1是）")
    @Range(min = 0, max = 1)
    @NotNull
    private Integer isPrivatizationDeployment;

    @ApiModelProperty(value = "模板id t_platform_shop_template_info 模版id")
    @NotNull
    private Long shopTemplateId;

    @ApiModelProperty(value = "商户id t_platform_account_info的id")
    @NotNull
    private Long accountId;

    @ApiModelProperty(value = "服务器IP")
    private String serverIp;

    @ApiModelProperty(value = "店铺首页图片")
    private String logoUrl;

    @ApiModelProperty(value = "服务器域名")
    private String domainName;


    @ApiModelProperty(value = "cdn配置json")
    private String cdnCfg;


    @ApiModelProperty(value = "0不使用，1七牛云，2阿里云，腾讯云")
    private Integer cdnType;

    @ApiModelProperty(value = "套餐id")
    @NotNull(message = "套餐id不能为空")
    private Long packageId;


    @ApiModelProperty(value = "套餐赠送时长,单位天")
    @NotNull(message = "赠送时长不能为空")
    private Integer givePackageTime;

    @ApiModelProperty(value = "订单价格")
    @NotNull(message = "订单价格不能为空")
    private BigDecimal orderPirce;


}
