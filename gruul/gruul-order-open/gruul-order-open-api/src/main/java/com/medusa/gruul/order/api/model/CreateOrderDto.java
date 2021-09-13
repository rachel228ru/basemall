package com.medusa.gruul.order.api.model;

import com.medusa.gruul.order.api.enums.DeliverTypeEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import com.medusa.gruul.order.api.enums.PayTypeEnum;
import com.medusa.gruul.order.api.enums.SourceTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 创建订单参数
 *
 * @author alan
 * @date 2019/10/4 14:36
 */
@Data
@ApiModel(value = "创建订单参数", description = "创建订单参数")
public class CreateOrderDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "商品不能为空")
    @ApiModelProperty("商品规格数量Map")
    private List<ItemDto> itemDtoList;

    @NotNull(message = "配送方式不能为空")
    @ApiModelProperty("配送方式")
    private DeliverTypeEnum deliverType;

    @NotNull(message = "收货地址不能为空")
    @ApiModelProperty("收货人信息id")
    private Long miniAccountAddressId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("参与的满减活动")
    private Long fullScaleId;

    @ApiModelProperty("用户留言")
    private String userNote;


    @ApiModelProperty("自定义表单")
    private String customForm;

    @NotNull(message = "支付方式不能为空")
    @ApiModelProperty("支付方式")
    private PayTypeEnum payType;


    @ApiModelProperty("微信formId")
    private String formId;

    @NotNull(message = "订单来源不能为空")
    @ApiModelProperty(value = "订单来源")
    private SourceTypeEnum sourceType;

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum orderType;

    @ApiModelProperty("发货消息订阅TemplateId")
    private String deliveryTemplateId;


    @ApiModelProperty(hidden = true)
    public List<Long> getItemSkuIds() {
        return new ArrayList<>(itemDtoList.stream().map(ItemDto::getSkuId).collect(Collectors.toSet()));
    }

}
