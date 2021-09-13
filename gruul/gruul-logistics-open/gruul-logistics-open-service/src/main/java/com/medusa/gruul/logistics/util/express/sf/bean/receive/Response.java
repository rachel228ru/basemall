/** 

* @Title: Response.java

* @Package com.ht.kjgl.bean.sf.receive

* @Description: TODO(用一句话描述该文件做什么)

* @author dell   

* @date 2017-8-17 下午04:12:33

* @version V1.0 

*/


package com.medusa.gruul.logistics.util.express.sf.bean.receive;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

* @author dtt

* @date 2017-8-17 下午04:12:33

* @Description: TODO(用一句话描述该文件做什么)

<Response service="OrderService">
<Head>OK</Head>
<Body>
<OrderResponse filter_result="2" destcode="755" mailno="444845301838" origincode="010" orderid="XJFS_07884030044"/>
</Body>
</Response>

*/
@XmlRootElement(name = "Response")
public class Response {
	
	@XmlAttribute(name = "service")
	private String service;
	
	@XmlElement(name = "Head")
	private String head;
	
	@XmlElement(name = "ERROR")
	private String error;
	
	@XmlElement(name = "Body")
	private Body body;

	@XmlTransient
	public String getService() {
		return service;
	}

	
	public void setService(String service) {
		this.service = service;
	}

	@XmlTransient
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@XmlTransient
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	@XmlTransient
	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}
	
	

}
