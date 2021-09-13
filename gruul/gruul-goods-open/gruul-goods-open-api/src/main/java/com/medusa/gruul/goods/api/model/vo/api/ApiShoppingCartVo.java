package com.medusa.gruul.goods.api.model.vo.api;

import com.medusa.gruul.goods.api.model.vo.manager.SkuStockVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 会员购物车信息
 *
 * @author lcy
 * @since 2019-11-15
 */
@Data
@ApiModel(value = "ApiShoppingCartVo对象", description = "小程序会员购物车查询返回信息")
public class ApiShoppingCartVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Long skuId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNumber;

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
     * 状态(默认上架，0--下架（仓库中），1--上架，2--已售完)
     */
    @ApiModelProperty(value = "状态(默认上架，0--下架（仓库中），1--上架，2--已售完)")
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
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private Date activityEndTime;

    /**
     * 活动状态 0-为开始 1-进行中 2-失效
     */
    @ApiModelProperty(value = "活动状态 0-为开始 1-进行中 2-失效")
    private String activityStatus;

    /**
     * 删除状态(0--未删除，1--已删除)
     */
    @ApiModelProperty(value = "删除状态(0--未删除，1--已删除)")
    private Integer deleted;

    /**
     * 选中状态(0--未选中，1--已选中)
     */
    @ApiModelProperty(value = "选中状态(0--未选中，1--已选中)")
    private Integer selectStatus;


    @ApiModelProperty(value = "商品sku信息")
    private List<SkuStockVo> skuStocks;

}