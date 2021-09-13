package com.medusa.gruul.account.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/5/11
 */
@Data
public class MiniAccountExtDto {

    @ApiModelProperty(value = "店铺用户id")
    private String shopUserId;

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

}
