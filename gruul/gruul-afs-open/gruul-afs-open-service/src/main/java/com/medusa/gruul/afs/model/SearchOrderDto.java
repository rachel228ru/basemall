package com.medusa.gruul.afs.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * The type Search order dto.
 *
 * @author alan
 * @description: 用户售后订单列表查询参数
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "用户售后订单列表查询参数")
public class SearchOrderDto extends QueryParam {

}
