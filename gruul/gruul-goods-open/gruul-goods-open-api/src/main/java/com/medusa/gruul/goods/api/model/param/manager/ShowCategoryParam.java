package com.medusa.gruul.goods.api.model.param.manager;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author lcysike
 * @Description: 展示分类
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ShowCategoryParam", description = "展示分类查询参数")
public class ShowCategoryParam extends QueryParam {

    @ApiModelProperty(value = "销售专区")
    @NotBlank(message = "请选择专区")
    private Long saleMode;

}