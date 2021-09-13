package com.medusa.gruul.logistics.model.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 运费计算请求参数
 * @author 王鹏
 */
@Data
@NoArgsConstructor
@ApiModel("运费计算")
public class LogisticsFreightDto {


    @NotNull
    @ApiModelProperty("运费模版id")
    private Long freightTemplateId;

    @NotNull
    @ApiModelProperty("件数")
    private Integer num;

    @NotNull
    @ApiModelProperty("每件重量")
    private BigDecimal weight;

    @NotNull
    @ApiModelProperty("区域编码")
    private String region;


    @NotNull
    @ApiModelProperty("金额")
    private BigDecimal price;




}
