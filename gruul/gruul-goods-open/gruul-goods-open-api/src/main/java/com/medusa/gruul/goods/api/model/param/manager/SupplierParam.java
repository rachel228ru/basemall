package com.medusa.gruul.goods.api.model.param.manager;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商查询参数
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SupplierParam对象", description = "供应商查询参数")
public class SupplierParam extends QueryParam {
    private static final long serialVersionUID = 1;

    /**
     * 供应商识别号
     */
    @ApiModelProperty(value = "供应商识别号")
    private String supplierSn;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 状态(0--已关闭，1--已审核，2--待审核，3--禁用中)
     */
    @ApiModelProperty(value = "状态(0--已关闭，1--已审核，2--待审核，3--禁用中)")
    private Integer status;
}