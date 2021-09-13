package com.medusa.gruul.goods.api.model.param.manager;


import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 属性模板查询参数
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AttributeTemplateParam对象", description = "属性模板查询参数")
public class AttributeTemplateParam extends QueryParam {
    private static final long serialVersionUID = 1;
}