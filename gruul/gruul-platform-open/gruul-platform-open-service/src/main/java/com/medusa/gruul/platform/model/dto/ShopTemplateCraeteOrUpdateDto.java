package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/3/6
 */
@Data
public class ShopTemplateCraeteOrUpdateDto {

    @ApiModelProperty(value = "模板id,更新的时候需要必须带上")
    private Long id;

    @ApiModelProperty(value = "模版名称")
    @NotBlank(message = "模版名称不能为空")
    private String name;

    @ApiModelProperty(value = "模版编号")
    @NotBlank(message = "模板编号不能为空")
    private String code;

    @ApiModelProperty(value = "分类类型：1 系统模版 2 定制模版")
    @NotNull
    @Range(max = 2, min = 1, message = "分类类型错误")
    private Integer type;

    @ApiModelProperty(value = "模板说明")
    private String description;
}
