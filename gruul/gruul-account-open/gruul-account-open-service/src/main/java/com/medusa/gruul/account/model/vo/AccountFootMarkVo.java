package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author  xiaoq
 *
 * @data 2020/2/28 11:01
 */
@Data
@ApiModel(value = "AccountFootMarkVo对象", description = "用户足迹展示信息")
public class AccountFootMarkVo {

    @ApiModelProperty(value = "用户收藏表id")
    private Long footmarkId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "商品主图")
    private String productPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品状态  0-上架 1-下架 2-售罄")
    private Integer status;

    @ApiModelProperty(value = "商品实际售价")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "指导价划线价")
    private BigDecimal originalPrice;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


    private String time;
}
