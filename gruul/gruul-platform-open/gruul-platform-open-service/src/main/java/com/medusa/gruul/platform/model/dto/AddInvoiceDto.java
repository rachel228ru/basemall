package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 * @description
 * @data: 2020/8/2
 */
@Data
public class AddInvoiceDto {

    /**
     * 抬头类型：1个人或事业单位，2企业
     */
    @ApiModelProperty(value = "抬头类型：1个人或事业单位，2企业")
    @Range(min = 1, max = 2)
    private Integer headType;

    /**
     * 抬头名称
     */
    @ApiModelProperty(value = "抬头名称")
    @NotBlank(message = "抬头不能为空")
    private String invoiceRiseName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String invoiceTaxpayerNum;


    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "是否默认使用 0-否  1-是")
    @Range(min = 0, max = 1)
    private Integer defaultStatus;

}
