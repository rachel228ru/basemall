package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 模板消息发送记录
 * </p>
 *
 * @author whh
 * @since 2020-02-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_template_msg_send_record")
@ApiModel(value = "TemplateMsgSendRecord对象", description = "模板消息发送记录")
public class TemplateMsgSendRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 使用类型 1-买家通知 2-商家通知
     */
    @ApiModelProperty(value = "使用类型 1-买家通知 2-商家通知")
    @TableField("use_type")
    private Boolean useType;

    /**
     * 消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动
     */
    @ApiModelProperty(value = "消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动")
    @TableField("message_type")
    private Integer messageType;

    /**
     * 发送内容json
     */
    @ApiModelProperty(value = "发送内容json")
    @TableField("send_json")
    private String sendJson;

    /**
     * 发送状态 0-发送失败  1-发送成功
     */
    @ApiModelProperty(value = "发送状态 0-发送失败  1-发送成功")
    @TableField("send_status")
    private Integer sendStatus;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
    @TableField("failure_reason")
    private String failureReason;

    /**
     * 使用的模板消息id
     */
    @ApiModelProperty(value = "使用的模板消息id")
    @TableField("use_teamplate_id")
    private String useTeamplateId;

    /**
     * 小程序表id
     */
    @ApiModelProperty(value = "小程序表id")
    @TableField("mini_id")
    private Long miniId;


}
