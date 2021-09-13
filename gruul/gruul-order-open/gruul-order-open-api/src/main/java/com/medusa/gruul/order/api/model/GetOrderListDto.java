package com.medusa.gruul.order.api.model;

import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.order.api.enums.DeliverTypeEnum;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
/**
 * <p>Description:  feign 获取订单内容返回实体类</P>
 * <p>Version: 1.0</p>
 * <p>Author: Mr.zhaozheng </P>
 * <p>Date: 2019-11-22 13:34 </p>
 */
public class GetOrderListDto {


    /**
     * orderId : 11
     * pointId : 123
     * lineId : 123
     * price : 1.23
     * goods : [{"goodId":1234,"img":"123444.jpg","num":13,"goodSkuId":123,"goodSkuCode":"123444","goodSpecs":"商品规格"}]
     */
    @ApiModelProperty(value = "订单id ,订单服务")
    private Long orderId;
    @ApiModelProperty(value = "订单 实际价格,订单服务")
    private BigDecimal price;
    @ApiModelProperty(value = "订单创建时间,订单服务")
    private String createTime;
    @ApiModelProperty(value = "付款时间,订单服务")
    private String payTime;
    @ApiModelProperty(value = "订单备注,订单服务")
    private String remark;
    @ApiModelProperty(value = "买家昵称,订单服务")
    private String buyerNick;
    @ApiModelProperty(value = "收货人-姓名,订单服务")
    private String reName;
    @ApiModelProperty(value = "收货人-电话,订单服务")
    private String rePhone;
    @ApiModelProperty(value = "收货人-地址,订单服务")
    private String reAddress;
    @ApiModelProperty(value = "收货人-地址,省份")
    private String rProvince;
    @ApiModelProperty(value = "收货人-地址,市")
    private String rCity;
    @ApiModelProperty(value = "收货人-地址,区")
    private String rRegion;
    @ApiModelProperty(value = "订单 关联所有的商品")
    private List<GoodsBean> goods;
    @Deprecated
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    @ApiModelProperty(value = "会员id")
    private Long userId;
    @ApiModelProperty(value = "会员头像")
    private String img;
    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum status;
    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum type;
    @ApiModelProperty(value = "配送方式")
    private DeliverTypeEnum deliverType;
    @ApiModelProperty(value = "进行中的售后订单")
    private List<AfsOrder> afsOrderList;
    /**
     * 自定义字段:[{"key":"IDCard","value":"332022199001010011"},{"key":"Phone","value":"13956852259"}]
     */
    @ApiModelProperty(value = "自定义字段:[{\"key\":\"IDCard\",\"value\":\"332022199001010011\"},{\"key\":\"Phone\"," +
            "\"value\":\"13956852259\"}]")
    private String customForm;
}
