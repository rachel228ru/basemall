package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xiaoq
 *
 * @data 2020/2/24 10:57
 */

@Data
@ApiModel(value = "UserCollectVo对象", description = "用户收藏展示信息")
public class UserCollectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户收藏表id")
    private Long collectId;

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


}
