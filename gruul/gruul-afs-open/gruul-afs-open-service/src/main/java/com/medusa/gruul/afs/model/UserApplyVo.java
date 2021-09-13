package com.medusa.gruul.afs.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.medusa.gruul.afs.api.enums.AfsOrderCloseTypeEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type User apply vo.
 *
 * @author alan
 * @description: 用户申请售后展示VO
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "用户申请售后展示VO")
public class UserApplyVo {

    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    private Long id;

    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    private String no;

    /**
     * 工单类型
     */
    @ApiModelProperty(value = "工单类型：REFUND->退款;" +
            "RETURN_REFUND->退货退款;")
    private AfsOrderTypeEnum type;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态：WAIT_FOR_BUSINESS_APPROVED->待商家审核;" +
            "WAIT_FOR_RETURN->待退货; " +
            "WAIT_FOR_SEND->待发货;" +
            "WAIT_FOR_PICKUP->待提货;" +
            "SHIPPED->配送中;" +
            "SUCCESS->成功;" +
            "CLOSE->已关闭")
    private AfsOrderStatusEnum status;

    /**
     * 关闭原因
     */
    @ApiModelProperty(value = "关闭原因：USER_CANCEL->用户撤销;" +
            "RE_EXCHANGE->重新申请换货;SELLER_REFUSE->卖家拒绝")
    private AfsOrderCloseTypeEnum closeType;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime deadline;

    /**
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号")
    private Long productSkuId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer productQuantity;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    @TableField("product_pic")
    private String productPic;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specs;

    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    private BigDecimal productPrice;

    /**
     * 申请此售后的订单
     */
    @ApiModelProperty(value = "申请此售后的订单")
    private Long orderId;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private AfsUserVo userVo;
}
