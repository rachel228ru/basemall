package com.medusa.gruul.common.data.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author whh
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class BaseNoTenantEntity<T extends Model<?>> extends Model<T> implements Serializable {


	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	private LocalDateTime createTime;


	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@TableField(value = "update_time",fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除
	 */
	@TableLogic
	@ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
	@TableField(value = "is_deleted", fill = FieldFill.INSERT)
	private Boolean deleted;
}
