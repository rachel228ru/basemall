package com.medusa.gruul.account.api.entity;

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

/**
 * <p>
 * 用户第三方Id表
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_account_oauths")
@ApiModel(value = "MiniAccountOauths对象", description = "用户第三方Id表")
public class MiniAccountOauths extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id ")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户唯一id
     */
    @ApiModelProperty(value = "用户唯一id")
    @TableField("user_id")
    private String userId;

    /**
     * 授权类型 1-微信小程序,2-公众号
     */
    @ApiModelProperty(value = "授权类型 1-微信小程序,2-公众号")
    @TableField("oauth_type")
    private Integer oauthType;

    /**
     * 第三方openId
     */
    @ApiModelProperty(value = "第三方openId")
    @TableField("open_id")
    private String openId;

    /**
     * 第三方unionid
     */
    @ApiModelProperty(value = "第三方unionid")
    @TableField("union_id")
    private String unionId;

}
