package com.medusa.gruul.platform.model.vo.agent;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/11/14
 */
@Data
public class AgentPackageOrdersVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "商户账号")
    private String account;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺模板名称(店铺类型)")
    private String templateName;

    @ApiModelProperty(value = "套餐名称")
    private String packageName;

    @ApiModelProperty(value = "套餐时长")
    private Integer packageTime;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal paidPayable;

    @ApiModelProperty(value = "1:余额支付2:扫码支付->微信3:扫码支付->支付宝4:汇款支付")
    private Integer payType;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "获利金额")
    private BigDecimal earnProfitMoney;

    @ApiModelProperty(value = "订单支付状态 0:待处理1:处理中2:已经完成 3:关闭")
    private Integer status;

    @ApiModelProperty(value = "汇款支付时填写支付方信息json")
    private String payInfo;

    private String avatarUrl;
}
