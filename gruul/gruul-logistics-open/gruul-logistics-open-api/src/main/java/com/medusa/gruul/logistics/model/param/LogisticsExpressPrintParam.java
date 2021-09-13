package com.medusa.gruul.logistics.model.param;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电子面单打印机设置查询参数
 *
 * @author lcysike
 * @since 2020-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogisticsExpressPrintParam对象", description = "电子面单打印机设置查询参数")
public class LogisticsExpressPrintParam extends QueryParam {

}