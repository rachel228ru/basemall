package com.medusa.gruul.goods.api.model.dto.api;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.medusa.gruul.goods.api.entity.ShoppingCart;
import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 购物车信息
 *
 * @author lcy
 * @since 2019-11-15
 */
@Data
@ApiModel(value = "新增或修改购物车DTO")
public class ApiShoppingCartDto implements Serializable {

    /**
     * 购物车操作时间 表示加入购物车或者更新购物车商品时间
     */
    @ApiModelProperty(value = "购物车商品操作时间")
    private Date sortTime;

    @ApiModelProperty(value = "购物车商品自增长id")
    private Long id;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id", example = "1")
    @NotNull
    private Long productId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * sku_id
     */
    @ApiModelProperty(value = "sku_id")
    @NotNull
    private Long skuId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    @NotNull
    private Integer goodsNumber;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @NotNull
    private String productName;

    /**
     * 展示图片
     */
    @ApiModelProperty(value = "展示图片")
    private String pic;

    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    @NotNull
    private String productSn;

    /**
     * 状态(默认上架，0--下架（仓库中），1--上架，2--已售完)
     */
    @ApiModelProperty(value = "状态(默认上架，0--下架（仓库中），1--上架，2--已售完)")
    @NotNull
    private Integer status;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityEndTime;

    /**
     * 选中状态(0--未选中，1--已选中)
     */
    @ApiModelProperty(value = "选中状态(0--未选中，1--已选中)")
    private Integer selectStatus;

    @ApiModelProperty(value = "商品sku信息")
    private List<SkuStockDto> skuStocks;

    public ShoppingCart coverShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtil.copyProperties(this, shoppingCart);
        return shoppingCart;
    }
}