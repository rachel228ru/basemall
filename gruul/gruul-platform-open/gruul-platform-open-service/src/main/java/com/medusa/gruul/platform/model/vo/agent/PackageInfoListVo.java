package com.medusa.gruul.platform.model.vo.agent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/12/21
 */
@Data
public class PackageInfoListVo {


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 套餐名称
     */
    @ApiModelProperty(value = "套餐名称")
    private String name;

}
