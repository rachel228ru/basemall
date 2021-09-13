package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/8/16
 */
@Data
public class InvoiceOrderAuditDto {
    @ApiModelProperty(value = "发票工单id")
    @NotNull(message = "工单id不能为空")
    private Long invoiceOrderId;

    @ApiModelProperty(value = "操作类型 1-同意")
    @NotNull(message = "操作类型不能为空")
    @Range(max = 1, min = 1, message = "操作类型错误")
    private Integer option;
}
