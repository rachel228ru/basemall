package com.medusa.gruul.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * The type Product evaluate vo.
 * <p>
 * 商品用户评价
 *
 * @author alan
 * @date 2020 /1/18 21:16
 */
@Data
@ApiModel(value = "商品用户评价")
public class ProductEvaluateVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

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
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "评价时间")
    private LocalDateTime createTime;

    /**
     * 商品评分
     */
    @ApiModelProperty(value = "商品评分")
    private Integer rate;

    /**
     * 商品的销售属性
     */
    @ApiModelProperty(value = "商品的销售属性")
    private String specs;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String comment;

    /**
     * 上传的图片
     */
    @ApiModelProperty(value = "上传的图片")
    private String picture;

    /**
     * 卖家回复
     */
    @ApiModelProperty(value = "卖家回复")
    private String reply;

    /**
     * 回复时间
     */
    @ApiModelProperty(value = "回复时间")
    private LocalDateTime replyTime;

}
