package com.medusa.gruul.order.model;

import com.medusa.gruul.order.api.entity.OrderProductEvaluate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Api order evaluate vo.
 * <p>
 * 订单评价列表
 *
 * @author alan
 * @date 2019 /11/25 21:41
 */
@Data
public class ApiOrderEvaluateVo {

    @ApiModelProperty(value = "订单评价ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "订单ID", example = "1")
    private Long orderId;

    @ApiModelProperty(value = "评价时间")
    private LocalDateTime createTime;

    @ApiModelProperty("商品详情")
    private List<OrderProductEvaluate> itemList;


    @Deprecated
    @ApiModelProperty("商城模式下店铺评分")
    private Integer shopRate;
}
