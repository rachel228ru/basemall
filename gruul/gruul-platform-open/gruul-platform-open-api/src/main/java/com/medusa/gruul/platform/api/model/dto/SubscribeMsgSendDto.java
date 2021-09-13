package com.medusa.gruul.platform.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * @author whh
 * @description 发送订阅消息队列
 * @data: 2020/2/06
 */
@Data
public class SubscribeMsgSendDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板id
     */
    @ApiModelProperty(value = "模板id,自行存储,在小程序端订阅时,由前端获取,订阅一次只能发送一次;订阅发送时间无期限")
    private String templateId;

    /**
     * 用户openId
     */
    @ApiModelProperty(value = "用户openId")
    private String openId;
    /**
     * 点击消息所需跳转路径
     */
    @ApiModelProperty(value = "点击消息所需跳转路径，为空不跳转")
    private String toPath;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    /**
     * 发送的内容，按顺序给入
     */
    @ApiModelProperty(value = "发送的内容，按顺序给入,具体内容看商家配置,(只负责接收发送,不完全保证发送成功,只发送一次无论失败成功)")
    private LinkedList<String> sendDatas;
}
