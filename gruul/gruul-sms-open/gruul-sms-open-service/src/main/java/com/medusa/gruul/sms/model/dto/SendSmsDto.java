package com.medusa.gruul.sms.model.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Copyright(C) 2019-12-22 17:12 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2019-12-22 17:12
 **/
@Data
public class SendSmsDto {
    @NotNull
    @ApiModelProperty(value = "商户id")
    private Long userId;
    @NotNull
    @ApiModelProperty(value = "发送时间")
    private Long smsSendTime;
   /** @ApiModelProperty(value = "发送")
    private String smsSendContent;*/
    @ApiModelProperty(value = "短信区号，默认中国86")
    private String smsSendZone;
    @ApiModelProperty(value = "短信参数")
    private String smsSendParam;
    @NotNull
    @ApiModelProperty(value = "手机号")
    private String smsSendMobiles;
    @NotNull
    @ApiModelProperty(value = "类型：1腾讯 2阿里")
    private Long smsType;/*2腾讯 1阿里*/
    @NotNull
    @ApiModelProperty(value = "签名id")
    private Long signId;
    @NotNull
    @ApiModelProperty(value = "模版id")
    private Long templateId;
    @NotNull
    @ApiModelProperty(value = "供应商id")
    private Long  providerId;




}
