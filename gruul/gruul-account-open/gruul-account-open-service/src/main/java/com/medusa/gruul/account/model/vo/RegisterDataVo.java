package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 小程序端购买会员卡填写信息
 * @Author zhaokw
 * @Date 10:59 2020\6\7 0007
 **/
@Data
@ApiModel(value = "小程序端购买会员卡填写信息")
@Accessors(chain = true)
public class RegisterDataVo {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "参数为空")
    private String phone;
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "参数为空")
    private String code;
    @ApiModelProperty(value = "扩展表单数据")
    @NotNull(message = "缺少必要数据")
    private @Valid List<FormExdDto> formExdDto;

    @Setter
    @Getter
    public static class FormExdDto {

        @ApiModelProperty(value = "会员注册表单id")
        @NotNull(message = "数据格式异常")
        private Long id;

        @ApiModelProperty(value = "表单id")
        @NotBlank(message = "数据格式异常")
        private String formId;

        @ApiModelProperty(value = "填写的表单值")
        private String value;

        @ApiModelProperty(value = "填写的表单名称")
        private String formName;
    }

}
