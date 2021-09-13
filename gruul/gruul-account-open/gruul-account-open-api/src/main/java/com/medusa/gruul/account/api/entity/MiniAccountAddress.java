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

/**
 * <p>
 * 会员地址表
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_account_address")
@ApiModel(value = "MiniAccountAddress对象", description = "用户地址表")
public class MiniAccountAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    @TableField("user_name")
    private String userName;

    /**
     * 收货人联系电话
     */
    @ApiModelProperty(value = "收货人联系电话")
    @TableField("phone")
    private String phone;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    @TableField("post_code")
    private String postCode;

    /**
     * 是否默认 0-非默认 1-默认
     */
    @ApiModelProperty(value = "是否默认 0-非默认 1-默认")
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    @TableField("city")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    @TableField("county")
    private String county;

    /**
     * 详细收货地址信息
     */
    @ApiModelProperty(value = "详细收货地址信息")
    @TableField("detail_Info")
    private String detailInfo;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    /**
     * 经度,维度
     */
    @ApiModelProperty(value = "经度,维度")
    @TableField("location")
    private String location;

}
