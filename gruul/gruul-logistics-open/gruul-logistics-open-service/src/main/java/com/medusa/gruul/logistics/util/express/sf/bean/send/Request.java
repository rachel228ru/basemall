package com.medusa.gruul.logistics.util.express.sf.bean.send;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

* @author lcysike

* @date 2017-8-16 下午03:36:03

* @Description: 顺丰请求头数据类
<Request service='OrderService' lang='zh-CN'>
<Head>BSPdevelop</Head>
<Body>
<Order orderid ='XJFS_07880033'
j_company='华为'
j_contact='任先生' j_tel='010-1111112' j_mobile='13800138000'
j_province='北京' j_city='北京' j_county='朝阳区'
j_address='北京市朝阳区科学园科健路338号'
d_company='顺丰速运'
d_contact='西门俊宇' d_tel='无' d_mobile='17002930913'
d_province='广东省' d_city='深圳市' d_county='福田区'
d_address='广东省深圳市福田区新洲十一街万基商务大厦10楼'
express_type ='1'
pay_method ='2' custid ='7551878519'
parcel_quantity ='1' cargo_total_weight ='2.35' sendstarttime ='2015-01-16 10:26:43'
order_source ='西门府' remark =''>
<Cargo Name='扇子' count='2' unit='台' weight='0.02' amount='100' currency='CNY' source_area='中国'></Cargo>
</Order>
</Body></Request>

*/
@XmlRootElement(name = "Request")
public class Request {
	
		@XmlAttribute(name = "service")
		private String service;
	
		@XmlAttribute(name = "lang")
		private String lang;
		
		@XmlElement(name = "Head")
		private String head;
		
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
		public String getLang() {
			return lang;
		}

		public void setLang(String lang) {
			this.lang = lang;
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

}
