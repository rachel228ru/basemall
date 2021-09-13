package com.medusa.gruul.logistics.util.express.sf.bean.receive;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

* @author dtt

* @date 2017-8-17 下午04:27:18

* @Description: TODO(用一句话描述该文件做什么)

<OrderResponse filter_result="2" destcode="755" mailno="444845301838" origincode="010" orderid="XJFS_07884030044"/>
*/
@XmlRootElement(name = "OrderResponse")
public class OrderResponse {
	
	@XmlAttribute(name = "filter_result")
	private String filterResult;
	
	@XmlAttribute(name = "destcode")
	private String destcode;
	
	@XmlAttribute(name = "mailno")
	private String mailno;
	
	@XmlAttribute(name = "origincode")
	private String origincode;
	
	@XmlAttribute(name = "orderid")
	private String orderid;
	
	@XmlAttribute(name = "remark")
	private String remark;

	@XmlElement(name = "rls_info")
	private RlsInfo rlsInfo;

	@XmlTransient
	public String getFilterResult() {
		return filterResult;
	}

	public void setFilterResult(String filterResult) {
		this.filterResult = filterResult;
	}

	@XmlTransient
	public String getDestcode() {
		return destcode;
	}

	public void setDestcode(String destcode) {
		this.destcode = destcode;
	}

	@XmlTransient
	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	@XmlTransient
	public String getOrigincode() {
		return origincode;
	}

	public void setOrigincode(String origincode) {
		this.origincode = origincode;
	}

	@XmlTransient
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	@XmlTransient
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlTransient
	public RlsInfo getRlsInfo() {
		return rlsInfo;
	}

	public void setRlsInfo(RlsInfo rlsInfo) {
		this.rlsInfo = rlsInfo;
	}
}
