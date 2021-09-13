package com.medusa.gruul.platform.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * @author whh
 * @description
 * @data: 2020/8/2
 */
@Data
public class EmailChangeDto {

    @Email(message = "邮箱格式错误")
    private String email;
}
