package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whh
 */
@Data
public class AuditRecordVo {

    @ApiModelProperty(value = "审核记录id")
    private Long id;

    @ApiModelProperty(value = "审核状态 0审核通过,1审核失败，2审核中")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核失败原因")
    private String reason;

    @ApiModelProperty(value = "自定义版本号")
    private String userVersion;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间  如果有更新时间则取更新时间没有则取创建时间")
    private LocalDateTime updateTime;
}
