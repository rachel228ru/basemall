package com.medusa.gruul.afs.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * The type Search dto.
 *
 * @author alan
 * @description: 商家售后订单列表查询参数
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "商家售后订单列表查询参数")
public class SearchDto extends QueryParam {

    @ApiModelProperty(value = "商品名称，模糊搜索")
    private String goodsName;

    @ApiModelProperty(value = "买家昵称，模糊搜索")
    private String userName;

    @ApiModelProperty(value = "收货人名称，模糊搜索")
    private String receiverName;

    @ApiModelProperty(value = "订单编号，精确搜索")
    private String orderId;

    @ApiModelProperty(value = "取货地点，模糊搜索")
    private String pointName;


    @ApiModelProperty(value = "线路id", example = "1")
    private Long lineId;

    @NotNull
    @ApiModelProperty(value = "配送方式 0->全部;100->用户自提;102->物流配送")
    private Integer deliverType;

    @ApiModelProperty(value = "开始日期,格式2019-10-10")
    private String startTime;

    @ApiModelProperty(value = "结束日期,格式2019-10-10")
    private String endTime;

    @NotNull
    @ApiModelProperty(value = "订单状态 -1：所有订单, 0.待处理, 1.处理中, 2.已完成, 3.已关闭")
    private Integer status;

    @NotNull
    @ApiModelProperty(value = "销售专区  1->社区；2->商超")
    private Integer area;
}
