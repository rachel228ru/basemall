package com.medusa.gruul.order.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * The type Manage search evaluate dto.
 * <p>
 * 管理后台评价查询条件
 *
 * @author alan
 * @date 2019 /11/10 9:21
 */
@Data
@ApiModel(value = "管理后台评价查询条件")
public class ManageSearchEvaluateDto extends QueryParam {


    @ApiModelProperty(value = "商品名称，模糊搜索")
    private String goodsName;


    @ApiModelProperty(value = "订单编号，精确搜索")
    private String orderId;


    @ApiModelProperty(value = "成交日期开始,格式2019-10-10")
    private String payTimeStart;


    @ApiModelProperty(value = "成交日期结束,格式2019-10-10")
    private String payTimeEnd;


    @ApiModelProperty(value = "评价星级")
    private Integer rate;


}
