package com.medusa.gruul.sms.dao.entity;
import java.io.Serializable;
import java.util.Date;


/**
 * t_sms_provider表
 * 
 * @author system
 * 
 */

public class TSmsProviderEntity implements Serializable{

	private Long id;
	private Long userId;
	private String smsProviderName;
	private String smsProviderAppId;
	private String smsProviderAppSecret;
	private Integer smsProviderAvailableCount;
	private Integer smsProviderUsedCount;
	private Integer smsProviderTotalCount;
	private Long smsProviderStatus;
	private Long isDeleted;
	private Date createTime;
	private Date updateTime;

	public TSmsProviderEntity() {
		super();
		clear();
	}

	public void clear() {
		id = null;
		userId = null;
		smsProviderName = null;
		smsProviderAppId = null;
		smsProviderAppSecret = null;
		smsProviderAvailableCount = null;
		smsProviderUsedCount = null;
		smsProviderTotalCount = null;
		smsProviderStatus = null;
		isDeleted = null;
		createTime = null;
		updateTime = null;
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


	public String getSmsProviderName() {
		return smsProviderName;
	}

	public void setSmsProviderName(String smsProviderName) {
		this.smsProviderName = smsProviderName;
	}


	public String getSmsProviderAppId() {
		return smsProviderAppId;
	}

	public void setSmsProviderAppId(String smsProviderAppId) {
		this.smsProviderAppId = smsProviderAppId;
	}

	public String getSmsProviderAppSecret() {
		return smsProviderAppSecret;
	}

	public void setSmsProviderAppSecret(String smsProviderAppSecret) {
		this.smsProviderAppSecret = smsProviderAppSecret;
	}


	public Integer getSmsProviderAvailableCount() {
		return smsProviderAvailableCount;
	}

	public void setSmsProviderAvailableCount(Integer smsProviderAvailableCount) {
		this.smsProviderAvailableCount = smsProviderAvailableCount;
	}


	public Integer getSmsProviderUsedCount() {
		return smsProviderUsedCount;
	}

	public void setSmsProviderUsedCount(Integer smsProviderUsedCount) {
		this.smsProviderUsedCount = smsProviderUsedCount;
	}


	public Integer getSmsProviderTotalCount() {
		return smsProviderTotalCount;
	}

	public void setSmsProviderTotalCount(Integer smsProviderTotalCount) {
		this.smsProviderTotalCount = smsProviderTotalCount;
	}


	public Long getSmsProviderStatus() {
		return smsProviderStatus;
	}

	public void setSmsProviderStatus(Long smsProviderStatus) {
		this.smsProviderStatus = smsProviderStatus;
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
}
