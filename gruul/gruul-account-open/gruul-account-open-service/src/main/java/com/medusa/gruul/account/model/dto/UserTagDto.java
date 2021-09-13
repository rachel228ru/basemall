package com.medusa.gruul.account.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/11/20
 */
@Data
public class UserTagDto {

    @ApiModelProperty(value = "添加标签数组")
    private List<TagDto> add;

    @ApiModelProperty(value = "修改的标签数组")
    private List<TagDto> update;

    @ApiModelProperty(value = "删除的标签数组")
    private List<TagDto> del;

    @ApiModelProperty(value = "用户id数组")
    private List<String> userIds;
}
