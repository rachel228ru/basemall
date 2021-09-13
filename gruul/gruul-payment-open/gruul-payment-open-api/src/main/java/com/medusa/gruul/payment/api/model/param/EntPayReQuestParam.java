package com.medusa.gruul.payment.api.model.param;


import com.medusa.gruul.payment.api.enums.CheckNameEnum;
import com.medusa.gruul.payment.api.enums.PayChannelEnum;
import com.medusa.gruul.payment.api.util.ParamMd5SignUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author create by zq
 * @date created in 2019/11/18
 */
@Data
@ToString(exclude = "md5")
@NoArgsConstructor
public class EntPayReQuestParam {


    /**
     * 支付渠道:1-微信
     */
    @NonNull
    @ApiModelProperty(value = "支付渠道:1-微信")
    private PayChannelEnum payChannel;


    /**
     * 订单总金额，单位为分
     */
    @NonNull
    @ApiModelProperty(value = "订单总金额，单位为分")
    private Integer amount;


    /**
     * 企业付款备注
     */
    @NonNull
    @ApiModelProperty(value = "企业付款备注")
    private String description;


    /**
     * 租户标识
     */
    @NonNull
    @ApiModelProperty(value = "租户标识")
    private String tenantId;


    /**
     * 用户标识
     */
    @NonNull
    @ApiModelProperty(value = "用户标识")
    private String openid;


    /**
     * 校验用户姓名选项
     */
    @NonNull
    @ApiModelProperty(value = "校验用户姓名选项")
    private CheckNameEnum checkName;


    /**
     * 收款用户姓名. 可选
     */
    @ApiModelProperty(value = "收款用户姓名.")
    private String reUserName;


    /**
     * 调用接口的机器Ip地址
     */
    @NonNull
    @ApiModelProperty(value = "调用接口的机器Ip地址")
    private String spbillCreateIp;


    /**
     * 参数校验
     */
    @NonNull
    @ApiModelProperty(value = "参数校验")
    private String md5;


    public EntPayReQuestParam md5() {
        this.md5 = ParamMd5SignUtils.md5(this);
        return this;
    }

}
