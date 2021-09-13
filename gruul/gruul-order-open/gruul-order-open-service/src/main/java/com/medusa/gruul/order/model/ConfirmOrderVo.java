package com.medusa.gruul.order.model;

import com.medusa.gruul.account.api.entity.MiniAccountAddress;
import com.medusa.gruul.goods.api.model.vo.manager.ItemVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * The type Confirm order vo.
 * <p>
 * 结算页信息封装
 *
 * @author alan
 * @date 2019 /10/6 12:38
 */
@Data
@ApiModel(value = "结算页信息封装")
public class ConfirmOrderVo {
    /**
     * 所选商品
     */
    @ApiModelProperty(value = "所选商品")
    List<ItemVo> itemVoList;

    /**
     * 系统配置的自定义表单
     */
    @ApiModelProperty(value = "系统配置的自定义表单")
    List<OrderComponentVo> componentVoList;
    /**
     * 收货地址列表
     */
    @ApiModelProperty(value = "收货地址列表")
    private List<MiniAccountAddress> miniAccountAddress;

}
