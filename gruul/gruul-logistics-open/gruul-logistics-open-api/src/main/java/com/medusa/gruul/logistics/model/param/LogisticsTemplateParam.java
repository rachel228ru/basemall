package com.medusa.gruul.logistics.model.param;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运费模版查询参数
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogisticsTemplateParam对象", description = "运费模版查询参数")
public class LogisticsTemplateParam extends QueryParam {

}