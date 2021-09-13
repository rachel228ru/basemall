package com.medusa.gruul.platform.api.model.dto;

import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺相关配置
 *
 * @author whh
 * @description
 * @data: 2020/6/13
 */
@Data
public class ShopConfigDto {

    @ApiModelProperty(value = "小程序信息")
    private MiniInfo miniInfo;
    @ApiModelProperty(value = "公众号信息")
    private MiniInfo mpInfo;
    @ApiModelProperty(value = "支付相关配置")
    private PayInfoVo payInfo;

}
