package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/8/8
 */
@Data
public class ShopPackageOrderDto {


    @ApiModelProperty(value = "操作类型 1-订购 2-续费  3-升级")
    @NotNull(message = "操作类型为空", groups = {User.class, Admin.class})
    @Range(min = 1, max = 3, message = "操作类型错误", groups = {User.class, Admin.class})
    private Integer optionType;


    @ApiModelProperty(value = "支付方式  1:余额支付2:微信3:支付宝4:汇款支付 5:系统支付 ")
    @NotNull(message = "支付方式为空", groups = User.class)
    @Range(min = 1, max = 4, message = "支付方式错误", groups = User.class)
    private Integer payType;


    @ApiModelProperty(value = "汇款支付时填写支付方信息json")
    private String payInfo;

    @ApiModelProperty(value = "购买周期,单位为天数")
    private Integer buyPeriod;

    @ApiModelProperty(value = "指定套餐Id")
    @NotNull(message = "套餐未选择", groups = {User.class, Admin.class})
    private Long packageId;


    @ApiModelProperty(value = "店铺id")
    @NotNull(message = "店铺id不能为空", groups = {User.class, Admin.class})
    private Long shopId;

    @ApiModelProperty(value = "是否自动续费 0-不自动  1-自动")
    private Integer autoDeduct;

    @ApiModelProperty(value = "是否同意协议 1-同意")
    @Range(min = 1, max = 1, message = "协议未同意", groups = {User.class})
    @NotNull(message = "协议未同意", groups = {User.class})
    private Integer agreeProtocol;

    /**
     * 订单来源   0-用户购买 1-管理台购买（店铺列表，购买，续费） 2-平台赠送（用户创建店铺时自动赠送） 3-平台创建（为指定商户直接创建指定套餐店铺） 4-代理付费
     */
    @ApiModelProperty(hidden = true)
    private Integer orderSource;

    public interface User {

    }

    public interface Admin {

    }
}
