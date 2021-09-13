package com.medusa.gruul.platform.model.vo.agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/11/1
 */
@Data
public class AgentMerchantVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "微信昵称")
    private String nikeName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "店铺数量")
    private Integer shopNumber;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "账号状态  0-正常  1-禁用")
    private Integer forbidStatus;


    @ApiModelProperty(value = "所属代理id(渠道商或代理id)")
    private Long agentId;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "代理")
    private String agentPhone;

    @ApiModelProperty(value = "渠道商名称")
    private String channelName;

    @ApiModelProperty(value = "渠道商账号")
    private String channelPhone;
}
