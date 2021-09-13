package com.medusa.gruul.goods.api.model.dto.api;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.Supplier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 供应商
 *
 * @author: KYL
 * @since: 2019/11/13
 */
@Data
@ApiModel(value = "小程序新增或修改供应商DTO")
public class ApiSupplierDto {
    private Long id;

    /**
     * 供应商识别号
     */
    @ApiModelProperty(value = "供应商识别号")
    private String supplierSn;

    /**
     * 供应商名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 64, message = "长度不能超过64")
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
     * 订阅消息id
     */
    @ApiModelProperty(value = "订阅消息id")
    private String templateId;

    /**
     * 注册来源（0--后台注册，1--小程序）
     */
    @ApiModelProperty(value = "注册来源，0--后台注册，1--小程序")
    private Integer comeFrom;

    public Supplier coverSupplier() {
        Supplier supplier = new Supplier();
        BeanUtil.copyProperties(this, supplier);
        return supplier;
    }

}
