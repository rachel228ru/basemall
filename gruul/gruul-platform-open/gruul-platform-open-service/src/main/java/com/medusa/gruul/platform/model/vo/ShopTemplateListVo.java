package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/3/6
 */
@Data
public class ShopTemplateListVo {

    @ApiModelProperty(value = "模板id")
    private Long id;

    @ApiModelProperty(value = "模版名称")
    private String name;

    @ApiModelProperty(value = "模版编号")
    private String code;

    @ApiModelProperty(value = "分类类型：1 系统模版 2 定制模版")
    private Integer type;

    @ApiModelProperty(value = "模板说明")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "模板版本列表")
    private List<ShopTemplateVo> shopTemplateVos;

    @ApiModelProperty(value = "是否不可删除 0-可删除 1-不可删除")
    private Integer notDel = 0;
}
