package com.medusa.gruul.sms.dao.entity;
import java.io.Serializable;
import java.util.Date;


/**
 * t_sms_template表
 * 
 * @author system
 * 
 */

public class TSmsTemplateEntity implements Serializable{

	private Long id;
	private Long userId;
	private Long smsProviderId;
	private String smsTemplateContent;
	private String smsTemplateCode;
	private Long smsTemplateIsPass;
	private String smsRemark;
	private Long isDeleted;
	private Date createTime;
	private Date updateTime;
	private Integer templateType;
	private Integer smsTemplateType;
	private String smsTemplateName;

	public TSmsTemplateEntity() {
		super();
		clear();
	}

	public void clear() {
		id = null;
		userId = null;
		smsProviderId = null;
		smsTemplateContent = null;
		smsTemplateCode = null;
		smsTemplateIsPass = null;
		smsRemark = null;
		isDeleted = null;
		createTime = null;
		updateTime = null;
		templateType = null;
		smsTemplateType = null;
		smsTemplateName = null;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Long getSmsProviderId() {
		return smsProviderId;
	}

	public void setSmsProviderId(Long smsProviderId) {
		this.smsProviderId = smsProviderId;
	}


	public String getSmsTemplateContent() {
		return smsTemplateContent;
	}

	public void setSmsTemplateContent(String smsTemplateContent) {
		this.smsTemplateContent = smsTemplateContent;
	}


	public String getSmsTemplateCode() {
		return smsTemplateCode;
	}

	public void setSmsTemplateCode(String smsTemplateCode) {
		this.smsTemplateCode = smsTemplateCode;
	}


	public Long getSmsTemplateIsPass() {
		return smsTemplateIsPass;
	}

	public void setSmsTemplateIsPass(Long smsTemplateIsPass) {
		this.smsTemplateIsPass = smsTemplateIsPass;
	}


	public String getSmsRemark() {
		return smsRemark;
	}

	public void setSmsRemark(String smsRemark) {
		this.smsRemark = smsRemark;
	}


	public Long getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}


	public Integer getSmsTemplateType() {
		return smsTemplateType;
	}

	public void setSmsTemplateType(Integer smsTemplateType) {
		this.smsTemplateType = smsTemplateType;
	}


	public String getSmsTemplateName() {
		return smsTemplateName;
	}

	public void setSmsTemplateName(String smsTemplateName) {
		this.smsTemplateName = smsTemplateName;
	}
}
