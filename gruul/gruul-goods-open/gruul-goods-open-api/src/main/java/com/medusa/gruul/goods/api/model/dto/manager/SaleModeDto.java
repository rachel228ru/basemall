package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.SaleMode;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * <p>
 * 商品自定义专区
 * </p>
 *
 * @author lcysike
 * @since 2020-10-26
 */
@Data
@ApiModel(value = "新增或修改商品自定义专区DTO")
public class SaleModeDto {

    private Long id;


    @ApiModelProperty(value = "专区名称")
    @NotBlank(message = "专区名称不能为空")
    @Size(max = 8, message = "专区名称长度不能超过8个字")
    private String modeName;

    @ApiModelProperty(value = "专区类型")
    private String modeType;

    @ApiModelProperty(value = "分类排序")
    private Integer sort;

    public SaleMode coverSaleMode() {
        SaleMode saleMode = new SaleMode();
        BeanUtil.copyProperties(this, saleMode);
        return saleMode;
    }
}