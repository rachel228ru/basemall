package com.medusa.gruul.account.model.vo;

import com.medusa.gruul.order.api.model.OrderOverviewVo;
import com.medusa.gruul.platform.api.model.dto.ShopPackageFunctionDto;
import com.medusa.gruul.shops.api.model.AccountCenterVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/5/13
 */
@Data
public class AggregateVo {
    @ApiModelProperty(value = "订单数据")
    private OrderOverviewVo orderOverviewVo;
    @ApiModelProperty(value = "用户收藏数量")
    private Integer collectCount;
    @ApiModelProperty(value = "用户足迹数量")
    private Integer accountFootMarkCount;
    @ApiModelProperty(value = "用户中心")
    private AccountCenterVo accountCenterVo;
    @ApiModelProperty(value = "积分用户所需")
    private UserInfoVo userInfoVo;
    @ApiModelProperty(value = "套餐相关开关")
    private ShopPackageFunctionDto packageFunctionVo;

}
