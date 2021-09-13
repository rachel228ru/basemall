package com.medusa.gruul.logistics.util.express.yto.bean.route;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>
 * 圆通物流查询请求包装数据
 * </p>
 *
 * @author lcysike
 * @since 2020-03-12
 */
@XmlRootElement(name = "WaybillCode")
public class WaybillCode {

    /**
     * 圆通运单号
     */
    @XmlElement(name = "Number")
    private String number;

    @XmlTransient
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
