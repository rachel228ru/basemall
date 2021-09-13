package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 展示分类二级分类vo
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "ShowCategorySecondVo对象", description = "展示分类二级分类查询返回信息")
public class ShowCategorySecondVo {

    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "上机分类的编号：0表示一级分类")
    private Long parentId;

    @ApiModelProperty(value = "商品数量")
    private Integer productNumber;

    @ApiModelProperty(value = "展示分类id(数据传输用)")
    private Long showCategoryId;

    @ApiModelProperty(value = "销售专区")
    private Long saleMode;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}