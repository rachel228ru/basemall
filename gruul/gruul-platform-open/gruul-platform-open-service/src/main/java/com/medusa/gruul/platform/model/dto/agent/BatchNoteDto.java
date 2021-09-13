package com.medusa.gruul.platform.model.dto.agent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/10/31
 */
@Data
public class BatchNoteDto {

    @ApiModelProperty(value = "商户数组id")
    @NotNull(message = "未选择有效用户")
    private List<Long> accountIds;
    @ApiModelProperty(value = "备注")
    @NotEmpty(message = "备注内容不能为空")
    private String commentText;
    @ApiModelProperty(value = "是否覆盖 0覆盖 1不覆盖")
    @NotNull(message = "数据错误")
    private Integer isCoverage;
}
