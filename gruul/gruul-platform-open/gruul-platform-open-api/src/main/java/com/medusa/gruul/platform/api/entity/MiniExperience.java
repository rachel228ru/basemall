package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 小程序体验者表
 * </p>
 *
 * @author whh
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_experience")
@ApiModel(value = "MiniExperience对象", description = "小程序体验者表")
public class MiniExperience extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id ")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 体验者
     */
    @ApiModelProperty(value = "体验者微信号")
    @TableField("experience_name")
    private String experienceName;

    /**
     * 小程序id
     */
    @ApiModelProperty(value = "小程序id")
    @TableField("mini_id")
    private Long miniId;

    /**
     * userstr
     */
    @ApiModelProperty(value = "userstr")
    @TableField("userstr")
    private String userstr;


}
