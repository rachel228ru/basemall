package com.medusa.gruul.shops.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description: 引导页信息表
 * @Author: xiaoq
 * @Date : 2020/10/15 15:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_shop_guide_page")
@ApiModel(value = "ShopGuidePage对象", description = "引导页信息表")
public class ShopGuidePage<T extends Model<?>> extends Model<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiModelProperty(value = "网址")
	@TableField(value = "url", updateStrategy = FieldStrategy.IGNORED)
	private String url;

	@ApiModelProperty(value = "路径")
	@TableField(value = "path", updateStrategy = FieldStrategy.IGNORED)
	private String path;

    @ApiModelProperty(value = "path名称 用于比较")
    @TableField(value = "link", updateStrategy = FieldStrategy.IGNORED)
	private String link;

	@ApiModelProperty(value = "小程序id 用于跳转")
	@TableField(value = "app_id", updateStrategy = FieldStrategy.IGNORED)
	private String appId;

	@ApiModelProperty(value = " 0为path 1为app_id")
	@TableField("type")
	private Integer type;

	@ApiModelProperty(value = "店铺id")
	@TableField("shop_id")
	private String shopId;

	@ApiModelProperty("租户ID")
	@TableField("tenant_id")
	private String tenantId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("更新时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	@TableLogic
	@ApiModelProperty("删除状态：0->未删除；1->已删除")
	@TableField(value = "is_deleted", fill = FieldFill.INSERT)
	private Boolean deleted;

}
