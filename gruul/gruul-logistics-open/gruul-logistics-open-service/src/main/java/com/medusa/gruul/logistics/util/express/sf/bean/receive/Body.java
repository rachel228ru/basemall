package com.medusa.gruul.logistics.util.express.sf.bean.receive;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

* @author dtt

* @date 2017-8-17 下午04:15:50

* @Description: TODO(用一句话描述该文件做什么)
<OrderResponse filter_result="2" destcode="755" mailno="444845301838" origincode="010" orderid="XJFS_07884030044"/>

*/
@XmlRootElement(name = "Body")
public class Body {
	
	@XmlElement(name = "OrderResponse")
	private OrderResponse orderresponse;

	@XmlTransient
	public OrderResponse getOrderresponse() {
		return orderresponse;
	}

	public void setOrderresponse(OrderResponse orderresponse) {
		this.orderresponse = orderresponse;
	}
	
	

}
