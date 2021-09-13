package com.medusa.gruul.afs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * The type Seller note dto.
 *
 * @author alan
 * @description: SellerNoteDto.java
 * @date 2020 /8/5 22:41
 */
@Data
@ApiModel(value = "商家备注的参数")
public class SellerNoteDto {

    @NotEmpty(message = "备注的订单不能为空")
    @ApiModelProperty(value = "选择的售后ID")
    private List<Long> afsIds;

    @ApiModelProperty(value = "是否覆盖")
    private Boolean over;

    @ApiModelProperty(value = "备注")
    private String note;
}
