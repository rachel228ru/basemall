package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseNoTenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 店铺套餐
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sys_shop_package")
@ApiModel(value = "SysShopPackage对象", description = "店铺套餐")
public class SysShopPackage extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 套餐名称
     */
    @ApiModelProperty(value = "套餐名称")
    @TableField("name")
    private String name;

    /**
     * 套餐说明
     */
    @ApiModelProperty(value = "套餐说明")
    @TableField("remark")
    private String remark;

    /**
     * 套餐等级
     */
    @ApiModelProperty(value = "套餐等级")
    @TableField("level")
    private Integer level;

    /**
     * 套餐价格
     */
    @ApiModelProperty(value = "套餐价格")
    @TableField("package_price")
    private BigDecimal packagePrice;

    /**
     * 套餐使用价格单位 1天，2月，3年
     */
    @ApiModelProperty(value = "套餐使用价格单位 1天，2月，3年")
    @TableField("package_price_unit")
    private Integer packagePriceUnit;

    /**
     * 优惠价json y-年 m-月 d-天  "[{"unit":"y","value":365,"price":256}]"
     */
    @ApiModelProperty(value = "优惠价json  unit=y-年 m-月 d-天,value =单位值,price优惠价 [{\"unit\":\"y\",\"value\":365,\"price\":100}]")
    @TableField("discounts_json")
    private String discountsJson;

    /**
     * 功能描述
     */
    @ApiModelProperty(value = "功能描述")
    @TableField("functionDesc")
    private String functionDesc;

    /**
     * 套餐开启状态 0-关闭  1-开启
     */
    @ApiModelProperty(value = "套餐开启状态 0-关闭  1-开启")
    @TableField("open_state")
    private Integer openState;

    /**
     * 模版版本id
     */
    @ApiModelProperty(value = "模版版本id")
    @TableField("template_version_id")
    private Long templateVersionId;

    /**
     * 模版id
     */
    @ApiModelProperty(value = "模版id")
    @TableField("template_id")
    private Long templateId;

    /**
     * 上一次操作人id
     */
    @ApiModelProperty(value = "上一次操作人id")
    @TableField("operate_id")
    private Long operateId;

    /**
     * 上一次操作人名称
     */
    @ApiModelProperty(value = "上一次操作人名称")
    @TableField("operate_name")
    private String operateName;


}
