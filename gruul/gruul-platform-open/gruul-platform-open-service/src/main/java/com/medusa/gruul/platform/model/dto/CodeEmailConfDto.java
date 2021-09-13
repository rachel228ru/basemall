package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/11/28
 */
@Data
public class CodeEmailConfDto {
    @ApiModelProperty(value = "阿里云AccessKeyId")
    private String accessKeyId;
    @ApiModelProperty(value = "阿里云AccessKeySecret")
    private String accessKeySecret;
    @ApiModelProperty(value = "模板id")
    private String templateCode;
    @ApiModelProperty(value = "接收手机号")
    private List<String> codePhones;


    @ApiModelProperty(value = "通知接收邮箱")
    private List<String> emails;

    @ApiModelProperty(value = "邮件通知email")
    private List<EmailCode> emailNotify;

    @ApiModelProperty(value = "电子发票email")
    private List<EmailCode> electronicInvoiceEmail;

    @Data
    public static class EmailCode {
        @ApiModelProperty(value = "发件人邮箱")
        private String email;
        @ApiModelProperty(value = "授权码")
        private String authCode;
        @ApiModelProperty(value = "smtp服务器")
        private String host;
        @ApiModelProperty(value = "smtp服务器端口")
        private String port;
    }
}
