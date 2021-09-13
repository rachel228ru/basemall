package com.medusa.gruul.order.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单评论表
 * </p>
 *
 * @author alan
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_evaluate")
@ApiModel(value = "OrderEvaluate对象", description = "订单评论表")
public class OrderEvaluate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @TableField("shop_id")
    private String shopId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    /**
     * 用户帐号
     */
    @ApiModelProperty(value = "用户帐号")
    @TableField("user_name")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @TableField("user_avatar_url")
    private String userAvatarUrl;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @TableField("order_id")
    private Long orderId;


    /**
     * 商城模式下店铺评分
     */
    @ApiModelProperty(value = "商城模式下店铺评分")
    @TableField("shop_rate")
    private Integer shopRate;


}
