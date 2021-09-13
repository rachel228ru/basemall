package com.medusa.gruul.logistics.util.express.sf.bean.receive;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

* @author lcysike

* @date 2017-8-17 下午04:42:22

* @Description: TODO(用一句话描述该文件做什么)


*/
@XmlRootElement(name = "ERROR")
public class ErrorR {

	@XmlAttribute(name = "code")
	private String code;

	@XmlTransient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
