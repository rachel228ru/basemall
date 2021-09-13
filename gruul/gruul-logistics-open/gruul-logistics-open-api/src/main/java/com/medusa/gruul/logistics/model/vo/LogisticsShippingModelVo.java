package com.medusa.gruul.logistics.model.vo;

import com.medusa.gruul.common.dto.AreaDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lcy
 */
@Data
@ApiModel("查询物流基础运送方式信息VO")
public class LogisticsShippingModelVo {

    @ApiModelProperty("唯一 id, 新增不传,修改传原值 ")
    private Long id;

    @ApiModelProperty("模板 id,新增的运费模板时候 不传")
    private Long logisticsId;

    @ApiModelProperty("计价方式: 1=按件数,2=按重量 ")
    private Integer valuationModel;

    @ApiModelProperty("首件数")
    private Integer firstPiece;

    @ApiModelProperty("首重 单位: 千克(kg) ")
    private BigDecimal firstWeight;

    @ApiModelProperty("首费 单位元 ")
    private BigDecimal firstAmount;

    @ApiModelProperty("续件数量 ")
    private Integer secondPiece;

    @ApiModelProperty("续重 单位 千克/kg ")
    private BigDecimal secondWeight;

    @ApiModelProperty("续费, 单位: 元 ")
    private BigDecimal secondAmount;

    @ApiModelProperty("运送地区 json 格式 {\"provinceid\": [\"cityId\",\"cityId2\"]} ")
    private String region;

}
