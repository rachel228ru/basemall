package com.medusa.gruul.logistics.util.express.yto.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

* @author lcysike

* @date 2017-8-16 下午03:36:03

* @Description: 圆通请求头数据类
<RequestOrder> <!—订单基本信息-->
<clientID>TEST</clientID > 客户编码（电商标识，由圆通人员给出）
<logisticProviderID>YTO</logisticProviderID>物流公司ID（YTO）
<customerId >TEST</customerId >客户标识
<txLogisticID>LP07082300225709</txLogisticID>物流号
<tradeNo>2007082300225709</tradeNo>tradeNo业务交易号
<mailNo></mailNo>运单号(快递单号)
<totalServiceFee>0.0</totalServiceFee>
<codSplitFee>0.0</codSplitFee>
<orderType>1</orderType>
<serviceType>0</serviceType>
<flag>0</flag>
<!--发货方信息-->
<sender>
<name>张三</name>
<postCode>310013</postCode>
<phone>231234134</phone>
<mobile>13575745195</mobile>
<prov>上海</prov>
<city>上海,浦东区</city>
<address>新龙科技大厦9层</address>
</sender>
<!--收货方信息-->
<receiver>
<name>李四</name>
<postCode>100000</postCode>
<phone>231234134</phone>
<prov>北京</prov>
<city>北京市,朝阳区</city>
<address>新龙科技大厦9层</address>
</receiver>
<!--物流公司上门取货时间段-->
<sendStartTime>2005-08-24 08:00:00.0 CST</sendStartTime>
<sendEndTime>2005-08-24 12:00:00.0 CST</sendEndTime>
<!--商品信息-->
<goodsValue>1900</goodsValue>
<itemsValue>2000</itemsValue>
<items>
<item>
<itemName>Nokia N73</itemName>
<number>2</number>
<itemValue>2</itemValue>
</item>
<item>
<itemName>Nokia N72</itemName>
<number>1</number>
<itemValue>2</itemValue>
</item>
</items>
<insuranceValue>0.0</insuranceValue>
<special>0</special>
<remark>易碎品</remark>
</RequestOrder>
*/
@XmlRootElement(name = "RequestOrder")
public class RequestOrder {

	/**
	 * 客户编码
	 */
	@XmlElement(name = "clientID")
	private String clientID;

	/**
	 * 物流公司ID
	 */
	@XmlElement(name = "logisticProviderID")
	private String logisticProviderID;

	/**
	 * 客户标识
	 */
	@XmlElement(name = "customerId")
	private String customerId;

	/**
	 * 物流号
	 */
	@XmlElement(name = "txLogisticID")
	private String txLogisticID;

	/**
	 * 订单类型(0-COD,1-普通订单,3-退货单)
	 */
	@XmlElement(name = "orderType")
	private Integer orderType;

	/**
	 * 服务类型(1-上门揽收, 2-次日达 4-次晨达 8-当日达,0-自己联系)。（数据库未使用）（目前暂未使用默认为0）
	 */
	@XmlElement(name = "serviceType")
	private Long serviceType;

	/**
	 * 商品类型（保留字段，暂时不用，默认填0）
	 */
	@XmlElement(name = "special")
	private Integer special;

	/**
	 * 发货人信息
	 */
	@XmlElement(name = "sender")
	private Sender sender;

	/**
	 * 收货人信息
	 */
	@XmlElement(name = "receiver")
	private Receiver receiver;

	@XmlTransient
	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	@XmlTransient
	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	@XmlTransient
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@XmlTransient
	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}

	@XmlTransient
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@XmlTransient
	public Long getServiceType() {
		return serviceType;
	}

	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}

	@XmlTransient
	public Integer getSpecial() {
		return special;
	}

	public void setSpecial(Integer special) {
		this.special = special;
	}

	@XmlTransient
	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	@XmlTransient
	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
}
