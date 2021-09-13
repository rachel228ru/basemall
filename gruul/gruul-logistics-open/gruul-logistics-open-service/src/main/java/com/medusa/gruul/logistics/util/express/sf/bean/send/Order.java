package com.medusa.gruul.logistics.util.express.sf.bean.send;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**

* @author lcysike

* @date 2017-8-16 下午03:44:16

* @Description: TODO(用一句话描述该文件做什么)
<Order
orderid="SFKD-20160219000026"
j_company="深圳宝龙达信息技术股份有限公司"
j_contact="邓丽君"
j_tel="15323233432"
j_mobile="15322234342"
j_province="广东省"
j_city="深圳市"
j_county="南山区"
j_address="广东省深圳市南山区西丽镇塘朗同富裕工业城7栋"
d_contact="四海" d_tel="15023434543"
d_mobile="15423456545"
d_province="广东省"
d_city="深圳市"
d_county="南山区"
d_address="科技园软件产业基地"
express_type="1"
pay_method="1"
custid="7551234567"
parcel_quantity="1"
is_docall="0"
sendstarttime=""
remark="电子产品 笔记本显卡"
is_unified_waybill_no="1" />

*/
@XmlRootElement(name = "Order")
public class Order {

	/**
	 * 客户订单号
	 */
	@XmlAttribute(name = "orderid")
	private String orderId;

	/**
	 * 寄件方公司名称
	 */
	@XmlAttribute(name = "j_company")
	private String jCompany;

	/**
	 * 寄件方联系人
	 */
	@XmlAttribute(name = "j_contact")
	private String jContact;

	/**
	 * 寄件方联系电话
	 */
	@XmlAttribute(name = "j_tel")
	private String jTel;

	/**
	 * 寄件方手机
	 */
	@XmlAttribute(name = "j_mobile")
	private String jMobile;

	/**
	 * 寄件方所在省份
	 * 字段填写要求:必须是标准的
	 * 省名称称谓 如:广东省,如果是
	 * 直辖市,请直接传北京、上海等。
	 */
	@XmlAttribute(name = "j_province")
	private String jProvince;

	/**
	 * 寄件方所在城市名称,字段填写
	 * 要求:必须是标准的城市称谓
	 * 如:深圳市。
	 */
	@XmlAttribute(name = "j_city")
	private String jCity;

	/**
	 * 寄件人所在县/区,必须是标准的
	 * 县/区称谓,示例:“福田区”。
	 */
	@XmlAttribute(name = "j_county")
	private String jCounty;

	/**
	 * 寄件方详细地址,包括省市区,
	 * 示例:“广东省深圳市福田区新
	 * 洲十一街万基商务大厦10楼” ,如果需要生成电子面单,则必填。
	 */
	@XmlAttribute(name = "j_address")
	private String jAddress;

	/**
	 * 到件方公司名称
	 */
	@XmlAttribute(name = "d_company")
	private String dCompany;

	/**
	 * 到件方联系人
	 */
	@XmlAttribute(name = "d_contact")
	private String dContact;

	/**
	 * 到件方联系电话
	 */
	@XmlAttribute(name = "d_tel")
	private String dTel;

	/**
	 * 到件方手机
	 */
	@XmlAttribute(name = "d_mobile")
	private String dMobile;

	/**
	 * 到件方所在省份,必须是标准的
	 * 省名称称谓 如:广东省,如果是
	 * 直辖市,请直接传北京、上海等
	 */
	@XmlAttribute(name = "d_province")
	private String dProvince;

	/**
	 * 到件方所在城市名称,必须是标
	 * 准的城市称谓 如:深圳市,如果
	 * 是直辖市,请直接传北京(或北京
	 * 市)、上海(或上海市)等
	 */
	@XmlAttribute(name = "d_city")
	private String dCity;

	/**
	 * 到件方所在县/区,必须是标准
	 * 的县/区称谓,示例:“福田区”。
	 */
	@XmlAttribute(name = "d_county")
	private String dCounty;

	/**
	 * 到件方详细地址,如果不传输
	 * d_province/d_city字段,此详细
	 * 地址需包含省市信息,以提高地
	 * 址识别的成功率,示例:“广东省
	 * 深圳市福田区新洲十一街万基
	 * 商务大厦10楼”
	 */
	@XmlAttribute(name = "d_address")
	private String dAddress;

	/**
	 * 快件产品编码
	 */
	@XmlAttribute(name = "express_type")
	private String expressType;

	/**
	 * 付款方式:
	 * 1:寄方付
	 * 2:收方付
	 * 3:第三方付
	 */
	@XmlAttribute(name = "pay_method")
	private String payMethod;

	/**
	 * 顺丰月结卡号
	 */
	@XmlAttribute(name = "custid")
	private String custId;

	/**
	 * 包裹数,一个包裹对应一个运单
	 * 号,如果是大于1个包裹,则返回
	 * 则按照子母件的方式返回母运
	 * 单号和子运单号
	 */
	@XmlAttribute(name = "parcel_quantity")
	private String parcelQuantity;

	/**
	 * 订单货物总重量,包含子母件,
	 * 单位千克,精确到小数点后3位,
	 * 如果提供此值,必须>0 。
	 */
	@XmlAttribute(name = "cargo_total_weight")
	private String cargoTotalWeight;

