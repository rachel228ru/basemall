package com.medusa.gruul.platform.api.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/5/22
 */
@Data
public class ShopMessageVo {

    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    private String msgTitle;

    /**
     * 模板消息id
     */
    @ApiModelProperty(value = "模板消息id")
    private List<ShopMessageDetailVo> shopMessageDetailVos;
}
