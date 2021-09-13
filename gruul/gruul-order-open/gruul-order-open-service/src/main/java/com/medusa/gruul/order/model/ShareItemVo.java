package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The type Share item vo.
 * <p>
 * 晒单商品详情
 *
 * @author alan
 * @date 2020 /7/8 21:28
 */
@Data
@ApiModel("晒单商品详情")
public class ShareItemVo {

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String productPic;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;

}
