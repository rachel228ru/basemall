package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/10/16
 */
@Data
public class MiniCodeVersionVo {

    @ApiModelProperty(value = "店铺模版详情小程序版本子表id")
    private Long id;

    @ApiModelProperty(value = "模版库中自定义版本号")
    private String codeTempleteVersion;

    @ApiModelProperty(value = "自定义的版本说明")
    private String versionExplain;

    @ApiModelProperty(value = "小程序模版库中templateId")
    private String codeTempleteId;

}
