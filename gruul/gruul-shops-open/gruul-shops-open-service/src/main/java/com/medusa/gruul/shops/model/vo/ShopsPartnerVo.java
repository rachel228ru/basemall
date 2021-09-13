package com.medusa.gruul.shops.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@ApiModel(value = "shops partner vo 实体", description = "商铺数据 vo")
public class ShopsPartnerVo implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    /**
     * 归属关联Id
     */
    @ApiModelProperty(value = "归属关联Id")
    private String tenantId;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;


    /**
     * 总店Id
     */
    @ApiModelProperty(value = "总店Id")
    private Long partnerId;


    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;


    /**
     * 逻辑删除标识  0正常 1已删除
     */
    @ApiModelProperty(value = "逻辑删除标识  0正常 1已删除")
    private String isDeleted;


    /**
     * 名字
     */
    @ApiModelProperty(value = "名字")
    private String name;


    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String pass;


    /**
     *  地域
     */
    @ApiModelProperty(value = "地域")
    private String region;


    /**
     *  区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String areaCode;


    /**
     *  省编码
     */
    @ApiModelProperty(value = "省编码")
    private String provinceCode;


    /**
     *  市编码
     */
    @ApiModelProperty(value = "市编码")
    private String cityCode;


    /**
     *  身份证正面
     */
    @ApiModelProperty(value = "身份证正面")
    private String cardIdUp;


    /**
     *  身份证反面
     */
    @ApiModelProperty(value = "身份证反面")
    private String cardIdDown;


    /**
     *  地图X
     */
    @ApiModelProperty(value = "地图X")
    private Double mapX;


    /**
     *  地图Y
     */
    @ApiModelProperty(value = "地图Y")
    private Double mapY;


    /**
     *  合伙人模式  0加盟 1子公司
     */
    @ApiModelProperty(value = "合伙人模式  0加盟 1子公司")
    private String partnerModel;


    /**
     * 审批状态 0审核中 1通过 2拒绝
     */
    @ApiModelProperty(value = "审批状态 0审核中 1通过 2拒绝")
    private String approvalStatus;


    /**
     * 禁用状态  0正常  1 禁用
     */
    @ApiModelProperty(value = "禁用状态  0正常  1 禁用")
    private String prohibitStatus;


    /**
     *  省name
     */
    @ApiModelProperty(value = "省name")
    private String provinceName;


    /**
     *  市name
     */
    @ApiModelProperty(value = "市name")
    private String cityName;


    /**
     *  区域name
     */
    @ApiModelProperty(value = "区域name")
    private String areaName;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 平台用户id
     */
    @ApiModelProperty(value = "平台用户id")
    private Long platformId;
}
