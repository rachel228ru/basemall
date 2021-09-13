package com.medusa.gruul.account.model.dto;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.account.api.entity.MiniAccountCollect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiaoq
 *
 * @description 用户收藏dto
 * @data 2020/2/22 15:22
 */
@Data
@ApiModel(value = "新增或修改用户收藏DTO")
public class UserCollectDto {

    @ApiModelProperty(value = "用户收藏表id")
    private Long collectId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "商品id")
    private Long productId;


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

    public MiniAccountCollect coverMiniAccountCollect() {
        MiniAccountCollect miniAccountCollect = new MiniAccountCollect();
        BeanUtil.copyProperties(this, miniAccountCollect);
        return miniAccountCollect;
    }
}
