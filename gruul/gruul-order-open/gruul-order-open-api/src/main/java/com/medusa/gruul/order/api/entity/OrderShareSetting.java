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
 * 订单晒单表
 * </p>
 *
 * @author alan
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_share_setting")
@ApiModel(value = "OrderShareSetting对象", description = "订单晒单表")
public class OrderShareSetting extends BaseEntity {
    public static final String DEFAULT_TITLE = "我是{sname}，刚在您的小区下单啦，记得进去小程序后台查看哦～";
    public static final String DEFAULT_BACKGROUND = "https://oss-tencent.bgniao" +
            ".cn/gruul/20200707/e6b0e09b8fa1495a911aa91ae3fe58c8.jpg";

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
     * 标题
     */
    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    /**
     * 背景图
     */
    @ApiModelProperty(value = "背景图")
    @TableField("background")
    private String background;


}
