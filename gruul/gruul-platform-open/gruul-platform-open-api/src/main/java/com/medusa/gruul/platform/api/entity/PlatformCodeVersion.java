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
 * 店铺信息表
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_code_version")
@ApiModel(value = "PlatformCodeVersion对象", description = "店铺信息表")
public class PlatformCodeVersion extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * pc后台版本
     */
    @ApiModelProperty(value = "pc后台版本")
    @TableField("pc_version")
    private String pcVersion;

    /**
     * 微信小程序版本
     */
    @ApiModelProperty(value = "微信小程序版本")
    @TableField("wx_version")
    private String wxVersion;

    /**
     * 微信小程序版本
     */
    @ApiModelProperty(value = "微信小程序版本")
    @TableField("mobile_version")
    private String mobileVersion;

    /**
     * 1 标准版，2企业版，3旗舰版
     */
    @ApiModelProperty(value = "1 标准版，2企业版，3旗舰版")
    @TableField("level")
    private Integer level;

    /**
     * 分类类型：1 系统模版 2 定制模版
     */
    @ApiModelProperty(value = "分类类型：1 系统模版 2 定制模版")
    @TableField("type")
    private Integer type;

    /**
     * 移动web路径
     */
    @ApiModelProperty(value = "移动web路径")
    @TableField("mobile_url")
    private String mobileUrl;

    /**
     * PC端后台url
     */
    @ApiModelProperty(value = "PC端后台url")
    @TableField("pc_url")
    private String pcUrl;

    /**
     * 模版编号
     */
    @ApiModelProperty(value = "模版编号")
    @TableField("template_code")
    private String templateCode;


}
