package com.medusa.gruul.account.model.dto;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.account.api.entity.MiniAccountFootMark;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author  xiaoq
 *
 * @data 2020/2/28 13:15
 */
@Data
@ApiModel(value = "新增或修改用户足迹DTO")
public class AccountFootMarkDto {
    @ApiModelProperty(value = "用户足迹表id")
    private Long footmarkId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "商品id")
    private String productId;


    @ApiModelProperty(value = "商品主图")
    private String productPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品状态  0-上架 1-下架 2-售罄")
    private Integer status;

    @ApiModelProperty(value = "商品实际售价")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "指导价划线价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "备注")
    private String ramark;

    public MiniAccountFootMark coverMiniAccountFootMark() {
        MiniAccountFootMark miniAccountFootMark = new MiniAccountFootMark();
        BeanUtil.copyProperties(this, miniAccountFootMark);
        return miniAccountFootMark;
    }


}
