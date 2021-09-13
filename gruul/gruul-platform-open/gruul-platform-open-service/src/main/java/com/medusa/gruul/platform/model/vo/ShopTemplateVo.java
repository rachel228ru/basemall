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
public class ShopTemplateVo {

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

    @ApiModelProperty(value = "子版本号列表")
    private List<MiniCodeVersionVo> miniCodeVersions;

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

    /**
     * 服务数量
     */
    @ApiModelProperty(value = "服务数量")
    private Integer serverCount;

    @ApiModelProperty(value = "店铺模版表id")
    private Long shopTemplateId;

}
