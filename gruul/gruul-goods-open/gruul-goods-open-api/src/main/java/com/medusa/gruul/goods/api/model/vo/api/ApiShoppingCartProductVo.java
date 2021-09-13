package com.medusa.gruul.goods.api.model.vo.api;

import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会员购物车商品缓存信息
 *
 * @author lcy
 * @since 2019-11-15
 */
@Data
@ApiModel(value = "ApiShoppingCartProductVo对象", description = "小程序会员购物车商品缓存信息")
public class ApiShoppingCartProductVo implements Serializable {

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private Long productId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 展示图片
     */
    @ApiModelProperty(value = "展示图片")
    private String pic;

    /**
     * 配送方式(0--商家配送，1--快递配送 2--同城配送)
     */
    @ApiModelProperty(value = "配送方式(0--商家配送，1--快递配送 2--同城配送)")
    private Integer distributionMode;

    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    private String productSn;

    /**
     * 销售专区(默认商超系统，0--商超系统，2--限时秒杀)
     */
    @ApiModelProperty(value = "销售专区(默认商超系统，0--商超系统，2--限时秒杀)")
    private Long saleMode;

    /**
     * 状态(默认上架，0--下架（仓库中），1--上架)
     */
    @ApiModelProperty(value = "状态(默认上架，0--下架，1--上架)")
    private Integer status;

    /**
     * 删除状态(0--未删除，1--已删除)
     */
    @ApiModelProperty(value = "删除状态(0--未删除，1--已删除)")
    private Integer deleted;

    @ApiModelProperty(value = "商品sku信息")
    private List<SkuStockDto> skuStocks;

}