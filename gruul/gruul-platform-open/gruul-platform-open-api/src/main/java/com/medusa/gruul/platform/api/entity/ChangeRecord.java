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
 * 小程序变更记录
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_change_record")
@ApiModel(value = "ChangeRecord对象", description = "小程序变更记录")
public class ChangeRecord extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 小程序id
     */
    @ApiModelProperty(value = "小程序id")
    @TableField("mini_info_id")
    private String miniInfoId;

    /**
     * 变更类型 0-续费 1-更改套餐 2-禁用或开启 3-信息更改
     */
    @ApiModelProperty(value = "变更类型 0-续费 1-更改套餐 2-禁用或开启  3-信息更改")
    @TableField("change_type")
    private Boolean changeType;

    /**
     * 老数据
     */
    @ApiModelProperty(value = "老数据")
    @TableField("old_data")
    private String oldData;

    /**
     * 新数据
     */
    @ApiModelProperty(value = "新数据")
    @TableField("new_data")
    private String newData;

}
