package com.medusa.gruul.payment.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author whh
 * @since 2019-11-04
 */
@Data
@Accessors(chain = true)
@TableName("t_payment_record")
@ApiModel(value = "PaymentRecord对象", description = "")
public class PaymentRecord {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 请求对象数据
     */
    @ApiModelProperty(value = "请求对象数据")
    @TableField("request_params")
    private String requestParams;

    /**
     * 最终发送数据
     */
    @ApiModelProperty(value = "最终发送数据")
    @TableField("send_param")
    private String sendParam;

    /**
     * 第三方回调的数据
     */
    @ApiModelProperty(value = "第三方回调的数据")
    @TableField("notify_param")
    private String notifyParam;

    /**
     * 支付 记录表id
     */
    @ApiModelProperty(value = "支付 记录表id")
    @TableField("payment_id")
    private Long paymentId;

}
