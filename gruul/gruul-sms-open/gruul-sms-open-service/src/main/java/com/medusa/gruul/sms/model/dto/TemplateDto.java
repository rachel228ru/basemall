package com.medusa.gruul.sms.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * t_sms_template表
 * 
 * @author system
 * 
 */
@Data
public class TemplateDto implements Serializable{

	@NotNull
	@ApiModelProperty(value = "商户id")
	private Long userId;
	@NotNull
	@ApiModelProperty(value = "供应商id")
	private Long smsProviderId;
	@NotNull
	@ApiModelProperty(value = "模版内容")
	private String smsTemplateContent;
	@ApiModelProperty(value = "模版备注")
	private String smsRemark;
	@NotNull
	@ApiModelProperty(value = "类型1阿里，2腾讯")
	private Integer templateType;
	@NotNull
	@ApiModelProperty(value = "类型 0：验证码。\n" +
			"    1：短信通知。\n" +
			"    2：推广短信。\n" +
			"    3：国际/港澳台消息。")
	private Integer smsTemplateType;



}
