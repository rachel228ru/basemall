package com.medusa.gruul.platform.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.medusa.gruul.platform.api.entity.SysShopPackageOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author whh
 * @description
 * @data: 2020/8/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysShopPackageOrderDto extends SysShopPackageOrder {

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    @TableField("nike_name")
    private String nikeName;

    /**
     * 头像url
     */
    @ApiModelProperty(value = "头像url")
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;
}
