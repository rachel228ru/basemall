package com.medusa.gruul.account.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description 用户扩展字段修改, 只需传入需要修改的字段
 * @data: 2020/1/6
 */
@Data
public class MiniAccountExtendsUpdateDto {


    /**
     * 设置连续签到天数
     */
    @ApiModelProperty(value = "连续签到天数")
    private Integer clockNum;



    /**
     * 设置用户
     */
    @ApiModelProperty(value = "设置用户")
    private Integer communityType;



    @ApiModelProperty(value = "optionType = 1时该字段有效 有效 累加额度")
    private BigDecimal cumsumLimit;

    @ApiModelProperty(value = "optionType = 1时该字段有效 有效 最后交易时间")
    private LocalDateTime lastDealTime;

    @ApiModelProperty(value = "optionType = 1时该字段有效 累加类型 1积分消费  2-订单完成 ")
    private Integer cumsumType;

    @ApiModelProperty(value = "optionType = 1时该字段有效  业务数据id(根据自身业务传入订单id或有唯一代表性的id)")
    private String businessId;

    @ApiModelProperty(value = "数据操作类型 1-累加消费总额 2-小区数据  3-签到数据")
    private Integer optionType;


}
