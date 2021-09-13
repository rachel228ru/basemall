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
 * 店铺模版详情小程序版本子表
 * </p>
 *
 * @author whh
 * @since 2020-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_shop_template_detail_minis")
@ApiModel(value = "PlatformShopTemplateDetailMinis对象", description = "店铺模版详情小程序版本子表")
public class PlatformShopTemplateDetailMinis extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 表id
     */
    @ApiModelProperty(value = "表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 小程序模版库中templateId
     */
    @ApiModelProperty(value = "小程序模版库中templateId")
    @TableField("code_templete_id")
    private String codeTempleteId;

    /**
     * 模版库中自定义版本号
     */
    @ApiModelProperty(value = "模版库中自定义版本号")
    @TableField("code_templete_version")
    private String codeTempleteVersion;

    /**
     * 版本说明
     */
    @ApiModelProperty(value = "版本说明")
    @TableField("version_explain")
    private String versionExplain;

    /**
     * 店铺模版详情表id
     */
    @ApiModelProperty(value = "店铺模版详情表id")
    @TableField("shop_template_detail_id")
    private Long shopTemplateDetailId;


}
