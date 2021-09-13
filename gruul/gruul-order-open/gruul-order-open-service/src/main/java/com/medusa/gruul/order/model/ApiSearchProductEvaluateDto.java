package com.medusa.gruul.order.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * The type Api search product evaluate dto.
 * <p>
 * 小程序端商品评论查询
 *
 * @author alan
 * @date 2019 /11/10 9:21
 */
@Data
@ApiModel(value = "小程序端商品评论查询")
public class ApiSearchProductEvaluateDto extends QueryParam {

    @NotNull
    @ApiModelProperty(value = "商品ID")
    private String productId;

    @NotNull
    @ApiModelProperty(value = "查询范围：0->全部;1->有图片;2->有内容;")
    private int type;
}
