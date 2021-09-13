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
 * 默认数据表
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_default_value")
@ApiModel(value = "DefaultValue对象", description = "默认数据表")
public class DefaultValue extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("默认值唯一标识")
    @TableField("unique_identification")
    private String uniqueIdentification;

    @ApiModelProperty("默认值版本")
    @TableField("version")
    private String version;

    @ApiModelProperty("默认值生成方式 1-发送指队列 2-url地址 3-自行查询")
    @TableField("build_action")
    private Integer buildAction;

    @ApiModelProperty("请求地址")
    @TableField("url")
    private String url;

    @ApiModelProperty("mq信息json")
    @TableField("mq")
    private String mq;

    @ApiModelProperty("key-value默认值json字符串")
    @TableField("kv")
    private String kv;

}
