package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The type User evaluate vo.
 * <p>
 * 商品详情页用户评价
 *
 * @author alan
 * @date 2020 /1/18 21:16
 */
@Data
@ApiModel(value = "商品详情页用户评价")
public class UserEvaluateVo {

    @ApiModelProperty(value = "总条数")
    private int total;

    @ApiModelProperty(value = "所有的")
    private int all;

    @ApiModelProperty(value = "有内容的")
    private int hasContent;

    @ApiModelProperty(value = "有图片的")
    private int hasPicture;

    @ApiModelProperty(value = "好评率：99 表示好评率为99%")
    private int praiseRate;


}
