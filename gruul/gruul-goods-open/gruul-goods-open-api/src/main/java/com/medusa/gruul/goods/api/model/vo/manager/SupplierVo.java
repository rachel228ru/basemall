package com.medusa.gruul.goods.api.model.vo.manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供应商
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "SupplierVo", description = "供应商查询返回信息")
public class SupplierVo {

    private Long id;

    /**
     * 供应商识别号
     */
    @ApiModelProperty(value = "供应商识别号")
    private String supplierSn;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 县
     */
    @ApiModelProperty(value = "县")
    private String country;

    /**
     * 地区
     */
    @ApiModelProperty(value = "地区")
    private String address;

    /**
     * 完整地址
     */
    @ApiModelProperty(value = "完整地址")
    private String area;

    /**
     * 产品信息
     */
    @ApiModelProperty(value = "产品信息")
    private String productInfo;

    /**
     * 状态(默认待审核，0--已关闭，1--已审核，2--待审核，3--禁用中)
     */
    @ApiModelProperty(value = "状态(默认待审核，0--已关闭，1--已审核，2--待审核，3--禁用中)", example = "1")
    private Integer status;

    /**
     * 评分
     */
    @ApiModelProperty(value = "评分（默认5.0）")
    private BigDecimal score;

    /**
     * 注册来源（0--后台注册，1--小程序）
     */
    @ApiModelProperty(value = "注册来源，0--后台注册，1--小程序")
    private Integer comeFrom;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}