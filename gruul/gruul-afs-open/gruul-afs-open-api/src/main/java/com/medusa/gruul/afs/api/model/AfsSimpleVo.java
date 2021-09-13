package com.medusa.gruul.afs.api.model;

import com.medusa.gruul.afs.api.enums.AfsOrderCloseTypeEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The type Afs simple vo.
 *
 * @author alan
 */
@Data
public class AfsSimpleVo {

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
            "SHIPPED->配送中;" +
            "SUCCESS->成功;" +
            "CLOSE->已关闭")
    private AfsOrderStatusEnum status;

    /**
     * 关闭原因
     */
    @ApiModelProperty(value = "关闭原因：USER_CANCEL->用户撤销;" +
            "SELLER_REFUSE->卖家拒绝")
    private AfsOrderCloseTypeEnum closeType;


    /**
     * 申请售后的订单ID
     */
    @ApiModelProperty(value = "申请售后的订单ID")
    private Long orderId;
}
