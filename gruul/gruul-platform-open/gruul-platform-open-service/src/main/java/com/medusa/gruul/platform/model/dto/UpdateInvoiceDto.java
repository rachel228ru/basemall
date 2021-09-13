package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/8/2
 */
@Data
public class UpdateInvoiceDto extends AddInvoiceDto {
    @ApiModelProperty("发票抬头id")
    private Long id;
}
