package com.medusa.gruul.logistics.model.param;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物流公司信息查询参数
 *
 * @author lcysike
 * @since 2019-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogisticsExpressParam对象", description = "物流公司信息查询参数")
public class LogisticsExpressParam extends QueryParam {

}