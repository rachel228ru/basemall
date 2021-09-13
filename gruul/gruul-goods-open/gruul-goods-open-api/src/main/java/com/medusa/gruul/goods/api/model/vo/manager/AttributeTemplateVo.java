package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 属性模板
 * </p>
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "AttributeTemplateVo", description = "属性模板查询返回信息")
public class AttributeTemplateVo {

    private Long id;

    @ApiModelProperty(value = "模版id", example = "1")
    private Long parentId;

    @ApiModelProperty("模版名称")
    private String name;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("属性列表")
    private List<AttributeTemplateSecondVo> attributeTemplates;
}