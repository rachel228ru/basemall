package com.medusa.gruul.sms.dao.entity;
import java.io.Serializable;
import java.util.Date;


/**
 * t_sms_order_send_detail_his_201912表
 * 
 * @author system
 * 
 */

public class TSmsOrderSendDetailHisEntity implements Serializable{

	private Long id;
	private Long userId;
	private Long smsSendTime;
	private Long smsSendReportStatus;
	private String smsSendReportCode;
	private String smsSendReportMsg;
	private String smsSendMobile;
	private Integer smsSendReportSmsSize;
	private Date smsSendReportTime;
	private String smsSendContent;
	private Long isDeleted;
	private Date createTime;
	private Date updateTime;

	public TSmsOrderSendDetailHisEntity() {
		super();
		clear();
	}

	public void clear() {
		id = null;
		userId = null;
		smsSendTime = null;
		smsSendReportStatus = null;
		smsSendReportCode = null;
		smsSendReportMsg = null;
		smsSendMobile = null;
		smsSendReportSmsSize = null;
		smsSendReportTime = null;
		smsSendContent = null;
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


	public Long getSmsSendTime() {
		return smsSendTime;
	}

	public void setSmsSendTime(Long smsSendTime) {
		this.smsSendTime = smsSendTime;
	}


	public Long getSmsSendReportStatus() {
		return smsSendReportStatus;
	}

	public void setSmsSendReportStatus(Long smsSendReportStatus) {
		this.smsSendReportStatus = smsSendReportStatus;
	}


	public String getSmsSendReportCode() {
		return smsSendReportCode;
	}

	public void setSmsSendReportCode(String smsSendReportCode) {
		this.smsSendReportCode = smsSendReportCode;
	}


	public String getSmsSendReportMsg() {
		return smsSendReportMsg;
	}

	public void setSmsSendReportMsg(String smsSendReportMsg) {
		this.smsSendReportMsg = smsSendReportMsg;
	}


	public String getSmsSendMobile() {
		return smsSendMobile;
	}

	public void setSmsSendMobile(String smsSendMobile) {
		this.smsSendMobile = smsSendMobile;
	}


	public Integer getSmsSendReportSmsSize() {
		return smsSendReportSmsSize;
	}

	public void setSmsSendReportSmsSize(Integer smsSendReportSmsSize) {
		this.smsSendReportSmsSize = smsSendReportSmsSize;
	}


	public Date getSmsSendReportTime() {
		return smsSendReportTime;
	}

	public void setSmsSendReportTime(Date smsSendReportTime) {
		this.smsSendReportTime = smsSendReportTime;
	}


	public String getSmsSendContent() {
		return smsSendContent;
	}

	public void setSmsSendContent(String smsSendContent) {
		this.smsSendContent = smsSendContent;
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
