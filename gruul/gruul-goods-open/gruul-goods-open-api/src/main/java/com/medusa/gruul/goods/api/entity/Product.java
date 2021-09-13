package com.medusa.gruul.goods.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 商品信息
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_product")
@ApiModel(value = "Product对象", description = "商品信息")
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 本店店铺id
     */
    @ApiModelProperty(value = "本店店铺id")
    @TableField("shop_id")
    private String shopId;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    @TableField("provider_id")
    private Long providerId;

    /**
     * 配送方式(0--商家配送，1--快递配送 2--同城配送)
     */
    @NotNull
    @ApiModelProperty(value = "配送方式(0--商家配送，1--快递配送 2--同城配送)")
    @TableField("distribution_mode")
    private Integer distributionMode;

    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    @TableField("freight_template_id")
    private Long freightTemplateId;

    /**
     * 属性模版ID
     */
    @NotNull
    @ApiModelProperty(value = "属性模版ID")
    @TableField("attribute_id")
    private Long attributeId;

    /**
     * 属性模板名称
     */
    @NotNull
    @ApiModelProperty(value = "属性模板名称")
    @TableField("attribute_name")
    private String attributeName;

    /**
     * 限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)
     */
    @NotNull
    @ApiModelProperty(value = "限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)")
    @TableField("limit_type")
    private Integer limitType;

    /**
     * 销售专区
     */
    @NotNull
    @ApiModelProperty(value = "销售专区(默认商超系统，0--商超系统，2--限时秒杀)")
    @TableField("sale_mode")
    private Long saleMode;

    /**
     * 商品名称
     */
    @NotBlank
    @ApiModelProperty(value = "商品名称")
    @TableField("name")
    private String name;

    /**
     * 展示图片
     */
    @NotBlank
    @ApiModelProperty(value = "展示图片")
    @TableField("pic")
    private String pic;

    /**
     * 宽屏展示图片
     */
    @ApiModelProperty(value = "宽屏展示图片")
    @TableField("wide_pic")
    private String widePic;

    /**
     * 画册图片，连产品图片限制为6张，以逗号分割
     */
    @NotBlank
    @ApiModelProperty(value = "画册图片，连产品图片限制为6张，以逗号分割")
    @TableField("album_pics")
    private String albumPics;

    /**
     * 视频url
     */
    @NotBlank
    @ApiModelProperty(value = "视频url")
    @TableField("video_url")
    private String videoUrl;

    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    @TableField("product_sn")
    private String productSn;

    /**
     * 状态(默认上架，0--下架，1--上架)
     */
    @ApiModelProperty(value = "状态(默认上架，0--下架，1--上架)")
    @TableField("status")
    private Integer status;

    /**
     * 商品位置(默认线上，0--线上，1--素材库)
     */
    @ApiModelProperty(value = "商品位置(默认线上，0--线上，1--素材库)")
    @TableField("place")
    private Integer place;

    /**
     * 电商平台链接
     */
    @ApiModelProperty(value = "电商平台链接")
    @TableField("csv_url")
    private String csvUrl;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    @TableField("sale")
    private Integer sale;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    @TableField("unit")
    private String unit;

    /**
     * 商品重量，默认为克
     */
    @NotNull
    @ApiModelProperty(value = "商品重量，默认为克")
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
     */
    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    @TableField("service_ids")
    private String serviceIds;


    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    @TableField("detail")
    private String detail;

    /**
     * 规格是否展开
     */
    @ApiModelProperty(value = "规格是否展开")
    @TableField("is_open_specs")
    private Boolean openSpecs;

    /**
     * 卖点描述
     */
    @ApiModelProperty(value = "卖点描述")
    @TableField("sale_describe")
    private String saleDescribe;

    /**
     * 销售属性
     */
    @ApiModelProperty(value = "销售属性")
    @TableField("attribute")
    private String attribute;

    /**
     * 评分
     */
    @ApiModelProperty(value = "评分")
    @TableField("score")
    private BigDecimal score;

}