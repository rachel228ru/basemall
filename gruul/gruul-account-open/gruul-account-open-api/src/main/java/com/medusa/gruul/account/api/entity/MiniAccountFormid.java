package com.medusa.gruul.account.api.entity;

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

import java.time.LocalDateTime;

/**
 * <p>
 * 小程序formid表
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_account_formid")
@ApiModel(value = "MiniAccountFormid对象", description = "小程序formid表")
public class MiniAccountFormid extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * formId
     */
    @ApiModelProperty(value = "formId")
    @TableField("form_value")
    private String formValue;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @TableField("expiration_time")
    private LocalDateTime expirationTime;

    /**
     * 用户唯一id
     */
    @ApiModelProperty(value = "用户唯一id")
    @TableField("user_id")
    private String userId;

}
