package com.medusa.gruul.order.model;

import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.platform.api.model.vo.MiniMsgVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The type Setting vo.
 * <p>
 * 设置相关
 *
 * @author alan
 * @date 2019 /10/6 12:38
 */
@Data
@ApiModel(value = "设置相关")
public class SettingVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 是否开启评价
     */
    @ApiModelProperty(value = "是否开启评价")
    private Boolean openEvaluate;
    private OrderSetting setting;
    /**
     * 可以使用的微信订阅消息
     */
    @ApiModelProperty(value = "可以使用的微信订阅消息")
    private List<MiniMsgVo> miniMsgVoList;
    /**
     * 当前的店铺详情
     */
    @ApiModelProperty(value = "当前的店铺详情")
    private CurShopInfoDto curShopInfoDto;

}
