package com.medusa.gruul.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medusa.gruul.order.api.entity.OrderProductEvaluate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The type Manage evaluate vo.
 * <p>
 * 管理后台评价查询
 *
 * @author alan
 * @date 2019 /11/10 9:21
 */
@Data
@ApiModel(value = "管理后台评价查询")
public class ManageEvaluateVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评价id
     */
    @ApiModelProperty(value = "评价id", example = "1")
    private Long id;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id", example = "1")
    private Long orderId;

    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评价时间")
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

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
     * 评分
     */
    @ApiModelProperty(value = "评分")
    private Integer rate;



    /**
     * 商铺评分
     */
    private Integer shopRate;


    /**
     * 商品评价
     */
    @ApiModelProperty(value = "商品评价")
    private List<OrderProductEvaluate> itemList;


}
