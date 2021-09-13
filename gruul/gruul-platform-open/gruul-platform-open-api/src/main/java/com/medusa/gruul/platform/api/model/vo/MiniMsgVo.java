package com.medusa.gruul.platform.api.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cqj
 */
@Data
public class MiniMsgVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "消息标识,唯一值,auditMsg-审核通知  sendMsg-发货通知")
    private String mark;

    @ApiModelProperty(value = "模板消息id")
    private String miniTemplateId;

    @ApiModelProperty(value = "开启状态 0-未使用  1-使用")
    private Integer miniOpen;

    /**
     * 小程序订阅模板
     */
    @ApiModelProperty(value = "小程序订阅模板")
    private String miniMsg;

}
