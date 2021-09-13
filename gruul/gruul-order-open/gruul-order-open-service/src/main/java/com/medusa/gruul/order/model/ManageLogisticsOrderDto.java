package com.medusa.gruul.order.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * The type Manage logistics order dto.
 * <p>
 * 快递订单后台订单查询条件
 */
@Data
@ApiModel(value = "快递订单后台订单查询条件")
public class ManageLogisticsOrderDto extends QueryParam {


    @ApiModelProperty(value = "商品名称，模糊搜索")
    private String goodsName;

    @ApiModelProperty(value = "订单编号，精确搜索")
    private String orderId;

    @ApiModelProperty(value = "取货地点，模糊搜索")
    private String address;

    @ApiModelProperty(value = "买家昵称，模糊搜索")
    private String userName;

    @ApiModelProperty(value = "取货地点，模糊搜索")
    private String receiverName;

}
