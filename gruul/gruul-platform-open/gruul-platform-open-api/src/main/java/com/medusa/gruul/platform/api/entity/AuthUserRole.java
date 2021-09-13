package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Data
@Accessors(chain = true)
@TableName("t_auth_user_role")
@ApiModel(value = "AuthUserRole对象", description = "用户角色关系表")
public class AuthUserRole {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    @TableField("role_id")
    private Long roleId;

    /**
     * 创建该角色用户id
     */
    @ApiModelProperty(value = "创建该角色用户id")
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 最近更新人id
     */
    @ApiModelProperty(value = "最近更新人id")
    @TableField("last_modify_user_id")
    private Long lastModifyUserId;

    /**
     * 最近更新人姓名
     */
    @ApiModelProperty(value = "最近更新人姓名")
    @TableField("last_modify_user_name")
    private String lastModifyUserName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Boolean deleted;


    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private String tenantId;
}
