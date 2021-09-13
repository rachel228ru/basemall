package com.medusa.gruul.shops.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_shops_menu_config")
@ApiModel(value = "店铺合伙人菜单属性配置实体", description = "店铺合伙人菜单属性配置表")
@Getter
@Setter
public class ShopsMenuConfig extends BaseEntity {


    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 店铺id
     */
    @NotNull
    @ApiModelProperty(value = "店铺id")
    @TableField("shop_id")
    private String shopId;


    /**
     * 组件属性 json串
     */
    @NotNull
    @ApiModelProperty(value = "组件属性 json串")
    @TableField("properties")
    private String properties;

}