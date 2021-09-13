package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 展示分类
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "新增或修改展示分类二级分类DTO")
public class ShowCategorySecondDto {

    private Long id;

    @ApiModelProperty(value = "上机分类的编号：0表示一级分类")
    private Long parentId;

    @ApiModelProperty(value = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty(value = "分类级别：0->1级；1->2级")
    @NotNull
    private Integer level;

    @ApiModelProperty(value = "销售专区")
    private Long saleMode;

    @ApiModelProperty(value = "分类排序")
    private Integer sort;

    public ShowCategory coverShowCategory() {
        ShowCategory showCategory = new ShowCategory();
        BeanUtil.copyProperties(this, showCategory);
        showCategory.setLevel(1);
        return showCategory;
    }
}