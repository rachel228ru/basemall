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

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whh
 * @since 2019-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_send_code")
@ApiModel(value = "SendCode对象", description = "")
public class SendCode extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @TableField("code")
    private String code;

    /**
     * 校验类型
     */
    @ApiModelProperty(value = "校验类型")
    @TableField("verify_type")
    private Integer verifyType;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    @TableField("expiration_time")
    private LocalDateTime expirationTime;


}
