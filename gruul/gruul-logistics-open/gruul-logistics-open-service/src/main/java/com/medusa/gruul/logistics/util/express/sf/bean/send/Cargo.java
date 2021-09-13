package com.medusa.gruul.logistics.util.express.sf.bean.send;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**

 * @author lcy

 * @date 2019-1-17

 * @Description: 生成电子面单数据类
   快件物品信息
 */

@XmlRootElement(name = "Cargo")
public class Cargo {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "count")
    private String count;

    @XmlAttribute(name = "unit")
    private String unit;

    @XmlAttribute(name = "weight")
    private String weight;

    @XmlAttribute(name = "amount")
    private String amount;

    @XmlAttribute(name = "currency")
    private String currency;

    @XmlAttribute(name = "source_area")
    private String source_area;

    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @XmlTransient
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @XmlTransient
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @XmlTransient
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @XmlTransient
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @XmlTransient
    public String getSource_area() {
        return source_area;
    }

    public void setSource_area(String source_area) {
        this.source_area = source_area;
    }
}
