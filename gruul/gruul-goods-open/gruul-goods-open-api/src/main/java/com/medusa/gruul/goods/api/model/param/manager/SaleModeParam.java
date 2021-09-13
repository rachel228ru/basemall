package com.medusa.gruul.goods.api.model.param.manager;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lcysike
 * @Description: 销售专区
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SaleModeParam", description = "销售专区查询参数")
public class SaleModeParam extends QueryParam {

    @ApiModelProperty(value = "专区名称")
    private String modeName;

}