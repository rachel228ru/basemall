package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/3/6
 */
@Data
public class ShopTemplateVersionOptionDto {


    @ApiModelProperty(value = "id,更新接口时使用")
    private Long id;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

    /**
     * 业务基础库id
     */
    @ApiModelProperty(value = "业务基础库id")
    private Long librariesInfoId;

    /**
     * 业务基础库名称
     */
    @ApiModelProperty(value = "业务基础库名称")
    private String librariesInfoName;

    /**
     * 小程序相关库
     */
    @ApiModelProperty(value = "小程序相关库")
    @NotNull(message = "微信小程序未选择")
    private List<MiniCodeVersionDto> miniCodeVersions;

    /**
     * pc端路径
     */
    @ApiModelProperty(value = "pc端路径")
    private String pcTerminaUrl;

    /**
     * pc端版本
     */
    @ApiModelProperty(value = "pc端版本")
    private String pcTerminaVersion;


    /**
     * pc端关联页面
     */
    @ApiModelProperty(value = "pc端关联页面,json存储,键值对")
    private String pcUrlMap;


    /**
     * 移动端路径
     */
    @ApiModelProperty(value = "移动端路径")
    private String mobileTerminalUrl;

    /**
     * 移动端版本
     */
    @ApiModelProperty(value = "移动端版本")
    private String mobileTerminalVersion;


    @ApiModelProperty(value = "店铺模版id")
    private Long shopTemplateId;


    @ApiModelProperty(value = "更新日志")
    private String versionLog;
}
