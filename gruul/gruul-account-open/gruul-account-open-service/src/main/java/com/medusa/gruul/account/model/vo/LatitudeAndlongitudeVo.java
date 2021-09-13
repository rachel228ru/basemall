package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/6/21
 */
@Data
public class LatitudeAndlongitudeVo {

    @ApiModelProperty(value = "经纬度返回顺序为 经度，纬度")
    private String location;

    @ApiModelProperty(value = "省市区")
    private AnalysisLatitudeAndlongitudeVo andlongitudeVo;
}
