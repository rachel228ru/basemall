package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/8/8
 */
@Data
public class SysShopPackageListVo extends SysShopPackageVo {


    /**
     * 营业中的店铺
     */
    @ApiModelProperty(value = "营业中的店铺数量")
    private Integer activeShopNumber;


    /**
     * 已打烊的店铺
     */
    @ApiModelProperty(value = "已打烊的店铺数量")
    private Integer classShopNumber;

}
