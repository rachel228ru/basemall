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

/**
 * <p>
 * 小程序审核记录表
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_audit_record")
@ApiModel(value = "AuditRecord对象", description = "小程序审核记录表")
public class AuditRecord extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 代码库中的代码模版ID
     */
    @ApiModelProperty(value = "代码库中的代码模版ID")
    @TableField("template_id")
    private Long templateId;

    /**
     * 授权APPID
     */
    @ApiModelProperty(value = "授权APPID")
    @TableField("app_id")
    private String appId;

    /**
     * 审核编号
     */
    @ApiModelProperty(value = "审核编号")
    @TableField("audit_id")
    private String auditId;

    /**
     * 审核状态 0审核通过,1审核失败，2审核中
     */
    @ApiModelProperty(value = "审核状态 0审核通过,1审核失败，2审核中")
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 发布状态
     */
    @ApiModelProperty(value = "发布状态 0-未发布 1-已发布")
    @TableField("release_state")
    private Integer releaseState;

    /**
     * 代码版本ID
     */
    @ApiModelProperty(value = "当前模板代码版本id(t_platform_shop_template_detail)")
    @TableField("version_id")
    private Long versionId;

    /**
     * 审核失败原因
     */
    @ApiModelProperty(value = "审核失败原因")
    @TableField("reason")
    private String reason;

}
