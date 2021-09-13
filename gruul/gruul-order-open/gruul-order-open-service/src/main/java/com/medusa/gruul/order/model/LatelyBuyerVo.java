package com.medusa.gruul.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * The type Lately buyer vo.
 * <p>
 * 最近购买者的信息
 *
 * @author alan
 * @date 2019 /10/6 12:38
 */
@Data
@ApiModel(value = "最近购买者的信息")
public class LatelyBuyerVo {

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer sn;

    /**
     * 用户帐号
     */
    @ApiModelProperty(value = "用户帐号")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatarUrl;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 商品总数量
     */
    @ApiModelProperty(value = "商品总数量")
    private Integer productTotalQuantity;

}
