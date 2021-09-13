package com.medusa.gruul.account.model.vo;

import com.medusa.gruul.common.dto.AreaDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/12/1
 */
@Data
public class AnalysisLatitudeAndlongitudeVo {

    @ApiModelProperty("省")
    AreaDto province;
    @ApiModelProperty("市")
    AreaDto city;
    @ApiModelProperty("区")
    AreaDto district;
    @ApiModelProperty("区")
    String adcode;
}
