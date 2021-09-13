package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
/**
 * <p> 货物实体类型</P>
 * <p>Version: 1.0</p>
 * <p>Author: Mr.zhaozheng </P>
 * <p>Date: 2019-11-22 13:37 </p>
 */
public class GoodsBean {
    /**
     * goodId : 1234
     * img : 123444.jpg
     * num : 13
     * goodSkuId : 123
     * goodSkuCode : 123444
     * goodSpecs : 商品规格
     */
    @ApiModelProperty(value = "商品id,订单服务")
    private Long goodId;
    @ApiModelProperty(value = "商品 名称,订单服务")
    private String goodName;
    @ApiModelProperty(value = "商品 图片,订单服务")
    private String img;
    @ApiModelProperty(value = "商品 规格 id,订单服务")
    private Long goodSkuId;
    @ApiModelProperty(value = "商品 规格 code,订单服务")
    private String goodSkuCode;
    @ApiModelProperty(value = "商品 规格中文解释,订单服务")
    private String goodSpecs;
    @ApiModelProperty(value = "商品 价格,订单服务")
    private BigDecimal realPrice;
    @ApiModelProperty(value = "商品 购买数量,订单服务")
    private Integer num;
}
