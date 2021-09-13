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
 * 店铺部署信息表
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_shop_deploy")
@ApiModel(value = "PlatformShopDeploy对象", description = "店铺部署信息表")
public class PlatformShopDeploy extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "所属店铺id")
    @TableField("shop_id")
    private Long shopId;

    /**
     * 服务器IP
     */
    @ApiModelProperty(value = "服务器IP")
    @TableField("server_ip")
    private String serverIp;

    /**
     * 服务器域名
     */
    @ApiModelProperty(value = "服务器域名")
    @TableField("domain_name")
    private String domainName;

    /**
     * cdn配置json
     */
    @ApiModelProperty(value = "cdn配置json")
    @TableField("cdn_cfg")
    private String cdnCfg;

    /**
     * 0不使用，1七牛云，2阿里云，腾讯云
     */
    @ApiModelProperty(value = "0不使用，1七牛云，2阿里云，腾讯云")
    @TableField("cdn_type")
    private Integer cdnType;


}
