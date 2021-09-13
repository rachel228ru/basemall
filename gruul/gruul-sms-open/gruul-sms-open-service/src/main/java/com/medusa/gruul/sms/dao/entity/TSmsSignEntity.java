package com.medusa.gruul.sms.dao.entity;
import java.io.Serializable;
import java.util.Date;


/**
 * t_sms_sign表
 * 
 * @author system
 * 
 */

public class TSmsSignEntity implements Serializable{

	private Long id;
	private Long userId;
	private Long smsProviderId;
	private String smsSign;
	private Long smsSignIsPass;
	private String smsSignRemark;
	private Long isDeleted;
	private Date createTime;
	private Date updateTime;
	private Integer smsSignType;
	private Integer signType;

	public TSmsSignEntity() {
		super();
		clear();
	}

	public void clear() {
		id = null;
		userId = null;
		smsProviderId = null;
		smsSign = null;
		smsSignIsPass = null;
		smsSignRemark = null;
		isDeleted = null;
		createTime = null;
		updateTime = null;
		smsSignType = null;
		signType = null;
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


	public String getSmsSign() {
		return smsSign;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}


	public Long getSmsSignIsPass() {
		return smsSignIsPass;
	}

	public void setSmsSignIsPass(Long smsSignIsPass) {
		this.smsSignIsPass = smsSignIsPass;
	}


	public String getSmsSignRemark() {
		return smsSignRemark;
	}

	public void setSmsSignRemark(String smsSignRemark) {
		this.smsSignRemark = smsSignRemark;
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


	public Integer getSmsSignType() {
		return smsSignType;
	}

	public void setSmsSignType(Integer smsSignType) {
		this.smsSignType = smsSignType;
	}


	public Integer getSignType() {
		return signType;
	}

	public void setSignType(Integer signType) {
		this.signType = signType;
	}
}
