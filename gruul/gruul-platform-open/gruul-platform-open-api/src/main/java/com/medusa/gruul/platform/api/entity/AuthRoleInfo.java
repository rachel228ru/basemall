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
 * 用户角色表
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Data
@Accessors(chain = true)
@TableName("t_auth_role_info")
@ApiModel(value = "AuthRoleInfo对象", description = "用户角色表")
public class AuthRoleInfo {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    @TableField("role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    @TableField("role_code")
    private String roleCode;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    /**
     * 创建人姓名
     */
    @ApiModelProperty(value = "创建人姓名")
    @TableField("create_user_name")
    private String createUserName;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 最近一次修改人id
     */
    @ApiModelProperty(value = "最近一次修改人id")
    @TableField("last_modify_user_id")
    private Long lastModifyUserId;

    /**
     * 最近一次修改人姓名
     */
    @ApiModelProperty(value = "最近一次修改人姓名")
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

}
