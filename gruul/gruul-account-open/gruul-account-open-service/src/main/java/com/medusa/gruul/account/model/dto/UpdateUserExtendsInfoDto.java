package com.medusa.gruul.account.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description 更新用户扩展字段部分信息
 * @data: 2020/1/6
 */
@Data
public class UpdateUserExtendsInfoDto {

    /**
     * 设置用户最后一次选择的地理位置经纬度
     */
    @ApiModelProperty(value = "经纬度,经度在前维度在后逗号分隔")
    private String lastChooseLcation;


    /**
     * 设置用户当前所在店铺
     */
    @ApiModelProperty(value = "商铺id")
    private String shopId;
}
