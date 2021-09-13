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
@XmlRootElement(name = "Result")
public class Result {

    /**
     * 请求头包装数据
     */
    @XmlElement(name = "WaybillCode")
    private WaybillCode waybillCode;

    @XmlTransient
    public WaybillCode getWaybillCode() {
        return waybillCode;
    }

    public void setWaybillCode(WaybillCode waybillCode) {
        this.waybillCode = waybillCode;
    }
}
