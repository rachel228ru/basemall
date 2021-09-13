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

import java.time.LocalDateTime;

/**
 * <p>
 * 接口调用凭据和授权信息
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_auth_token")
@ApiModel(value = "AuthToken对象", description = "接口调用凭据和授权信息")
public class AuthToken extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 授权方appid
     */
    @ApiModelProperty(value = "授权方appid")
    @TableField("authorizer_appid")
    private String authorizerAppid;

    /**
     * 授权方令牌
     */
    @ApiModelProperty(value = "授权方令牌")
    @TableField("authorizer_access_token")
    private String authorizerAccessToken;

    /**
     * 令牌到期时间
     */
    @ApiModelProperty(value = "令牌到期时间")
    @TableField("expires_in")
    private LocalDateTime expiresIn;

    /**
     * 刷新令牌
     */
    @ApiModelProperty(value = "刷新令牌")
    @TableField("authorizer_refresh_token")
    private String authorizerRefreshToken;

    /**
     * 授权给开发者的权限集列表
     */
    @ApiModelProperty(value = "授权给开发者的权限集列表")
    @TableField("func_info")
    private String funcInfo;

}
