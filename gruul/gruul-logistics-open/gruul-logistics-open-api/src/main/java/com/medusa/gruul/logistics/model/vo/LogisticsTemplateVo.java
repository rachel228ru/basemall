package com.medusa.gruul.logistics.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lcy
 */
@Data
@ApiModel("查询运费模版信息VO")
public class LogisticsTemplateVo {

    @ApiModelProperty("主键 id, 新增 不传,修改传原值 ")
    private Long id;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("计价方式: 1=按件数,2=按重量 ")
    private Integer valuationModel;

    @ApiModelProperty("是否指定包邮条件: 0=未指定,1=已指定 ")
    private Integer choiceConditionPostage;

    @ApiModelProperty("基础运送方式信息")
    private List<LogisticsShippingModelVo> logisticsShippingModelVos;

    @ApiModelProperty("指定包邮运送方式信息")
    private List<LogisticsIncludePostageVo> logisticsIncludePostageVos;

}
