package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * <p>
 * 商品自定义专区Vo
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "SaleModeVo对象", description = "商品自定义专区查询返回信息")
public class SaleModeVo {

    private Long id;

    @ApiModelProperty(value = "专区名称")
    private String modeName;

    @ApiModelProperty(value = "专区类型")
    private String modeType;

    @ApiModelProperty(value = "排序id")
    private Integer sort;

    @ApiModelProperty(value = "分区商品数量")
    private Integer productNumber;

}