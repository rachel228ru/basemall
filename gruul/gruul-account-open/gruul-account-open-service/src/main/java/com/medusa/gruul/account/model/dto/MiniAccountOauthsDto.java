package com.medusa.gruul.account.model.dto;

import com.medusa.gruul.account.api.entity.MiniAccountOauths;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 登陆缓存对象
 *
 * @author whh
 * @description
 * @data: 2019/11/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MiniAccountOauthsDto extends MiniAccountOauths implements Serializable {

    private static final long serialVersionUID = 1783387265606361591L;

    @ApiModelProperty("用户token")
    private String token;
}
