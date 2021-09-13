package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.AttributeTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 属性模板子属性
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "新增或修改模板子属性DTO")
public class AttributeTemplateSecondDto {

    private Long id;

    @ApiModelProperty(value = "上级id（一级为0）", example = "1")
    private Long parentId;

    @NotBlank
    @Size(max = 64)
    @ApiModelProperty("属性名称")
    private String name;

    @ApiModelProperty("内容")
    private String content;

    public AttributeTemplate coverAttributeTemplate() {
        AttributeTemplate attributeTemplate = new AttributeTemplate();
        BeanUtil.copyProperties(this, attributeTemplate);
        return attributeTemplate;
    }
}