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
 * @Description: 引导页开关
 * @Author: xiaoq
 * @Date : 2020/10/17 13:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_shop_guide_page_switch")
@ApiModel(value = "ShopGuidePageSwitch对象", description = "引导页开关表")
public class ShopGuidePageSwitch<T extends Model<?>> extends Model<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("引导页是否开启 0 -->未开启 1-->已开启")
	@TableField("is_open")
	private Boolean open;

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
