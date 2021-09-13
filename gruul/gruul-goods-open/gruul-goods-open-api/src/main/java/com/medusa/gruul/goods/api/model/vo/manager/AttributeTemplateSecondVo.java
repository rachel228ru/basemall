package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 属性模板子属性
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "AttributeTemplateSecondVo", description = "属性模板查子属性询返回信息")
public class AttributeTemplateSecondVo {
    private Long id;

    @ApiModelProperty(value = "模版id", example = "1")
    private Long parentId;

    @ApiModelProperty("模版名称")
    private String name;

    @ApiModelProperty("内容")
    private String content;
}