package com.medusa.gruul.logistics.util.express.yto.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author lcy
 * @date 20120-03-11
 * @Description: 发货人信息
 */

@XmlRootElement(name = "sender")
public class Sender {

    /**
     * 用户姓名
     */
    @XmlElement(name = "name")
    private String name;

    /**
     * 用户电话，包括区号、电话号码及分机号，中间用“-”分隔
     */
    @XmlElement(name = "phone")
    private String phone;

    /**
     * 用户移动电话
     */
    @XmlElement(name = "mobile")
    private String mobile;

    /**
     * 用户邮编（如果没有可以填默认的0）
     */
    @XmlElement(name = "postCode")
    private String postCode;

    /**
     * 用户所在省
     */
    @XmlElement(name = "prov")
    private String prov;

    /**
     * 用户所在市、县（区），市和区中间用“,”分隔；注意有些市下面是没有区
     */
    @XmlElement(name = "city")
    private String city;

    /**
     * 用户详细地址
     */
    @XmlElement(name = "address")
    private String address;

    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlTransient
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @XmlTransient
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @XmlTransient
    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    @XmlTransient
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @XmlTransient
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
