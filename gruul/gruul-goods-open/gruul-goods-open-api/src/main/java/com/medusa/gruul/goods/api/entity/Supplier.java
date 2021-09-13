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
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <p>
 * 供应商信息
 * </p>
 *
 * @author lcysike
 * @since 2019-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_supplier")
@ApiModel(value = "Supplier对象", description = "供应商信息")
public class Supplier extends BaseEntity {

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
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    /**
     * 供应商识别号
     */
    @ApiModelProperty(value = "供应商识别号")
    @TableField("supplier_sn")
    private String supplierSn;

    /**
     * 供应商名称
     */
    @NotBlank
    @Size(max = 64)
    @ApiModelProperty(value = "供应商名称")
    @TableField("name")
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @TableField("mobile")
    private String mobile;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    @TableField("city")
    private String city;

    /**
     * 县
     */
    @ApiModelProperty(value = "县")
    @TableField("country")
    private String country;

    /**
     * 地区
     */
    @ApiModelProperty(value = "地区")
    @TableField("address")
    private String address;

    /**
     * 完整地址
     */
    @ApiModelProperty(value = "完整地址")
    @TableField("area")
    private String area;

    /**
     * 产品信息
     */
    @ApiModelProperty(value = "产品信息")
    @TableField("product_info")
    private String productInfo;

    /**
     * 状态(默认待审核，0--已关闭，1--已审核，2--待审核，3--禁用中)
     */
    @ApiModelProperty(value = "状态(默认待审核，0--已关闭，1--已审核，2--待审核，3--禁用中)")
    @TableField("status")
    private Integer status;

    /**
     * 评分
     */
    @ApiModelProperty(value = "评分")
    @TableField("score")
    private BigDecimal score;

    /**
     * 注册来源（0--后台注册，1--小程序）
     */
    @ApiModelProperty(value = "注册来源，0--后台注册，1--小程序")
    @TableField("come_from")
    private Integer comeFrom;

    /**
     * 订阅消息模版id
     */
    @ApiModelProperty(value = "订阅消息模版id")
    @TableField("template_id")
    private String templateId;

}