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
 * 基础库信息表
 * </p>
 *
 * @author alan
 * @since 2020-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_platform_libraries_info")
@ApiModel(value = "PlatformLibrariesInfo对象", description = "基础库信息表")
public class PlatformLibrariesInfo extends BaseNoTenantEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "基础库名称")
    @TableField("name")
    private String name;

    /**
     * 基础库类型 1通用 2 定制  暂时无用
     */
    @ApiModelProperty(value = "基础库类型 1通用 2定制")
    @TableField("type")
    private Integer type;

    /**
     * 0未运行服务  1正常 2异常，3已下线
     */
    @ApiModelProperty(value = "0未运行服务  1正常 2异常 3已下线 ")
    @TableField("status")
    private Integer status;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    @TableField("version")
    private String version;

    /**
     * 服务数量 todo 暂时无用
     */
    @ApiModelProperty(value = "服务数量")
    @TableField("count")
    private Integer count;

    /**
     * 分类类型：1 业务基础库 2 支撑基础库
     */
    @ApiModelProperty(value = "分类类型：1 业务基础库 2 支撑基础库")
    @TableField("category_type")
    private Integer categoryType;

    /**
     * 所属支撑基础库id ，只有是业务基础库时，才有此值
     */
    @ApiModelProperty(value = "所属支撑基础库id ，只有是业务基础库时，才有此值")
    @TableField("belong_id")
    private Long belongId;

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


    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识")
    @TableField("uniqueness")
    private String uniqueness;

}
