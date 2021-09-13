package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/8/16
 */
@Data
public class InvoiceOrderApplyVo {

    /**
     * id
     */
    @ApiModelProperty(value = "发票工单id")
    private Long id;


    /**
     * 抬头类型：1个人或事业单位，2企业
     */
    @ApiModelProperty(value = "抬头类型：1个人或事业单位，2企业")
    private Integer invoiceRiseType;

    /**
     * 抬头名称
     */
    @ApiModelProperty(value = "抬头名称")
    private String invoiceRiseName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String invoiceTaxpayerNum;

    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票金额")
    private String amount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    /**
     * 邮件地址
     */
    @ApiModelProperty(value = "邮件地址")
    private String email;

    /**
     * 审核状态：0待审核，1已审核
     */
    @ApiModelProperty(value = "审核状态：0待审核，1已审核")
    private Integer status;
    /**
     * 订购订单id
     */
    @ApiModelProperty(value = "订购订单id")
    private Long orderId;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "发票编号")
    private String numberNo;
}
