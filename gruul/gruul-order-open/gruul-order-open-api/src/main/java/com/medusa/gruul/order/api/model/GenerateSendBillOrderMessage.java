package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发货单生成(发送)消息体
 *
 * @author alan
 * @date 2019/10/6 14:08
 */
@Data
@ApiModel("发货单生成")
public class GenerateSendBillOrderMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 发货单ID
     */
    @ApiModelProperty(value = "发货单ID,必填", example = "1")
    private Long id;
    /**
     * 发货单名称
     */
    @ApiModelProperty(value = "发货单名称,必填", example = "发货单202001132022")
    private String name;
    /**
     * 发货单的状态 1->新增；2->发货;
     */
    @ApiModelProperty(value = "发货单的状态 1->新增；2->发货;必填", example = "1")
    private Integer status;
    /**
     * 发货单的类型 1->直配发货单；2->物流发货单;
     */
    @ApiModelProperty(value = "发货单的类型 1->直配发货单；2->物流发货单;必填", example = "1")
    private Integer type;
    /**
     * 发货单包含的ID
     */
    @ApiModelProperty(value = "订单ID,必填")
    private List<Long> orderIds;
    /**
     * 物流公司 物流模式
     */
    @ApiModelProperty(value = "物流公司,物流发货单必填", example = "顺丰快递")
    private String deliveryCompany;
    /**
     * 物流单号 物流模式
     */
    @ApiModelProperty(value = "物流单号,物流发货单必填", example = "75324542769471")
    private String deliverySn;
    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间,发货单状态为2必填", example = "2020-01-13 12:00:00")
    private String deliveryTime;

}
