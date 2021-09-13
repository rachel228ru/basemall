package com.medusa.gruul.afs.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import com.medusa.gruul.common.dto.CurUserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协商历史
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_afs_negotiate_history")
@ApiModel(value = "AfsNegotiateHistory对象", description = "协商历史")
public class AfsNegotiateHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    @TableId("id")
    private Long id;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @TableField("shop_id")
    private String shopId;

    /**
     * 申请人ID
     */
    @ApiModelProperty(value = "申请人ID")
    @TableField("user_id")
    private String userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @TableField("user_avatar_url")
    private String userAvatarUrl;

    /**
     * 详情
     */
    @ApiModelProperty(value = "详情")
    @TableField("info")
    private String info;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    @TableField("image")
    private String image;

    /**
     * 关联订单
     */
    @ApiModelProperty(value = "关联订单")
    @TableField("order_id")
    private Long orderId;

    /**
     * 申请用户的类型
     */
    @ApiModelProperty(value = "申请用户的类型")
    @TableField("apply_user_type")
    private Integer applyUserType;


    /**
     * User create afs negotiate history.
     *
     * @param curUserDto the cur user dto
     * @return the afs negotiate history
     */
    public static AfsNegotiateHistory userCreate(CurUserDto curUserDto) {
        AfsNegotiateHistory negotiateHistory = new AfsNegotiateHistory();
        negotiateHistory.setUserId(curUserDto.getUserId());
        negotiateHistory.setUserName(curUserDto.getNikeName());
        negotiateHistory.setUserAvatarUrl(curUserDto.getAvatarUrl());
        return negotiateHistory;
    }

    /**
     * Seller create afs negotiate history.
     *
     * @param logo the logo
     * @return the afs negotiate history
     */
    public static AfsNegotiateHistory sellerCreate(String logo) {
        AfsNegotiateHistory negotiateHistory = new AfsNegotiateHistory();
        negotiateHistory.setUserId("1");
        negotiateHistory.setUserName("卖家");
        negotiateHistory.setUserAvatarUrl(logo);
        return negotiateHistory;
    }

    /**
     * System create afs negotiate history.
     *
     * @param logo the logo
     * @return the afs negotiate history
     */
    public static AfsNegotiateHistory systemCreate(String logo) {
        AfsNegotiateHistory negotiateHistory = new AfsNegotiateHistory();
        negotiateHistory.setUserId("0");
        negotiateHistory.setUserName("系统");
        negotiateHistory.setUserAvatarUrl(logo);
        return negotiateHistory;

    }
}
