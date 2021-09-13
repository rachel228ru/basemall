package com.medusa.gruul.logistics.util.express.sf.bean.send;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

* @author lcysike

* @date 2017-8-16 下午03:44:16

* @Description: 路由查询路由体

<RouteRequest
tracking_type='1'
method_type='1'
tracking_number='444003077898'/>

*/
@XmlRootElement(name = "RouteRequest")
public class RouteRequest {

	/**
	 * 查询号类别:
	 * 1:根据顺丰运单号查询,order节点中tracking_number将被当作顺丰运单号处理
	 * 2:根据客户订单号查询,order节点中tracking_number将被当作客户订单号处理
	 * 3:逆向单,根据客户原始订单号查询,order节点中tracking_number将被当作逆向单原始订单号处理
	 */
	@XmlAttribute(name = "tracking_type")
	private String trackingType;

	/**
	 * 路由查询类别:
	 * 1:标准路由查询
	 */
	@XmlAttribute(name = "method_type")
	private String methodType;

	/**
	 * 查询号:
	 * 如果tracking_type=1,则此值为顺丰运单号
	 * 如果tracking_type=2,则此值为客户订单号
	 * 如果tracking_type=3,则此值为逆向单原始订单号
	 * 如果有多个单号,以逗号分隔,如"123,124,125"。
	 */
	@XmlAttribute(name = "tracking_number")
	private String trackingNumber;

	@XmlTransient
	public String getTrackingType() {
		return trackingType;
	}

	public void setTrackingType(String trackingType) {
		this.trackingType = trackingType;
	}

	@XmlTransient
	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	@XmlTransient
	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
}
