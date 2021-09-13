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
 * 店铺模版详情表
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_shop_template_detail")
@ApiModel(value = "PlatformShopTemplateDetail对象", description = "店铺模版详情表")
public class PlatformShopTemplateDetail extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    @TableField("version")
    private String version;

    /**
     * 业务基础库id
     */
    @ApiModelProperty(value = "业务基础库id")
    @TableField("libraries_info_id")
    private Long librariesInfoId;

    /**
     * 业务基础库名称
     */
    @ApiModelProperty(value = "业务基础库名称")
    @TableField("libraries_info_name")
    private String librariesInfoName;

    /**
     * 小程序代码模版id
     */
    @ApiModelProperty(value = "小程序代码模版id")
    @TableField("code_templete_id")
    @Deprecated
    private Long codeTempleteId;

    /**
     * 小程序代码模版名称
     */
    @ApiModelProperty(value = "小程序代码模版名称")
    @TableField("code_templete_name")
    @Deprecated
    private String codeTempleteName;

    /**
     * pc端路径
     */
    @ApiModelProperty(value = "pc端路径")
    @TableField("pc_termina_url")
    private String pcTerminaUrl;

    /**
     * pc端版本
     */
    @ApiModelProperty(value = "pc端版本")
    @TableField("pc_termina_version")
    private String pcTerminaVersion;


    /**
     * pc端关联页面
     */
    @ApiModelProperty(value = "pc端关联页面,json存储,键值对")
    private String pcUrlMap;


    /**
     * 移动端路径
     */
    @ApiModelProperty(value = "移动端路径")
    @TableField("mobile_terminal_url")
    private String mobileTerminalUrl;

    /**
     * 移动端版本
     */
    @ApiModelProperty(value = "移动端版本")
    @TableField("mobile_terminal_version")
    private String mobileTerminalVersion;

    /**
     * 服务店铺数量
     *
     * @Deprecated 在s1.0.5中移除了该值得修改, 改为实时查询
     */
    @ApiModelProperty(value = "服务店铺数量")
    @TableField("server_count")
    @Deprecated
    private Integer serverCount;

    /**
     * 店铺模版表id
     */
    @ApiModelProperty(value = "店铺模版表id")
    @TableField("shop_template_id")
    private Long shopTemplateId;


    /**
     * 更新日志
     */
    @ApiModelProperty(value = "更新日志")
    @TableField("version_log")
    private String versionLog;


}
