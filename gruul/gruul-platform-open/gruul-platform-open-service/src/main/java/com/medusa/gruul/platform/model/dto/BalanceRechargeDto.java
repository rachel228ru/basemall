package com.medusa.gruul.platform.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author whh
 * @description
 * @data: 2020/8/1
 */
@Data
public class BalanceRechargeDto {

    @ApiModelProperty(value = "余额")
    @NotNull
    private BigDecimal balance;

    @ApiModelProperty(value = "支付类型 1-支付宝支付   2-微信支付  3-汇款支付")
    @NotNull(message = "支付类型错误")
    @Range(message = "支付类型错误", min = 1, max = 3)
    private Integer payType;


    @ApiModelProperty(value = "付款方信息(json) 线下打款(汇款支付)使用 {\"name\":\"姓名\",\"paymentTime\":\"付款时间\",\"account\":\"付款账号\"}")
    private String payInfo;

}
