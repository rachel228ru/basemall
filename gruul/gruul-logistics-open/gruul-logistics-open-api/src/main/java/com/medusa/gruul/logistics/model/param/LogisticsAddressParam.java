package com.medusa.gruul.logistics.model.param;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收发货地址查询参数
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogisticsAddressParam对象", description = "收发货地址查询参数")
public class LogisticsAddressParam extends QueryParam {

}