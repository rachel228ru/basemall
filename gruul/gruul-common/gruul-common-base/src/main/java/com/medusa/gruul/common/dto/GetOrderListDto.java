package com.medusa.gruul.common.dto;

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
    @ApiModelProperty(value = "订单关联 直配的提货点id ,订单服务")
    private Long pointId;
    @ApiModelProperty(value = "订单关联 的提货点id关联的线路id ,订单服务")
    private Long lineId;
    @ApiModelProperty(value = "订单 实际价格,订单服务")
    private BigDecimal price;
    @ApiModelProperty(value = "订单编号,订单服务")
    private String orderCode;
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
    @ApiModelProperty(value = "团长id,订单服务")
    private Long leaderId;
    @ApiModelProperty(value = "团长 姓名,用户服务")
    private String leaderName;
    @ApiModelProperty(value = "团长 电话,用户服务")
    private String leaderPhone;
    @ApiModelProperty(value = "订单 关联所有的商品")
    private List<GoodsBean> goods;
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    @ApiModelProperty(value = "会员头像")
    private String img;
    @ApiModelProperty(value = "订单状态")
    private Integer type;


    @NoArgsConstructor
    @Data
    /**
     * <p>@Description:  货物实体类型</P>
     * <p>Version: 1.0</p>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019-11-22 13:37 </p>
     */
    public static class GoodsBean {

        /**
         * goodId : 1234
         * img : 123444.jpg
         * num : 13
         * goodSkuId : 123
         * goodSkuCode : 123444
         * goodSpecs : 商品规格
         */
        @ApiModelProperty(value = "商品id,订单服务")
        private Long goodId;
        @ApiModelProperty(value = "商品 名称,订单服务")
        private String goodName;
        @ApiModelProperty(value = "商品 图片,订单服务")
        private String img;
        @ApiModelProperty(value = "商品 规格 id,订单服务")
        private Long goodSkuId;
        @ApiModelProperty(value = "商品 规格 code,订单服务")
        private String goodSkuCode;
        @ApiModelProperty(value = "商品 规格中文解释,订单服务")
        private String goodSpecs;
        @ApiModelProperty(value = "商品 价格,订单服务")
        private BigDecimal realPrice;
        @ApiModelProperty(value = "商品 购买数量,订单服务")
        private Integer num;
        @ApiModelProperty(value = "商品 佣金,财务服务")
        private BigDecimal employeeMoney;
        @ApiModelProperty(value = "商品 分类 中文,商品服务")
        private String classify;

    }

    @Data
    @NoArgsConstructor
    public static class GetOrderListParam {
        private String tenantId;
        private String shopId;
        private List<Long> orderIds;

        public GetOrderListParam(String tenantId, String shopId, List<Long> orderIds) {
            this.tenantId = tenantId;
            this.shopId = shopId;
            this.orderIds = orderIds;
        }
    }
}
