package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * The type Order share info.
 * <p>
 * 订单晒单字段
 *
 * @author alan
 * @date 2020 /7/8 21:24
 */
@Data
@ApiModel("订单晒单字段")
public class OrderShareInfo {
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 背景图
     */
    @ApiModelProperty(value = "背景图")
    private String background;
    /**
     * 商品详情集合
     */
    @ApiModelProperty(value = "商品详情集合")
    private List<ShareItemVo> itemList;
}
