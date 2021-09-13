package com.medusa.gruul.account.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息扩展表
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_account_extends")
@ApiModel(value = "MiniAccountExtends对象", description = "用户信息扩展表")
public class MiniAccountExtends extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id ")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    /**
     * 店铺用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("shop_user_id")
    private String shopUserId;

    /**
     * 当前使用  0未使用 1-当前使用
     */
    @ApiModelProperty(value = "当前使用  0未使用 1-当前使用")
    @TableField("current_status")
    private Integer currentStatus;


    /**
     * 最后交易时间
     */
    @ApiModelProperty(value = "最后交易时间")
    @TableField("last_deal_time")
    private LocalDateTime lastDealTime;

    /**
     * 是否黑名单用户 0-否 1-是
     */
    @ApiModelProperty(value = "是否黑名单用户 0-否 1-是")
    @TableField("is_blacklist")
    private Integer isBlacklist;

    /**
     * 消费次数
     */
    @ApiModelProperty(value = "消费次数")
    @TableField("consume_num")
    private Integer consumeNum;

    /**
     * 交易总额
     */
    @ApiModelProperty(value = "交易总额")
    @TableField("consume_totle_money")
    private BigDecimal consumeTotleMoney;

    /**
     * 用户最后一次选择的地理位置经纬度
     */
    @ApiModelProperty(value = "用户最后一次选择的地理位置经纬度 经纬度,经度在前维度在后逗号分隔")
    @TableField("last_choose_lcation")
    private String lastChooseLcation;


    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    /**
     * 加入黑名单时间
     */
    @ApiModelProperty(value = "加入黑名单时间")
    @TableField("join_blacklist_time")
    private LocalDateTime joinBlacklistTime;


    /**
     * 用户身份类型  0-普通用户
     */
    @ApiModelProperty(value = "用户身份类型  0-普通用户")
    @TableField("community_type")
    private Integer communityType;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @TableField("shop_id")
    private String shopId;

    /**
     * 用户余额
     */

    @ApiModelProperty(value = "用户余额")
    @TableField("supply_bonus")
    private BigDecimal supplyBonus;

    /**
     * 用户返利余额
     */
    @ApiModelProperty(value = "用户返利余额")
    @TableField("rebate_bonus")
    private BigDecimal rebateBonus;

    /**
     * 冻结余额
     */
    @ApiModelProperty(value = "冻结余额")
    @TableField("freeze_bonus")
    private BigDecimal freezeBonus;
}
