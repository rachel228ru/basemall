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

import java.io.Serializable;

/**
 * <p>
 * 服务信息表
 * </p>
 *
 * @author alan
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_platform_service_info")
@ApiModel(value = "PlatformServiceInfo对象", description = "服务信息表")
public class PlatformServiceInfo extends BaseNoTenantEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称")
    @TableField("name")
    private String name;

    /**
     * 服务类型 1通用 2 定制
     */
    @ApiModelProperty(value = "服务类型 1通用 2 定制")
    @TableField("type")
    private Integer type;

    /**
     * 1正常 2异常
     */
    @ApiModelProperty(value = "1正常 2异常")
    @TableField("status")
    private Integer status;

    /**
     * 服务版本
     */
    @ApiModelProperty(value = "服务版本")
    @TableField("version")
    private String version;

    /**
     * 服务数量  实时查询多少个服务在运行
     */
    @ApiModelProperty(value = "服务数量")
    @TableField("count")
    private Integer count;

    /**
     * oundation_libraries id 区分 业务基础库和支撑基础库
     */
    @ApiModelProperty(value = "oundation_libraries id 区分 业务基础库和支撑基础库")
    @TableField("libraries_info_id")
    private Long librariesInfoId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    /**
     * 服务说明
     */
    @ApiModelProperty(value = "服务说明")
    @TableField("description")
    private String description;


}
