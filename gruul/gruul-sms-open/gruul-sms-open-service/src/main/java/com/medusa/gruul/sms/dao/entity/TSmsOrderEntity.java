package com.medusa.gruul.sms.dao.entity;
import java.io.Serializable;
import java.util.Date;


/**
 * t_sms_order表
 * 
 * @author system
 * 
 */

public class TSmsOrderEntity implements Serializable{

	private Long id;
	private Long userId;
	private Long smsSendTime;
	private Long smsSendType;
	private String smsSendContent;
	private String smsSendZone;
	private String smsSendParam;
	private String smsSendMobiles;
	private Integer smsSendCount;
	private Integer smsSendSuccessCount;
	private Integer smsSendFailCount;
	private Integer smsSendStatus;
	private Long providerId;
	private Long smsType;
	private Long isDeleted;
	private Date createTime;
	private Date updateTime;
	private Long templateId;
	private Long signId;

	public TSmsOrderEntity() {
		super();
		clear();
	}

	public void clear() {
		id = null;
		userId = null;
		smsSendTime = null;
		smsSendType = null;
		smsSendContent = null;
		smsSendZone = null;
		smsSendParam = null;
		smsSendMobiles = null;
		smsSendCount = null;
		smsSendSuccessCount = null;
		smsSendFailCount = null;
		smsSendStatus = null;
		providerId = null;
		smsType = null;
		isDeleted = null;
		createTime = null;
		updateTime = null;
		templateId = null;
		signId = null;
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


	public Long getSmsSendTime() {
		return smsSendTime;
	}

	public void setSmsSendTime(Long smsSendTime) {
		this.smsSendTime = smsSendTime;
	}


	public Long getSmsSendType() {
		return smsSendType;
	}

	public void setSmsSendType(Long smsSendType) {
		this.smsSendType = smsSendType;
	}


	public String getSmsSendContent() {
		return smsSendContent;
	}

	public void setSmsSendContent(String smsSendContent) {
		this.smsSendContent = smsSendContent;
	}


	public String getSmsSendZone() {
		return smsSendZone;
	}

	public void setSmsSendZone(String smsSendZone) {
		this.smsSendZone = smsSendZone;
	}


	public String getSmsSendParam() {
		return smsSendParam;
	}

	public void setSmsSendParam(String smsSendParam) {
		this.smsSendParam = smsSendParam;
	}


	public String getSmsSendMobiles() {
		return smsSendMobiles;
	}

	public void setSmsSendMobiles(String smsSendMobiles) {
		this.smsSendMobiles = smsSendMobiles;
	}


	public Integer getSmsSendCount() {
		return smsSendCount;
	}

	public void setSmsSendCount(Integer smsSendCount) {
		this.smsSendCount = smsSendCount;
	}


	public Integer getSmsSendSuccessCount() {
		return smsSendSuccessCount;
	}

	public void setSmsSendSuccessCount(Integer smsSendSuccessCount) {
		this.smsSendSuccessCount = smsSendSuccessCount;
	}


	public Integer getSmsSendFailCount() {
		return smsSendFailCount;
	}

	public void setSmsSendFailCount(Integer smsSendFailCount) {
		this.smsSendFailCount = smsSendFailCount;
	}


	public Integer getSmsSendStatus() {
		return smsSendStatus;
	}

	public void setSmsSendStatus(Integer smsSendStatus) {
		this.smsSendStatus = smsSendStatus;
	}


	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}


	public Long getSmsType() {
		return smsType;
	}

	public void setSmsType(Long smsType) {
		this.smsType = smsType;
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


	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}


	public Long getSignId() {
		return signId;
	}

	public void setSignId(Long signId) {
		this.signId = signId;
	}
}
