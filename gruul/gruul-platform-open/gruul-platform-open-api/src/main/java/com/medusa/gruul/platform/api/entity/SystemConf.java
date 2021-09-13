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
 * 系统配置
 * </p>
 *
 * @author whh
 * @since 2019-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_system_conf")
@ApiModel(value = "SystemConf对象", description = "系统配置")
public class SystemConf extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * key
     */
    @ApiModelProperty(value = "key")
    @TableField("param_key")
    private String paramKey;

    /**
     * value
     */
    @ApiModelProperty(value = "value 格式json形式")
    @TableField("param_value")
    private String paramValue;

    /**
     * 状态 0：隐藏 1：显示
     */
    @ApiModelProperty(value = "状态   0：隐藏   1：显示")
    @TableField("status")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

}
