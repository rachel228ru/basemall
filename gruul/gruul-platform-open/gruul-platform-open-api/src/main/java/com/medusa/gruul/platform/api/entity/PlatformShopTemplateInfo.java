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
 * 店铺模版表
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_shop_template_info")
@ApiModel(value = "PlatformShopTemplateInfo对象", description = "店铺模版表")
public class PlatformShopTemplateInfo extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺模版名称
     */
    @ApiModelProperty(value = "店铺模版名称")
    @TableField("name")
    private String name;

    /**
     * 模版编号
     */
    @ApiModelProperty(value = "模版编号")
    @TableField("code")
    private String code;

    /**
     * 分类类型：1 系统模版 2 定制模版
     */
    @ApiModelProperty(value = "分类类型：1 系统模版 2 定制模版")
    @TableField("type")
    private Integer type;

    /**
     * 模版应用类型：1商城，2社区拼团，3门店版
     */
    @ApiModelProperty(value = "模版应用类型：1商城，2社区拼团，3门店版")
    @TableField("shop_template_type")
    private Integer shopTemplateType;

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
