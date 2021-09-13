package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Huihuang Wu
 * @description
 * @data: 2020/12/13
 */
@Data
public class BatchOptionUserDto {

    @ApiModelProperty(value = "商户数组id")
    @NotNull(message = "未选择有效用户")
    private List<Long> accountIds;

    @ApiModelProperty(value = "操作类型1-批量备注 2-批量删除 3-批量停用  4-批量启用")
    @NotNull(message = "操作类型不能为空")
    private Integer optionType;

    @ApiModelProperty(value = "备注")
    private String commentText;

    @ApiModelProperty(value = "是否覆盖 0覆盖 1不覆盖")
    private Integer isCoverage;


}
