package com.medusa.gruul.platform.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺功能套餐
 *
 * @author whh
 * @description
 * @data: 2020/8/29
 */
@Data
public class ShopPackageFunctionDto {

    /**
     * 是否购买过大于门店版的版本套餐
     */
    @ApiModelProperty(value = "是否购买过大于门店版的版本套餐   true=有 false=无")
    private Boolean boughtEnterpriseVersion;


    /**
     * 套餐等级
     */
    @ApiModelProperty(value = "社区拼团套餐等级 1-门店版 2-企业版 3-集团版 4-私有化部署")
    private Integer communityPackagelevel;

    @ApiModelProperty("提货点功能  true=开启 false=无法使用 level=2")
    private Boolean point;


    @ApiModelProperty("直播  true=开启 false=无法使用 level=2")
    private Boolean live;



    @ApiModelProperty("版权自定义 true=开启 false=无法使用 level=2")
    private Boolean copyright;


    @ApiModelProperty("总店 true=开启 false=无法使用 旗舰版 level=3")
    private Boolean headOffice;
}
