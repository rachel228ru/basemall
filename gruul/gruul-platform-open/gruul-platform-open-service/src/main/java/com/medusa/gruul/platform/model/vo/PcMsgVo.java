package com.medusa.gruul.platform.model.vo;

import com.medusa.gruul.platform.api.model.vo.MiniMsgVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author cqj
 */
@Data
public class PcMsgVo {

    /**
     * 消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动
     */
    @ApiModelProperty(value = "消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动")
    private Integer messageType;

    /**
     * 消息项
     */
    private List<MiniMsgVo> miniMsgVos;
}