	/**
	 * 要求上门取件开始时间,格式:
	 * YYYY-MM-DD HH24:MM:SS
	 * 示例:2012-7-30 09:30:00。
	 */
	@XmlAttribute(name = "sendstarttime")
	private String sendStartTime;

	/**
	 * 是否要求通过手持终端通知顺
	 * 丰收派员收件:
	 * 1:要求
	 * 其它为不要求
	 */
	@XmlAttribute(name = "is_docall")
	private String isdocall;

	/**
	 * 备注
	 */
	@XmlAttribute(name = "remark")
	private String remark;

	/**
	 * 路由标签查询服务:
	 * 默认0。
	 * 1:查询,其他:不查询
	 */
	@XmlAttribute(name = "routelabelService")
	private String routelabelService;

	/**
	 * 是否使用国家统一面单号:
	 * 默认0 。
	 * 1:是, 0:否
	 */
	@XmlAttribute(name = "is_unified_waybill_no")
	private String isUnifiedWaybillNo;

	/**
	 * 货物描述
	 */
	@XmlElement
	private List<Cargo> Cargo;

	@XmlTransient
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@XmlTransient
	public String getjCompany() {
		return jCompany;
	}

	public void setjCompany(String jCompany) {
		this.jCompany = jCompany;
	}

	@XmlTransient
	public String getjContact() {
		return jContact;
	}

	public void setjContact(String jContact) {
		this.jContact = jContact;
	}

	@XmlTransient
	public String getjTel() {
		return jTel;
	}

	public void setjTel(String jTel) {
		this.jTel = jTel;
	}

	@XmlTransient
	public String getjMobile() {
		return jMobile;
	}

	public void setjMobile(String jMobile) {
		this.jMobile = jMobile;
	}

	@XmlTransient
	public String getjProvince() {
		return jProvince;
	}

	public void setjProvince(String jProvince) {
		this.jProvince = jProvince;
	}

	@XmlTransient
	public String getjCity() {
		return jCity;
	}

	public void setjCity(String jCity) {
		this.jCity = jCity;
	}

	@XmlTransient
	public String getjCounty() {
		return jCounty;
	}

	public void setjCounty(String jCounty) {
		this.jCounty = jCounty;
	}

	@XmlTransient
	public String getjAddress() {
		return jAddress;
	}

	public void setjAddress(String jAddress) {
		this.jAddress = jAddress;
	}

	@XmlTransient
	public String getdCompany() {
		return dCompany;
	}

	public void setdCompany(String dCompany) {
		this.dCompany = dCompany;
	}

	@XmlTransient
	public String getdContact() {
		return dContact;
	}

	public void setdContact(String dContact) {
		this.dContact = dContact;
	}

	@XmlTransient
	public String getdTel() {
		return dTel;
	}

	public void setdTel(String dTel) {
		this.dTel = dTel;
	}

	@XmlTransient
	public String getdMobile() {
		return dMobile;
	}

	public void setdMobile(String dMobile) {
		this.dMobile = dMobile;
	}

	@XmlTransient
	public String getdProvince() {
		return dProvince;
	}

	public void setdProvince(String dProvince) {
		this.dProvince = dProvince;
	}

	@XmlTransient
	public String getdCity() {
		return dCity;
	}

	public void setdCity(String dCity) {
		this.dCity = dCity;
	}

	@XmlTransient
	public String getdCounty() {
		return dCounty;
	}

	public void setdCounty(String dCounty) {
		this.dCounty = dCounty;
	}

	@XmlTransient
	public String getdAddress() {
		return dAddress;
	}

	public void setdAddress(String dAddress) {
		this.dAddress = dAddress;
	}

	@XmlTransient
	public String getExpressType() {
		return expressType;
	}

	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}

	@XmlTransient
	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	@XmlTransient
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	@XmlTransient
	public String getParcelQuantity() {
		return parcelQuantity;
	}

	public void setParcelQuantity(String parcelQuantity) {
		this.parcelQuantity = parcelQuantity;
	}

	@XmlTransient
	public String getCargoTotalWeight() {
		return cargoTotalWeight;
	}

	public void setCargoTotalWeight(String cargoTotalWeight) {
		this.cargoTotalWeight = cargoTotalWeight;
	}

	@XmlTransient
	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	@XmlTransient
	public String getIsdocall() {
		return isdocall;
	}

	public void setIsdocall(String isdocall) {
		this.isdocall = isdocall;
	}

	@XmlTransient
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlTransient
	public String getRoutelabelService() {
		return routelabelService;
	}

	public void setRoutelabelService(String routelabelService) {
		this.routelabelService = routelabelService;
	}

	@XmlTransient
	public String getIsUnifiedWaybillNo() {
		return isUnifiedWaybillNo;
	}

	public void setIsUnifiedWaybillNo(String isUnifiedWaybillNo) {
		this.isUnifiedWaybillNo = isUnifiedWaybillNo;
	}

	@XmlTransient
	public List<Cargo> getCargo() {
		return Cargo;
	}

	public void setCargo(List<Cargo> cargo) {
		Cargo = cargo;
	}
}
