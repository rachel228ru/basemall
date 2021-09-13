package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/5/18
 */
@Data
public class SkipUrlVo {


    @ApiModelProperty(value = "pc端路径")
    private String pcTerminaUrl;

    @ApiModelProperty(value = "pc端版本")
    private String pcTerminaVersion;

    private  String RegionalUrl;

}
